package com.gtnewhorizons.wdmla.plugin.core;

import static mcp.mobius.waila.api.SpecialChars.*;

import com.gtnewhorizons.wdmla.impl.ui.component.BlockComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.Theme;
import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.api.ui.MessageType;
import com.gtnewhorizons.wdmla.config.General;
import com.gtnewhorizons.wdmla.config.PluginsConfig;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.impl.ui.component.ItemComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.TextComponent;
import com.gtnewhorizons.wdmla.impl.ui.style.TextStyle;
import com.gtnewhorizons.wdmla.wailacompat.RayTracingCompat;

import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.ModIdentification;

public enum DefaultBlockInfoProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public ResourceLocation getUid() {
        return Identifiers.DEFAULT_BLOCK;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.DEFAULT_INFO;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        // step 1: check whether waila has custom Wailastack or not
        ItemStack overrideStack = RayTracingCompat.INSTANCE.getWailaStack(accessor.getHitResult());

        // step 2: construct an actual icon
        ITooltip row = tooltip.horizontal();
        ItemStack itemStack = overrideStack != null ? overrideStack : accessor.getItemForm();
        if (PluginsConfig.core.defaultBlock.showIcon) {
            if (PluginsConfig.core.defaultBlock.fancyRenderer) {
                row.child(new BlockComponent(
                        accessor.getHitResult().blockX, accessor.getHitResult().blockY, accessor.getHitResult().blockZ)
                        .tag(Identifiers.ITEM_ICON));
            }
            else {
                row.child(new ItemComponent(itemStack).doDrawOverlay(false).tag(Identifiers.ITEM_ICON));
            }
        }

        ITooltip row_vertical = row.vertical();
        if (PluginsConfig.core.defaultBlock.showBlockName) {
            if (accessor.getServerData() != null && accessor.getServerData().hasKey("CustomName")) {
                itemStack.setStackDisplayName(accessor.getServerData().getString("CustomName"));
            }
            String itemName = DisplayUtil.itemDisplayNameShort(itemStack);
            row_vertical.horizontal().tag(Identifiers.ITEM_NAME_ROW)
                    .child(ThemeHelper.INSTANCE.title(itemName).tag(Identifiers.ITEM_NAME));
        }
        String modName = ModIdentification.nameFromStack(itemStack);
        if (PluginsConfig.core.defaultBlock.showModName) {
            Theme theme = General.currentTheme.get();
            if (modName != null) {
                row_vertical.child(
                        new TextComponent(ITALIC + modName)
                                .style(new TextStyle().color(theme.textColor(MessageType.MOD_NAME)))
                                .tag(Identifiers.MOD_NAME));
            } else {
                // reserve for replacement
                row_vertical.child(
                        new TextComponent("").style(new TextStyle().color(theme.textColor(MessageType.MOD_NAME)))
                                .tag(Identifiers.MOD_NAME));
            }
        }
    }

    @Override
    public boolean isPriorityFixed() {
        return true;
    }
}
