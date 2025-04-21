package com.gtnewhorizons.wdmla.plugin.universal;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.config.Configuration;

import com.gtnewhorizons.wdmla.api.IWDMlaClientRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaCommonRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaPlugin;
import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.WDMlaPlugin;
import com.gtnewhorizons.wdmla.config.WDMlaConfig;

/**
 * Registers special storage related providers.
 */
@SuppressWarnings("unused")
@WDMlaPlugin(uid = Identifiers.NAMESPACE_UNIVERSAL)
public class UniversalPlugin implements IWDMlaPlugin {

    @Override
    public void register(IWDMlaCommonRegistration registration) {
        registration.registerBlockDataProvider(ItemStorageProvider.getBlock(), Block.class);
        registration.registerEntityDataProvider(ItemStorageProvider.getEntity(), Entity.class);
        registration.registerBlockDataProvider(FluidStorageProvider.getBlock(), Block.class);
        registration.registerEntityDataProvider(FluidStorageProvider.getEntity(), Entity.class);
        registration.registerBlockDataProvider(ProgressProvider.getBlock(), Block.class);
        registration.registerEntityDataProvider(ProgressProvider.getEntity(), Entity.class);
    }

    @Override
    public void registerClient(IWDMlaClientRegistration registration) {
        registration.registerBlockComponent(ItemStorageProvider.getBlock(), Block.class);
        registration.registerEntityComponent(ItemStorageProvider.getEntity(), Entity.class);
        registration.registerBlockComponent(FluidStorageProvider.getBlock(), Block.class);
        registration.registerEntityComponent(FluidStorageProvider.getEntity(), Entity.class);
        registration.registerBlockComponent(ProgressProvider.getBlock(), Block.class);
        registration.registerEntityComponent(ProgressProvider.getEntity(), Entity.class);
        registration.registerItemStorageClient(ItemStorageProvider.Extension.INSTANCE);
        registration.registerFluidStorageClient(FluidStorageProvider.Extension.INSTANCE);

        WDMlaConfig.instance()
                .getCategory(
                        Identifiers.CONFIG_AUTOGEN + Configuration.CATEGORY_SPLITTER + Identifiers.NAMESPACE_UNIVERSAL)
                .setLanguageKey("provider.wdmla.universal.category");
    }
}
