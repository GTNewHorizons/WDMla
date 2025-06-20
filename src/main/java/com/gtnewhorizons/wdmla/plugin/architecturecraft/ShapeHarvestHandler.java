package com.gtnewhorizons.wdmla.plugin.architecturecraft;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityTestPhase;
import com.gtnewhorizons.wdmla.api.provider.HarvestHandler;

import gcewing.architecture.common.block.BlockShape;
import gcewing.architecture.compat.BlockCompatUtils;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.IBlockState;

public enum ShapeHarvestHandler implements HarvestHandler {

    INSTANCE;

    @Override
    public boolean testHarvest(HarvestabilityInfo info, HarvestabilityTestPhase phase, EntityPlayer player, Block block,
            int meta, MovingObjectPosition position) {
        return true;
    }

    @Override
    public Block getEffectiveBlock(EntityPlayer player, Block block, ItemStack itemForm, int meta,
            MovingObjectPosition position) {
        if (!(block instanceof BlockShape blockShape)) {
            return null;
        }
        IBlockState state = blockShape
                .getBaseBlockState(player.worldObj, new BlockPos(position.blockX, position.blockY, position.blockZ));
        if (state == null) {
            return null;
        }

        return state.getBlock();
    }

    @Override
    public Integer getEffectiveMeta(EntityPlayer player, Block block, ItemStack itemForm, int meta,
            MovingObjectPosition position) {
        if (!(block instanceof BlockShape blockShape)) {
            return null;
        }
        IBlockState state = blockShape
                .getBaseBlockState(player.worldObj, new BlockPos(position.blockX, position.blockY, position.blockZ));
        if (state == null) {
            return null;
        }

        return BlockCompatUtils.getMetaFromBlockState(state);
    }

    @Override
    public ResourceLocation getUid() {
        return ArchitectureCraftPlugin.path("shape_harvest");
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL;
    }
}
