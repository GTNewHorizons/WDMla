package mcp.mobius.waila.overlay;

import net.minecraftforge.common.config.Configuration;

import com.gtnewhorizons.wdmla.impl.ui.DefaultThemes;

import mcp.mobius.waila.api.BackwardCompatibility;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.utils.Constants;

/**
 * Waila display config which is also referenced on WDMla side. These variables are public so expect addons depending on
 */
@BackwardCompatibility
public class OverlayConfig {

    /**
     * X position of the panel. Range:0~10000
     */
    public static int posX;
    /**
     * Y position of the panel. Range:0~10000
     */
    public static int posY;
    /**
     * Alpha value of the panel.
     */
    public static int alpha;
    /**
     * Background color. It is part of the WDMla theme.
     */
    public static int bgcolor;
    /**
     * Background gradient start color. It is part of the WDMla theme.
     */
    public static int gradient1;
    /**
     * Background gradient end color. It is part of the WDMla theme.
     */
    public static int gradient2;
    /**
     * Default text color that can be overridden by {@link com.gtnewhorizons.wdmla.api.ui.MessageType} colors.
     */
    public static int fontcolor;
    /**
     * Panel scale. it will be passed to {@link org.lwjgl.opengl.GL11#glScalef(float, float, float)} directly
     */
    public static float scale;

    /**
     * Scale for the fancy block preview renderer.
     * 1.0 = default size.
     */
    public static float fancyBlockScale;


    public static void updateColors() {
        OverlayConfig.alpha = (int) (ConfigHandler.instance()
                .getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_ALPHA, 0) / 100.0f
                * 256) << 24;
        OverlayConfig.bgcolor = OverlayConfig.alpha
                + ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_BGCOLOR, 0);
        OverlayConfig.gradient1 = OverlayConfig.alpha
                + ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_GRADIENT1, 0);
        OverlayConfig.gradient2 = OverlayConfig.alpha
                + ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_GRADIENT2, 0);
        OverlayConfig.fontcolor = 0xFF000000
                + ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_FONTCOLOR, 0);

        DefaultThemes.CUSTOM.get().bgColor = OverlayConfig.bgcolor;
        DefaultThemes.CUSTOM.get().bgGradient1 = OverlayConfig.gradient1;
        DefaultThemes.CUSTOM.get().bgGradient2 = OverlayConfig.gradient2;
        DefaultThemes.CUSTOM.get().textColors._default = OverlayConfig.fontcolor;
    }
}
