package com.gtnewhorizons.wdmla.api.harvestability;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HarvestabilityInfoImpl implements HarvestabilityInfo {
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

    @Override
    public void setEffectiveTool(@NotNull EffectiveTool effectiveTool) {
        Objects.requireNonNull(effectiveTool);
        this.effectiveTool = effectiveTool;
    }

    @Override
    public @NotNull EffectiveTool getEffectiveTool() {
        return effectiveTool;
    }

    @Override
    public void setHarvestLevel(@NotNull HarvestLevel harvestLevel) {
        Objects.requireNonNull(harvestLevel);
        this.harvestLevel = harvestLevel;
    }

    @Override
    public @NotNull HarvestLevel getHarvestLevel() {
        return harvestLevel;
    }

    @Override
    public void setCurrentlyHarvestable(boolean canHarvest) {
        this.currentlyHarvestable = canHarvest;
    }

    @Override
    public boolean isCurrentlyHarvestable() {
        return currentlyHarvestable;
    }

    @Override
    public void registerAdditionalToolInfo(@NotNull AdditionalToolInfo toolInfo) {
        Objects.requireNonNull(toolInfo);
        additionalToolsInfo.add(toolInfo);
    }

    @Override
    public List<AdditionalToolInfo> getAdditionalToolsInfo() {
        return additionalToolsInfo;
    }

    @Override
    public void setHeldToolEffective(boolean heldToolEffective) {
        this.heldToolEffective = heldToolEffective;
    }

    @Override
    public boolean isHeldToolEffective() {
        return heldToolEffective;
    }

}
