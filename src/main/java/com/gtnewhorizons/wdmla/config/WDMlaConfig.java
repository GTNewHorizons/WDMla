package com.gtnewhorizons.wdmla.config;

import java.io.File;
import java.util.Arrays;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.TextColors;
import com.gtnewhorizons.wdmla.api.Theme;
import com.gtnewhorizons.wdmla.api.provider.IComponentProvider;
import com.gtnewhorizons.wdmla.api.provider.ITimeFormatConfigurable;
import com.gtnewhorizons.wdmla.impl.WDMlaClientRegistration;
import com.gtnewhorizons.wdmla.impl.WDMlaCommonRegistration;
import com.gtnewhorizons.wdmla.impl.format.TimeFormattingPattern;
import com.gtnewhorizons.wdmla.impl.ui.DefaultThemes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * The new configuration file added by WDMla.<br>
 * This file handles all auto generated config for plugins which GTNHLib can't do.
 */
public class WDMlaConfig extends Configuration {

    @SideOnly(Side.CLIENT)
    private static WDMlaConfig _instance;

    public WDMlaConfig(File configFile) {
        super(configFile);
        _instance = this;
    }

    public static WDMlaConfig instance() {
        return _instance;
    }

    public void reloadConfig() {
        getCategory(Identifiers.CONFIG_AUTOGEN).setLanguageKey("option.wdmla.autogen.category");
        getCategory(Identifiers.CONFIG_AUTOGEN + Configuration.CATEGORY_SPLITTER + Identifiers.NAMESPACE_CORE)
                .setLanguageKey("provider.wdmla.core.category");
        reloadProviderAutogenConfigs();
        reloadTheme();
    }

    public void reloadProviderAutogenConfigs() {
        for (IComponentProvider<?> provider : WDMlaClientRegistration.instance().getAllProvidersWithoutInfo()) {
            getCategory(provider.getConfigCategory()).setLanguageKey(provider.getLangKey());
            isProviderEnabled(provider);
            WDMlaCommonRegistration.instance().priorities.put(provider, getProviderPriority(provider));
            if (provider instanceof ITimeFormatConfigurable timeFormat) {
                getTimeFormatter(timeFormat);
            }
        }
    }

    // TODO:split provider config file
    public boolean isProviderEnabled(IComponentProvider<?> provider) {
        Property prop = get(
                provider.getConfigCategory(),
                "option.wdmla.autogen.enabled",
                provider.enabledByDefault(),
                "");
        if (!provider.canToggleInGui()) {
            prop.setShowInGui(false);
        }
        return prop.getBoolean();
    }

    public int getProviderPriority(IComponentProvider<?> provider) {
        if (provider.isPriorityFixed()) {
            return provider.getDefaultPriority();
        }

        Property prop = get(
                provider.getConfigCategory(),
                "option.wdmla.autogen.priority",
                provider.getDefaultPriority(),
                "");
        if (!provider.canPrioritizeInGui()) {
            prop.setShowInGui(false);
        }

        return prop.getInt();
    }

    public TimeFormattingPattern getTimeFormatter(ITimeFormatConfigurable instance) {
        return loadEnum(
                instance.getConfigCategory(),
                "option.wdmla.autogen.time.format",
                instance.getDefaultTimeFormatter(),
                "");
    }

    private void reloadTheme() {
        Theme custom = DefaultThemes.CUSTOM.get();
        custom.textColors = new TextColors(
                0,
                General.textColor.info,
                General.textColor.title,
                General.textColor.success,
                General.textColor.warning,
                General.textColor.danger,
                General.textColor.failure,
                General.modName.hudColor);
    }

    public <T extends Enum<T>> T loadEnum(String category, String name, T defaultValue, String comment) {

        Class<T> enumType = defaultValue.getDeclaringClass();
        return T.valueOf(
                enumType,
                getString(
                        name,
                        category,
                        defaultValue.toString(),
                        comment,
                        Arrays.stream(enumType.getEnumConstants()).map(Enum::toString).toArray(String[]::new)));

    }
}
