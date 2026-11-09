package com.gtnewhorizons.wdmla.example;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.config.General;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;

public enum ExampleHeaderProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public ResourceLocation getUid() {
        return Identifiers.EXAMPLE_HEAD;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.HEAD;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        ThemeHelper.INSTANCE.overrideTooltipIcon(tooltip, new ItemStack(Blocks.lit_furnace), true);
        ThemeHelper.INSTANCE.overrideTooltipTitle(tooltip, "Furnace");
        if (General.modName.hudShow) {
            tooltip.replaceChildWithTag(
                    Identifiers.MOD_NAME,
                    ThemeHelper.INSTANCE.modName("WDMla").tag(Identifiers.MOD_NAME));
        }
    }
}
