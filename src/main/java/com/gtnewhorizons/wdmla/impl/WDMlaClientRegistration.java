package com.gtnewhorizons.wdmla.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.gtnewhorizons.wdmla.api.IWDMlaClientRegistration;
import com.gtnewhorizons.wdmla.api.accessor.Accessor;
import com.gtnewhorizons.wdmla.api.accessor.AccessorClientHandler;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessorImpl;
import com.gtnewhorizons.wdmla.api.accessor.EntityAccessor;
import com.gtnewhorizons.wdmla.api.accessor.EntityAccessorImpl;
import com.gtnewhorizons.wdmla.api.provider.IClientExtensionProvider;
import com.gtnewhorizons.wdmla.api.provider.IComponentProvider;
import com.gtnewhorizons.wdmla.api.view.FluidView;
import com.gtnewhorizons.wdmla.api.view.ItemView;
import com.gtnewhorizons.wdmla.api.view.ProgressView;
import com.gtnewhorizons.wdmla.impl.lookup.HierarchyLookup;

import mcp.mobius.waila.Waila;
import mcp.mobius.waila.client.KeyEvent;

public class WDMlaClientRegistration implements IWDMlaClientRegistration {

    private static final WDMlaClientRegistration INSTANCE = new WDMlaClientRegistration();

    public final HierarchyLookup<IComponentProvider<BlockAccessor>> blockComponentProviders;
    public final HierarchyLookup<IComponentProvider<EntityAccessor>> entityComponentProviders;
    private final Set<IComponentProvider<?>> allProviders;

    public final Map<ResourceLocation, IClientExtensionProvider<ItemStack, ItemView>> itemStorageProviders = Maps
            .newHashMap();
    public final Map<ResourceLocation, IClientExtensionProvider<FluidView.Data, FluidView>> fluidStorageProviders = Maps
            .newHashMap();
    public final Map<ResourceLocation, IClientExtensionProvider<ProgressView.Data, ProgressView>> progressProviders = Maps
            .newHashMap();

    public final Map<Class<Accessor>, AccessorClientHandler<Accessor>> accessorHandlers = Maps.newIdentityHashMap();

    private ClientRegistrationSession session;

    WDMlaClientRegistration() {
        blockComponentProviders = new HierarchyLookup<>(Block.class);
        entityComponentProviders = new HierarchyLookup<>(Entity.class);
        allProviders = new HashSet<>();
    }

    public static WDMlaClientRegistration instance() {
        return INSTANCE;
    }

    @Override
    public void registerEntityComponent(IComponentProvider<EntityAccessor> provider,
            Class<? extends Entity> entityClass) {
        if (isSessionActive()) {
            session.registerEntityComponent(provider, entityClass);
        } else {
            entityComponentProviders.register(entityClass, provider);
            allProviders.add(provider);
        }
    }

    @Override
    public void registerBlockComponent(IComponentProvider<BlockAccessor> provider, Class<? extends Block> blockClass) {
        if (isSessionActive()) {
            session.registerBlockComponent(provider, blockClass);
        } else {
            blockComponentProviders.register(blockClass, provider);
            allProviders.add(provider);
        }
    }

    public List<IComponentProvider<BlockAccessor>> getBlockProviders(Block block,
            Predicate<IComponentProvider<? extends Accessor>> filter) {
        return blockComponentProviders.get(block).stream().filter(filter).collect(Collectors.toList());
    }

    public List<IComponentProvider<EntityAccessor>> getEntityProviders(Entity entity,
            Predicate<IComponentProvider<? extends Accessor>> filter) {
        return entityComponentProviders.get(entity).stream().filter(filter).collect(Collectors.toList());
    }

    public ImmutableSet<IComponentProvider<?>> getAllProvidersWithoutInfo() {
        return ImmutableSet.copyOf(allProviders);
    }

    @Override
    public void registerItemStorageClient(IClientExtensionProvider<ItemStack, ItemView> provider) {
        Objects.requireNonNull(provider.getUid());
        itemStorageProviders.put(provider.getUid(), provider);
    }

    @Override
    public void registerFluidStorageClient(IClientExtensionProvider<FluidView.Data, FluidView> provider) {
        Objects.requireNonNull(provider.getUid());
        fluidStorageProviders.put(provider.getUid(), provider);
    }

    @Override
    public void registerProgressClient(IClientExtensionProvider<ProgressView.Data, ProgressView> provider) {
        Objects.requireNonNull(provider.getUid());
        progressProviders.put(provider.getUid(), provider);
    }

    @Override
    public boolean isServerConnected() {
        return Waila.instance.serverPresent;
    }

    @Override
    public boolean isShowDetailsPressed() {
        return Keyboard.isKeyDown(KeyEvent.key_details.getKeyCode());
    }

    @Override
    public NBTTagCompound getServerData() {
        return ObjectDataCenter.getServerData();
    }

    @Override
    public BlockAccessor.Builder blockAccessor() {
        Minecraft mc = Minecraft.getMinecraft();

        return new BlockAccessorImpl.Builder().level(mc.theWorld).player(mc.thePlayer)
                .serverConnected(isServerConnected()).serverData(getServerData()).showDetails(isShowDetailsPressed());
    }

    public EntityAccessor.Builder entityAccessor() {
        Minecraft mc = Minecraft.getMinecraft();

        return new EntityAccessorImpl.Builder().level(mc.theWorld).player(mc.thePlayer)
                .serverConnected(isServerConnected()).serverData(getServerData()).showDetails(isShowDetailsPressed());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Accessor> void registerAccessorHandler(Class<T> clazz, AccessorClientHandler<T> handler) {
        accessorHandlers.put((Class<Accessor>) clazz, (AccessorClientHandler<Accessor>) handler);
    }

    public AccessorClientHandler<Accessor> getAccessorHandler(Class<? extends Accessor> clazz) {
        return Objects.requireNonNull(accessorHandlers.get(clazz), () -> "No accessor handler for " + clazz);
    }

    public void loadComplete() {
        var priorities = WDMlaCommonRegistration.instance().priorities;
        blockComponentProviders.invalidate();
        blockComponentProviders.loadComplete(priorities);
        entityComponentProviders.invalidate();
        entityComponentProviders.loadComplete(priorities);
        session = null;
    }

    public void startSession() {
        if (session == null) {
            session = new ClientRegistrationSession(this);
        }
        session.reset();
    }

    public void endSession() {
        Preconditions.checkState(session != null, "Session not started");
        session.end();
    }

    public boolean isSessionActive() {
        return session != null && session.isActive();
    }
}
