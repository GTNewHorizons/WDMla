package com.gtnewhorizons.wdmla.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.gtnewhorizons.wdmla.api.ui.ColorPalette;
import com.gtnewhorizons.wdmla.api.ui.sizer.IArea;
import com.gtnewhorizons.wdmla.config.General;

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
    public static void drawString(String text, int x, int y, int colour, boolean shadow, float scale) {
        if (scale != 1) {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glPushMatrix();
            GL11.glScalef(scale, scale, 1);
        }
        if (shadow) {
            fontRenderer.drawStringWithShadow(text, (int) (x / scale), (int) (y / scale), colour);
        } else {
            fontRenderer.drawString(text, x, y, colour);
        }

        if (scale != 1) {
            GL11.glPopMatrix();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
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

        int x = area.getX();
        int y = area.getY();
        int w = area.getW();
        int h = area.getH();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        float xScale = (float) w / VANILLA_ITEM_SCALE;
        float yScale = (float) h / VANILLA_ITEM_SCALE;
        try {
            GL11.glPushMatrix();

            GL11.glScalef(xScale, yScale, 1);

            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            renderItem.renderItemAndEffectIntoGUI(
                    fontRenderer,
                    textureManager,
                    stack,
                    (int) (x / xScale),
                    (int) (y / yScale));
            if (drawOverlay) {
                if (stack.stackSize > 0) {
                    renderItem.renderItemOverlayIntoGUI(
                            fontRenderer,
                            textureManager,
                            stack,
                            (int) (x / xScale),
                            (int) (y / yScale),
                            stackSizeOverride);
                } else if (General.ghostProduct) {
                    if (stackSizeOverride == null) {
                        stackSizeOverride = String.valueOf(stack.stackSize);
                    }
                    renderItem.renderItemOverlayIntoGUI(
                            fontRenderer,
                            textureManager,
                            stack,
                            (int) (x / xScale),
                            (int) (y / yScale),
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

    public static void drawGradientRect(IArea area, int grad1, int grad2) {
        int x = area.getX();
        int y = area.getY();
        int w = area.getW();
        int h = area.getH();

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
        int x = area.getX();
        int y = area.getY();
        int w = area.getW();
        int h = area.getH();

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

    public static void drawBoxBorder(IArea area, int thickness, int topleftcolor, int botrightcolor) {
        int x1 = area.getX();
        int y1 = area.getY();
        int x2 = area.getEX();
        int y2 = area.getEY();

        Gui.drawRect(x1, y1, x2 - 1, y1 + thickness, topleftcolor);
        Gui.drawRect(x1, y1, x1 + thickness, y2 - 1, topleftcolor);
        Gui.drawRect(x2 - thickness, y1, x2, y2 - 1, botrightcolor);
        Gui.drawRect(x1, y2 - thickness, x2, y2, botrightcolor);
    }

    public static void drawVerticalLine(int x1, int y1, int y2, int color) {
        Gui.drawRect(x1, y1, x1 + 1, y2, color);
    }

    public static void drawHorizontalLine(int x1, int y1, int x2, int color) {
        Gui.drawRect(x1, y1, x2, y1 + 1, color);
    }

    public static void drawTexturedModelRect(int x, int y, int u, int v, int w, int h, int tw, int th) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        float zLevel = 0.0F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(1, 1, 1);
        tessellator.addVertexWithUV(x, y + h, zLevel, (u) * f, (v + th) * f1);
        tessellator.addVertexWithUV(x + w, y + h, zLevel, (u + tw) * f, (v + th) * f1);
        tessellator.addVertexWithUV(x + w, y, zLevel, (u + tw) * f, (v) * f1);
        tessellator.addVertexWithUV(x, y, zLevel, (u) * f, (v) * f1);
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    public static void renderVanillaIcon(int x, int y, int w, int h, IIcon icon, ResourceLocation path) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(path);

        if (icon == null) return;
        drawTexturedModelRect(
                x,
                y,
                (int) icon.getMinU(),
                (int) icon.getMinV(),
                w,
                h,
                icon.getIconWidth(),
                icon.getIconHeight());
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
}
