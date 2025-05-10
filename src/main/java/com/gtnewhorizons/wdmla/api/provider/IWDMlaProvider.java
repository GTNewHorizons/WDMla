package com.gtnewhorizons.wdmla.api.provider;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import com.gtnewhorizons.wdmla.api.identifier.WDMlaIDs;
import com.gtnewhorizons.wdmla.api.TooltipPosition;

/**
 * Base provider class that interacts with Waila HUD panel.<br>
 */
public interface IWDMlaProvider {

    /**
     * The unique id of this provider.<br>
     * Providers from different registries can have the same id.<br>
     * The Resource Domain is the mod category that the provider belongs, and Resource Path is expected to be
     * snake_case.<br>
     * Warning: Due to Minecraft ResourceLocation implementation, if Uid contains upper case character the registry will
     * confuse.
     */
    ResourceLocation getUid();

    /**
     * Affects the registration and display order showing in the tooltip. Leave this to default if you want to show
     * normal tooltip.
     * <p>
     * If you want to show your tooltip a bit to the bottom, you should return a value greater than 0, and less than
     * 5000.
     * <p>
     * Registration of Default component such as item name occurs on -10000, and Registration of Harvestability occurs
     * on -8000. You can add your own default component between them.
     */
    default int getDefaultPriority() {
        return TooltipPosition.BODY;
    }

    /**
     * @return If it is false, tooltips priority cannot be configurable in both file and gui.
     */
    default boolean isPriorityFixed() {
        return false;
    }

    /**
     * Whether player can edit the priority of the provider with in game config.
     */
    default boolean canPrioritizeInGui() {
        return true;
    }

    default String getConfigCategory() {
        return WDMlaIDs.CONFIG_AUTOGEN + Configuration.CATEGORY_SPLITTER
                + getUid().getResourceDomain()
                + Configuration.CATEGORY_SPLITTER
                + getUid().getResourcePath();
    }

    /**
     * @return The language key of this provider name.<br>
     *         Mainly used in config.
     */
    default String getLangKey() {
        return "provider.wdmla" + Configuration.CATEGORY_SPLITTER
                + getUid().getResourceDomain().replace("_", ".")
                + Configuration.CATEGORY_SPLITTER
                + getUid().getResourcePath().replace("_", ".");
    }
}
