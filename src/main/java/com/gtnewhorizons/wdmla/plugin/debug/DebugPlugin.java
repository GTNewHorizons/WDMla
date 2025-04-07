package com.gtnewhorizons.wdmla.plugin.debug;

import com.gtnewhorizons.wdmla.api.IWDMlaClientRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaPlugin;
import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.config.WDMlaConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.config.Configuration;

public class DebugPlugin implements IWDMlaPlugin {

    @Override
    public void registerClient(IWDMlaClientRegistration registration) {
        registration.registerBlockComponent(RegistryNameProvider.getBlock(), Block.class);

        registration.registerEntityComponent(RegistryNameProvider.getEntity(), Entity.class);

        WDMlaConfig.instance()
                .getCategory(
                        Identifiers.CONFIG_AUTOGEN + Configuration.CATEGORY_SPLITTER
                                + Identifiers.NAMESPACE_DEBUG)
                .setLanguageKey("provider.wdmla.debug.category");
    }
}
