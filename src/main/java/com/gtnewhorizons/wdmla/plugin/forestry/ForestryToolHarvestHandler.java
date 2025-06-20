package com.gtnewhorizons.wdmla.plugin.forestry;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.harvestability.EffectiveTool;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityTestPhase;
import com.gtnewhorizons.wdmla.api.provider.HarvestHandler;

import forestry.plugins.PluginApiculture;

// TODO: grafter, wrench
public enum ForestryToolHarvestHandler implements HarvestHandler {

    INSTANCE;

    private static final EffectiveTool SCOOP = new EffectiveTool(
            "scoop",
            Arrays.asList(PluginApiculture.items.scoop.getItemStack()));

    @Override
    public boolean testHarvest(HarvestabilityInfo info, HarvestabilityTestPhase phase, EntityPlayer player, Block block,
            int meta, MovingObjectPosition position) {
        if (phase == HarvestabilityTestPhase.EFFECTIVE_TOOL_NAME) {
            if (info.getEffectiveTool().isSameTool(SCOOP)) {
                info.setEffectiveTool(SCOOP);
            }
        }
        return true;
    }

    @Override
    public ResourceLocation getUid() {
        return ForestryPlugin.path("tool_harvest");
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL;
    }
}
