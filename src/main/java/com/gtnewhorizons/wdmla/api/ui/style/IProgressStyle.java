package com.gtnewhorizons.wdmla.api.ui.style;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.wdmla.api.ui.IDrawable;

/**
 * Collection of filled bar settings.
 */
public interface IProgressStyle {

    /**
     * If this is not null, it will be rendered instead of filled color.<br>
     * It must have flexible size
     * 
     * @return the overlay drawable
     */
    @Nullable
    IDrawable getOverlay();

    /**
     * TODO:animated sprite instead of single color
     * 
     * @return The single color for partially filling the background rectangle.
     */
    int getFilledColor();

    /**
     * @return The color which is applied to the vertical indicator of the bar. It will not be rendered unless
     *         specified.
     */
    int getAlternateFilledColor();
}
