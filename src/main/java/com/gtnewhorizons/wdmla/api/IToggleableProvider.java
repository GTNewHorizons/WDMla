package com.gtnewhorizons.wdmla.api;

public interface IToggleableProvider extends IWdmlaProvider {

	/**
	 * Whether this provider can be disabled in config.
	 */
	default boolean isRequired() {
		return false;
	}

	default boolean enabledByDefault() {
		return true;
	}

}
