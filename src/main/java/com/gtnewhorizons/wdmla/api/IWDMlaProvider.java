package com.gtnewhorizons.wdmla.api;

import net.minecraft.util.ResourceLocation;

public interface IWDMlaProvider {

    /**
     * The unique id of this provider. Providers from different registries can have the same id.
     */
    ResourceLocation getUid();

    /**
     * Affects the display order showing in the tooltip.
     * <p>
     * If you want to show your tooltip a bit to the bottom, you should return a value greater than 0, and less than
     * 5000. If it is greater than 5000, the content will not be collapsed in lite mode.
     */
    default int getDefaultPriority() {
        return 0;
    }
}
