package com.gtnewhorizons.wdmla.plugin.harvestability;

import com.gtnewhorizons.wdmla.api.harvestability.EffectiveTool;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityTestPhase;
import com.gtnewhorizons.wdmla.api.provider.HarvestHandler;
import com.gtnewhorizons.wdmla.config.PluginsConfig;
import com.gtnewhorizons.wdmla.plugin.vanilla.VanillaIdentifiers;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public enum VanillaHarvestToolHandler implements HarvestHandler {
    INSTANCE;

    private static final HashMap<EffectiveTool, ItemStack> testTools = new HashMap<>();

    public static final EffectiveTool TOOL_PICKAXE = new EffectiveTool("pickaxe", new HashMap<>() {
        {
            put(0, new ItemStack(Items.wooden_pickaxe));
            put(1, new ItemStack(Items.stone_pickaxe));
            put(2, new ItemStack(Items.iron_pickaxe));
            put(3, new ItemStack(Items.diamond_pickaxe));
        }
    });
    public static final EffectiveTool TOOL_SHOVEL = new EffectiveTool("shovel", new HashMap<>() {
        {
            put(0, new ItemStack(Items.wooden_shovel));
        }
    });
    public static final EffectiveTool TOOL_AXE = new EffectiveTool("axe", new HashMap<>() {
        {
            put(0, new ItemStack(Items.wooden_axe));
        }
    });
    public static final EffectiveTool TOOL_SWORD = new EffectiveTool("sword", new HashMap<>() {
        {
            put(0, new ItemStack(Items.wooden_sword));
        }
    });

    static {
        testTools.put(TOOL_PICKAXE, new ItemStack(Items.wooden_pickaxe));
        testTools.put(TOOL_SHOVEL, new ItemStack(Items.wooden_shovel));
        testTools.put(TOOL_AXE, new ItemStack(Items.wooden_axe));
    }

    @Override
    public void testHarvest(HarvestabilityInfo info, HarvestabilityTestPhase phase,
                            EntityPlayer player, Block block, int meta, MovingObjectPosition position) {
        if(phase == HarvestabilityTestPhase.EFFECTIVE_TOOL_NAME) {
            if (!info.effectiveTool.isValid()) {
                float hardness = block.getBlockHardness(player.worldObj, position.blockX, position.blockY, position.blockZ);
                if (hardness > 0f) {
                    for (Map.Entry<EffectiveTool, ItemStack> testToolEntry : testTools.entrySet()) {
                        ItemStack testTool = testToolEntry.getValue();
                        if (testTool.func_150997_a(block) >= ((ItemTool) testTool.getItem()).func_150913_i()
                                .getEfficiencyOnProperMaterial()) {
                            info.effectiveTool = testToolEntry.getKey();
                            break;
                        }
                    }
                }
            }
            else {
                for (Map.Entry<EffectiveTool, ItemStack> testTool : testTools.entrySet()) {
                    if (info.effectiveTool.isSameTool(testTool.getKey())) {
                        info.effectiveTool = testTool.getKey();
                    }
                }
                if (info.effectiveTool.isSameTool(TOOL_SWORD)) {
                    info.effectiveTool = TOOL_SWORD;
                }
            }
        }
        else if (phase == HarvestabilityTestPhase.ADDITIONAL_TOOLS_ICON) {
            if (PluginsConfig.harvestability.icon.showShearabilityIcon) {
                Map.Entry<ItemStack, Boolean> canShear = BlockHelper.getShearability(player, block, meta, position);
                if (canShear != null) {
                    info.additionalToolsIcon.add(canShear);
                }
            }
            if (PluginsConfig.harvestability.icon.showSilkTouchabilityIcon) {
                Map.Entry<ItemStack, Boolean> canSilktouch = BlockHelper.getSilktouchAbility(player, block, meta, position);
                if (canSilktouch != null) {
                    info.additionalToolsIcon.add(canSilktouch);
                }
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return VanillaIdentifiers.HARVEST_TOOL;
    }

    @Override
    public int getDefaultPriority() {
        return 1000;
    }
}
