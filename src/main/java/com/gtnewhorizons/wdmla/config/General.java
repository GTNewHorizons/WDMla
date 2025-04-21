package com.gtnewhorizons.wdmla.config;

import com.gtnewhorizon.gtnhlib.config.Config;
import com.gtnewhorizons.wdmla.WDMla;
import com.gtnewhorizons.wdmla.api.ui.ColorPalette;
import com.gtnewhorizons.wdmla.impl.ui.DefaultThemes;

import mcp.mobius.waila.api.impl.ConfigHandler;

/**
 * List of WDMla general configs. Don't get confused with Waila general config in
 * {@link ConfigHandler#reloadDefaultConfig()}<br>
 * All static configuration entry which does not belong to specific plugin should go here.
 */
@Config(modid = WDMla.MODID, category = "wdmla_general", configSubDirectory = "WDMla", filename = "general")
@Config.LangKey("option.wdmla.general.category")
@Config.Comment("These are WDMla exclusive settings")
public class General {

    public static TextColor textColor = new TextColor();

    public static ProgressColor progressColor = new ProgressColor();

    public static BreakProgress breakProgress = new BreakProgress();

    @Config.LangKey("option.wdmla.general.forcelegacy")
    @Config.DefaultBoolean(false)
    @Config.Comment("Disables all modern WDMla features. This will make the system ignore all settings in this category")
    public static boolean forceLegacy;

    @Config.LangKey("option.wdmla.general.ghostproduct")
    @Config.DefaultBoolean(true)
    @Config.Comment("Shows the ghost product on process")
    public static boolean ghostProduct;

    @Config.LangKey("option.wdmla.general.theme")
    @Config.DefaultEnum("CUSTOM")
    @Config.Comment("Current theme of tooltips. If other than NONE is selected, some values will be overridden")
    public static DefaultThemes currentTheme;

    @Config.LangKey("option.wdmla.general.previewincfg")
    @Config.DefaultBoolean(true)
    @Config.Comment("Shows current tooltip preview in config screen")
    public static boolean previewInCfg;

    @Config.LangKey("option.wdmla.general.customnameoverride")
    @Config.DefaultBoolean(true)
    @Config.Comment("Allow renamed object taking place on tooltip title")
    public static boolean customNameOverride;

    @Config.Comment("Text color for the custom theme. \n" + "See general category for the default text color.")
    @Config.LangKey("option.wdmla.textcolor.category")
    public static class TextColor {

        @Config.LangKey("option.wdmla.general.textcolor.info")
        @Config.DefaultInt(ColorPalette.INFO)
        public int info;

        @Config.LangKey("option.wdmla.general.textcolor.title")
        @Config.DefaultInt(ColorPalette.TITLE)
        public int title;

        @Config.LangKey("option.wdmla.general.textcolor.success")
        @Config.DefaultInt(ColorPalette.SUCCESS)
        public int success;

        @Config.LangKey("option.wdmla.general.textcolor.warning")
        @Config.DefaultInt(ColorPalette.WARNING)
        public int warning;

        @Config.LangKey("option.wdmla.general.textcolor.danger")
        @Config.DefaultInt(ColorPalette.DANGER)
        public int danger;

        @Config.LangKey("option.wdmla.general.textcolor.failure")
        @Config.DefaultInt(ColorPalette.FAILURE)
        public int failure;

        @Config.LangKey("option.wdmla.general.textcolor.modname")
        @Config.DefaultInt(ColorPalette.MOD_NAME)
        public int modName;
    }

    @Config.Comment("The colors used in progress bar. \n"
            + "This category is shared between fluid, energy and general progress display.")
    @Config.LangKey("option.wdmla.progresscolor.category")
    public static class ProgressColor {

        @Config.LangKey("option.wdmla.general.progresscolor.background")
        @Config.DefaultInt(ColorPalette.PROGRESS_BACKGROUND)
        public int background;

        @Config.LangKey("option.wdmla.general.progresscolor.border")
        @Config.DefaultInt(ColorPalette.PROGRESS_BORDER)
        public int border;

        @Config.LangKey("option.wdmla.general.progresscolor.filled")
        @Config.DefaultInt(ColorPalette.PROGRESS_FILLED)
        public int filled;

        @Config.LangKey("option.wdmla.general.progresscolor.filled.alternate")
        @Config.DefaultInt(ColorPalette.PROGRESS_FILLED_ALTERNATE)
        public int filledAlternate;
    }

    @Config.LangKey("option.wdmla.breakprogress.category")
    public static class BreakProgress {

        @Config.LangKey("option.wdmla.general.breakprogress.mode")
        @Config.DefaultEnum("FILLING_BAR")
        public Mode mode;

        @Config.LangKey("option.wdmla.general.breakprogress.position")
        @Config.DefaultEnum("BOTTOM") // Waila displays thing at top of the screen by default
        public Position position;

        @Config.LangKey("option.wdmla.breakprogress.fill.animation")
        @Config.DefaultBoolean(true)
        public boolean fillAnimation;

        @Config.LangKey("option.wdmla.breakprogress.fade.animation")
        @Config.DefaultBoolean(true)
        public boolean fadeAnimation;

        @Config.LangKey("option.wdmla.general.breakprogress.fade.speed")
        @Config.DefaultInt(4)
        public int fadeSpeed;

        public enum Mode {
            FILLING_BAR,
            TEXT,
            NONE
        }

        public enum Position {
            TOP,
            BOTTOM
        }
    }
}
