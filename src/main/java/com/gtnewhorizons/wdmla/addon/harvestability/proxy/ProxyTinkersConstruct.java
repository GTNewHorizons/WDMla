package com.gtnewhorizons.wdmla.addon.harvestability.proxy;

import com.gtnewhorizons.wdmla.api.Mods;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeHooks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.gtnewhorizons.wdmla.addon.harvestability.proxy.TinkersHarvestMaterialIDs.*;

public class ProxyTinkersConstruct {

    private static Class<?> HarvestTool = null;
    private static Class<?> DualHarvestTool = null;
    private static Method getHarvestType = null;
    private static Method getSecondHarvestType = null;
    private static Method getHarvestLevelName = null;
    public static final List<ItemStack> creativePickaxes = new ArrayList<>();

    public static void init() {
        try {
            HarvestTool = Class.forName("tconstruct.library.tools.HarvestTool");
            DualHarvestTool = Class.forName("tconstruct.library.tools.DualHarvestTool");
            getHarvestType = HarvestTool.getDeclaredMethod("getHarvestType");
            getSecondHarvestType = DualHarvestTool.getDeclaredMethod("getSecondHarvestType");
            getHarvestType.setAccessible(true);
            getSecondHarvestType.setAccessible(true);
            Class<?> HarvestLevels = Class.forName("tconstruct.library.util.HarvestLevels");
            getHarvestLevelName = HarvestLevels.getDeclaredMethod("getHarvestLevelName", int.class);
            GameRegistry.findItem("TConstruct", "pickaxe").getSubItems(null, null, creativePickaxes);
        } catch (Exception ignore) {
        }
    }

    public static boolean hasToolTag(ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("InfiTool");
    }

    public static NBTTagCompound getToolTag(ItemStack tool) {
        NBTTagCompound tag = null;
        if (tool.hasTagCompound()) tag = tool.getTagCompound().getCompoundTag("InfiTool");
        return tag;
    }

    public static Set<String> getToolClassesOf(ItemStack tool) {
        return tool.getItem().getToolClasses(tool);
    }

    public static boolean isToolOfClass(ItemStack tool, String toolClass) {
        return getToolClassesOf(tool).contains(toolClass);
    }

    public static boolean toolHasAnyToolClass(ItemStack tool) {
        return !getToolClassesOf(tool).isEmpty();
    }

    public static int getPrimaryHarvestLevel(NBTTagCompound toolTag) {
        return toolTag.getInteger("HarvestLevel");
    }

    public static int getSecondaryHarvestLevel(NBTTagCompound toolTag) {
        return toolTag.getInteger("HarvestLevel2");
    }

    public static boolean isToolEffectiveAgainst(ItemStack tool, Block block, int metadata, String effectiveToolClass) {
        if (Mods.TCONSTUCT.isLoaded() && HarvestTool.isInstance(tool.getItem())) {
            Item item = tool.getItem();
            List<String> harvestTypes = new ArrayList<String>();
            try {
                harvestTypes.add((String) getHarvestType.invoke(item));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (DualHarvestTool.isInstance(item)) {
                try {
                    harvestTypes.add((String) getSecondHarvestType.invoke(item));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return harvestTypes.contains(effectiveToolClass);
        }
        return ForgeHooks.isToolEffective(tool, block, metadata)
                || (toolHasAnyToolClass(tool) ? isToolOfClass(tool, effectiveToolClass)
                : tool.getItem().getDigSpeed(tool, block, metadata) > 1.5f);
    }

    public static boolean canToolHarvestLevel(ItemStack tool, Block block, int metadata, int harvestLevel) {
        boolean canTinkersToolHarvestBlock = false;

        NBTTagCompound toolTag = getToolTag(tool);
        if (toolTag != null) {
            int toolHarvestLevel = Math
                    .max(getPrimaryHarvestLevel(toolTag), getSecondaryHarvestLevel(toolTag));
            canTinkersToolHarvestBlock = toolHarvestLevel >= harvestLevel;
        }

        return canTinkersToolHarvestBlock || ForgeHooks.canToolHarvestBlock(block, metadata, tool);
    }

    public static String getTicHarvestLevelName(int num) {
        String name = "Err";
        try {
            name =  (String) getHarvestLevelName.invoke(null, num);
        } catch (IllegalAccessException | InvocationTargetException ignore) {
        }

        return name;
    }

    public static ItemStack getEffectivePickaxeIcon(int num) {
        return switch (num) {
            case 0 -> creativePickaxes.get(ANY);
            case 1 -> creativePickaxes.get(IRON);
            case 2 -> creativePickaxes.get(REDSTONE);
            case 3 -> creativePickaxes.get(OBSIDIAN);
            case 4 -> creativePickaxes.get(COBALT);
            case 5 -> creativePickaxes.get(MANYULLYN);
            case 6 -> creativePickaxes.get(MANYULLYNPLUS);
            default -> new ItemStack(Blocks.iron_bars);
        };
    }
}
