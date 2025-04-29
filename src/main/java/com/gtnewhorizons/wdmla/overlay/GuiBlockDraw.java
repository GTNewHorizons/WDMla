package com.gtnewhorizons.wdmla.overlay;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import com.gtnewhorizons.wdmla.util.HotSwapUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.Vector3f;
import org.joml.Vector4i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static org.lwjgl.opengl.GL11.*;

/**
 * drawing 3D Block by calling lower level api.
 */
public class GuiBlockDraw {

    public BlockPos renderedBlock;
    private final Vector3f eyePos = new Vector3f(0, 0, -10f);
    private final Vector3f lookAt = new Vector3f(0, 0, 0);
    private final Vector3f worldUp = new Vector3f(0, 1, 0);
    private Vector4i rect = new Vector4i();
    private final RenderBlocks bufferBuilder = new RenderBlocks();

    private static final GuiBlockDraw instance = new GuiBlockDraw();
    public static final float ZOOM = 2.3f;

    public static void drawWorldBlock(int x, int y, int width, int height, int blockX, int blockY, int blockZ,
                                      float rotationYaw, float rotationPitch) {
        Vector3f center = new Vector3f(blockX + 0.5f, blockY + 0.5f, blockZ + 0.5f);

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        // compute window size from scaled width & height
        int windowWidth = getScaledX(mc, resolution, width);
        int windowHeight = getScaledY(mc, resolution, height);
        // translate gui coordinates to window's ones (y is inverted)
        int windowX = getScaledX(mc, resolution, x);
        int windowY = mc.displayHeight - getScaledY(mc, resolution, y) - windowHeight;

        instance.renderedBlock = new BlockPos(blockX, blockY, blockZ);
        instance.setCameraLookAt(center, ZOOM, Math.toRadians(rotationPitch), Math.toRadians(rotationYaw));
        instance.render(windowX, windowY, windowWidth, windowHeight);
    }

    private void render(int x, int y, int width, int height) {
        rect.set(x, y, width, height);
        // setupCamera
        setupCamera();

        // render World
        drawWorld();

        resetCamera();
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
        HotSwapUtil.gluLookat(eyePos.x, eyePos.y, eyePos.z, lookAt.x, lookAt.y, lookAt.z, worldUp.x, worldUp.y, worldUp.z);
    }

    protected void scissorView(int x, int y, int width, int height) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x, y, width, height);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void resetCamera() {
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
    }

    protected void drawWorld() {

        Minecraft mc = Minecraft.getMinecraft();
        glEnable(GL_CULL_FACE);
        glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        mc.entityRenderer.disableLightmap(0);
        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        glDisable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_ALPHA_TEST);

        Tessellator tessellator = Tessellator.instance;
        renderBlocks(tessellator, renderedBlock);

        RenderHelper.enableStandardItemLighting();
        glEnable(GL_LIGHTING);

        // render TESR
        TileEntityRendererDispatcher tesr = TileEntityRendererDispatcher.instance;
        for (int pass = 0; pass < 2; pass++) {
            ForgeHooksClient.setRenderPass(pass);
            int finalPass = pass;

            int x = renderedBlock.x;
            int y = renderedBlock.y;
            int z = renderedBlock.z;
            setDefaultPassRenderState(finalPass);
            TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(x, y, z);
            if (tile != null && tesr.hasSpecialRenderer(tile)) {
                if (tile.shouldRenderInPass(finalPass)) {
                    tesr.renderTileEntityAt(tile, x, y, z, 0);
                }
            }
        }
        ForgeHooksClient.setRenderPass(-1);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glDepthMask(true);
    }

    public void renderBlocks(Tessellator tessellator, BlockPos blocksToRender) {
        if (blocksToRender == null) return;
        Minecraft mc = Minecraft.getMinecraft();
        final int savedAo = mc.gameSettings.ambientOcclusion;
        mc.gameSettings.ambientOcclusion = 0;
        tessellator.startDrawingQuads();
        try {
            tessellator.setBrightness(15 << 20 | 15 << 4);
            for (int i = 0; i < 2; i++) {
                int x = blocksToRender.x;
                int y = blocksToRender.y;
                int z = blocksToRender.z;
                Block block = Minecraft.getMinecraft().theWorld.getBlock(x, y, z);
                if (block.equals(Blocks.air) || !block.canRenderInPass(i)) continue;

                bufferBuilder.blockAccess = Minecraft.getMinecraft().theWorld;
                bufferBuilder.setRenderBounds(0, 0, 0, 1, 1, 1);
                bufferBuilder.renderAllFaces = true;
                bufferBuilder.renderBlockByRenderType(block, x, y, z);
            }
        } finally {
            mc.gameSettings.ambientOcclusion = savedAo;
            tessellator.draw();
            tessellator.setTranslation(0, 0, 0);
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
