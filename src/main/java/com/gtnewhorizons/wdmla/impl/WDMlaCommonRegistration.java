package com.gtnewhorizons.wdmla.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Preconditions;
import com.gtnewhorizons.wdmla.api.IWDMlaCommonRegistration;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.accessor.EntityAccessor;
import com.gtnewhorizons.wdmla.api.provider.IServerDataProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerExtensionProvider;
import com.gtnewhorizons.wdmla.api.provider.IWDMlaProvider;
import com.gtnewhorizons.wdmla.api.view.FluidView;
import com.gtnewhorizons.wdmla.api.view.ProgressView;
import com.gtnewhorizons.wdmla.impl.lookup.HierarchyLookup;
import com.gtnewhorizons.wdmla.impl.lookup.PairHierarchyLookup;
import com.gtnewhorizons.wdmla.impl.lookup.WrappedHierarchyLookup;

public class WDMlaCommonRegistration implements IWDMlaCommonRegistration {

    private static final WDMlaCommonRegistration INSTANCE = new WDMlaCommonRegistration();

    public final PairHierarchyLookup<IServerDataProvider<BlockAccessor>> blockDataProviders;
    public final HierarchyLookup<IServerDataProvider<EntityAccessor>> entityDataProviders;
    public final PriorityStore<ResourceLocation, IWDMlaProvider> priorities;

    public final WrappedHierarchyLookup<IServerExtensionProvider<ItemStack>> itemStorageProviders;
    public final WrappedHierarchyLookup<IServerExtensionProvider<FluidView.Data>> fluidStorageProviders;
    public final WrappedHierarchyLookup<IServerExtensionProvider<ProgressView.Data>> progressProviders;

    private CommonRegistrationSession session;

    public static WDMlaCommonRegistration instance() {
        return INSTANCE;
    }

    WDMlaCommonRegistration() {
        blockDataProviders = new PairHierarchyLookup<>(
                new HierarchyLookup<>(Block.class),
                new HierarchyLookup<>(TileEntity.class));
        entityDataProviders = new HierarchyLookup<>(Entity.class);
        priorities = new PriorityStore<>(IWDMlaProvider::getDefaultPriority, IWDMlaProvider::getUid);
        priorities.setSortingFunction((store, allKeys) -> {
            List<ResourceLocation> keys = allKeys.stream()
                    // .filter(PluginConfig::isPrimaryKey)
                    .sorted(Comparator.comparingInt(store::byKey)).collect(Collectors.toCollection(ArrayList::new));
            // allKeys.stream().filter(Predicate.not(PluginConfig::isPrimaryKey)).forEach($ -> {
            // int index = keys.indexOf(PluginConfig.getPrimaryKey($));
            // keys.add(index + 1, $);
            // });
            return keys;
        });

        itemStorageProviders = WrappedHierarchyLookup.forAccessor();
        fluidStorageProviders = WrappedHierarchyLookup.forAccessor();
        progressProviders = WrappedHierarchyLookup.forAccessor();
    }

    @Override
    public void registerBlockDataProvider(IServerDataProvider<BlockAccessor> dataProvider,
            Class<?> blockOrTileEntityClass) {
        if (isSessionActive()) {
            session.registerBlockDataProvider(dataProvider, blockOrTileEntityClass);
        } else {
            blockDataProviders.register(blockOrTileEntityClass, dataProvider);
        }
    }

    @Override
    public void registerEntityDataProvider(IServerDataProvider<EntityAccessor> dataProvider,
            Class<? extends Entity> entityClass) {
        if (isSessionActive()) {
            session.registerEntityDataProvider(dataProvider, entityClass);
        } else {
            entityDataProviders.register(entityClass, dataProvider);
        }
    }

    public List<IServerDataProvider<BlockAccessor>> getBlockNBTProviders(Block block, @Nullable TileEntity tileEntity) {
        if (tileEntity == null) {
            return blockDataProviders.first.get(block);
        }
        return blockDataProviders.getMerged(block, tileEntity);
    }

    public List<IServerDataProvider<EntityAccessor>> getEntityNBTProviders(Entity entity) {
        return entityDataProviders.get(entity);
    }

    public void loadComplete() {
        blockDataProviders.invalidate();
        blockDataProviders.loadComplete(priorities);
        entityDataProviders.invalidate();
        entityDataProviders.loadComplete(priorities);
        itemStorageProviders.invalidate();
        itemStorageProviders.loadComplete(priorities);
        fluidStorageProviders.invalidate();
        fluidStorageProviders.loadComplete(priorities);
        session = null;
    }

    public void startSession() {
        if (session == null) {
            session = new CommonRegistrationSession(this);
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

    @Override
    public <T> void registerItemStorage(IServerExtensionProvider<ItemStack> provider, Class<? extends T> clazz) {
        itemStorageProviders.register(clazz, provider);
    }

    @Override
    public <T> void registerFluidStorage(IServerExtensionProvider<FluidView.Data> provider, Class<? extends T> clazz) {
        fluidStorageProviders.register(clazz, provider);
    }

    @Override
    public <T> void registerProgress(IServerExtensionProvider<ProgressView.Data> provider, Class<? extends T> clazz) {
        progressProviders.register(clazz, provider);
    }
}
