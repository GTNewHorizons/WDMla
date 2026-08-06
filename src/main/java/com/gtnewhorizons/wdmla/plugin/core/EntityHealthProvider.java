package com.gtnewhorizons.wdmla.plugin.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.accessor.EntityAccessor;
import com.gtnewhorizons.wdmla.api.provider.IEntityComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.config.PluginsConfig;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.impl.ui.component.HPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.HealthComponent;
import com.gtnewhorizons.wdmla.util.FormatUtil;

import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.utils.Constants;

public enum EntityHealthProvider implements IEntityComponentProvider {

    INSTANCE;

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.HEAD;
    }

    @Override
    public ResourceLocation getUid() {
        return Identifiers.ENTITY_HEALTH;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor) {
        if (!(accessor.getEntity() instanceof EntityLivingBase livingEntity)) {
            return;
        }

        float health = livingEntity.getHealth() / 2.0f;
        float maxhp = livingEntity.getMaxHealth() / 2.0f;

        int maxHPForText = ConfigHandler.instance()
                .getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_MAXHP, 40);
        if (livingEntity.getMaxHealth() > maxHPForText) {
            String prefix = PluginsConfig.core.defaultEntity.showHPText ? "HP: " : " ";
            tooltip.child(
                    new HPanelComponent().child(new HealthComponent(1, 1)).text(prefix)
                            .child(ThemeHelper.INSTANCE.info(FormatUtil.STANDARD.format(livingEntity.getHealth())))
                            .text(" / ")
                            .child(ThemeHelper.INSTANCE.info(FormatUtil.STANDARD.format(livingEntity.getMaxHealth())))
                            .tag(Identifiers.ENTITY_HEALTH));
        } else {
            tooltip.child(new HealthComponent(health, maxhp).tag(Identifiers.ENTITY_HEALTH));
        }
    }
}
