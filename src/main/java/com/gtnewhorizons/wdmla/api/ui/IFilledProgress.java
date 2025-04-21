package com.gtnewhorizons.wdmla.api.ui;

/**
 * Represents something is being filled with two positive values
 */
public interface IFilledProgress {

    /**
     * @return The current progress of filling value which is always smaller than max value.
     */
    long getCurrent();

    /**
     * @return The maximum progress of filling value.
     */
    long getMax();
}
