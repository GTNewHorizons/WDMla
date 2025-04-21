package com.gtnewhorizons.wdmla.impl.ui.style;

import com.gtnewhorizons.wdmla.api.ui.ColorPalette;
import com.gtnewhorizons.wdmla.api.ui.style.IRectStyle;

public class RectStyle implements IRectStyle {

    private int backgroundColor1;
    private int backgroundColor2;
    private int borderColor;

    public RectStyle() {
        this.backgroundColor1 = ColorPalette.TRANSPARENT;
        this.backgroundColor2 = ColorPalette.TRANSPARENT;
        this.borderColor = ColorPalette.TRANSPARENT;
    }

    public RectStyle borderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public RectStyle backgroundColor(int backgroundColor) {
        this.backgroundColor1 = backgroundColor;
        this.backgroundColor2 = backgroundColor;
        return this;
    }

    @Override
    public int getBackgroundColor1() {
        return backgroundColor1;
    }

    @Override
    public int getBackgroundColor2() {
        return backgroundColor2;
    }

    @Override
    public int getBorderColor() {
        return borderColor;
    }
}
