package com.gtnewhorizons.wdmla.plugin.harvestability;

import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityTestPhase;
import com.gtnewhorizons.wdmla.api.provider.HarvestHandler;
import com.gtnewhorizons.wdmla.plugin.harvestability.proxy.ProxyIguanaTweaks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;

public enum IguanaHarvestHandler implements HarvestHandler {
    INSTANCE;

    @Override
    public boolean testHarvest(HarvestabilityInfo info, HarvestabilityTestPhase phase, EntityPlayer player, Block block, int meta, MovingObjectPosition position) {
        if (phase == HarvestabilityTestPhase.EFFECTIVE_TOOL_NAME) {
            if (info.getEffectiveTool().isSameTool(ProxyIguanaTweaks.pickaxe)) {
                //override tic pickaxe
                info.setEffectiveTool(ProxyIguanaTweaks.pickaxe);
            }
        }
        else if (phase == HarvestabilityTestPhase.HARVEST_LEVEL) {
            info.setHarvestLevel(new ProxyIguanaTweaks.IguanaHarvestLevel(info.getHarvestLevel()));
        }

        return true;
    }

    @Override
    public ResourceLocation getUid() {
        return HarvestabilityIdentifiers.IGUANATWEAKS;
    }

    @Override
    public int getDefaultPriority() {
        return TinkersHarvestHandler.INSTANCE.getDefaultPriority() + 500;
    }
}
