package com.gtnewhorizons.wdmla.test;

import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;

import com.gtnewhorizons.wdmla.api.IWDMlaClientRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaCommonRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaPlugin;

public class TestPlugin implements IWDMlaPlugin {

    @Override
    public void register(IWDMlaCommonRegistration registration) {
        registration.registerBlockDataProvider(TestNBTBlockProvider.INSTANCE, BlockCommandBlock.class);
        registration.registerEntityDataProvider(TestEntityProvider.INSTANCE, EntityPig.class);
        registration.registerItemStorage(TestItemStorageProvider.INSTANCE, BlockDispenser.class);
        registration.registerFluidStorage(TestFluidStorageProvider.INSTANCE, EntitySheep.class);
        registration.registerProgress(TestProgressProvider.INSTANCE, BlockSign.class);
    }

    @Override
    public void registerClient(IWDMlaClientRegistration registration) {
        registration.registerBlockComponent(TestHeaderProvider.INSTANCE, BlockCommandBlock.class);
        registration.registerBlockComponent(TestNBTBlockProvider.INSTANCE, BlockCommandBlock.class);
        registration.registerBlockComponent(TestThemeBlockProvider.INSTANCE, BlockStoneSlab.class);
        registration.registerEntityComponent(TestEntityProvider.INSTANCE, EntityPig.class);
        registration.registerItemStorageClient(TestItemStorageProvider.INSTANCE);
        registration.registerFluidStorageClient(TestFluidStorageProvider.INSTANCE);
        registration.registerProgressClient(TestProgressProvider.INSTANCE);
    }
}
