package com.gtnewhorizons.wdmla.api.provider;

import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityTestPhase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

public interface HarvestHandler extends IWDMlaProvider {

    //return boolean: continue testing
    //TODO:javadoc
    boolean testHarvest(HarvestabilityInfo info, HarvestabilityTestPhase phase, EntityPlayer player, Block block, int meta,
                             MovingObjectPosition position);

    /**
     * Implement This to fix meta block related issue
     * @return The correct block used for harvest level and interaction calculation
     */
    default Block getEffectiveBlock(Block block, ItemStack itemForm, int meta) {
        return null;
    }

    /**
     * Implement This to fix meta block related issue
     * @return The correct meta used for harvest level and interaction calculation
     */
    default Integer getEffectiveMeta(Block block, ItemStack itemForm, int meta) {
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
