package com.gtnewhorizons.wdmla.util;

import java.util.Locale;

import net.minecraft.util.MathHelper;

public class Color {

    private static final int[] MC_COLORS = new int[] {
            0x000000, // black
            0x0000AA, // dark blue
            0x00AA00, // dark green
            0x00AAAA, // dark aqua
            0xAA0000, // dark red
            0xAA00AA, // dark purple
            0xFFAA00, // gold
            0xAAAAAA, // gray
            0x555555, // dark gray
            0x5555FF, // blue
            0x55FF55, // green
            0x55FFFF, // aqua
            0xFF5555, // red
            0xFF55FF, // light purple
            0xFFFF55, // yellow
            0xFFFFFF  // white
    };

    private static final String[] MC_CODES = new String[] {
            "\u00a70", "\u00a71", "\u00a72", "\u00a73",
            "\u00a74", "\u00a75", "\u00a76", "\u00a77",
            "\u00a78", "\u00a79", "\u00a7a", "\u00a7b",
            "\u00a7c", "\u00a7d", "\u00a7e", "\u00a7f"
    };

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

    public static int setInterporation(int colorBegin, int colorEnd, float interporation) {
        int a1 = (colorBegin >> 24) & 0xFF;
        int r1 = (colorBegin >> 16) & 0xFF;
        int g1 = (colorBegin >> 8) & 0xFF;
        int b1 = colorBegin & 0xFF;

        int a2 = (colorEnd >> 24) & 0xFF;
        int r2 = (colorEnd >> 16) & 0xFF;
        int g2 = (colorEnd >> 8) & 0xFF;
        int b2 = colorEnd & 0xFF;

        int a = MathHelper.clamp_int(a1 + (int) ((a2 - a1) * interporation), 0, 255);
        int r = MathHelper.clamp_int(r1 + (int) ((r2 - r1) * interporation), 0, 255);
        int g = MathHelper.clamp_int(g1 + (int) ((g2 - g1) * interporation), 0, 255);
        int b = MathHelper.clamp_int(b1 + (int) ((b2 - b1) * interporation), 0, 255);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int parseColor(String color, int fallback) {
        if (color == null) {
            return fallback;
        }

        String s = color.trim();
        if (s.isEmpty()) {
            return fallback;
        }

        String hex = s;
        if (hex.charAt(0) == '#') {
            hex = hex.substring(1);
        } else if (hex.length() > 2 && (hex.startsWith("0x") || hex.startsWith("0X"))) {
            hex = hex.substring(2);
        }
        if (hex.matches("(?i)[0-9a-f]{6}")) {
            try {
                return 0xFF000000 | Integer.parseInt(hex, 16);
            } catch (NumberFormatException ignored) {
                return fallback;
            }
        }

        String key = s.toUpperCase(Locale.ROOT);
        return switch (key) {
            case "BLACK" -> 0xFF000000;
            case "DARK_BLUE" -> 0xFF0000AA;
            case "DARK_GREEN" -> 0xFF00AA00;
            case "DARK_AQUA" -> 0xFF00AAAA;
            case "DARK_RED" -> 0xFFAA0000;
            case "DARK_PURPLE" -> 0xFFAA00AA;
            case "GOLD", "ORANGE" -> 0xFFFFAA00;
            case "GRAY", "GREY", "LIGHT_GRAY", "LIGHTGRAY", "LIGHT_GREY", "LIGHTGREY" -> 0xFFAAAAAA;
            case "DARK_GRAY", "DARKGRAY", "DARK_GREY", "DARKGREY" -> 0xFF555555;
            case "BLUE" -> 0xFF5555FF;
            case "GREEN", "LIME" -> 0xFF55FF55;
            case "CYAN", "AQUA" -> 0xFF55FFFF;
            case "RED" -> 0xFFFF5555;
            case "PURPLE", "MAGENTA", "LIGHT_PURPLE", "LIGHTPURPLE" -> 0xFFFF55FF;
            case "YELLOW" -> 0xFFFFFF55;
            case "WHITE" -> 0xFFFFFFFF;
            default -> fallback;
        };
    }

    public static String toNearestChatColorCode(int argb) {
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;

        int bestIndex = 9;
        int bestDist = Integer.MAX_VALUE;
        for (int i = 0; i < MC_COLORS.length; i++) {
            int c = MC_COLORS[i];
            int dr = r - ((c >> 16) & 0xFF);
            int dg = g - ((c >> 8) & 0xFF);
            int db = b - (c & 0xFF);
            int dist = (dr * dr) + (dg * dg) + (db * db);
            if (dist < bestDist) {
                bestDist = dist;
                bestIndex = i;
            }
        }

        return MC_CODES[bestIndex];
    }
}
