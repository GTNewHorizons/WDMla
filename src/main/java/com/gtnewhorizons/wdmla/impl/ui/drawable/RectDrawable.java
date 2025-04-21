package com.gtnewhorizons.wdmla.impl.ui.drawable;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.wdmla.api.ui.ColorPalette;
import com.gtnewhorizons.wdmla.api.ui.IDrawable;
import com.gtnewhorizons.wdmla.api.ui.sizer.IArea;
import com.gtnewhorizons.wdmla.api.ui.style.IRectStyle;
import com.gtnewhorizons.wdmla.impl.ui.style.RectStyle;
import com.gtnewhorizons.wdmla.overlay.GuiDraw;

public class RectDrawable implements IDrawable {

    private @NotNull IRectStyle style;

    public RectDrawable() {
        style = new RectStyle();
    }

    public RectDrawable style(IRectStyle style) {
        this.style = style;
        return this;
    }

    @Override
    public void draw(IArea area) {
        if (area.getH() == 1) {
            GuiDraw.drawHorizontalLine(area.getX(), area.getY(), area.getEX(), style.getBackgroundColor1());
        } else if (area.getW() == 1) {
            GuiDraw.drawVerticalLine(area.getX(), area.getY(), area.getEY(), style.getBackgroundColor1());
        } else {
            GuiDraw.drawGradientRect(area, style.getBackgroundColor1(), style.getBackgroundColor2());
            if (style.getBorderColor() != ColorPalette.TRANSPARENT) {
                GuiDraw.drawBoxBorder(area, 1, style.getBorderColor(), style.getBorderColor());
            }
        }
    }
}
