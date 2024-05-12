package mcp.mobius.wdmla.impl.drawable;

import mcp.mobius.wdmla.util.RenderUtil;

import mcp.mobius.wdmla.api.IDrawable;
import mcp.mobius.wdmla.api.IPanelStyle;
import mcp.mobius.wdmla.api.IArea;

public class BorderDrawable implements IDrawable {

    protected IPanelStyle style;

    public BorderDrawable style(IPanelStyle style) {
        this.style = style;
        return this;
    }

    @Override
    public void draw(IArea area) {
        if (this.style.getBorderColor() != IPanelStyle.NO_BORDER) {
            RenderUtil.drawHorizontalLine(area.getX(), area.getY(), area.getEX() - 1, this.style.getBorderColor());
            RenderUtil.drawHorizontalLine(area.getX(), area.getEY() - 1, area.getEX() - 1, this.style.getBorderColor());
            RenderUtil.drawVerticalLine(area.getX(), area.getY(), area.getEY() - 1, this.style.getBorderColor());
            RenderUtil.drawVerticalLine(area.getEX() - 1, area.getY(), area.getEY(), this.style.getBorderColor());
        }
    }
}
