package com.gtnewhorizons.wdmla.impl.ui.component;

import java.util.ArrayList;

import com.gtnewhorizons.wdmla.impl.ui.drawable.ProgressionDrawable;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Padding;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;
import com.gtnewhorizons.wdmla.impl.ui.value.FilledProgress;

// TODO: full implementation to use any texture
public class TexturedProgressComponent extends TooltipComponent {

    public static final int DEFAULT_W = 28;
    public static final int DEFAULT_H = 16;

    public TexturedProgressComponent(long current, long max) {
        super(
                new ArrayList<>(),
                new Padding().horizontal(2),
                new Size(DEFAULT_W, DEFAULT_H),
                new ProgressionDrawable(new FilledProgress(current, max)));
    }
}
