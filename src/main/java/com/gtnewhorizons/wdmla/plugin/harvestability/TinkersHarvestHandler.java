package com.gtnewhorizons.wdmla.plugin.harvestability;

import com.gtnewhorizons.wdmla.api.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.HarvestabilityTestPhase;
import com.gtnewhorizons.wdmla.api.provider.InteractionHandler;
import com.gtnewhorizons.wdmla.plugin.harvestability.proxy.ProxyTinkersConstruct;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

import static com.gtnewhorizons.wdmla.plugin.harvestability.VanillaHarvestToolHandler.TOOL_PICKAXE;

public enum TinkersHarvestHandler implements InteractionHandler {
    INSTANCE;

    @Override
    public void testHarvest(HarvestabilityInfo info, HarvestabilityTestPhase phase, EntityPlayer player, Block block, int meta, MovingObjectPosition position) {
        if (phase == HarvestabilityTestPhase.HARVEST_LEVEL_NAME) {
            info.harvestLevelName = ProxyTinkersConstruct.getTicHarvestLevelName(info.harvestLevel);
        }
        else if (phase == HarvestabilityTestPhase.EFFECTIVE_TOOL_ICON) {
            if (info.harvestLevel != -1 && TOOL_PICKAXE.equals(info.effectiveTool)) {
                //override vanilla pickaxe icon
                info.effectiveToolIcon = ProxyTinkersConstruct.getEffectivePickaxeIcon(info.harvestLevel);
            }
        }
        else if (phase == HarvestabilityTestPhase.CURRENTLY_HARVESTABLE) {
            if (player.getHeldItem() != null) {
                info.canHarvest = isCurrentlyHarvestable(player, block, meta, player.getHeldItem(), info.effectiveTool, info.harvestLevel);
            }
        }
        else if (phase == HarvestabilityTestPhase.IS_HELD_TOOL_EFFECTIVE) {
            ItemStack tool = player.getHeldItem();
            if (tool != null) {
                boolean isHoldingTinkersTool = ProxyTinkersConstruct.hasToolTag(tool);
                boolean isEffective = ProxyTinkersConstruct.isToolEffectiveAgainst(tool, block, meta, info.effectiveTool);
                info.isHeldToolEffective = isEffective && (!isHoldingTinkersTool || info.canHarvest);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return HarvestabilityIdentifiers.TINKERSCONSTRUCT;
    }

    @Override
    public int getDefaultPriority() {
        return VanillaHarvestToolHandler.INSTANCE.getDefaultPriority() + 1000;
    }

    //TiCon simplified check handler
    public boolean isCurrentlyHarvestable(EntityPlayer player, Block block, int meta, @NotNull ItemStack itemHeld,
                                          String effectiveTool, int harvestLevel) {
        boolean isHoldingTinkersTool = ProxyTinkersConstruct.hasToolTag(itemHeld);
        boolean isHeldToolCorrect = BaseHarvestLogicHandler.canToolHarvestBlock(itemHeld, block)
                || (!isHoldingTinkersTool && block.canHarvestBlock(player, meta));
        boolean isAboveMinHarvestLevel = ProxyTinkersConstruct.canToolHarvestLevel(itemHeld, block, meta, harvestLevel);
        return (isHeldToolCorrect && isAboveMinHarvestLevel)
                || !isHoldingTinkersTool
                && ForgeHooks.canHarvestBlock(block, player, meta);
    }
}
