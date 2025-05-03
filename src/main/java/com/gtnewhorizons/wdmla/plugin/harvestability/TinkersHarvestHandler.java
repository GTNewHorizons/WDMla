package com.gtnewhorizons.wdmla.plugin.harvestability;

import com.gtnewhorizons.wdmla.api.harvestability.HarvestLevel;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityTestPhase;
import com.gtnewhorizons.wdmla.api.provider.HarvestHandler;
import com.gtnewhorizons.wdmla.plugin.harvestability.proxy.ProxyTinkersConstruct;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

public enum TinkersHarvestHandler implements HarvestHandler {
    INSTANCE;

    @Override
    public boolean testHarvest(HarvestabilityInfo info, HarvestabilityTestPhase phase, EntityPlayer player, Block block, int meta, MovingObjectPosition position) {
        if (phase == HarvestabilityTestPhase.EFFECTIVE_TOOL_NAME) {
            if (info.getEffectiveTool().isSameTool(ProxyTinkersConstruct.pickaxe)) {
                //override vanilla pickaxe icon
                info.setEffectiveTool(ProxyTinkersConstruct.pickaxe);
            }
        }
        else if (phase == HarvestabilityTestPhase.HARVEST_LEVEL) {
            info.setHarvestLevel(new ProxyTinkersConstruct.TiCHarvestLevel(info.getHarvestLevel()));
        }
        else if (phase == HarvestabilityTestPhase.CURRENTLY_HARVESTABLE) {
            if (player.getHeldItem() != null) {
                info.setCurrentlyHarvestable(isCurrentlyHarvestable(player, block, meta, player.getHeldItem(), info.getHarvestLevel()));
            }
        }
        else if (phase == HarvestabilityTestPhase.IS_HELD_TOOL_EFFECTIVE) {
            ItemStack tool = player.getHeldItem();
            if (tool != null) {
                boolean isHoldingTinkersTool = ProxyTinkersConstruct.hasToolTag(tool);
                boolean isEffective = ProxyTinkersConstruct.isToolEffectiveAgainst(tool, block, meta, info.getEffectiveTool());
                info.setHeldToolEffective(isEffective && (!isHoldingTinkersTool || info.isCurrentlyHarvestable()));
            }
        }

        return true;
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
    public boolean isCurrentlyHarvestable(EntityPlayer player, Block block, int meta, @NotNull ItemStack itemHeld, HarvestLevel harvestLevel) {
        boolean isHoldingTinkersTool = ProxyTinkersConstruct.hasToolTag(itemHeld);
        boolean isHeldToolCorrect = BaseHarvestLogicHandler.canToolHarvestBlock(itemHeld, block)
                || (!isHoldingTinkersTool && block.canHarvestBlock(player, meta));
        boolean isAboveMinHarvestLevel = ProxyTinkersConstruct.canToolHarvestLevel(itemHeld, block, meta, harvestLevel);
        return (isHeldToolCorrect && isAboveMinHarvestLevel)
                || !isHoldingTinkersTool
                && ForgeHooks.canHarvestBlock(block, player, meta);
    }
}
