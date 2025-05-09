package com.gtnewhorizons.wdmla.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.gtnewhorizons.wdmla.api.ui.ColorPalette;
import com.gtnewhorizons.wdmla.api.ui.sizer.IArea;
import com.gtnewhorizons.wdmla.config.General;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Area;

import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.utils.WailaExceptionHandler;

/**
 * Collection of methods draw gui by calling lower level api.
 */
public final class GuiDraw {

    private GuiDraw() {
        throw new AssertionError();
    }

    private static final int VANILLA_ITEM_SCALE = 16;

    private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    private static final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
    private static final RenderItem renderItem = new RenderItem();

    /**
     * Draws string on ui with Minecraft font renderer.<br>
     * This means Angelica batch font renderer will be applied and causes unexpected behaviour. (if present)
     * 
     * @param text   target string
     * @param x      x ui coordinate
     * @param y      y ui coordinate
     * @param colour color of string, may be affected by color code
     * @param shadow apply shadow or not
     * @param scale  scale compared to standard vanilla text scale
     */
    public static void drawString(String text, float x, float y, int colour, boolean shadow, float scale) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        if (scale != 1) {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(scale, scale, 1);
        }
        if (shadow) {
            fontRenderer.drawStringWithShadow(text, 0, 0, colour);
        } else {
            fontRenderer.drawString(text, 0, 0, colour);
        }

        if (scale != 1) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glPopMatrix();
    }

    /**
     * Draws ItemStack with Minecraft Item renderer.<br>
     * 
     * @param area              the place ItemStack will be rendererd
     * @param stack             the ItemStack. If the stack size is less than 1,
     *                          {@code General.ghostProduct check will be used}
     * @param drawOverlay       Draws overlay below the icon or not (usually the stack size number and <b>durability
     *                          bar</b>)
     * @param stackSizeOverride custom text below the icon if {@code drawOverlay} is enabled. If null, the
     *                          {@link ItemStack#stackSize} will be used.
     */
    public static void renderStack(IArea area, ItemStack stack, boolean drawOverlay,
            @Nullable String stackSizeOverride) {
        if (stack.getItem() == null) {
            drawString("Err", area.getX(), area.getY(), ColorPalette.WARNING, true, 1);
            return;
        }

        float x = area.getX();
        float y = area.getY();
        float w = area.getW();
        float h = area.getH();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        float xScale = w / VANILLA_ITEM_SCALE;
        float yScale = h / VANILLA_ITEM_SCALE;
        try {
            GL11.glPushMatrix();

            GL11.glTranslatef(x, y, 0);
            GL11.glScalef(xScale, yScale, 1);

            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            renderItem.renderItemAndEffectIntoGUI(fontRenderer, textureManager, stack, 0, 0);
            if (drawOverlay) {
                if (stack.stackSize > 0) {
                    renderItem.renderItemOverlayIntoGUI(fontRenderer, textureManager, stack, 0, 0, stackSizeOverride);
                } else if (General.ghostProduct) {
                    if (stackSizeOverride == null) {
                        stackSizeOverride = String.valueOf(stack.stackSize);
                    }
                    renderItem.renderItemOverlayIntoGUI(
                            fontRenderer,
                            textureManager,
                            stack,
                            0,
                            0,
                            SpecialChars.YELLOW + stackSizeOverride);
                }
            }
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        } catch (Exception e) {
            String stackStr = stack.toString();
            WailaExceptionHandler.handleErr(e, "renderStack | " + stackStr, null);
        } finally {
            GL11.glPopMatrix();
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    public static void drawVerticalLine(float x1, float y1, float h, int color) {
        drawRect(new Area(x1, y1, 1, h), color);
    }

    public static void drawHorizontalLine(float x1, float y1, float w, int color) {
        drawRect(new Area(x1, y1, w, 1), color);
    }

    public static void drawRect(IArea area, int color) {
        drawGradientRect(area, color, color);
    }

    public static void drawGradientRect(IArea area, int grad1, int grad2) {
        float x = area.getX();
        float y = area.getY();
        float w = area.getW();
        float h = area.getH();

        float zLevel = 0.0f;

        float f = (float) (grad1 >> 24 & 255) / 255.0F;
        float f1 = (float) (grad1 >> 16 & 255) / 255.0F;
        float f2 = (float) (grad1 >> 8 & 255) / 255.0F;
        float f3 = (float) (grad1 & 255) / 255.0F;
        float f4 = (float) (grad2 >> 24 & 255) / 255.0F;
        float f5 = (float) (grad2 >> 16 & 255) / 255.0F;
        float f6 = (float) (grad2 >> 8 & 255) / 255.0F;
        float f7 = (float) (grad2 & 255) / 255.0F;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(x + w, y, zLevel);
        tessellator.addVertex(x, y, zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(x, y + h, zLevel);
        tessellator.addVertex(x + w, y + h, zLevel);
        tessellator.draw();

        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawStraightGradientRect(IArea area, int grad1, int grad2, boolean horizontal) {
        float x = area.getX();
        float y = area.getY();
        float w = area.getW();
        float h = area.getH();

        float zLevel = 0.0f;

        float a1 = (grad1 >> 24 & 255) / 255.0F;
        float r1 = (grad1 >> 16 & 255) / 255.0F;
        float g1 = (grad1 >> 8 & 255) / 255.0F;
        float b1 = (grad1 & 255) / 255.0F;
        float a2 = (grad2 >> 24 & 255) / 255.0F;
        float r2 = (grad2 >> 16 & 255) / 255.0F;
        float g2 = (grad2 >> 8 & 255) / 255.0F;
        float b2 = (grad2 & 255) / 255.0F;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        if (horizontal) {
            tessellator.setColorRGBA_F(r1, g1, b1, a1);
            tessellator.addVertex(x, y, zLevel);
            tessellator.addVertex(x, y + h, zLevel);

            tessellator.setColorRGBA_F(r2, g2, b2, a2);
            tessellator.addVertex(x + w, y + h, zLevel);
            tessellator.addVertex(x + w, y, zLevel);
        } else {
            tessellator.setColorRGBA_F(r1, g1, b1, a1);
            tessellator.addVertex(x + w, y, zLevel);
            tessellator.addVertex(x, y, zLevel);

            tessellator.setColorRGBA_F(r2, g2, b2, a2);
            tessellator.addVertex(x, y + h, zLevel);
            tessellator.addVertex(x + w, y + h, zLevel);
        }

        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawBoxBorder(IArea area, float thickness, int topLeftColor, int botRightColor) {
        float x1 = area.getX();
        float y1 = area.getY();
        float w = area.getW();
        float h = area.getH();

        drawRect(new Area(x1, y1, w - 1, thickness), topLeftColor);
        drawRect(new Area(x1, y1, thickness, h - 1), topLeftColor);
        drawRect(new Area(x1 + w - thickness, y1, thickness, h), botRightColor);
        drawRect(new Area(x1, y1 + h - thickness, w, thickness), botRightColor);
    }

    public static void drawTexturedModelRect(float x, float y, float u, float v, float w, float h, float tw, float th) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        float zLevel = 0.0F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(1, 1, 1);
        tessellator.addVertexWithUV(x, y + h, zLevel, u, v + th);
        tessellator.addVertexWithUV(x + w, y + h, zLevel, u + tw, v + th);
        tessellator.addVertexWithUV(x + w, y, zLevel, u + tw, v);
        tessellator.addVertexWithUV(x, y, zLevel, u, v);
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    public static void renderFluidStack(FluidStack content, float x0, float y0, float width, float height, float z) {
        if (content == null) {
            return;
        }
        Fluid fluid = content.getFluid();
        IIcon fluidStill = fluid.getIcon(content);
        // if (ModularUI.isHodgepodgeLoaded && fluidStill instanceof IPatchedTextureAtlasSprite) {
        // ((IPatchedTextureAtlasSprite) fluidStill).markNeedsAnimationUpdate();
        // }
        int fluidColor = fluid.getColor(content);
        float r = (fluidColor >> 16 & 255) / 255f, g = (fluidColor >> 8 & 255) / 255f, b = (fluidColor & 255) / 255f,
                a = (fluidColor >> 24 & 255) / 255f;
        a = a == 0f ? 1f : a;
        GL11.glColor4f(r, g, b, a);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        drawTiledTexture(
                x0,
                y0,
                width,
                height,
                fluidStill.getMinU(),
                fluidStill.getMinV(),
                fluidStill.getMaxU(),
                fluidStill.getMaxV(),
                fluidStill.getIconWidth(),
                fluidStill.getIconHeight(),
                z);
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    // from ModularUI2
    public static void drawTiledTexture(float x, float y, float w, float h, float u0, float v0, float u1, float v1,
            int tileWidth, int tileHeight, float z) {
        int countX = (((int) w - 1) / tileWidth) + 1;
        int countY = (((int) h - 1) / tileHeight) + 1;
        float fillerX = w - (countX - 1) * tileWidth;
        float fillerY = h - (countY - 1) * tileHeight;
        float fillerU = u0 + (u1 - u0) * fillerX / tileWidth;
        float fillerV = v0 + (v1 - v0) * fillerY / tileHeight;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        for (int i = 0, c = countX * countY; i < c; i++) {
            int ix = i % countX;
            int iy = i / countX;
            float xx = x + ix * tileWidth;
            float yy = y + iy * tileHeight;
            float xw = tileWidth, yh = tileHeight, uEnd = u1, vEnd = v1;
            if (ix == countX - 1) {
                xw = fillerX;
                uEnd = fillerU;
            }
            if (iy == countY - 1) {
                yh = fillerY;
                vEnd = fillerV;
            }

            drawTexture(xx, yy, xx + xw, yy + yh, u0, v0, uEnd, vEnd, z);
        }

        tessellator.draw();
    }

    public static void drawTexture(float x0, float y0, float x1, float y1, float u0, float v0, float u1, float v1,
            float z) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.addVertexWithUV(x0, y1, z, u0, v1);
        tessellator.addVertexWithUV(x1, y1, z, u1, v1);
        tessellator.addVertexWithUV(x1, y0, z, u1, v0);
        tessellator.addVertexWithUV(x0, y0, z, u0, v0);
    }

    // @see GuiInventory#func_147046_a
    public static void drawNonLivingEntity(int x, int y, int size, float yaw, float pitch, Entity entity) {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, 50.0F);
        GL11.glScalef((float) (-size), (float) size, (float) size);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(180.0F - yaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-pitch, 1.0F, 0.0F, 1.0F);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableStandardItemLighting();

        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);

        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    }
}
