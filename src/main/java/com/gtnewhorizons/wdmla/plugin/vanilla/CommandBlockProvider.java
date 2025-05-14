package com.gtnewhorizons.wdmla.plugin.vanilla;

import com.gtnewhorizons.wdmla.api.identifier.VanillaIDs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.StringUtils;

import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerDataProvider;
import com.gtnewhorizons.wdmla.api.ui.IComponent;
import com.gtnewhorizons.wdmla.api.config.PluginsConfig;

public enum CommandBlockProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    @Override
    public void appendTooltip(IComponent tooltip, BlockAccessor accessor) {
        if (accessor.getTileEntity() instanceof TileEntityCommandBlock) {
            String command = accessor.getServerData().getString("command");
            if (StringUtils.isBlank(command)) {
                return;
            }

            tooltip.text("> " + command);
        }
    }

    @Override
    public void appendServerData(NBTTagCompound data, BlockAccessor accessor) {
        if (accessor.getTileEntity() instanceof TileEntityCommandBlock commandBlock
                && MinecraftServer.getServer().isCommandBlockEnabled()
                && accessor.getPlayer().canCommandSenderUseCommand(2, "")) {
            String command = commandBlock.func_145993_a().func_145753_i();
            int maxLength = PluginsConfig.vanilla.commandBlock.maxCommandLength;
            if (command.length() > maxLength) {
                command = command.substring(0, maxLength - 3) + "...";
            }
            data.setString("command", command);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return VanillaIDs.COMMAND_BLOCK;
    }
}
