package mcp.mobius.waila.addons.thermalexpansion;

import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.cbcore.LangUtil;

public class HUDHandlerEnergyCell implements IWailaDataProvider {


    private final DecimalFormat energyFormat;

    public HUDHandlerEnergyCell() {
        this.energyFormat = new DecimalFormat("###,###,###,###,###,###,###");
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (!config.getConfig("thermalexpansion.energycell")) return currenttip;

        int energyReceive = accessor.getNBTInteger(accessor.getNBTData(), "Recv");
        int energySend = accessor.getNBTInteger(accessor.getNBTData(), "Send");
        
        if (!config.getConfig("thermalexpansion.digitgrouping")) {
            currenttip.add(
                String.format(
                        "%s/%s : %d / %d RF/t",
                        LangUtil.translateG("hud.msg.input"),
                        LangUtil.translateG("hud.msg.output"),
                        energyReceive,
                        energySend));
        } else {
            currenttip.add(
                String.format(
                        "%s/%s : %s / %s RF/t",
                        LangUtil.translateG("hud.msg.input"),
                        LangUtil.translateG("hud.msg.output"),
                        energyFormat.format(energyReceive),
                        energyFormat.format(energySend)));
        }


        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x,
            int y, int z) {
        try {
            int recv = ThermalExpansionModule.TileEnergyCell_Recv.getInt(te);
            int send = ThermalExpansionModule.TileEnergyCell_Send.getInt(te);
            tag.setInteger("Recv", recv);
            tag.setInteger("Send", send);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tag;
    }

}
