package com.gtnewhorizons.wdmla.plugin.harvestability;

import com.gtnewhorizons.wdmla.api.harvestability.EffectiveTool;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestLevel;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityTestPhase;
import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.provider.HarvestHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

public enum BaseHarvestLogicHandler implements HarvestHandler {
    INSTANCE;

    @Override
    public boolean testHarvest(HarvestabilityInfo info, HarvestabilityTestPhase phase, EntityPlayer player, Block block, int meta, MovingObjectPosition position) {
        if (phase == HarvestabilityTestPhase.EFFECTIVE_TOOL_NAME) {
            if (!player.isCurrentToolAdventureModeExempt(position.blockX, position.blockY, position.blockZ)
                    || isBlockUnbreakable(block, player.worldObj, position.blockX, position.blockY, position.blockZ)) {
                info.setEffectiveTool(EffectiveTool.CANNOT_HARVEST);
            }
            else {
                info.setEffectiveTool(new EffectiveTool(block.getHarvestTool(meta), null));
            }
        }
        else if (phase == HarvestabilityTestPhase.HARVEST_LEVEL) {
            info.setHarvestLevel(info.getEffectiveTool().getHarvestLevel(block, meta));
        }
        else if (phase == HarvestabilityTestPhase.CURRENTLY_HARVESTABLE) {
            if (info.getEffectiveTool().isSameTool(EffectiveTool.CANNOT_HARVEST)) {
                info.setCurrentlyHarvestable(false);
                info.setHarvestLevel(HarvestLevel.NO_TOOL);
                return false;
            } else if (canInstaBreak(info.getHarvestLevel(), info.getEffectiveTool().isValid(), block, !info.getAdditionalToolsInfo().isEmpty())) {
                info.setEffectiveTool(EffectiveTool.NO_TOOL);
                info.setCurrentlyHarvestable(true);
                info.setHarvestLevel(HarvestLevel.NO_TOOL);
                info.setHeldToolEffective(true);
                return false;
            } else if (player.getHeldItem() == null) {
                info.setCurrentlyHarvestable(ForgeHooks.canHarvestBlock(block, player, meta));
            } else {
                info.setCurrentlyHarvestable(isCurrentlyHarvestable(player, block, meta, player.getHeldItem()));
            }
        }
        else if (phase == HarvestabilityTestPhase.IS_HELD_TOOL_EFFECTIVE) {
            ItemStack tool = player.getHeldItem();
            if (tool != null) {
                boolean isEffective = isToolEffectiveAgainst(tool, block, meta, info.getEffectiveTool());
                info.setHeldToolEffective(isEffective && info.isCurrentlyHarvestable());
            }
        }

        return true;
    }

    @Override
    public ResourceLocation getUid() {
        return HarvestabilityIdentifiers.BASE_LOGIC;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.DEFAULT_INFO;
    }

    public boolean isBlockUnbreakable(Block block, World world, int x, int y, int z) {
        return block.getBlockHardness(world, x, y, z) == -1.0f;
    }

    public boolean canInstaBreak(HarvestLevel harvestLevel, boolean hasEffectiveTool, Block block, boolean hasAdditionalInfo) {
        boolean blockHasEffectiveTools = harvestLevel.isToolRequired() && hasEffectiveTool;
        return block.getMaterial().isToolNotRequired() && !blockHasEffectiveTools && !hasAdditionalInfo;
    }

    //vanilla simplified check handler
    public boolean isCurrentlyHarvestable(EntityPlayer player, Block block, int meta, @NotNull ItemStack itemHeld) {
        boolean isHeldToolCorrect = canToolHarvestBlock(itemHeld, block)
                || block.canHarvestBlock(player, meta);
        boolean isAboveMinHarvestLevel = ForgeHooks.canToolHarvestBlock(block, meta, itemHeld);
        return (isHeldToolCorrect && isAboveMinHarvestLevel)
                || ForgeHooks.canHarvestBlock(block, player, meta);
    }

    public static boolean canToolHarvestBlock(ItemStack tool, Block block) {
        return block.getMaterial().isToolNotRequired() || tool.func_150998_b(block); // func_150998_b = canHarvestBlock
    }

    public static boolean isToolEffectiveAgainst(ItemStack tool, Block block, int metadata, EffectiveTool effectiveTool) {
        return ForgeHooks.isToolEffective(tool, block, metadata)
                || (toolHasAnyToolClass(tool) ? effectiveTool.isToolInstance(tool)
                : tool.getItem().getDigSpeed(tool, block, metadata) > 1.5f);
    }

    public static boolean toolHasAnyToolClass(ItemStack tool) {
        return !tool.getItem().getToolClasses(tool).isEmpty();
    }
}
