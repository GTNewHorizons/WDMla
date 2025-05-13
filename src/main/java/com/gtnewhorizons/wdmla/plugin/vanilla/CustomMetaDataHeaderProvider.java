package com.gtnewhorizons.wdmla.plugin.vanilla;

import com.gtnewhorizons.wdmla.api.identifier.VanillaIDs;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.api.ui.helper.ThemeHelper;

public enum CustomMetaDataHeaderProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        Block block = accessor.getBlock();
        ItemStack newItemStack = null;
        if ((block == Blocks.leaves || block == Blocks.leaves2) && (accessor.getMetadata() > 3)) {
            newItemStack = new ItemStack(block, 1, accessor.getMetadata() - 4);
        }

        if (block == Blocks.log || block == Blocks.log2) {
            newItemStack = new ItemStack(block, 1, accessor.getMetadata() % 4);
        }

        if ((block == Blocks.quartz_block) && (accessor.getMetadata() > 2)) {
            newItemStack = new ItemStack(block, 1, 2);
        }

        if (newItemStack != null) {
            ThemeHelper.instance().overrideTooltipTitle(tooltip, newItemStack);
            ThemeHelper.instance().overrideTooltipIcon(tooltip, newItemStack, false);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return VanillaIDs.CUSTOM_META_HEADER;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.HEAD;
    }
}
