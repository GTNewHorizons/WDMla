package mcp.mobius.waila.handlers;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.gtnewhorizons.wdmla.api.ui.MessageType;
import com.gtnewhorizons.wdmla.config.General;
import com.gtnewhorizons.wdmla.util.Color;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mcp.mobius.waila.api.BackwardCompatibility;
import mcp.mobius.waila.utils.ModIdentification;

@Deprecated
@BackwardCompatibility
public class VanillaTooltipHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void tooltipEvent(ItemTooltipEvent event) {
        if (!General.modName.inventoryShow) {
            return;
        }
        String canonicalName = ModIdentification.nameFromStack(event.itemStack);
        if (canonicalName != null && !canonicalName.isEmpty()) {
            int fallback = General.currentTheme.get().textColor(MessageType.MOD_NAME);
            int color = Color.parseColor(General.modName.inventoryColorOverride, fallback);
            StringBuilder prefix = new StringBuilder(Color.toNearestChatColorCode(color));
            if (General.modName.inventoryItalic) {
                prefix.append("\u00a7o");
            }
            event.toolTip.add(prefix + canonicalName);
        }
    }
}
