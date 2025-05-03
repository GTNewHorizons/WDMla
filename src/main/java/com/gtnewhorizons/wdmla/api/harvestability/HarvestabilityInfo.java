package com.gtnewhorizons.wdmla.api.harvestability;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO: encapsulation
public class HarvestabilityInfo {
    /**
     * the primary harvest tool (pickaxe for hopper, shovel for dirt...)
     */
    public @NotNull EffectiveTool effectiveTool = EffectiveTool.NO_TOOL;

    /**
     * if -1: hand(no tool)
     */
    public @NotNull HarvestLevel harvestLevel = HarvestLevel.NO_TOOL;

    /**
     * can the block be harvested by player currently?
     */
    public boolean canHarvest = false;

    /**
     * the secondary harvest tool (wrench for hopper, shears for leaves)
     */
    public List<AdditionalToolInfo> additionalToolsInfo = new ArrayList<>();

    /**
     * can current held tool mine the block faster?
     */
    public boolean isHeldToolEffective = false;

    /**
     * stores information of additional tools relate to harvest
     */
    public static class AdditionalToolInfo {
        public final ItemStack icon;
        public final boolean isHolding;

        public AdditionalToolInfo(ItemStack icon, boolean isHolding) {
            this.icon = icon;
            this.isHolding = isHolding;
        }
    }
}
