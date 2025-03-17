package com.gtnewhorizons.wdmla.addon.vanilla;

import com.gtnewhorizon.gtnhlib.compat.Mods;
import com.gtnewhorizons.wdmla.api.BlockAccessor;
import com.gtnewhorizons.wdmla.api.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.IPluginConfig;
import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import mcp.mobius.waila.overlay.DisplayUtil;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Field;
import java.util.HashMap;

public class MobSpawnerHeaderProvider implements IBlockComponentProvider {

    private HashMap entityIDs;

    public MobSpawnerHeaderProvider() {
        if(!Mods.NEI) {
            return;
        }

        try {
            Field entityIDsMap = EntityList.class.getDeclaredField("stringToIDMapping");
            entityIDsMap.setAccessible(true);
            entityIDs = (HashMap)entityIDsMap.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getTileEntity() instanceof TileEntityMobSpawner spawnerTile) {
            String spawnerName = DisplayUtil.itemDisplayNameShort(accessor.getItemForm());
            String mobName = spawnerTile.func_145881_a().getEntityNameToSpawn();
            ThemeHelper.INSTANCE.overrideTooltipTitle(tooltip, String.format("%s (%s)", spawnerName, mobName));

            // @see codechicken.nei.ItemMobSpawner
            if(Mods.NEI) {
                Object entityID = entityIDs.get(mobName);
                if(entityID == null) {
                    return;
                }

                ItemStack newStack = new ItemStack(Blocks.mob_spawner, 1, (int)entityID);
                ThemeHelper.INSTANCE.overrideTooltipIcon(tooltip, newStack);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return VanillaIdentifiers.MOB_SPAWNER_HEADER;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.HEAD;
    }
}