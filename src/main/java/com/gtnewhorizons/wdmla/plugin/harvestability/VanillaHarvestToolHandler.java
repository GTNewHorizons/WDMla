package com.gtnewhorizons.wdmla.plugin.harvestability;

import com.gtnewhorizons.wdmla.api.harvestability.EffectiveTool;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityTestPhase;
import com.gtnewhorizons.wdmla.api.provider.HarvestHandler;
import com.gtnewhorizons.wdmla.config.PluginsConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum VanillaHarvestToolHandler implements HarvestHandler {
    INSTANCE;

    private static final HashMap<EffectiveTool, ItemStack> testTools = new HashMap<>();

    public static final EffectiveTool TOOL_PICKAXE = new EffectiveTool("pickaxe",
            Arrays.asList(
                    new ItemStack(Items.wooden_pickaxe),
                    new ItemStack(Items.stone_pickaxe),
                    new ItemStack(Items.iron_pickaxe),
                    new ItemStack(Items.diamond_pickaxe)));
    public static final EffectiveTool TOOL_SHOVEL = new EffectiveTool("shovel",
            Arrays.asList(new ItemStack(Items.wooden_shovel)));
    public static final EffectiveTool TOOL_AXE = new EffectiveTool("axe",
            Arrays.asList(new ItemStack(Items.wooden_axe)));
    public static final EffectiveTool TOOL_SWORD = new EffectiveTool("sword",
            Arrays.asList(new ItemStack(Items.wooden_sword)));

    static {
        testTools.put(TOOL_PICKAXE, new ItemStack(Items.wooden_pickaxe));
        testTools.put(TOOL_SHOVEL, new ItemStack(Items.wooden_shovel));
        testTools.put(TOOL_AXE, new ItemStack(Items.wooden_axe));
    }

    @Override
    public boolean testHarvest(HarvestabilityInfo info, HarvestabilityTestPhase phase,
                               EntityPlayer player, Block block, int meta, MovingObjectPosition position) {
        if (phase == HarvestabilityTestPhase.EFFECTIVE_TOOL_NAME) {
            if (!info.getEffectiveTool().isValid()) {
                float hardness = block.getBlockHardness(player.worldObj, position.blockX, position.blockY, position.blockZ);
                if (hardness > 0f) {
                    for (Map.Entry<EffectiveTool, ItemStack> testToolEntry : testTools.entrySet()) {
                        ItemStack testTool = testToolEntry.getValue();
                        if (testTool.func_150997_a(block) >= ((ItemTool) testTool.getItem()).func_150913_i()
                                .getEfficiencyOnProperMaterial()) {
                            info.setEffectiveTool(testToolEntry.getKey());
                            break;
                        }
                    }
                }
            }
            else {
                for (Map.Entry<EffectiveTool, ItemStack> testTool : testTools.entrySet()) {
                    if (info.getEffectiveTool().isSameTool(testTool.getKey())) {
                        info.setEffectiveTool(testTool.getKey());
                    }
                }
                if (info.getEffectiveTool().isSameTool(TOOL_SWORD)) {
                    info.setEffectiveTool(TOOL_SWORD);
                }
            }
        }
        else if (phase == HarvestabilityTestPhase.ADDITIONAL_TOOLS_ICON) {
            if (PluginsConfig.harvestability.icon.showShearabilityIcon) {
                HarvestabilityInfo.AdditionalToolInfo canShear = BlockHelper.getShearability(player, block, meta, position);
                if (canShear != null) {
                    info.registerAdditionalToolInfo(canShear);
                }
            }
            if (PluginsConfig.harvestability.icon.showSilkTouchabilityIcon) {
                HarvestabilityInfo.AdditionalToolInfo canSilktouch = BlockHelper.getSilktouchAbility(player, block, meta, position);
                if (canSilktouch != null) {
                    info.registerAdditionalToolInfo(canSilktouch);
                }
            }
        }
        return true;
    }

    @Override
    public ResourceLocation getUid() {
        return HarvestabilityIdentifiers.VANILLA;
    }

    @Override
    public int getDefaultPriority() {
        return 1000;
    }
}
