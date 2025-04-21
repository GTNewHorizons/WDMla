package com.gtnewhorizons.wdmla.impl.ui.style;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.wdmla.api.ui.IDrawable;
import com.gtnewhorizons.wdmla.api.ui.style.IProgressStyle;
import com.gtnewhorizons.wdmla.config.General;

public class ProgressStyle implements IProgressStyle {

    private int filledColor;
    private int alternateFilledColor;
    @Nullable
    private IDrawable overlay;

    public ProgressStyle(int filledColor, int alternateFilledColor, @Nullable IDrawable overlay) {
        this.filledColor = filledColor;
        this.alternateFilledColor = alternateFilledColor;
        this.overlay = overlay;
    }

    public ProgressStyle() {
        // fallback colors
        this.filledColor = General.progressColor.filled;
        this.alternateFilledColor = General.progressColor.filled;
        this.overlay = null;
    }

    public ProgressStyle singleColor(int singleColor) {
        return color(singleColor, singleColor);
    }

    public ProgressStyle color(int filledColor, int alternateFilledColor) {
        this.filledColor = filledColor;
        this.alternateFilledColor = alternateFilledColor;
        return this;
    }

    public ProgressStyle overlay(IDrawable overlay) {
        this.overlay = overlay;
        return this;
    }

    @Override
    public int getFilledColor() {
        return filledColor;
    }

    @Override
    public int getAlternateFilledColor() {
        return alternateFilledColor;
    }

    @Override
    public @Nullable IDrawable getOverlay() {
        return overlay;
    }
}
