package com.gtnewhorizons.wdmla.plugin.vanilla;

import net.minecraft.block.BlockCrops;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.impl.ui.component.HPanelComponent;
import com.gtnewhorizons.wdmla.util.FormatUtil;

public enum GrowthRateProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        boolean iscrop = BlockCrops.class.isInstance(accessor.getBlock()); // Done to cover all inheriting mods
        if (iscrop || accessor.getBlock() == Blocks.melon_stem || accessor.getBlock() == Blocks.pumpkin_stem) {
            float growthValue = (accessor.getMetadata() / 7.0F);
            appendGrowthRate(tooltip, growthValue);
        } else if (accessor.getBlock() == Blocks.cocoa) {
            float growthValue = ((accessor.getMetadata() >> 2) / 2.0F);
            appendGrowthRate(tooltip, growthValue);
        } else if (accessor.getBlock() == Blocks.nether_wart) {
            float growthValue = (accessor.getMetadata() / 3.0F);
            appendGrowthRate(tooltip, growthValue);
        }
    }

    public void appendGrowthRate(ITooltip tooltip, float growthValue) {
        if (growthValue < 1) {
            tooltip.child(
                    ThemeHelper.INSTANCE
                            .value(
                                    StatCollector.translateToLocal("hud.msg.wdmla.growth"),
                                    FormatUtil.PERCENTAGE_STANDARD.format(growthValue))
                            .tag(VanillaIdentifiers.GROWTH_RATE));
        } else {
            tooltip.child(
                    new HPanelComponent()
                            .text(String.format("%s: ", StatCollector.translateToLocal("hud.msg.wdmla.growth")))
                            .child(
                                    ThemeHelper.INSTANCE.success(
                                            String.format(
                                                    "%s",
                                                    StatCollector.translateToLocal("hud.msg.wdmla.mature"))))
                            .tag(VanillaIdentifiers.GROWTH_RATE));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return VanillaIdentifiers.GROWTH_RATE;
    }
}
