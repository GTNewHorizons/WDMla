package mcp.mobius.wdmla.impl.ui.drawable;

import org.jetbrains.annotations.NotNull;

import mcp.mobius.wdmla.api.ui.IDrawable;
import mcp.mobius.wdmla.api.ui.sizer.IArea;
import mcp.mobius.wdmla.api.ui.style.ITextStyle;
import mcp.mobius.wdmla.impl.ui.style.TextStyle;

public class TextDrawable implements IDrawable {

    private final @NotNull String text;
    private @NotNull ITextStyle style;

    public TextDrawable(@NotNull String text) {
        this.text = text;
        this.style = new TextStyle();
    }

    public TextDrawable style(ITextStyle style) {
        this.style = style;
        return this;
    }

    @Override
    public void draw(IArea area) {
        GuiDraw.drawString(text, area.getX(), area.getY(), style.getColor(), style.getShadow());
    }
}
