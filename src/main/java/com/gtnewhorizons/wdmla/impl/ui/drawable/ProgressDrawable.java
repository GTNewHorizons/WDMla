package com.gtnewhorizons.wdmla.impl.ui.drawable;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.wdmla.api.ui.IDrawable;
import com.gtnewhorizons.wdmla.api.ui.IFilledProgress;
import com.gtnewhorizons.wdmla.api.ui.sizer.IArea;
import com.gtnewhorizons.wdmla.api.ui.style.IProgressStyle;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Area;
import com.gtnewhorizons.wdmla.impl.ui.style.ProgressStyle;
import com.gtnewhorizons.wdmla.overlay.GuiDraw;
import com.gtnewhorizons.wdmla.util.Color;

public class ProgressDrawable implements IDrawable {

    private final @NotNull IFilledProgress progress;
    private @NotNull IProgressStyle style;

    public ProgressDrawable(@NotNull IFilledProgress progress) {
        this.progress = progress;
        this.style = new ProgressStyle();
    }

    public ProgressDrawable style(IProgressStyle style) {
        this.style = style;
        return this;
    }

    @Override
    public void draw(IArea area) {
        long current = progress.getCurrent();
        long max = progress.getMax();
        if (current <= 0L || max <= 0L) {
            return;
        }

        int dx = (int) Math.min(current * (long) (area.getW() - 2) / max, (long) (area.getW() - 2));
        if (dx > 0) {
            if (style.getOverlay() != null) {
                style.getOverlay().draw(new Area(area.getX() + 1, area.getY() + 1, dx + 1, area.getH() - 2));
            } else {
                fillWithGradient(area, dx);
            }
        }
        if (style.getFilledColor() != style.getAlternateFilledColor()) {
            for (int xx = area.getX() + 1; xx <= area.getX() + dx + 1; ++xx) {
                if ((xx % 3) != 2) {
                    continue;
                }
                GuiDraw.drawVerticalLine(xx, area.getY() + 1, area.getEY() - 1, style.getAlternateFilledColor());
            }
        }
    }

    private void fillWithGradient(IArea area, int dx) {
        int darker = Color.setLightness(style.getFilledColor(), 0.7f);
        int half = (area.getH() - 2) / 2;
        GuiDraw.drawStraightGradientRect(
                new Area(area.getX() + 1, area.getY() + 1, dx + 1, half),
                darker,
                style.getFilledColor(),
                false);
        GuiDraw.drawStraightGradientRect(
                new Area(area.getX() + 1, area.getY() + 1 + half, dx + 1, area.getH() - 2 - half),
                style.getFilledColor(),
                darker,
                false);
    }
}
