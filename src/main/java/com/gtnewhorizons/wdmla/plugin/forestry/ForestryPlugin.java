package com.gtnewhorizons.wdmla.plugin.forestry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import com.gtnewhorizons.wdmla.api.IWDMlaClientRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaPlugin;
import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.WDMlaPlugin;
import com.gtnewhorizons.wdmla.config.WDMlaConfig;

import forestry.apiculture.blocks.BlockBeehives;

// TODO: finish supporting rest of tile entities
@WDMlaPlugin(uid = "forestry", dependencies = "Forestry")
public class ForestryPlugin implements IWDMlaPlugin {

    @Override
    public void registerClient(IWDMlaClientRegistration registration) {
        registration.registerHarvest(ForestryToolHarvestHandler.INSTANCE, BlockBeehives.class);

        WDMlaConfig.instance().getCategory(Identifiers.CONFIG_AUTOGEN + Configuration.CATEGORY_SPLITTER + "forestry")
                .setLanguageKey("provider.wdmla.forestry.category");
    }

    public static ResourceLocation path(String path) {
        return new ResourceLocation("forestry", path);
    }
}
