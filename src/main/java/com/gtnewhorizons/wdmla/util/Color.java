package com.gtnewhorizons.wdmla.util;

public class Color {

    public static int setLightness(int color, float multiplier) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        r = Math.min(255, (int) (r * multiplier));
        g = Math.min(255, (int) (g * multiplier));
        b = Math.min(255, (int) (b * multiplier));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
