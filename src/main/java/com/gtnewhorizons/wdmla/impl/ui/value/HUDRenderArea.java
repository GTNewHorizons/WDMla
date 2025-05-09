package com.gtnewhorizons.wdmla.impl.ui.value;

import java.awt.Point;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.config.Configuration;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.wdmla.impl.ui.sizer.Area;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;

import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.overlay.OverlayConfig;
import mcp.mobius.waila.utils.Constants;

public class HUDRenderArea {

    private static final int MARGIN = 5;

    private final @NotNull Size hudSize;

    public HUDRenderArea(@NotNull Size hudSize) {
        this.hudSize = hudSize;
    }

    public Area computeBackground() {
        Point pos = new Point(
                ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_POSX, 0),
                ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_POSY, 0));

        float w = hudSize.getW() + MARGIN * 2;
        float h = hudSize.getH() + MARGIN * 2;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int width = res.getScaledWidth();
        int height = res.getScaledHeight();
        float x = ((int) (width / OverlayConfig.scale) - w - 1) * pos.x / 10000;
        float y = ((int) (height / OverlayConfig.scale) - h - 1) * pos.y / 10000;

        return new Area(x, y, w, h);
    }

    public Area computeForeground() {
        Area bg = computeBackground();
        return new Area(bg.getX() + MARGIN, bg.getY() + MARGIN, bg.getW(), bg.getH());
    }
}
