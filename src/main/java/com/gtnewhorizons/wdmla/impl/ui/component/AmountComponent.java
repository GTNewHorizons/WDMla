package com.gtnewhorizons.wdmla.impl.ui.component;

import java.util.ArrayList;

import com.gtnewhorizons.wdmla.api.ui.style.IAmountStyle;
import com.gtnewhorizons.wdmla.impl.ui.drawable.AmountDrawable;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Padding;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;
import com.gtnewhorizons.wdmla.impl.ui.value.FilledAmount;

public class AmountComponent extends TooltipComponent {

    public static final int DEFAULT_W = 100;
    public static final int DEFAULT_H = 12;

    public AmountComponent(float ratio) {
        this(Math.round(ratio * 1000), 1000);
    }

    public AmountComponent(long current, long max) {
        super(
                new ArrayList<>(),
                new Padding(),
                new Size(DEFAULT_W, DEFAULT_H),
                new AmountDrawable(new FilledAmount(current, max)));
    }

    public AmountComponent style(IAmountStyle style) {
        ((AmountDrawable) foreground).style(style);
        return this;
    }
}
