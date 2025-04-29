package com.gtnewhorizons.wdmla.plugin.vanilla;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.wdmla.api.accessor.EntityAccessor;
import com.gtnewhorizons.wdmla.api.provider.IEntityComponentProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerDataProvider;
import com.gtnewhorizons.wdmla.api.provider.ITimeFormatConfigurable;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.config.WDMlaConfig;
import com.gtnewhorizons.wdmla.impl.format.TimeFormattingPattern;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;

public enum PrimedTNTProvider
        implements IEntityComponentProvider, IServerDataProvider<EntityAccessor>, ITimeFormatConfigurable {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor) {
        TimeFormattingPattern timePattern = WDMlaConfig.instance().getTimeFormatter(this);
        tooltip.child(
                ThemeHelper.INSTANCE
                        .value(
                                StatCollector.translateToLocal("hud.msg.wdmla.fuse"),
                                timePattern.tickFormatter.apply((int) accessor.getServerData().getByte("Fuse")))
                        .tag(VanillaIdentifiers.PRIMED_TNT));
    }

    @Override
    public void appendServerData(NBTTagCompound data, EntityAccessor accessor) {
        accessor.getEntity().writeToNBT(data);
    }

    @Override
    public ResourceLocation getUid() {
        return VanillaIdentifiers.PRIMED_TNT;
    }
}
