package com.gtnewhorizons.wdmla.addon.vanilla;

import com.gtnewhorizons.wdmla.api.BlockAccessor;
import com.gtnewhorizons.wdmla.api.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.IPluginConfig;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class PlayerHeadProvider implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getTileEntity() instanceof TileEntitySkull
                && accessor.getServerData().hasKey("Owner")) {
            GameProfile profile = NBTUtil.func_152459_a(accessor.getServerData().getCompoundTag("Owner"));
            if(profile == null) {
                return;
            }

            String playerName = profile.getName();
            String formattedHeadName = String.format(StatCollector.translateToLocal("item.skull.player.name"), playerName);
            ThemeHelper.INSTANCE.overrideTooltipTitle(tooltip, formattedHeadName);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return VanillaIdentifiers.PLAYER_HEAD;
    }
}
