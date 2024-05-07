package mcp.mobius.waila.addons;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.elements.IElement;
import mcp.mobius.waila.api.elements.IProbeInfo;
import mcp.mobius.waila.api.impl.elements.ElementProgress;
import mcp.mobius.waila.api.impl.elements.ElementText;
import mcp.mobius.waila.api.impl.elements.ProgressStyle;
import mcp.mobius.waila.handlers.HUDHandlerBlocks;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

import static mcp.mobius.waila.api.SpecialChars.*;

public class DefaultProbeInfoProvider {

    public static void addStandardBlockInfo(ItemStack itemStack, IProbeInfo probeInfo, IWailaDataAccessor accessor,
                                            IWailaConfigHandler config) {

        //TODO: item icon, horizontal layout
//        if (!itemStack.isEmpty()) {
//            if (Tools.show(mode, config.getShowModName())) {
//                probeInfo.horizontal().item(pickBlock).vertical().itemLabel(pickBlock).text(TextStyleClass.MODNAME + modid);
//            } else {
//                probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER)).item(pickBlock).itemLabel(pickBlock);
//            }
//        } else if (Tools.show(mode, config.getShowModName())) {
//            probeInfo.vertical().text(TextStyleClass.NAME + getBlockUnlocalizedName(block)).text(TextStyleClass.MODNAME + modid);
//        } else {
//            probeInfo.vertical().text(TextStyleClass.NAME + getBlockUnlocalizedName(block));
//        }

        HUDHandlerBlocks hudHandlerBlocks = new HUDHandlerBlocks();
        IProbeInfo row = probeInfo.horizontal();
        row.item(itemStack);
        IProbeInfo row_vertical = row.vertical();
        row_vertical.text(hudHandlerBlocks.getWailaHead(itemStack, new ArrayList<>(), accessor, config).get(0));
        String modName = ModIdentification.nameFromStack(itemStack);
        if (modName != null && !modName.isEmpty()) {
            row_vertical.text(BLUE + ITALIC + modName);
        }

        float damage = accessor.getBlockBreakDamage();
        if (damage > 0) {
            probeInfo.vertical().progress((long) (damage * 100), 100, new ProgressStyle()
                    .text("Progress " + damage * 100 + "%")
                    .width(85)
                    .borderColor(0)
                    .filledColor(0xff990000)
                    .alternateFilledColor(0xff550000));
        }
    }
}
