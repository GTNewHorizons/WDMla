package com.gtnewhorizons.wdmla.api.provider;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityTestPhase;

public interface HarvestHandler extends IWDMlaProvider {

    // return boolean: continue testing
    // TODO:javadoc
    boolean testHarvest(HarvestabilityInfo info, HarvestabilityTestPhase phase, EntityPlayer player, Block block,
            int meta, MovingObjectPosition position);

    /**
     * Implement This to fix meta block related issue
     * 
     * @return The correct block used for harvest level and interaction calculation
     */
    default Block getEffectiveBlock(EntityPlayer player, Block block, ItemStack itemForm, int meta,
            MovingObjectPosition position) {
        return null;
    }

    /**
     * Implement This to fix meta block related issue
     * 
     * @return The correct meta used for harvest level and interaction calculation
     */
    default Integer getEffectiveMeta(EntityPlayer player, Block block, ItemStack itemForm, int meta,
            MovingObjectPosition position) {
        return null;
    }

    /**
     * harvestHandler will not be registered with config
     */
    @Override
    default boolean isPriorityFixed() {
        return true;
    }

    /**
     * harvestHandler will not be registered with config
     */
    @Override
    default boolean canPrioritizeInGui() {
        return false;
    }
}
