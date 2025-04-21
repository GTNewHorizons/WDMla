package com.gtnewhorizons.wdmla.impl.ui.drawable;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.wdmla.api.ui.ColorPalette;
import com.gtnewhorizons.wdmla.api.ui.IDrawable;
import com.gtnewhorizons.wdmla.api.ui.sizer.IArea;
import com.gtnewhorizons.wdmla.api.ui.style.IPanelStyle;
import com.gtnewhorizons.wdmla.impl.ui.style.PanelStyle;
import com.gtnewhorizons.wdmla.overlay.GuiDraw;

public class BorderDrawable implements IDrawable {

    protected @NotNull IPanelStyle style;

    public BorderDrawable() {
        style = new PanelStyle();
    }

    public BorderDrawable style(IPanelStyle style) {
        this.style = style;
        return this;
    }

    @Override
    public void draw(IArea area) {
        int color = this.style.getBorderColor();
        if (this.style.getBorderColor() != ColorPalette.TRANSPARENT) {
            GuiDraw.drawHorizontalLine(area.getX(), area.getY(), area.getEX() - 1, color);
            GuiDraw.drawHorizontalLine(area.getX(), area.getEY() - 1, area.getEX() - 1, color);
            GuiDraw.drawVerticalLine(area.getX(), area.getY(), area.getEY() - 1, color);
            GuiDraw.drawVerticalLine(area.getEX() - 1, area.getY(), area.getEY(), color);
        }
    }
}
