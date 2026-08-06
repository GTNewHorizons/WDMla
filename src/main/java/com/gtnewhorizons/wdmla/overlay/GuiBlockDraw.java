package com.gtnewhorizons.wdmla.overlay;

import static org.lwjgl.opengl.GL11.*;

import mcp.mobius.waila.overlay.OverlayConfig;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.ForgeHooksClient;

import org.joml.Vector3f;
import org.joml.Vector4i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import com.gtnewhorizons.wdmla.util.HotSwapUtil;

/**
 * drawing 3D Block by calling lower level api.
 */
public class GuiBlockDraw {

    public BlockPos renderedBlock;
    private final Vector3f eyePos = new Vector3f(0, 0, -10f);
    private final Vector3f lookAt = new Vector3f(0, 0, 0);
    private final Vector3f worldUp = new Vector3f(0, 1, 0);
    private Vector4i rect = new Vector4i();
    private final HudRenderBlocks bufferBuilder = new HudRenderBlocks();

    private float prevLightmapX;
    private float prevLightmapY;
    private int prevActiveTexture;
    private boolean prevLightmapTex2DEnabled;

    private static final GuiBlockDraw instance = new GuiBlockDraw();
    public static final float ZOOM = 2.3f;

    public static boolean drawWorldBlock(int x, int y, int width, int height, int blockX, int blockY, int blockZ,
            float rotationYaw, float rotationPitch) {
        Minecraft mc = Minecraft.getMinecraft();

// Center the camera on the full multi-block shape for doors/beds/double plants.
Block baseBlock = mc.theWorld.getBlock(blockX, blockY, blockZ);
int baseMeta = mc.theWorld.getBlockMetadata(blockX, blockY, blockZ);

// Some multi-block / 2-tall blocks (e.g. Waystones) have their TileEntity or TESR only on the lower half.
// If we are looking at the upper half of the same block, shift rendering to the lower half so the
// fancy preview works without incorrectly hijacking unrelated blocks placed above TESRs.
TileEntity teHere = mc.theWorld.getTileEntity(blockX, blockY, blockZ);
if (teHere == null) {
    Block blockBelow = mc.theWorld.getBlock(blockX, blockY - 1, blockZ);
    TileEntity teBelow = mc.theWorld.getTileEntity(blockX, blockY - 1, blockZ);
    if (blockBelow == baseBlock && teBelow != null && TileEntityRendererDispatcher.instance.hasSpecialRenderer(teBelow)) {
        blockY -= 1;
        baseBlock = mc.theWorld.getBlock(blockX, blockY, blockZ);
        baseMeta = mc.theWorld.getBlockMetadata(blockX, blockY, blockZ);
    }
}

int minX = blockX, minY = blockY, minZ = blockZ;
int maxX = blockX, maxY = blockY, maxZ = blockZ;

if (baseBlock instanceof net.minecraft.block.BlockDoublePlant || baseBlock instanceof net.minecraft.block.BlockDoor) {
    if ((baseMeta & 8) == 0) { // lower
        maxY = blockY + 1;
    } else { // upper
        minY = blockY - 1;
    }
} else if (baseBlock instanceof net.minecraft.block.BlockBed) {
    int facing = baseMeta & 3;
    int dx = 0, dz = 0;
    if (facing == 0) dz = 1;
    else if (facing == 1) dx = -1;
    else if (facing == 2) dz = -1;
    else if (facing == 3) dx = 1;

    boolean isHead = (baseMeta & 8) != 0;
    int ox = isHead ? (blockX - dx) : (blockX + dx);
    int oz = isHead ? (blockZ - dz) : (blockZ + dz);

    minX = Math.min(minX, ox);
    maxX = Math.max(maxX, ox);
    minZ = Math.min(minZ, oz);
    maxZ = Math.max(maxZ, oz);
}


// Waystones and RiftFlux Blessing Pillars are two-block tall like doors; include both halves for centering (no lighting/AO changes).
if (isWaystoneBlock(baseBlock) || isBlessingPillarBlock(baseBlock)) {
    Block above = mc.theWorld.getBlock(blockX, blockY + 1, blockZ);
    Block below = mc.theWorld.getBlock(blockX, blockY - 1, blockZ);
    if (above == baseBlock) {
        maxY = blockY + 1;
    } else if (below == baseBlock) {
        minY = blockY - 1;
    }
}

// Average of the bounding box of all blocks we will render.
Vector3f center = new Vector3f((minX + maxX) * 0.5f + 0.5f, (minY + maxY) * 0.5f + 0.5f, (minZ + maxZ) * 0.5f + 0.5f);

        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        // compute window size from scaled width & height
        int windowWidth = getScaledX(mc, resolution, width);
        int windowHeight = getScaledY(mc, resolution, height);
        // translate gui coordinates to window's ones (y is inverted)
        int windowX = getScaledX(mc, resolution, x);
        int windowY = mc.displayHeight - getScaledY(mc, resolution, y) - windowHeight;

        instance.renderedBlock = new BlockPos(blockX, blockY, blockZ);
        instance.setCameraLookAt(center, ZOOM, Math.toRadians(rotationPitch), Math.toRadians(rotationYaw));
        return instance.render(windowX, windowY, windowWidth, windowHeight);
    }

    private boolean render(int x, int y, int width, int height) {
        rect.set(x, y, width, height);
        // Always restore render state even if a mod block renderer throws.
        setupCamera();
        boolean rendered = false;
        try {
            rendered = drawWorld();
        } finally {
            resetCamera();
        }
        return rendered;
    }

    public void setCameraLookAt(Vector3f lookAt, double radius, double rotationPitch, double rotationYaw) {
        this.lookAt.set(lookAt);
        eyePos.set((float) Math.cos(rotationPitch), 0, (float) Math.sin(rotationPitch))
                .add(0, (float) (Math.tan(rotationYaw) * eyePos.length()), 0).normalize().mul((float) radius)
                .add(lookAt);
    }

    private static int getScaledX(Minecraft mc, ScaledResolution res, int x) {
        return (int) (x / (res.getScaledWidth() * 1.0) * mc.displayWidth);
    }

    private static int getScaledY(Minecraft mc, ScaledResolution res, int y) {
        return (int) (y / (res.getScaledHeight() * 1.0) * mc.displayHeight);
    }

    public void setupCamera() {
        int x = rect.x;
        int y = rect.y;
        int width = rect.z;
        int height = rect.w;

        Minecraft mc = Minecraft.getMinecraft();

        prevLightmapX = OpenGlHelper.lastBrightnessX;
        prevLightmapY = OpenGlHelper.lastBrightnessY;

        prevActiveTexture = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        GL13.glActiveTexture(OpenGlHelper.lightmapTexUnit);
        prevLightmapTex2DEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        GL13.glActiveTexture(prevActiveTexture);

        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glPushClientAttrib(GL_ALL_CLIENT_ATTRIB_BITS);
        mc.entityRenderer.disableLightmap(0);
        glDisable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);

        // setup viewport and clear GL buffers
        glViewport(x, y, width, height);

        scissorView(x, y, width, height);

        // setup projection matrix to perspective
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();

        float aspectRatio = width / (height * 1.0f);
        HotSwapUtil.gluPerspective(60.0f, aspectRatio, 0.1f, 10000.0f);

        // setup modelview matrix
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
        HotSwapUtil
                .gluLookat(eyePos.x, eyePos.y, eyePos.z, lookAt.x, lookAt.y, lookAt.z, worldUp.x, worldUp.y, worldUp.z);
    }

    protected void scissorView(int x, int y, int width, int height) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x, y, width, height);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private void resetCamera() {
        // reset viewport
        Minecraft minecraft = Minecraft.getMinecraft();
        glViewport(0, 0, minecraft.displayWidth, minecraft.displayHeight);

        // reset modelview matrix
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();

        // reset projection matrix
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();

        glMatrixMode(GL_MODELVIEW);

        // reset attributes
        glPopClientAttrib();
        glPopAttrib();

        // Restore the exact lightmap + active-texture state that existed before we rendered the HUD preview.
        // This prevents the "whole screen gets darker" leak that happens when the active texture unit is left
        // on the lightmap (or when GL_TEXTURE_2D enable state differs per unit) near night time.
        Minecraft mc = Minecraft.getMinecraft();

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevLightmapX, prevLightmapY);

        GL13.glActiveTexture(OpenGlHelper.lightmapTexUnit);
        if (prevLightmapTex2DEnabled) GL11.glEnable(GL11.GL_TEXTURE_2D);
        else GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL13.glActiveTexture(prevActiveTexture);

        mc.entityRenderer.disableLightmap(0);

    }

    protected boolean drawWorld() {

        Minecraft mc = Minecraft.getMinecraft();
        float fancyScale = OverlayConfig.fancyBlockScale;
        if (fancyScale <= 0.0f) fancyScale = 1.0f;
        boolean renderedAny = false;
        glPushMatrix();
        glTranslatef(lookAt.x, lookAt.y, lookAt.z);
        glScalef(fancyScale, fancyScale, fancyScale);
        glTranslatef(-lookAt.x, -lookAt.y, -lookAt.z);

        glEnable(GL_CULL_FACE);
        glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        mc.entityRenderer.disableLightmap(0);
        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        glDisable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_ALPHA_TEST);

        Tessellator tessellator = Tessellator.instance;
        renderedAny |= renderBlocks(tessellator, renderedBlock);

        RenderHelper.enableStandardItemLighting();
        glEnable(GL_LIGHTING);

        // render TESR
        TileEntityRendererDispatcher tesr = TileEntityRendererDispatcher.instance;
        for (int pass = 0; pass < 2; pass++) {
            int finalPass = pass;

            int x = renderedBlock.x;
            int y = renderedBlock.y;
            int z = renderedBlock.z;
            setDefaultPassRenderState(finalPass);
            Block blockAtPos = mc.theWorld.getBlock(x, y, z);
            TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(x, y, z);
            if (tile != null && tesr.hasSpecialRenderer(tile)) {
                // ProjectRed lamps have a TESR that mutates global halo-light state rather than drawing geometry.
                // Running it from HUD preview can cause visible in-world flicker, while
                // the lamp body itself is already rendered by the standard block renderer.
                if (shouldSkipTesrForFancyPreview(blockAtPos, tile)) continue;
                ForgeHooksClient.setRenderPass(pass);
                if (tile.shouldRenderInPass(finalPass)) {
            // Waystones + RiftFlux Blessing Pillars: HUD-only render should ignore lighting/AO influence from the world/hand.
            if (isWaystoneBlock(blockAtPos) || isBlessingPillarBlock(blockAtPos)) {
                // Save current lightmap + lighting state, force fullbright for just this TESR draw.
                final float prevX = OpenGlHelper.lastBrightnessX;
                final float prevY = OpenGlHelper.lastBrightnessY;
                final boolean wasLighting = GL11.glIsEnabled(GL11.GL_LIGHTING);

                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glColor4f(1f, 1f, 1f, 1f);

                renderedAny = true;
                tesr.renderTileEntityAt(tile, x, y, z, 0);

                // Restore previous state.
                if (wasLighting) GL11.glEnable(GL11.GL_LIGHTING);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevX, prevY);
            } else {
                renderedAny = true;
                tesr.renderTileEntityAt(tile, x, y, z, 0);
            }
                }
                ForgeHooksClient.setRenderPass(-1);
            }
        }
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glPopMatrix();
        glDepthMask(true);
    
        return renderedAny;
    }

    public boolean renderBlocks(Tessellator tessellator, BlockPos blocksToRender) {
        if (blocksToRender == null) return false;

        boolean renderedAny = false;

        Minecraft mc = Minecraft.getMinecraft();
        final int hudBrightness = 0x00A000A0;

        tessellator.startDrawingQuads();
        try {
            tessellator.setBrightness(hudBrightness);

            // Use a full-bright IBlockAccess wrapper, but do NOT mutate global GameSettings (prevents dynamic light flicker).
            bufferBuilder.blockAccess = new HudIsolatedFullBrightBlockAccess(
                    mc.theWorld,
                    blocksToRender.x,
                    blocksToRender.y,
                    blocksToRender.z,
                    hudBrightness);
            bufferBuilder.renderAllFaces = true;

            // Determine which world positions to render (some blocks span multiple blocks).
final java.util.ArrayList<BlockPos> toRender = new java.util.ArrayList<BlockPos>(2);
toRender.add(blocksToRender);

Block baseBlock = mc.theWorld.getBlock(blocksToRender.x, blocksToRender.y, blocksToRender.z);
int baseMeta = mc.theWorld.getBlockMetadata(blocksToRender.x, blocksToRender.y, blocksToRender.z);

if (baseBlock instanceof net.minecraft.block.BlockDoublePlant || baseBlock instanceof net.minecraft.block.BlockDoor) {
    if ((baseMeta & 8) == 0) toRender.add(new BlockPos(blocksToRender.x, blocksToRender.y + 1, blocksToRender.z));
    else toRender.add(new BlockPos(blocksToRender.x, blocksToRender.y - 1, blocksToRender.z));
} else if (baseBlock instanceof net.minecraft.block.BlockBed) {
    int facing = baseMeta & 3;
    int dx = 0, dz = 0;
    if (facing == 0) dz = 1;
    else if (facing == 1) dx = -1;
    else if (facing == 2) dz = -1;
    else if (facing == 3) dx = 1;

    boolean isHead = (baseMeta & 8) != 0;
    int ox = isHead ? (blocksToRender.x - dx) : (blocksToRender.x + dx);
    int oz = isHead ? (blocksToRender.z - dz) : (blocksToRender.z + dz);
    toRender.add(new BlockPos(ox, blocksToRender.y, oz));
}

if (isWaystoneBlock(baseBlock) || isBlessingPillarBlock(baseBlock)) {
    Block above = mc.theWorld.getBlock(blocksToRender.x, blocksToRender.y + 1, blocksToRender.z);
    Block below = mc.theWorld.getBlock(blocksToRender.x, blocksToRender.y - 1, blocksToRender.z);
    if (above == baseBlock) toRender.add(new BlockPos(blocksToRender.x, blocksToRender.y + 1, blocksToRender.z));
    else if (below == baseBlock) toRender.add(new BlockPos(blocksToRender.x, blocksToRender.y - 1, blocksToRender.z));
}



for (int pass = 0; pass < 2; pass++) {
    for (int i = 0; i < toRender.size(); i++) {
        BlockPos p = toRender.get(i);
        int x = p.x;
        int y = p.y;
        int z = p.z;

        Block block = mc.theWorld.getBlock(x, y, z);
        if (block == null || block == Blocks.air || !block.canRenderInPass(pass)) continue;


        // Ensure the block's bounds match its in-world state (fixes slabs/stairs/etc rendering as full cubes).
        block.setBlockBoundsBasedOnState(bufferBuilder.blockAccess, x, y, z);
        bufferBuilder.setRenderBoundsFromBlock(block);
        bufferBuilder.enableAO = pass == 0;

// For grass blocks (vanilla + most modded), never show snowy sides in the HUD preview.
// BlockGrass picks snowy-side textures by checking the block above in getIcon(IBlockAccess,...),
// so we bypass the world-dependent path entirely and render using:
//  - base dirt sides/bottom (getIcon(side, meta))
//  - tinted top (biome tint)
//  - tinted side overlay (getIconSideOverlay)

if (block instanceof net.minecraft.block.BlockGrass) {
    // This branch uses manual per-face coloring; RenderBlocks AO mode would ignore those colors and
    // reuse cached AO vertex data, which turns the preview grey/dark.
    bufferBuilder.enableAO = false;
    int meta = mc.theWorld.getBlockMetadata(x, y, z);

    IIcon iconBottom = block.getIcon(0, meta);
    IIcon iconTop = block.getIcon(1, meta);
    IIcon iconSideBase = block.getIcon(2, meta);
    IIcon iconSideOverlay = ((net.minecraft.block.BlockGrass) block).getIconSideOverlay();

    int c = block.colorMultiplier(bufferBuilder.blockAccess, x, y, z);
    float r = ((c >> 16) & 255) / 255.0f;
    float g = ((c >> 8) & 255) / 255.0f;
    float b = (c & 255) / 255.0f;

    // Manual per-face shading (AO-look) ONLY for this isolated grass preview.
    final float shadeBottom = 0.5f;
    final float shadeTop = 1.0f;
    final float shadeZ = 0.8f;
    final float shadeX = 0.6f;

    // Bottom
    tessellator.setColorOpaque_F(shadeBottom, shadeBottom, shadeBottom);
    bufferBuilder.renderFaceYNeg(block, (double) x, (double) y, (double) z, iconBottom);

    // Top (tinted)
    tessellator.setColorOpaque_F(r * shadeTop, g * shadeTop, b * shadeTop);
    bufferBuilder.renderFaceYPos(block, (double) x, (double) y, (double) z, iconTop);

    // Sides base
    tessellator.setColorOpaque_F(shadeZ, shadeZ, shadeZ);
    bufferBuilder.renderFaceZNeg(block, (double) x, (double) y, (double) z, iconSideBase);
    bufferBuilder.renderFaceZPos(block, (double) x, (double) y, (double) z, iconSideBase);

    tessellator.setColorOpaque_F(shadeX, shadeX, shadeX);
    bufferBuilder.renderFaceXNeg(block, (double) x, (double) y, (double) z, iconSideBase);
    bufferBuilder.renderFaceXPos(block, (double) x, (double) y, (double) z, iconSideBase);

    // Sides overlay (tinted)
    tessellator.setColorOpaque_F(r * shadeZ, g * shadeZ, b * shadeZ);
    bufferBuilder.renderFaceZNeg(block, (double) x, (double) y, (double) z, iconSideOverlay);
    bufferBuilder.renderFaceZPos(block, (double) x, (double) y, (double) z, iconSideOverlay);

    tessellator.setColorOpaque_F(r * shadeX, g * shadeX, b * shadeX);
    bufferBuilder.renderFaceXNeg(block, (double) x, (double) y, (double) z, iconSideOverlay);
    bufferBuilder.renderFaceXPos(block, (double) x, (double) y, (double) z, iconSideOverlay);

    // Reset color
    tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
    renderedAny = true;
    continue;
}

        int rt = block.getRenderType();
        if (rt == 0) {
            // Many vanilla-style blocks (glass, leaves, etc.) return renderAsNormalBlock()==false but still
            // render correctly via the standard block renderer. The main case we want to avoid here is
            // TESR-based blocks that also report renderType==0 (e.g. DeepResonance crystals).
            int meta = mc.theWorld.getBlockMetadata(x, y, z);
            TileEntity te = mc.theWorld.getTileEntity(x, y, z);
            boolean hasTesr = te != null && TileEntityRendererDispatcher.instance.hasSpecialRenderer(te);

            if (!block.renderAsNormalBlock() && hasTesr) {
                // Skip cube rendering; the TESR will render the proper model.
                continue;
            }

            if (block.renderAsNormalBlock() || !block.hasTileEntity(meta)) {
                renderedAny |= bufferBuilder.renderBlockByRenderType(block, x, y, z);
            } else {
                // Skip cube rendering; the TESR (if any) will render the proper model.
                continue;
            }
        } else {
            renderedAny |= bufferBuilder.renderBlockByRenderType(block, x, y, z);
        }
    }
}
        } finally {
            tessellator.draw();
            tessellator.setTranslation(0, 0, 0);
        }
        return renderedAny;
    }

    private static class HudRenderBlocks extends RenderBlocks {

        @Override
        public boolean renderStandardBlock(Block block, int x, int y, int z) {
            int color = block.colorMultiplier(this.blockAccess, x, y, z);
            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;

            if (EntityRenderer.anaglyphEnable) {
                float adjRed = (red * 30.0F + green * 59.0F + blue * 11.0F) / 100.0F;
                float adjGreen = (red * 30.0F + green * 70.0F) / 100.0F;
                float adjBlue = (red * 30.0F + blue * 70.0F) / 100.0F;
                red = adjRed;
                green = adjGreen;
                blue = adjBlue;
            }

            // Always use the isolated non-world fake shading path for the fancy HUD preview.
            return this.renderStandardBlockWithColorMultiplier(block, x, y, z, red, green, blue);
        }
    }

private static boolean isWaystoneBlock(net.minecraft.block.Block b) {
    if (b == null) return false;
    Object regNameObj = net.minecraft.block.Block.blockRegistry.getNameForObject(b);
    String reg = regNameObj != null ? regNameObj.toString() : "";
    String unloc = b.getUnlocalizedName();
    String cls = b.getClass().getName();
    String s = (reg + " " + unloc + " " + cls).toLowerCase(java.util.Locale.ROOT);
    return s.contains("waystone");
}

private static boolean isBlessingPillarBlock(net.minecraft.block.Block b) {
    if (b == null) return false;
    Object regNameObj = net.minecraft.block.Block.blockRegistry.getNameForObject(b);
    String reg = regNameObj != null ? regNameObj.toString() : "";
    if ("riftflux:blessing_pillar".equals(reg)) return true;
    String unloc = b.getUnlocalizedName();
    return unloc != null && unloc.contains("riftflux.blessing_pillar");
}

private static boolean shouldSkipTesrForFancyPreview(Block block, TileEntity tile) {
    if (isProjectRedLampBlock(block)) return true;

    if (tile != null) {
        String tileClass = tile.getClass().getName();
        if ("mrtjp.projectred.illumination.TileLamp".equals(tileClass)) return true;
    }
    return false;
}

private static boolean isProjectRedLampBlock(Block b) {
    if (b == null) return false;
    Object regNameObj = net.minecraft.block.Block.blockRegistry.getNameForObject(b);
    String reg = regNameObj != null ? regNameObj.toString() : "";
    if (reg.toLowerCase(java.util.Locale.ROOT).contains("projectred.illumination.lamp")) return true;

    String unloc = b.getUnlocalizedName();
    String cls = b.getClass().getName();
    String s = (unloc + " " + cls).toLowerCase(java.util.Locale.ROOT);
    return s.contains("projectred.illumination.lamp")
            || s.contains("mrtjp.projectred.illumination.blocklamp");
}


private static final class HudIsolatedFullBrightBlockAccess implements net.minecraft.world.IBlockAccess {
    private final net.minecraft.world.IBlockAccess delegate;
    private final java.util.HashSet<Long> include;
    private final int hudBrightness;

    private HudIsolatedFullBrightBlockAccess(net.minecraft.world.IBlockAccess delegate, int cx, int cy, int cz,
            int hudBrightness) {
    this.delegate = delegate;
    this.hudBrightness = hudBrightness;
    this.include = new java.util.HashSet<Long>(6);


    includePos(cx, cy, cz);
    // IMPORTANT:
    // Do NOT include generic neighbors (above/below/sides). Many blocks change shading/textures based on neighbor lookups
    // (AO, snow checks, CTM/texturepack logic, etc.).
    net.minecraft.block.Block b = delegate.getBlock(cx, cy, cz);
    int meta = delegate.getBlockMetadata(cx, cy, cz);

    // Minimal "multi-block" support: include only the linked half for known two-block structures.
    if (b instanceof net.minecraft.block.BlockDoublePlant) {
        if ((meta & 8) == 0) includePos(cx, cy + 1, cz);
        else includePos(cx, cy - 1, cz);
    } else if (b instanceof net.minecraft.block.BlockDoor) {
        if ((meta & 8) == 0) includePos(cx, cy + 1, cz);
        else includePos(cx, cy - 1, cz);
    } else if (b instanceof net.minecraft.block.BlockBed) {
        int facing = meta & 3;
        int dx = 0, dz = 0;
        if (facing == 0) dz = 1;
        else if (facing == 1) dx = -1;
        else if (facing == 2) dz = -1;
        else if (facing == 3) dx = 1;

        if ((meta & 8) == 0) includePos(cx + dx, cy, cz + dz);
        else includePos(cx - dx, cy, cz - dz);
    } else if (isWaystoneBlock(b) || isBlessingPillarBlock(b)) {
        // Door-style: include only the other half if it is the same block.
        net.minecraft.block.Block above = delegate.getBlock(cx, cy + 1, cz);
        net.minecraft.block.Block below = delegate.getBlock(cx, cy - 1, cz);
        if (above == b) includePos(cx, cy + 1, cz);
        else if (below == b) includePos(cx, cy - 1, cz);
    } else if (b == net.minecraft.init.Blocks.snow_layer) {
        // Snow layer needs the block below to exist for proper rendering (but still don't include anything else).
        includePos(cx, cy - 1, cz);
    }
}

    private void includePos(int x, int y, int z) {
        include.add((((long)x & 0x3FFFFFFL) << 38) | (((long)z & 0x3FFFFFFL) << 12) | ((long)y & 0xFFFL));
    }

    private boolean isIncluded(int x, int y, int z) {
        long key = (((long)x & 0x3FFFFFFL) << 38) | (((long)z & 0x3FFFFFFL) << 12) | ((long)y & 0xFFFL);
        return include.contains(key);
    }
    @Override
    public net.minecraft.block.Block getBlock(int x, int y, int z) {
        if (!isIncluded(x, y, z)) return net.minecraft.init.Blocks.air;
        return delegate.getBlock(x, y, z);
    }

    @Override
    public net.minecraft.tileentity.TileEntity getTileEntity(int x, int y, int z) {
        if (!isIncluded(x, y, z)) return null;
        return delegate.getTileEntity(x, y, z);
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int x, int y, int z, int p_72802_4_) {
        return hudBrightness;
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {
        if (!isIncluded(x, y, z)) return 0;
        return delegate.getBlockMetadata(x, y, z);
    }

    @Override
    public boolean isAirBlock(int x, int y, int z) {
        return getBlock(x, y, z) == net.minecraft.init.Blocks.air;
    }

    @Override
    public net.minecraft.world.biome.BiomeGenBase getBiomeGenForCoords(int x, int z) {
        return delegate.getBiomeGenForCoords(x, z);
    }

    @Override
    public int getHeight() {
        return delegate.getHeight();
    }

    @Override
    public boolean extendedLevelsInChunkCache() {
        return delegate.extendedLevelsInChunkCache();
    }

    @Override
    public int isBlockProvidingPowerTo(int x, int y, int z, int side) {
        if (!isIncluded(x, y, z)) return 0;
        return delegate.isBlockProvidingPowerTo(x, y, z, side);
    }
    @Override
    public boolean isSideSolid(int x, int y, int z,
                              net.minecraftforge.common.util.ForgeDirection side,
                              boolean _default) {
        if (!isIncluded(x, y, z)) return false;
        return delegate.isSideSolid(x, y, z, side, _default);
    }

}


    public static void setDefaultPassRenderState(int pass) {
            glColor4f(1, 1, 1, 1);
            if (pass == 0) { // SOLID
                glEnable(GL_DEPTH_TEST);
                glDisable(GL_BLEND);
                glDepthMask(true);
            } else { // TRANSLUCENT
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                glDepthMask(false);
            }
        }
}
