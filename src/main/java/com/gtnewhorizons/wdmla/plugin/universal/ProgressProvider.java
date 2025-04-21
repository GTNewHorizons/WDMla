package com.gtnewhorizons.wdmla.plugin.universal;

import static com.gtnewhorizons.wdmla.impl.ui.component.TooltipComponent.DEFAULT_PROGRESS_DESCRIPTION_PADDING;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.ClientProxy;
import com.gtnewhorizons.wdmla.CommonProxy;
import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.accessor.Accessor;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.accessor.EntityAccessor;
import com.gtnewhorizons.wdmla.api.provider.IComponentProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerDataProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerExtensionProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.api.view.ClientViewGroup;
import com.gtnewhorizons.wdmla.api.view.ProgressView;
import com.gtnewhorizons.wdmla.api.view.ViewGroup;
import com.gtnewhorizons.wdmla.config.General;
import com.gtnewhorizons.wdmla.impl.WDMlaClientRegistration;
import com.gtnewhorizons.wdmla.impl.WDMlaCommonRegistration;
import com.gtnewhorizons.wdmla.impl.ui.component.ProgressComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.VPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.style.ProgressStyle;

public class ProgressProvider<T extends Accessor> implements IComponentProvider<T>, IServerDataProvider<T> {

    public static ForBlock getBlock() {
        return ForBlock.INSTANCE;
    }

    public static ForEntity getEntity() {
        return ForEntity.INSTANCE;
    }

    public static class ForBlock extends ProgressProvider<BlockAccessor> {

        private static final ForBlock INSTANCE = new ForBlock();
    }

    public static class ForEntity extends ProgressProvider<EntityAccessor> {

        private static final ForEntity INSTANCE = new ForEntity();
    }

    @Override
    public void appendTooltip(ITooltip tooltip, T accessor) {
        List<ClientViewGroup<ProgressView>> groups = ClientProxy.mapToClientGroups(
                accessor,
                Identifiers.PROGRESS,
                ProgressProvider::decodeGroups,
                WDMlaClientRegistration.instance().progressProviders::get);
        if (groups == null || groups.isEmpty()) {
            return;
        }

        boolean renderGroup = groups.size() > 1 || groups.get(0).shouldRenderGroup();
        ClientViewGroup.tooltip(tooltip, groups, renderGroup, (theTooltip, group) -> {
            if (renderGroup && group.title != null) {
                group.renderHeader(theTooltip);
            }
            for (var view : group.views) {
                ProgressStyle progressStyle = view.style == null
                        ? new ProgressStyle().singleColor(General.progressColor.filled)
                        : view.style;
                if (view.hasScale && view.style == null) {
                    progressStyle.color(General.progressColor.filled, General.progressColor.filledAlternate);
                }
                ProgressComponent progress = new ProgressComponent(view.progress, view.maxProgress)
                        .style(progressStyle);
                if (view.description != null) {
                    progress.child(
                            new VPanelComponent().padding(DEFAULT_PROGRESS_DESCRIPTION_PADDING)
                                    .child(view.description));
                }
                tooltip.child(progress);
            }
        });
    }

    @Override
    public void appendServerData(NBTTagCompound data, T accessor) {
        Map.Entry<ResourceLocation, List<ViewGroup<ProgressView.Data>>> entry = CommonProxy
                .getServerExtensionData(accessor, WDMlaCommonRegistration.instance().progressProviders);
        if (entry != null) {
            data.setTag(Identifiers.PROGRESS.toString(), encodeGroups(entry));
        }
    }

    public static NBTTagCompound encodeGroups(Map.Entry<ResourceLocation, List<ViewGroup<ProgressView.Data>>> entry) {
        List<ViewGroup<ProgressView.Data>> viewGroups = entry.getValue();
        NBTTagList groupsNBT = new NBTTagList();
        for (ViewGroup<ProgressView.Data> viewGroup : viewGroups) {
            groupsNBT.appendTag(encodeGroup(viewGroup));
        }
        NBTTagCompound root = new NBTTagCompound();
        root.setTag(entry.getKey().toString(), groupsNBT);

        return root;
    }

    public static NBTTagCompound encodeGroup(ViewGroup<ProgressView.Data> viewGroup) {
        List<NBTTagCompound> encodedProgressData = new ArrayList<>();
        for (ProgressView.Data progressData : viewGroup.views) {
            encodedProgressData.add(ProgressView.Data.encode(progressData));
        }
        ViewGroup<NBTTagCompound> contentEncodedGroup = new ViewGroup<>(encodedProgressData, viewGroup);
        return ViewGroup.encode(contentEncodedGroup);
    }

    public static Map.Entry<ResourceLocation, List<ViewGroup<ProgressView.Data>>> decodeGroups(NBTTagCompound root) {
        if (root.hasNoTags()) {
            return null;
        }

        String key = root.func_150296_c().iterator().next();
        ResourceLocation resourceLocation = new ResourceLocation(key);

        NBTTagList groupsNBT = root.getTagList(key, 10);
        List<ViewGroup<ProgressView.Data>> viewGroups = new ArrayList<>();
        for (int i = 0; i < groupsNBT.tagCount(); i++) {
            NBTTagCompound groupNBT = groupsNBT.getCompoundTagAt(i);
            viewGroups.add(decodeGroup(groupNBT));
        }

        return new AbstractMap.SimpleEntry<>(resourceLocation, viewGroups);
    }

    public static ViewGroup<ProgressView.Data> decodeGroup(NBTTagCompound groupNBT) {
        ViewGroup<NBTTagCompound> contentDecodedGroup = ViewGroup.decode(groupNBT);

        List<ProgressView.Data> progressDataList = new ArrayList<>();
        for (NBTTagCompound progressNBT : contentDecodedGroup.views) {
            progressDataList.add(ProgressView.Data.decode(progressNBT));
        }

        return new ViewGroup<>(progressDataList, contentDecodedGroup);
    }

    @Override
    public boolean shouldRequestData(T accessor) {
        return WDMlaCommonRegistration.instance().progressProviders
                .hitsAny(accessor, IServerExtensionProvider::shouldRequestData);
    }

    @Override
    public ResourceLocation getUid() {
        return Identifiers.PROGRESS;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL + 400;
    }

    @Override
    public boolean canToggleInGui() {
        return false;
    }
}
