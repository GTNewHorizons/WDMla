package com.gtnewhorizons.wdmla.api.view;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * The collection of server side views, or any class that needs to be grouped in WDMla view creation process.
 * This class can be synced from server to client
 */
public class ViewGroup<T> {

    public List<T> views;

    /**
     * The id of this viewGroup, which is convenient for a simple if state processing.
     */
    @Nullable
    public String id;
    /**
     * The extra data about the views that have to be synced from server to client
     */
    @Nullable
    protected NBTTagCompound extraData;

    public ViewGroup(List<T> views) {
        this(views, null, null);
    }

    public ViewGroup(List<T> views, @Nullable String id, @Nullable NBTTagCompound extraData) {
        this.views = views;
        this.id = id;
        this.extraData = extraData;
    }

    /**
     * instantiate new ViewGroup without extraData check
     */
    public ViewGroup(List<T> newViews, ViewGroup<?> oldGroup) {
        this.views = newViews;
        this.id = oldGroup.id;
        this.extraData = oldGroup.extraData;
    }

    public NBTTagCompound getExtraData() {
        if (extraData == null) {
            extraData = new NBTTagCompound();
        }
        return extraData;
    }

    public void setProgress(float progress) {
        getExtraData().setFloat("Progress", progress);
    }

    public static NBTTagCompound encode(ViewGroup<NBTTagCompound> viewGroup) {
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < viewGroup.views.size(); i++) {
            NBTTagCompound viewNBTTag = new NBTTagCompound();
            viewNBTTag.setByte("index", (byte) i);
            viewNBTTag.setTag("view", viewGroup.views.get(i));
            nbttaglist.appendTag(viewNBTTag);
        }

        NBTTagCompound data = new NBTTagCompound();
        data.setTag("views", nbttaglist);
        if (viewGroup.id != null) {
            data.setString("id", viewGroup.id);
        }
        if (viewGroup.extraData != null) {
            data.setTag("extradata", viewGroup.extraData);
        }
        return data;
    }

    public static ViewGroup<NBTTagCompound> decode(NBTTagCompound data) {
        NBTTagList nbttaglist = data.getTagList("views", 10);

        List<NBTTagCompound> views = new ArrayList<>();
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound view = nbttaglist.getCompoundTagAt(i).getCompoundTag("view");
            views.add(view);
        }

        String id = data.hasKey("id") ? data.getString("id") : null;
        NBTTagCompound extraData = data.hasKey("extradata") ? data.getCompoundTag("extradata") : null;

        return new ViewGroup<>(views, id, extraData);
    }
}
