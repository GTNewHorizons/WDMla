package com.gtnewhorizons.wdmla.plugin.core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.StringUtils;

import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.accessor.EntityAccessor;
import com.gtnewhorizons.wdmla.api.provider.IEntityComponentProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerDataProvider;
import com.gtnewhorizons.wdmla.api.ui.IComponent;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.impl.ui.component.IconComponent;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;
import com.gtnewhorizons.wdmla.overlay.PotionIcon;
import com.gtnewhorizons.wdmla.util.FormatUtil;

public enum StatusEffectProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor) {
        if (!accessor.getServerData().hasKey("ActiveEffects")) {
            return;
        }

        NBTTagList activeEffects = accessor.getServerData().getTagList("ActiveEffects", 10);
        for (int i = 0; i < activeEffects.tagCount(); i++) {
            PotionEffect effect = PotionEffect.readCustomPotionEffectFromNBT(activeEffects.getCompoundTagAt(i));
            if (effect != null) {
                String amplifier;
                if (effect.getAmplifier() >= 0 && effect.getAmplifier() <= 9) {
                    amplifier = StatCollector.translateToLocal("enchantment.level." + (effect.getAmplifier() + 1));
                } else {
                    amplifier = FormatUtil.STANDARD.format(effect.getAmplifier() + 1);
                }
                String effectName = StatCollector.translateToLocal(effect.getEffectName()) + StringUtils.SPACE
                        + amplifier;
                String duration;
                if (effect.getIsPotionDurationMax()) {
                    duration = StatCollector.translateToLocal("hud.msg.wdmla.infinity.icon");
                } else {
                    duration = net.minecraft.util.StringUtils.ticksToElapsedTime(effect.getDuration());
                }

                String builtLine = String
                        .format(StatCollector.translateToLocal("hud.msg.wdmla.effect.format"), effectName, duration);
                IComponent lineComponent;
                if (Potion.potionTypes[effect.getPotionID()].isBadEffect()) {
                    lineComponent = ThemeHelper.INSTANCE.danger(builtLine);
                } else {
                    lineComponent = ThemeHelper.INSTANCE.success(builtLine);
                }
                tooltip.horizontal()
                        .child(
                                new IconComponent(new PotionIcon(effect), PotionIcon.PATH)
                                        .size(new Size(lineComponent.getHeight(), lineComponent.getHeight())))
                        .child(lineComponent);
            }
        }
    }

    @Override
    public void appendServerData(NBTTagCompound data, EntityAccessor accessor) {
        NBTTagCompound fullNBT = new NBTTagCompound();
        accessor.getEntity().writeToNBT(fullNBT);
        if (fullNBT.hasKey("ActiveEffects")) {
            data.setTag("ActiveEffects", fullNBT.getTagList("ActiveEffects", 10));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Identifiers.STATUS_EFFECT;
    }
}
