package com.gtnewhorizons.wdmla.impl.ui.component;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.wdmla.api.ui.IComponent;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.api.ui.style.IProgressStyle;
import com.gtnewhorizons.wdmla.api.ui.style.IRectStyle;
import com.gtnewhorizons.wdmla.config.General;
import com.gtnewhorizons.wdmla.impl.ui.drawable.ProgressDrawable;
import com.gtnewhorizons.wdmla.impl.ui.drawable.RectDrawable;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Area;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Padding;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;
import com.gtnewhorizons.wdmla.impl.ui.style.RectStyle;
import com.gtnewhorizons.wdmla.impl.ui.value.FilledProgress;

import mcp.mobius.waila.utils.WailaExceptionHandler;

public class ProgressComponent extends TooltipComponent {

    public static final int MINIMAL_W = 125;
    public static final int MINIMAL_H = 8;
    private final RectDrawable rectDrawable;

    public ProgressComponent(float ratio) {
        this(Math.round(ratio * 1000), 1000);
    }

    public ProgressComponent(long current, long max) {
        super(
                new ArrayList<>(),
                new Padding(),
                new Size(MINIMAL_W, MINIMAL_H),
                new ProgressDrawable(new FilledProgress(current, max)));
        this.rectDrawable = new RectDrawable().style(
                new RectStyle().backgroundColor(General.progressColor.background)
                        .borderColor(General.progressColor.border));
        // TODO:register ProgressTracker to unify the Width of All ProgressComponent
    }

    public ProgressComponent style(IProgressStyle style) {
        ((ProgressDrawable) foreground).style(style);
        return this;
    }

    public ProgressComponent rectStyle(IRectStyle style) {
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
        if (children.isEmpty()) {
            this.children.add(child);
        } else {
            WailaExceptionHandler.handleErr(
                    new IllegalArgumentException(
                            "ProgressComponent only accepts one child! Consider appending PanelComponent if you want multiple."),
                    this.getClass().getName(),
                    null);
        }
        int width = Math.max(size.getW(), children.get(0).getWidth());
        int height = Math.max(size.getH(), children.get(0).getHeight());
        size(new Size(width, height));
        return this;
    }
}
