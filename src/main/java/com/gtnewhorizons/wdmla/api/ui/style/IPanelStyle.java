package com.gtnewhorizons.wdmla.api.ui.style;

import com.gtnewhorizons.wdmla.api.ui.ComponentAlignment;

/**
 * Collection of panel settings.
 */
public interface IPanelStyle {

    ComponentAlignment getAlignment();

    /**
     * @return the space pixel between each panel elements
     */
    float getSpacing();
}
