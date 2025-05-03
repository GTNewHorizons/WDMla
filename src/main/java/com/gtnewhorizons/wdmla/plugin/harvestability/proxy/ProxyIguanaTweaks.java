package com.gtnewhorizons.wdmla.plugin.harvestability.proxy;

import com.gtnewhorizons.wdmla.api.harvestability.EffectiveTool;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestLevel;
import com.gtnewhorizons.wdmla.config.PluginsConfig;
import net.minecraft.item.ItemStack;
import tconstruct.library.util.HarvestLevels;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import static com.gtnewhorizons.wdmla.plugin.harvestability.proxy.ProxyTinkersConstruct.defaultPickaxes;

public class ProxyIguanaTweaks {

    private static Class<?> HarvestLevels = null;
    private static Method proxyGetHarvestLevelName;
    public static EffectiveTool pickaxe;

    public static void init() {
        try {
            HarvestLevels = Class.forName("iguanaman.iguanatweakstconstruct.util.HarvestLevels");
            proxyGetHarvestLevelName = HarvestLevels.getDeclaredMethod("getHarvestLevelName", int.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initPickaxeTool();
    }

    /**
     * Sets the icon of the effective Pickaxe from config.
     *
     * @see ProxyTinkersConstruct#initPickaxeTool()
     */
    public static void initPickaxeTool() {
        PluginsConfig.Harvestability.IguanaTweaks iguanaConfig = PluginsConfig.harvestability.iguanaTweaks;
        pickaxe = new EffectiveTool("pickaxe",
                Arrays.asList(
                        defaultPickaxes.get(iguanaConfig.harvestLevel0),
                        defaultPickaxes.get(iguanaConfig.harvestLevel1),
                        defaultPickaxes.get(iguanaConfig.harvestLevel2),
                        defaultPickaxes.get(iguanaConfig.harvestLevel3),
                        defaultPickaxes.get(iguanaConfig.harvestLevel4),
                        defaultPickaxes.get(iguanaConfig.harvestLevel5),
                        defaultPickaxes.get(iguanaConfig.harvestLevel6),
                        defaultPickaxes.get(iguanaConfig.harvestLevel7),
                        defaultPickaxes.get(iguanaConfig.harvestLevel8),
                        defaultPickaxes.get(iguanaConfig.harvestLevel9)
                ));
    }

    public static class IguanaHarvestLevel extends HarvestLevel {

        public IguanaHarvestLevel(HarvestLevel vanillaLevel) {
            super(vanillaLevel);
        }

        @Override
        public String getName() {
            String harvestLevelName = "<Unknown>";

            try {
                harvestLevelName = (String) proxyGetHarvestLevelName.invoke(null, value);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return harvestLevelName;
        }
    }
}
