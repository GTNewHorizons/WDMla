package com.gtnewhorizons.wdmla.plugin.harvestability;

import com.gtnewhorizons.wdmla.api.harvestability.EffectiveTool;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestLevel;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityTestPhase;
import com.gtnewhorizons.wdmla.api.provider.HarvestHandler;
import com.gtnewhorizons.wdmla.plugin.harvestability.proxy.ProxyGregTech;
import com.gtnewhorizons.wdmla.plugin.harvestability.proxy.ProxyTinkersConstruct;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

public enum GregTechHarvestHandler implements HarvestHandler {
    INSTANCE;

    @Override
    public boolean testHarvest(HarvestabilityInfo info, HarvestabilityTestPhase phase,
                               EntityPlayer player, Block block, int meta, MovingObjectPosition position) {
        if (phase == HarvestabilityTestPhase.EFFECTIVE_TOOL_NAME) {
            if(info.getEffectiveTool().isSameTool(ProxyGregTech.toolWrench)) {
                info.setEffectiveTool(ProxyGregTech.toolWrench);
            }
            else if (info.getEffectiveTool().isSameTool(ProxyGregTech.toolWireCutter)) {
                info.setEffectiveTool(ProxyGregTech.toolWireCutter);
            }
        }
        else if (phase == HarvestabilityTestPhase.CURRENTLY_HARVESTABLE) {
            if (player.getHeldItem() != null) {
                info.setCurrentlyHarvestable(
                        isCurrentlyHarvestable(player, block, meta, player.getHeldItem(), info.getEffectiveTool(), info.getHarvestLevel()));
            }
        }

        return true;
    }

    @Override
    public ResourceLocation getUid() {
        return HarvestabilityIdentifiers.GREGTECH;
    }

    @Override
    public Block getEffectiveBlock(Block block, ItemStack itemForm, int meta) {
        return isDisguised(block, itemForm, meta) ? Block.getBlockFromItem(itemForm.getItem()) : null;
    }

    @Override
    public Integer getEffectiveMeta(Block block, ItemStack itemForm, int meta) {
        return isDisguised(block, itemForm, meta) ? itemForm.getItemDamage() : null;
    }

    @Override
    public int getDefaultPriority() {
        return TinkersHarvestHandler.INSTANCE.getDefaultPriority() + 1000;
    }

    public boolean isDisguised(Block block, ItemStack itemForm, int meta) {
        return itemForm.getItem() instanceof ItemBlock && !ProxyGregTech.isOreBlock(block, meta)
                && !ProxyGregTech.isCasing(block)
                && !ProxyGregTech.isMachine(block);
    }

    // run full check
    public boolean isCurrentlyHarvestable(EntityPlayer player, Block block, int meta, @NotNull ItemStack itemHeld,
                                          EffectiveTool effectiveTool, HarvestLevel harvestLevel) {
        boolean isHoldingTinkersTool = ProxyTinkersConstruct.hasToolTag(itemHeld);
        boolean isHeldToolCorrect = isHeldToolCorrect(
                player,
                block,
                meta,
                itemHeld,
                effectiveTool,
                isHoldingTinkersTool);
        boolean isAboveMinHarvestLevel = ProxyTinkersConstruct.canToolHarvestLevel(itemHeld, block, meta, harvestLevel);
        return (isHeldToolCorrect && isAboveMinHarvestLevel)
                || (!ProxyGregTech.isMachine(block) && !isHoldingTinkersTool
                && ForgeHooks.canHarvestBlock(block, player, meta));
    }

    public static boolean isHeldToolCorrect(EntityPlayer player, Block block, int meta, @NotNull ItemStack itemHeld,
                                            EffectiveTool effectiveTool, boolean isHoldingTinkersTool) {
        if (ProxyGregTech.isMachine(block)) {
            // GT_MetaGenerated_Tool's getDigSpeed is broken
            return effectiveTool.isSameTool(ProxyGregTech.toolWrench)
                    && ProxyGregTech.isWrench(itemHeld)
                    || effectiveTool.isSameTool(ProxyGregTech.toolWireCutter)
                    && ProxyGregTech.isWireCutter(itemHeld);
        } else if (ProxyGregTech.isGTTool(itemHeld)) {
            // GT tool don't care net.minecraft.block.material.Material#isToolNotRequired
            return itemHeld.func_150998_b(block);
        } else {
            return BaseHarvestLogicHandler.canToolHarvestBlock(itemHeld, block)
                    || (!isHoldingTinkersTool && block.canHarvestBlock(player, meta));
        }
    }
}
