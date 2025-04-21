package com.gtnewhorizons.wdmla.api.view;

import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.wdmla.api.ui.IComponent;
import com.gtnewhorizons.wdmla.impl.ui.style.ProgressStyle;

@ApiStatus.Experimental
public class ProgressView {

    /**
     * if the style is null, the default progress style is applied
     */
    @Nullable
    public ProgressStyle style;
    public long progress;
    public long maxProgress;
    @Nullable
    public IComponent description;
    /**
     * If true, the progress bar will have default vertical stripes This will do nothing if custom style is present
     */
    public boolean hasScale;

    public ProgressView(@Nullable ProgressStyle style) {
        this.style = style;
    }

    public static ProgressView read(Data data) {
        ProgressView progressView = new ProgressView(null);
        progressView.progress = data.progress;
        progressView.maxProgress = data.maxProgress;
        progressView.hasScale = false;
        return progressView;
    }

    public static class Data {

        long progress = 0;
        long maxProgress = 0;

        public Data(long progress, long maxProgress) {
            this.progress = progress;
            this.maxProgress = maxProgress;
        }

        public static NBTTagCompound encode(Data data) {
            NBTTagCompound encoded = new NBTTagCompound();
            encoded.setLong("Progress", data.progress);
            encoded.setLong("MaxProgress", data.maxProgress);
            return encoded;
        }

        public static Data decode(NBTTagCompound nbt) {
            return new Data(nbt.getLong("Progress"), nbt.getLong("MaxProgress"));
        }
    }
}
