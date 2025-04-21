package com.gtnewhorizons.wdmla.api;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.ApiStatus;

import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.accessor.EntityAccessor;
import com.gtnewhorizons.wdmla.api.provider.IServerDataProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerExtensionProvider;
import com.gtnewhorizons.wdmla.api.view.FluidView;
import com.gtnewhorizons.wdmla.api.view.ProgressView;

/**
 * Main server registration class of WDMla.<br>
 * All registration that requires mod-wide reference should go here.
 * 
 * @see IWDMlaClientRegistration
 */
@ApiStatus.NonExtendable
public interface IWDMlaCommonRegistration {

    /**
     * registers server side provider that provides block info
     * 
     * @param dataProvider           provider instance
     * @param blockOrTileEntityClass the block or tile entity class the provider want to support
     */
    void registerBlockDataProvider(IServerDataProvider<BlockAccessor> dataProvider, Class<?> blockOrTileEntityClass);

    /**
     * registers server side provider that provides entity info
     * 
     * @param dataProvider provider instance
     * @param entityClass  the entity class the provider want to support
     */
    void registerEntityDataProvider(IServerDataProvider<EntityAccessor> dataProvider,
            Class<? extends Entity> entityClass);

    @ApiStatus.Experimental
    <T> void registerItemStorage(IServerExtensionProvider<ItemStack> provider, Class<? extends T> clazz);

    @ApiStatus.Experimental
    <T> void registerFluidStorage(IServerExtensionProvider<FluidView.Data> provider, Class<? extends T> clazz);

    @ApiStatus.Experimental
    <T> void registerProgress(IServerExtensionProvider<ProgressView.Data> provider, Class<? extends T> clazz);
}
