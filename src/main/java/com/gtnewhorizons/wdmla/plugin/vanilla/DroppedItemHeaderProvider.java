package com.gtnewhorizons.wdmla.plugin.vanilla;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;

public enum DroppedItemHeaderProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        ItemStack newStack = new ItemStack(
                accessor.getBlock(),
                1,
                accessor.getBlock().damageDropped(accessor.getMetadata()));
        ThemeHelper.INSTANCE.overrideTooltipIcon(tooltip, newStack, false);
        ThemeHelper.INSTANCE.overrideTooltipTitle(tooltip, newStack);
    }

    @Override
    public ResourceLocation getUid() {
        return VanillaIdentifiers.DROPPED_ITEM_HEADER;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.HEAD;
    }
}
