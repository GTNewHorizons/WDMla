package com.gtnewhorizons.wdmla.api;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.ApiStatus;

import com.gtnewhorizons.wdmla.api.accessor.Accessor;
import com.gtnewhorizons.wdmla.api.accessor.AccessorClientHandler;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.accessor.EntityAccessor;
import com.gtnewhorizons.wdmla.api.provider.IClientExtensionProvider;
import com.gtnewhorizons.wdmla.api.provider.IComponentProvider;
import com.gtnewhorizons.wdmla.api.view.FluidView;
import com.gtnewhorizons.wdmla.api.view.ItemView;
import com.gtnewhorizons.wdmla.api.view.ProgressView;

/**
 * Main client registration class of WDMla.<br>
 * All registration that requires mod-wide reference should go here.
 * 
 * @see IWDMlaCommonRegistration
 */
@ApiStatus.NonExtendable
public interface IWDMlaClientRegistration {

    /**
     * Registers client side block provider.
     * 
     * @param provider   the provider instance
     * @param blockClass the target of the provider. If it is tile entity, it should specify its base block.
     */
    void registerBlockComponent(IComponentProvider<BlockAccessor> provider, Class<? extends Block> blockClass);

    /**
     * Registers client side entity provider.
     * 
     * @param provider    the provider instance
     * @param entityClass The target of provider. If a parent class is registered (like Animal) all child classes will
     *                    be targeted.
     */
    void registerEntityComponent(IComponentProvider<EntityAccessor> provider, Class<? extends Entity> entityClass);

    void registerItemStorageClient(IClientExtensionProvider<ItemStack, ItemView> provider);

    void registerFluidStorageClient(IClientExtensionProvider<FluidView.Data, FluidView> provider);

    void registerProgressClient(IClientExtensionProvider<ProgressView.Data, ProgressView> provider);

    /**
     * @return is server validation succeeded on world join
     */
    @ApiStatus.Internal
    boolean isServerConnected();

    /**
     * @return is "Show Details" key pressed or not<br>
     *         Note it no longer has to be the sneak key
     */
    @ApiStatus.Internal
    boolean isShowDetailsPressed();

    /**
     * Gets server data requested by entries in {@link IWDMlaCommonRegistration}.
     * 
     * @return nbt tag contains all server side info requested
     */
    @ApiStatus.Internal
    NBTTagCompound getServerData();

    /**
     * Gets the partially built default block accessor of current frame<br>
     */
    @ApiStatus.Internal
    BlockAccessor.Builder blockAccessor();

    /**
     * Gets the partially built default entity accessor of current frame<br>
     */
    @ApiStatus.Internal
    EntityAccessor.Builder entityAccessor();

    /**
     * registers accessor handlers to registry so the proper target information will be delivered to all providers
     * 
     * @param clazz   the accessor type which accessor handler wants to handle
     * @param handler accessor handler itself.
     * @param <T>     same as {@code clazz}
     */
    <T extends Accessor> void registerAccessorHandler(Class<T> clazz, AccessorClientHandler<T> handler);
}
