package com.gtnewhorizons.wdmla.plugin.harvestability.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gtnewhorizons.wdmla.api.harvestability.EffectiveTool;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestLevel;
import com.gtnewhorizons.wdmla.plugin.harvestability.BaseHarvestLogicHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeHooks;

import com.gtnewhorizons.wdmla.api.Mods;
import com.gtnewhorizons.wdmla.config.PluginsConfig;

import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.ToolBuilder;
import tconstruct.library.tools.DualHarvestTool;
import tconstruct.library.tools.HarvestTool;
import tconstruct.library.util.HarvestLevels;
import tconstruct.tools.TinkerTools;

public class ProxyTinkersConstruct {

    private static Method getHarvestType = null;
    private static Method getSecondHarvestType = null;
    public static final List<ItemStack> defaultPickaxes = new ArrayList<>();
    public static EffectiveTool pickaxe;

    public static void init() {
        try {
            getHarvestType = HarvestTool.class.getDeclaredMethod("getHarvestType");
            getSecondHarvestType = DualHarvestTool.class.getDeclaredMethod("getSecondHarvestType");
            getHarvestType.setAccessible(true);
            getSecondHarvestType.setAccessible(true);
            for (int materialID : TConstructRegistry.toolMaterials.keySet()) {
                defaultPickaxes.add(buildDefaultPickaxe(materialID));
            }
        } catch (Exception ignore) {}

        initPickaxeTool();
    }

    /**
     * Sets the icon of the effective Pickaxe from config. Important note: the default config value is tuned for Iguana
     * Tweaks with vanilla mode disabled. You have to edit the config in order to play with vanilla mode or TiC alone
     * See: <a href=
     * "https://github.com/GTNewHorizons/TinkersConstruct/blob/master/src/main/java/tconstruct/tools/TinkerTools.java#L1771">...</a>
     */
    public static void initPickaxeTool() {
        PluginsConfig.Harvestability.TinkersConstruct tiCConfig = PluginsConfig.harvestability.tinkersConstruct;
        pickaxe = new EffectiveTool("pickaxe",
                Arrays.asList(
                        defaultPickaxes.get(tiCConfig.harvestLevel0),
                        defaultPickaxes.get(tiCConfig.harvestLevel1),
                        defaultPickaxes.get(tiCConfig.harvestLevel2),
                        defaultPickaxes.get(tiCConfig.harvestLevel3),
                        defaultPickaxes.get(tiCConfig.harvestLevel4),
                        defaultPickaxes.get(tiCConfig.harvestLevel5)
                ));
    }

    public static boolean hasToolTag(ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("InfiTool");
    }

    public static NBTTagCompound getToolTag(ItemStack tool) {
        NBTTagCompound tag = null;
        if (tool.hasTagCompound()) tag = tool.getTagCompound().getCompoundTag("InfiTool");
        return tag;
    }

    public static HarvestLevel getPrimaryHarvestLevel(NBTTagCompound toolTag) {
        return new HarvestLevel(toolTag.getInteger("HarvestLevel"));
    }

    public static HarvestLevel getSecondaryHarvestLevel(NBTTagCompound toolTag) {
        return new HarvestLevel(toolTag.getInteger("HarvestLevel2"));
    }

    public static boolean isToolEffectiveAgainst(ItemStack tool, Block block, int metadata, EffectiveTool effectiveTool) {
        if (Mods.TCONSTUCT.isLoaded() && tool.getItem() instanceof HarvestTool harvestTool) {
            EffectiveTool firstType = null;
            try {
                firstType = new EffectiveTool((String) getHarvestType.invoke(harvestTool), null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            EffectiveTool secondType = null;
            if (harvestTool instanceof DualHarvestTool) {
                try {
                    secondType = new EffectiveTool((String) getSecondHarvestType.invoke(harvestTool), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return (firstType != null && effectiveTool.isSameTool(firstType)) ||
                    (secondType != null && effectiveTool.isSameTool(secondType));
        }
        return BaseHarvestLogicHandler.isToolEffectiveAgainst(tool, block, metadata, effectiveTool);
    }

    public static boolean canToolHarvestLevel(ItemStack tool, Block block, int metadata, HarvestLevel harvestLevel) {
        boolean canTinkersToolHarvestBlock = false;

        NBTTagCompound toolTag = getToolTag(tool);
        if (toolTag != null) {
            canTinkersToolHarvestBlock = getPrimaryHarvestLevel(toolTag).doesSatisfy(harvestLevel)
                    || getSecondaryHarvestLevel(toolTag).doesSatisfy(harvestLevel);
        }

        return canTinkersToolHarvestBlock || ForgeHooks.canToolHarvestBlock(block, metadata, tool);
    }

    /**
     * build TCon Pickaxe for WDMla harvest tool display
     * @param materialID id of pickaxe head and binding material
     * @return a pickaxe with wood tool rod
     */
    public static ItemStack buildDefaultPickaxe(int materialID) {
        ItemStack tool = ToolBuilder.instance.buildTool(
                new ItemStack(TinkerTools.pickaxeHead, 1, materialID),
                new ItemStack(TinkerTools.toolRod, 1, 0),
                new ItemStack(TinkerTools.binding, 1, materialID),
                null,
                "");

        if (tool != null) {
            tool.getTagCompound().getCompoundTag("InfiTool").setBoolean("Built", true);
        }

        return tool;
    }

    public static class TiCHarvestLevel extends HarvestLevel {

        public TiCHarvestLevel(HarvestLevel vanillaLevel) {
            super(vanillaLevel);
        }

        @Override
        public String getName() {
            return HarvestLevels.getHarvestLevelName(value);
        }
    }
}
