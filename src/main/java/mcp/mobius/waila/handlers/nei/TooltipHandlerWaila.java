package mcp.mobius.waila.handlers.nei;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import codechicken.nei.guihook.IContainerTooltipHandler;
import com.gtnewhorizons.wdmla.api.ui.MessageType;
import com.gtnewhorizons.wdmla.config.General;
import com.gtnewhorizons.wdmla.util.Color;
import mcp.mobius.waila.utils.ModIdentification;

/**
 * Its 2025, we still rely on Waila see the mod name of items in inventory
 */
public class TooltipHandlerWaila implements IContainerTooltipHandler {

    @Override
    public List<String> handleItemDisplayName(GuiContainer arg0, ItemStack itemstack, List<String> currenttip) {
        return currenttip;
    }

    @Override
    public List<String> handleItemTooltip(GuiContainer arg0, ItemStack itemstack, int arg2, int arg3,
            List<String> currenttip) {
        if (!General.modName.inventoryShow) {
            return currenttip;
        }
        String canonicalName = ModIdentification.nameFromStack(itemstack);
        if (canonicalName != null && !canonicalName.isEmpty()) {
            StringBuilder prefix = new StringBuilder();
            prefix.append(resolveModNameColorCode());
            if (General.modName.inventoryItalic) {
                prefix.append("\u00a7o");
            }
            currenttip.add(prefix + canonicalName);
        }
        return currenttip;
    }

    @Override
    public List<String> handleTooltip(GuiContainer arg0, int arg1, int arg2, List<String> currenttip) {
        return currenttip;
    }

    private static String resolveModNameColorCode() {
        int fallback = General.currentTheme.get().textColor(MessageType.MOD_NAME);
        int color = Color.parseColor(General.modName.inventoryColorOverride, fallback);
        return Color.toNearestChatColorCode(color);
    }
}
