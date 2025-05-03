package com.gtnewhorizons.wdmla.api.harvestability;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO: abstraction
public class HarvestabilityInfo {
    /**
     * the primary harvest tool (pickaxe for hopper, shovel for dirt...)
     */
    private @NotNull EffectiveTool effectiveTool = EffectiveTool.NO_TOOL;

    /**
     * if -1: hand(no tool)
     */
    private @NotNull HarvestLevel harvestLevel = HarvestLevel.NO_TOOL;

    /**
     * can the block be harvested by player currently?
     */
    private boolean currentlyHarvestable = false;

    /**
     * the secondary harvest tool (wrench for hopper, shears for leaves)
     */
    private final List<AdditionalToolInfo> additionalToolsInfo = new ArrayList<>();

    /**
     * can current held tool mine the block faster?
     */
    private boolean heldToolEffective = false;

    public void setEffectiveTool(@NotNull EffectiveTool effectiveTool) {
        Objects.requireNonNull(effectiveTool);
        this.effectiveTool = effectiveTool;
    }

    public @NotNull EffectiveTool getEffectiveTool() {
        return effectiveTool;
    }

    public void setHarvestLevel(@NotNull HarvestLevel harvestLevel) {
        Objects.requireNonNull(harvestLevel);
        this.harvestLevel = harvestLevel;
    }

    public @NotNull HarvestLevel getHarvestLevel() {
        return harvestLevel;
    }

    public void setCurrentlyHarvestable(boolean canHarvest) {
        this.currentlyHarvestable = canHarvest;
    }

    public boolean isCurrentlyHarvestable() {
        return currentlyHarvestable;
    }

    public void registerAdditionalToolInfo(@NotNull AdditionalToolInfo toolInfo) {
        Objects.requireNonNull(toolInfo);
        additionalToolsInfo.add(toolInfo);
    }

    public List<AdditionalToolInfo> getAdditionalToolsInfo() {
        return additionalToolsInfo;
    }

    public void setHeldToolEffective(boolean heldToolEffective) {
        this.heldToolEffective = heldToolEffective;
    }

    public boolean isHeldToolEffective() {
        return heldToolEffective;
    }

    /**
     * stores information of additional tools relate to harvest
     */
    public static class AdditionalToolInfo {
        public final ItemStack icon;
        //TODO: add text mode text
        public final boolean isHolding;

        public AdditionalToolInfo(ItemStack icon, boolean isHolding) {
            this.icon = icon;
            this.isHolding = isHolding;
        }
    }
}
