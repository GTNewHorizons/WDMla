package mcp.mobius.waila.overlay;

import static mcp.mobius.waila.api.SpecialChars.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.wdmla.api.ui.sizer.IArea;
import com.gtnewhorizons.wdmla.overlay.GuiDraw;
import com.gtnewhorizons.wdmla.util.FormatUtil;

import mcp.mobius.waila.utils.WailaExceptionHandler;

/**
 * Processes Strings and ItemStack into valuable part.
 * 
 * @see mcp.mobius.waila.gui.helpers.UIHelper
 */
public class DisplayUtil {

    private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    private static final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
    private static final RenderItem renderItem = new RenderItem();

    /**
     * Strip all symbols from string includes Minecraft color code and Waila TTRender embedding
     * 
     * @param s input string
     * @return stripped string
     */
    public static String stripSymbols(String s) {
        String result = patternRender.matcher(s).replaceAll("");
        result = patternMinecraft.matcher(result).replaceAll("");
        result = patternWaila.matcher(result).replaceAll("");
        return result;
    }

    /**
     * Strip Waila TTRender symbol
     * 
     * @param s input string
     * @return stripped string
     */
    public static String stripWailaSymbols(String s) {
        String result = patternRender.matcher(s).replaceAll("");
        result = patternWaila.matcher(result).replaceAll("");
        return result;
    }

    /**
     * @deprecated Use {@link GuiDraw#renderStack(IArea, ItemStack, boolean, String)}
     */
    @Deprecated
    public static void renderStack(int x, int y, ItemStack stack) {
        enable3DRender();
        try {
            renderItem.renderItemAndEffectIntoGUI(fontRenderer, textureManager, stack, x, y);
            renderItem.renderItemOverlayIntoGUI(fontRenderer, textureManager, stack, x, y);
        } catch (Exception e) {
            String stackStr = stack != null ? stack.toString() : "NullStack";
            WailaExceptionHandler.handleErr(e, "renderStack | " + stackStr, null);
        }
        enable2DRender();
    }

    public static void enable3DRender() {
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public static void enable2DRender() {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    /**
     * gets all item tooltip lines
     * 
     * @param itemstack ItemStack that has custom tooltip
     * @return tooltip list
     */
    public static @NotNull List<String> itemDisplayNameMultiline(@NotNull ItemStack itemstack) {
        List<String> namelist = null;
        try {
            namelist = itemstack.getTooltip(
                    Minecraft.getMinecraft().thePlayer,
                    Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
        } catch (Throwable ignored) {}

        if (namelist == null) namelist = new ArrayList<>();

        if (namelist.isEmpty()) namelist.add("Unnamed");

        if (namelist.get(0) == null || namelist.get(0).isEmpty()) namelist.set(0, "Unnamed");

        String rarityColor = itemstack.getItem() != null ? itemstack.getRarity().rarityColor.toString() : "";
        namelist.set(0, rarityColor + namelist.get(0));
        for (int i = 1; i < namelist.size(); i++) namelist.set(i, "\u00a77" + namelist.get(i));

        return namelist;
    }

    /**
     * Gets the first line of item tooltip.<br>
     * Changes from Waila: The name will be formatted(stripped) automatically!<br>
     *
     * @param itemstack ItemStack that has custom tooltip
     * @return formatted display name
     */
    public static @NotNull String itemDisplayNameShortFormatted(@NotNull ItemStack itemstack) {
        List<String> list = itemDisplayNameMultiline(itemstack);
        return FormatUtil.formatNameByPixelCount(list.get(0));
    }

    /**
     * Gets the first line of item tooltip.<br>
     *
     * @deprecated This will return full length item name which may fill the whole screen in certain situation.
     * @param itemstack ItemStack that has custom tooltip
     * @return display name
     */
    @Deprecated
    public static @NotNull String itemDisplayNameShort(@NotNull ItemStack itemstack) {
        List<String> list = itemDisplayNameMultiline(itemstack);
        return list.get(0);
    }

    @Deprecated
    public static int getDisplayWidth(String s) {
        return 0;
    }
}
