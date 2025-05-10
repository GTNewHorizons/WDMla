package com.gtnewhorizons.wdmla.plugin.vanilla;

import com.gtnewhorizons.wdmla.api.identifier.VanillaIDs;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.mojang.authlib.GameProfile;

public enum PlayerHeadHeaderProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        if (accessor.getTileEntity() instanceof TileEntitySkull && accessor.getServerData().hasKey("Owner")) {
            GameProfile profile = NBTUtil.func_152459_a(accessor.getServerData().getCompoundTag("Owner"));
            if (profile == null) {
                return;
            }

            String playerName = profile.getName();
            String formattedHeadName = String
                    .format(StatCollector.translateToLocal("item.skull.player.name"), playerName);
            ThemeHelper.INSTANCE.overrideTooltipTitle(tooltip, formattedHeadName);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return VanillaIDs.PLAYER_HEAD_HEADER;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.HEAD;
    }
}
