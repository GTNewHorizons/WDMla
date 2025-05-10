package com.gtnewhorizons.wdmla.plugin.core;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

import com.gtnewhorizons.wdmla.api.IWDMlaClientRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaCommonRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaPlugin;
import com.gtnewhorizons.wdmla.api.identifier.WDMlaIDs;
import com.gtnewhorizons.wdmla.api.WDMlaPlugin;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.accessor.EntityAccessor;
import com.gtnewhorizons.wdmla.impl.BlockAccessorClientHandler;
import com.gtnewhorizons.wdmla.impl.EntityAccessorClientHandler;

/**
 * The base plugin that registers everything essential.<br>
 * any provider that provides important information to very wide range objects should be registered here.
 */
@SuppressWarnings("unused")
@WDMlaPlugin(uid = WDMlaIDs.NAMESPACE_CORE)
public class CorePlugin implements IWDMlaPlugin {

    @Override
    public void registerClient(IWDMlaClientRegistration registration) {
        registration.registerAccessorHandler(BlockAccessor.class, new BlockAccessorClientHandler());
        registration.registerAccessorHandler(EntityAccessor.class, new EntityAccessorClientHandler());

        registration.registerBlockComponent(DefaultBlockInfoProvider.INSTANCE, Block.class);
        registration.registerBlockComponent(EnchantmentPowerProvider.INSTANCE, Block.class);
        registration.registerBlockComponent(BlockFaceProvider.INSTANCE, Block.class);
        registration.registerBlockComponent(BreakProgressTextProvider.INSTANCE, Block.class);

        registration.registerEntityComponent(DefaultEntityInfoProvider.INSTANCE, Entity.class);
        registration.registerEntityComponent(EntityHealthProvider.INSTANCE, Entity.class);
        registration.registerEntityComponent(EntityEquipmentProvider.INSTANCE, EntityLiving.class);
        registration.registerEntityComponent(EntityArmorProvider.INSTANCE, EntityLiving.class);
        registration.registerEntityComponent(StatusEffectProvider.INSTANCE, EntityLiving.class);
    }

    @Override
    public void register(IWDMlaCommonRegistration registration) {
        registration.registerEntityDataProvider(StatusEffectProvider.INSTANCE, EntityLiving.class);
    }
}
