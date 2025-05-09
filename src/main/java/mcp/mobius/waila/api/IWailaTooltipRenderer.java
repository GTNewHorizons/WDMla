package mcp.mobius.waila.api;

import java.awt.Dimension;

/**
 * I have switched the render system so this class no longer works.<br>
 * Common "rendername"s are supported in {@link com.gtnewhorizons.wdmla.wailacompat.TooltipCompat}. I cannot guarantee I
 * can support special cases which depends on this render.
 *
 * @deprecated every renderer now implements {@link com.gtnewhorizons.wdmla.api.ui.IDrawable} instead
 */
@SuppressWarnings("unused")
@Deprecated
@BackwardCompatibility
public interface IWailaTooltipRenderer {

    /**
     * 
     * @param params   Array of string parameters as passed to the RENDER arg in the tooltip
     *                 ({rendername,param1,param2,...})
     * @param accessor A global accessor for TileEntities and Entities
     * @return Dimension of the reserved area
     */
    Dimension getSize(String[] params, IWailaCommonAccessor accessor);

    /**
     * Draw method for the renderer. The GL matrice is automatically moved to the top left of the reserved zone.<br>
     * All calls should be relative to (0,0)
     * 
     * @param params   Array of string parameters as passed to the RENDER arg in the tooltip
     *                 ({rendername,param1,param2,...})
     * @param accessor A global accessor for TileEntities and Entities
     */
    void draw(String[] params, IWailaCommonAccessor accessor);
}
