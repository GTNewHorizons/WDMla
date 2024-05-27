package mcp.mobius.wdmla.impl.ui.value.setting;

import mcp.mobius.wdmla.api.ui.style.IProgressStyle;

public class ProgressStyle implements IProgressStyle {

    private int borderColor;
    private int backgroundColor;
    private int filledColor;
    private int alternateFilledColor;

    public ProgressStyle() {
        this.borderColor = DEFAULT_BORDER;
        this.backgroundColor = DEFAULT_BACKGROUND;
        this.filledColor = DEFAULT_FILLED;
        this.alternateFilledColor = DEFAULT_FILLED_ALTERNATE;
    }

    public ProgressStyle borderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public ProgressStyle backgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public ProgressStyle filledColor(int filledColor) {
        this.filledColor = filledColor;
        return this;
    }

    public ProgressStyle alternateFilledColor(int alternateFilledColor) {
        this.alternateFilledColor = alternateFilledColor;
        return this;
    }

    @Override
    public int getBorderColor() {
        return borderColor;
    }

    @Override
    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public int getFilledColor() {
        return filledColor;
    }

    @Override
    public int getAlternateFilledColor() {
        return alternateFilledColor;
    }
}
