package com.gtnewhorizons.wdmla.plugin.harvestability;

import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.harvestability.EffectiveTool;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityTestPhase;
import com.gtnewhorizons.wdmla.api.provider.HarvestHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

// liquid has very special behavior so we override it at the very end
public enum LiquidHarvestHandler implements HarvestHandler {
    INSTANCE;

    public static final EffectiveTool BUCKET = new EffectiveTool("bucket",
            Arrays.asList(new ItemStack(Items.bucket)));

    @Override
    public boolean testHarvest(HarvestabilityInfo info, HarvestabilityTestPhase phase, EntityPlayer player, Block block, int meta, MovingObjectPosition position) {
        if (phase == HarvestabilityTestPhase.EFFECTIVE_TOOL_NAME) {
            if (isStaticWaterOrLava(block)) {
                info.setEffectiveTool(BUCKET);
            }
            //flowing liquids and mod fluids
            else if (block instanceof BlockLiquid) {
                info.setEffectiveTool(EffectiveTool.CANNOT_HARVEST);
            }
        }
        else if (phase == HarvestabilityTestPhase.CURRENTLY_HARVESTABLE) {
            if (isStaticWaterOrLava(block)) {
                info.setCurrentlyHarvestable(player.getHeldItem() != null && player.getHeldItem().getItem() == Items.bucket);
            }
            else if (block instanceof BlockLiquid) {
                info.setCurrentlyHarvestable(false);
            }
        }
        else if (phase == HarvestabilityTestPhase.IS_HELD_TOOL_EFFECTIVE) {
            if (isStaticWaterOrLava(block)) {
                info.setHeldToolEffective(player.getHeldItem() != null && player.getHeldItem().getItem() == Items.bucket);
            }
            else if (block instanceof BlockLiquid) {
                info.setHeldToolEffective(false);
            }
        }

        return true;
    }

    private boolean isStaticWaterOrLava(Block block) {
        if (block instanceof BlockStaticLiquid staticLiquid) {
            return staticLiquid.getMaterial() == Material.water || staticLiquid.getMaterial() == Material.lava;
        }

        return false;
    }

    @Override
    public ResourceLocation getUid() {
        return HarvestabilityIdentifiers.LIQUID;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL - 100;
    }
}
