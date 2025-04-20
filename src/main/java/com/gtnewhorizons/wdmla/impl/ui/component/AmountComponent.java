package com.gtnewhorizons.wdmla.impl.ui.component;

import java.util.ArrayList;

import com.gtnewhorizons.wdmla.api.ui.ColorPalette;
import com.gtnewhorizons.wdmla.api.ui.IComponent;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.api.ui.style.IAmountStyle;
import com.gtnewhorizons.wdmla.api.ui.style.IRectStyle;
import com.gtnewhorizons.wdmla.impl.ui.drawable.AmountDrawable;
import com.gtnewhorizons.wdmla.impl.ui.drawable.RectDrawable;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Area;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Padding;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;
import com.gtnewhorizons.wdmla.impl.ui.style.RectStyle;
import com.gtnewhorizons.wdmla.impl.ui.value.FilledAmount;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import org.jetbrains.annotations.NotNull;

/**
 * aka. ProgressComponent
 */
public class AmountComponent extends TooltipComponent {

    public static final int MINIMAL_W = 100;
    public static final int MINIMAL_H = 8;
    private final RectDrawable rectDrawable;

    public AmountComponent(float ratio) {
        this(Math.round(ratio * 1000), 1000);
    }

    public AmountComponent(long current, long max) {
        super(
                new ArrayList<>(),
                new Padding(),
                new Size(MINIMAL_W, MINIMAL_H),
                new AmountDrawable(new FilledAmount(current, max)));
        this.rectDrawable = new RectDrawable().style(new RectStyle()
                .backgroundColor(ColorPalette.AMOUNT_BACKGROUND)
                .borderColor(ColorPalette.AMOUNT_BORDER));
        //TODO:register AmountTracker to unify the Width of All AmountComponent
    }

    public AmountComponent style(IAmountStyle style) {
        ((AmountDrawable) foreground).style(style);
        return this;
    }

    public AmountComponent rectStyle(IRectStyle style) {
        rectDrawable.style(style);
        return this;
    }

    @Override
    public void tick(int x, int y) {
        rectDrawable.draw(new Area(x + padding.getLeft(), y + padding.getTop(), size.getW(), size.getH()));
        super.tick(x, y);
    }

    @Override
    public ITooltip child(@NotNull IComponent child) {
        if(children.isEmpty()) {
            this.children.add(child);
        }
        else {
            WailaExceptionHandler.handleErr(
                    new IllegalArgumentException("AmountComponent only accepts one child! Consider appending PanelComponent if you want multiple."),
                    this.getClass().getName(), null);
        }
        int width = Math.max(size.getW(), children.get(0).getWidth());
        int height = Math.max(size.getH(), children.get(0).getHeight());
        size(new Size(width, height));
        return this;
    }
}
