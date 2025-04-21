package com.gtnewhorizons.wdmla.api.ui;

/**
 * Every color constant used in WDMla should be stored here for the accessibility.<br>
 * These are configurable values unless explicitly stated.<br>
 * DO NOT USE THESE IN YOUR PROVIDER DIRECTLY!<br>
 */
public final class ColorPalette {

    // non-configurable
    public static final int TRANSPARENT = 0x00FFFFFF;

    public static final int BREAK_PROGRESS_DEFAULT = 0xFFA0A0A0;
    public static final int BREAK_PROGRESS_FAILURE = 0xFFAA0000;

    // configurable
    // text colors. access through theme
    public static final int DEFAULT = 0xFFA0A0A0;
    public static final int INFO = 0xFFFFFFFF;
    public static final int TITLE = 0xFFFFFFFF;
    public static final int SUCCESS = 0xFF00AA00; // dark green
    public static final int WARNING = 0xFFFFF3CD;
    public static final int DANGER = 0xFFFF5555;
    public static final int FAILURE = 0xFFAA0000;
    public static final int MOD_NAME = 0xFF5555FF;

    public static final int SUCCESS_JADE = 0xFF55FF55; // green
    public static final int SUCCESS_TOP = 0xFF55FF55; // green
    public static final int WARNING_TOP = 0xFFFFFF55; // yellow

    public static final int PROGRESS_BACKGROUND = TRANSPARENT;
    public static final int PROGRESS_BORDER = 0xFF555555;
    public static final int PROGRESS_FILLED = 0xFFFFCC00;
    public static final int PROGRESS_FILLED_ALTERNATE = 0xFFBB9900;

    public static final int ENERGY_FILLED = 0xFFAA0000;
    public static final int ENERGY_FILLED_ALTERNATE = 0xFF660000;

    @SuppressWarnings("unused")
    public static final int BG_COLOR_WAILA = 0x9E100010;
    @SuppressWarnings("unused")
    public static final int BG_GRADIENT1_WAILA = 0x9E5000ff;
    @SuppressWarnings("unused")
    public static final int BG_GRADIENT2_WAILA = 0x9E28007f;

    public static final int BG_COLOR_JADE = 0xB3131313;
    public static final int BG_GRADIENT1_JADE = 0xB3383838;
    public static final int BG_GRADIENT2_JADE = 0xB3242424;

    public static final int BG_COLOR_TOP = 0xB3006699;
    public static final int BG_GRADIENT1_TOP = 0xB39999FF;
    public static final int BG_GRADIENT2_TOP = 0xB39999FF;
}
