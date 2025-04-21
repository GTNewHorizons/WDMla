package com.gtnewhorizons.wdmla.plugin.universal;

import static com.gtnewhorizons.wdmla.impl.ui.component.TooltipComponent.DEFAULT_PROGRESS_DESCRIPTION_PADDING;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.wdmla.ClientProxy;
import com.gtnewhorizons.wdmla.CommonProxy;
import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.accessor.Accessor;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.accessor.EntityAccessor;
import com.gtnewhorizons.wdmla.api.provider.IClientExtensionProvider;
import com.gtnewhorizons.wdmla.api.provider.IComponentProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerDataProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerExtensionProvider;
import com.gtnewhorizons.wdmla.api.ui.IComponent;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.api.view.ClientViewGroup;
import com.gtnewhorizons.wdmla.api.view.FluidView;
import com.gtnewhorizons.wdmla.api.view.ViewGroup;
import com.gtnewhorizons.wdmla.config.General;
import com.gtnewhorizons.wdmla.config.PluginsConfig;
import com.gtnewhorizons.wdmla.impl.WDMlaClientRegistration;
import com.gtnewhorizons.wdmla.impl.WDMlaCommonRegistration;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.impl.ui.component.FluidComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.HPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.ProgressComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.TextComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.VPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.drawable.FluidDrawable;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Padding;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;
import com.gtnewhorizons.wdmla.impl.ui.style.ProgressStyle;
import com.gtnewhorizons.wdmla.util.FormatUtil;

import mcp.mobius.waila.overlay.DisplayUtil;

public class FluidStorageProvider<T extends Accessor> implements IComponentProvider<T>, IServerDataProvider<T> {

    public static ForBlock getBlock() {
        return ForBlock.INSTANCE;
    }

    public static ForEntity getEntity() {
        return ForEntity.INSTANCE;
    }

    public static class ForBlock extends FluidStorageProvider<BlockAccessor> {

        private static final ForBlock INSTANCE = new ForBlock();
    }

    public static class ForEntity extends FluidStorageProvider<EntityAccessor> {

        private static final ForEntity INSTANCE = new ForEntity();
    }

    @Override
    public void appendTooltip(ITooltip tooltip, T accessor) {
        List<ClientViewGroup<FluidView>> groups = ClientProxy.mapToClientGroups(
                accessor,
                Identifiers.FLUID_STORAGE,
                FluidStorageProvider::decodeGroups,
                WDMlaClientRegistration.instance().fluidStorageProviders::get);
        if (groups == null || groups.isEmpty()) {
            return;
        }

        append(tooltip, accessor, groups);
    }

    public void append(ITooltip tooltip, T accessor, List<ClientViewGroup<FluidView>> groups) {
        if (!accessor.showDetails() && PluginsConfig.universal.fluidStorage.detailed) {
            return;
        }

        boolean renderGroup = groups.size() > 1 || groups.get(0).shouldRenderGroup();
        ClientViewGroup.tooltip(tooltip, groups, renderGroup, (theTooltip, group) -> {
            if (renderGroup) {
                group.renderHeader(tooltip);
            }
            for (var view : group.views) {
                IComponent mainText;
                ThemeHelper helper = ThemeHelper.INSTANCE;
                String currentStr = FormatUtil.STANDARD.format(view.current)
                        + StatCollector.translateToLocal("hud.wdmla.msg.millibucket");
                String maxStr = FormatUtil.STANDARD.format(view.max)
                        + StatCollector.translateToLocal("hud.wdmla.msg.millibucket");
                PluginsConfig.Universal.FluidStorage.Mode showMode = General.forceLegacy
                        ? PluginsConfig.Universal.FluidStorage.Mode.TEXT
                        : PluginsConfig.universal.fluidStorage.mode;
                if (view.overrideText != null) {
                    mainText = new TextComponent(view.overrideText);
                } else if (view.fluidName == null) {
                    if (accessor.showDetails() && showMode != PluginsConfig.Universal.FluidStorage.Mode.GAUGE) {
                        mainText = new HPanelComponent()
                                .child(helper.info(StatCollector.translateToLocal("hud.msg.wdmla.empty"))).text(": / ")
                                .text(maxStr);
                    } else {
                        mainText = helper.info(StatCollector.translateToLocal("hud.msg.wdmla.empty"));
                    }
                } else {
                    String fluidName = DisplayUtil.stripSymbols(view.fluidName);
                    if (accessor.showDetails() && showMode != PluginsConfig.Universal.FluidStorage.Mode.GAUGE) {
                        mainText = new HPanelComponent().child(helper.info(currentStr)).text(" / ").text(maxStr);
                    } else {
                        mainText = helper.info(currentStr);
                    }
                    mainText = new HPanelComponent().child(helper.info(fluidName)).text(": ").child(mainText);
                }
                switch (showMode) {
                    case GAUGE -> {
                        // TODO:invert text color with bright fluid
                        ProgressStyle progressStyle = new ProgressStyle().singleColor(General.progressColor.filled)
                                .overlay(new FluidDrawable(view.overlay));
                        if (view.hasScale) {
                            // TODO: proper fluid scale color
                            progressStyle.color(General.progressColor.filled, General.progressColor.border);
                        }
                        tooltip.child(
                                new ProgressComponent(view.current, view.max).style(progressStyle).child(
                                        new VPanelComponent().padding(DEFAULT_PROGRESS_DESCRIPTION_PADDING)
                                                .child(mainText)));
                    }
                    case ICON_TEXT -> {
                        if (view.overlay != null) {
                            tooltip.horizontal()
                                    .child(
                                            new FluidComponent(view.overlay)
                                                    .size(new Size(mainText.getHeight(), mainText.getHeight())))
                                    .child(mainText);
                        } else {
                            theTooltip.horizontal()
                                    .item(
                                            new ItemStack(Items.bucket),
                                            new Padding(),
                                            new Size(mainText.getHeight(), mainText.getHeight()))
                                    .child(mainText);
                        }
                    }
                    case TEXT -> {
                        theTooltip.child(mainText);
                    }
                }
            }
            if (group.extraData != null) {
                int extra = group.extraData.getInteger("+");
                if (extra > 0) {
                    theTooltip.text(StatCollector.translateToLocalFormatted("hud.msg.wdmla.more.tanks", extra));
                }
            }
        });
    }

    @Override
    public void appendServerData(NBTTagCompound data, T accessor) {
        Map.Entry<ResourceLocation, List<ViewGroup<FluidView.Data>>> entry = CommonProxy
                .getServerExtensionData(accessor, WDMlaCommonRegistration.instance().fluidStorageProviders);
        if (entry != null) {
            data.setTag(Identifiers.FLUID_STORAGE.toString(), encodeGroups(entry));
        }
    }

    public static NBTTagCompound encodeGroups(Map.Entry<ResourceLocation, List<ViewGroup<FluidView.Data>>> entry) {
        List<ViewGroup<FluidView.Data>> viewGroups = entry.getValue();
        NBTTagList groupsNBT = new NBTTagList();
        for (ViewGroup<FluidView.Data> viewGroup : viewGroups) {
            groupsNBT.appendTag(encodeGroup(viewGroup));
        }
        NBTTagCompound root = new NBTTagCompound();
        root.setTag(entry.getKey().toString(), groupsNBT);

        return root;
    }

    public static NBTTagCompound encodeGroup(ViewGroup<FluidView.Data> viewGroup) {
        List<NBTTagCompound> encodedFluidData = new ArrayList<>();
        for (FluidView.Data fluidData : viewGroup.views) {
            encodedFluidData.add(FluidView.Data.encode(fluidData));
        }
        ViewGroup<NBTTagCompound> contentEncodedGroup = new ViewGroup<>(encodedFluidData, viewGroup);
        return ViewGroup.encode(contentEncodedGroup);
    }

    public static Map.Entry<ResourceLocation, List<ViewGroup<FluidView.Data>>> decodeGroups(NBTTagCompound root) {
        if (root.hasNoTags()) {
            return null;
        }

        String key = root.func_150296_c().iterator().next();
        ResourceLocation resourceLocation = new ResourceLocation(key);

        NBTTagList groupsNBT = root.getTagList(key, 10);
        List<ViewGroup<FluidView.Data>> viewGroups = new ArrayList<>();
        for (int i = 0; i < groupsNBT.tagCount(); i++) {
            NBTTagCompound groupNBT = groupsNBT.getCompoundTagAt(i);
            viewGroups.add(decodeGroup(groupNBT));
        }

        return new AbstractMap.SimpleEntry<>(resourceLocation, viewGroups);
    }

    public static ViewGroup<FluidView.Data> decodeGroup(NBTTagCompound groupNBT) {
        ViewGroup<NBTTagCompound> contentDecodedGroup = ViewGroup.decode(groupNBT);

        List<FluidView.Data> fluidDataList = new ArrayList<>();
        for (NBTTagCompound fluidNBT : contentDecodedGroup.views) {
            fluidDataList.add(FluidView.Data.decode(fluidNBT));
        }

        return new ViewGroup<>(fluidDataList, contentDecodedGroup);
    }

    @Override
    public ResourceLocation getUid() {
        return Identifiers.FLUID_STORAGE;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL + 200;
    }

    @Override
    public boolean shouldRequestData(T accessor) {
        return (accessor.showDetails() || !PluginsConfig.universal.fluidStorage.detailed)
                && accessor.getTarget() != null
                && !WDMlaCommonRegistration.instance().fluidStorageProviders.wrappedGet(accessor).isEmpty();
    }

    public enum Extension
            implements IServerExtensionProvider<FluidView.Data>, IClientExtensionProvider<FluidView.Data, FluidView> {

        INSTANCE;

        @Override
        public ResourceLocation getUid() {
            return Identifiers.FLUID_STORAGE_DEFAULT;
        }

        @Override
        public List<ClientViewGroup<FluidView>> getClientGroups(Accessor accessor,
                List<ViewGroup<FluidView.Data>> groups) {
            return ClientViewGroup.map(groups, FluidView::readDefault, null);
        }

        @Nullable
        @Override
        public List<ViewGroup<FluidView.Data>> getGroups(Accessor accessor) {
            return CommonProxy.wrapFluidStorage(accessor);
        }

        @Override
        public boolean shouldRequestData(Accessor accessor) {
            return true; // I need to change this when I want to apply this to every TE
        }

        @Override
        public int getDefaultPriority() {
            return 9999;
        }
    }
}
