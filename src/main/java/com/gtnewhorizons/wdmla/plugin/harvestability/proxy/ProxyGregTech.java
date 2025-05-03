package com.gtnewhorizons.wdmla.plugin.harvestability.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import com.gtnewhorizons.wdmla.api.harvestability.EffectiveTool;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.gtnewhorizons.wdmla.api.Mods;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * We support GregTech 5 tool stats on our side to support non-GTNH GT5-unofficial<br>
 * TODO: after The Great Renaming, this class no longer works outside GTNH, fix it
 */
public class ProxyGregTech {

    public static final String CASING_ID = "gt.blockcasings";
    public static final String CASING_UNIQUE_IDENTIFIER = Mods.GREGTECH.modID + ":" + CASING_ID;
    public static final String MACHINE_ID = "gt.blockmachines";
    public static final String MACHINE_UNIQUE_IDENTIFIER = Mods.GREGTECH.modID + ":" + MACHINE_ID;

    private static int wrenchID;
    private static int wireCutterID;
    private static ItemStack iconIronWrench;
    private static ItemStack iconSteelWrench;
    private static ItemStack iconIronWireCutter;
    public static EffectiveTool toolWrench;
    public static EffectiveTool toolWireCutter;

    @SuppressWarnings("unchecked")
    public static void init() {
        try {
            Class<?> GT_MetaGenerated_Tool = Class.forName("gregtech.api.items.MetaGeneratedTool");
            Field sInstancesField = GT_MetaGenerated_Tool.getField("sInstances");
            ConcurrentHashMap<String, ?> sInstances = (ConcurrentHashMap<String, ?>) (sInstancesField.get(null));
            Object metaTool01 = sInstances.get("gt.metatool.01");
            Class<?> Materials = Class.forName("gregtech.api.enums.Materials");
            Method getToolWithStatsMethod = GT_MetaGenerated_Tool
                    .getDeclaredMethod("getToolWithStats", int.class, int.class, Materials, Materials, long[].class);
            Class<?> GT_MetaGenerated_Tool_01 = Class.forName("gregtech.common.items.IDMetaTool01");

            Field idField = GT_MetaGenerated_Tool_01.getField("ID");
            Object wrenchConstant = Enum.valueOf((Class<Enum>) GT_MetaGenerated_Tool_01, "WRENCH");
            wrenchID = idField.getInt(wrenchConstant);
            Object wireCutterConstant = Enum.valueOf((Class<Enum>) GT_MetaGenerated_Tool_01, "WIRECUTTER");
            wireCutterID = idField.getInt(wireCutterConstant);

            Object ironMaterial = Materials.getField("Iron").get(null);
            Object steelMaterial = Materials.getField("Steel").get(null);

            iconIronWrench = (ItemStack) getToolWithStatsMethod
                    .invoke(metaTool01, wrenchID, 1, ironMaterial, ironMaterial, null);
            iconSteelWrench = (ItemStack) getToolWithStatsMethod
                    .invoke(metaTool01, wrenchID, 1, steelMaterial, steelMaterial, null);
            iconIronWireCutter = (ItemStack) getToolWithStatsMethod
                    .invoke(metaTool01, wireCutterID, 1, ironMaterial, ironMaterial, null);

            toolWrench = new EffectiveTool("wrench",
                    Arrays.asList(
                            iconIronWrench,
                            iconIronWrench,
                            iconIronWrench,
                            iconSteelWrench,
                            iconSteelWrench
                    ));
            toolWireCutter = new EffectiveTool("cutter",
                    Arrays.asList(
                            iconIronWireCutter,
                            iconIronWireCutter,
                            iconIronWireCutter
                    ));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Use a nested class so that ProxyGregTech can load without loading GTMethods. This allows us to keep GTUTIL_IS_ORE
     * static final without hacky code (MethodHandles have zero runtime cost if they're stored in a static final field).
     */
    private static class GTMethods {

        public static final MethodHandle GTUTIL_IS_ORE;

        static {
            try {
                GTUTIL_IS_ORE = MethodHandles.lookup().findStatic(
                        Class.forName("gregtech.api.util.GTUtility"),
                        "isOre",
                        MethodType.methodType(boolean.class, Block.class, int.class));
            } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isOreBlock(Block block, int meta) {
        if (!Mods.GREGTECH.isLoaded()) return false;

        try {
            // loads GTMethods
            return (boolean) GTMethods.GTUTIL_IS_ORE.invokeExact(block, meta);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isCasing(Block block) {
        return Mods.GREGTECH.isLoaded()
                && GameRegistry.findUniqueIdentifierFor(block).toString().equals(CASING_UNIQUE_IDENTIFIER);
    }

    public static boolean isMachine(Block block) {
        return Mods.GREGTECH.isLoaded()
                && GameRegistry.findUniqueIdentifierFor(block).toString().equals(MACHINE_UNIQUE_IDENTIFIER);
    }

    public static boolean isGTTool(ItemStack itemStack) {
        return Mods.GREGTECH.isLoaded() && itemStack.hasTagCompound()
                && itemStack.getTagCompound().hasKey("GT.ToolStats");
    }

    public static boolean isWrench(ItemStack itemStack) {
        return isGTTool(itemStack) && itemStack.getItemDamage() == wrenchID;
    }

    public static boolean isWireCutter(ItemStack itemStack) {
        return isGTTool(itemStack) && itemStack.getItemDamage() == wireCutterID;
    }
}
