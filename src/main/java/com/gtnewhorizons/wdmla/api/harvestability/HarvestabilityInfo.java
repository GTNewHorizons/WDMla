package com.gtnewhorizons.wdmla.api.harvestability;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface HarvestabilityInfo {
    void setEffectiveTool(@NotNull EffectiveTool effectiveTool);

    @NotNull EffectiveTool getEffectiveTool();

    void setHarvestLevel(@NotNull HarvestLevel harvestLevel);

    @NotNull HarvestLevel getHarvestLevel();

    void setCurrentlyHarvestable(boolean canHarvest);

    boolean isCurrentlyHarvestable();

    void registerAdditionalToolInfo(@NotNull AdditionalToolInfo toolInfo);

    List<AdditionalToolInfo> getAdditionalToolsInfo();

    void setHeldToolEffective(boolean heldToolEffective);

    boolean isHeldToolEffective();

    /**
     * stores information of additional tools relate to harvest
     */
    class AdditionalToolInfo {
        public final ItemStack icon;
        //TODO: add text mode text
        public final boolean isHolding;

        public AdditionalToolInfo(ItemStack icon, boolean isHolding) {
            this.icon = icon;
            this.isHolding = isHolding;
        }
    }
}
