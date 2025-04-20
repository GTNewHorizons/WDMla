package com.gtnewhorizons.wdmla.impl.ui.component;

import java.util.ArrayList;

import com.gtnewhorizons.wdmla.api.ui.ColorPalette;
import com.gtnewhorizons.wdmla.api.ui.style.IAmountStyle;
import com.gtnewhorizons.wdmla.api.ui.style.IRectStyle;
import com.gtnewhorizons.wdmla.impl.ui.drawable.AmountDrawable;
import com.gtnewhorizons.wdmla.impl.ui.drawable.RectDrawable;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Area;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Padding;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;
import com.gtnewhorizons.wdmla.impl.ui.style.RectStyle;
import com.gtnewhorizons.wdmla.impl.ui.value.FilledAmount;

/**
 * aka. ProgressComponent
 */
public class AmountComponent extends TooltipComponent {

    public static final int DEFAULT_W = 100;
    public static final int DEFAULT_H = 12;
    private final RectDrawable rectDrawable;

    public AmountComponent(float ratio) {
        this(Math.round(ratio * 1000), 1000);
    }

    public AmountComponent(long current, long max) {
        super(
                new ArrayList<>(),
                new Padding(),
                new Size(DEFAULT_W, DEFAULT_H),
                new AmountDrawable(new FilledAmount(current, max)));
        this.rectDrawable = new RectDrawable().style(new RectStyle()
                .backgroundColor(ColorPalette.AMOUNT_BACKGROUND_WAILA)
                .borderColor(ColorPalette.AMOUNT_BORDER_WAILA));
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
}
