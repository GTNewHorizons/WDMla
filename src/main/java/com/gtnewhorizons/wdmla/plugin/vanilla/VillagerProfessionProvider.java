package com.gtnewhorizons.wdmla.plugin.vanilla;

import com.gtnewhorizons.wdmla.api.EntityAccessor;
import com.gtnewhorizons.wdmla.api.IEntityComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import cpw.mods.fml.common.registry.VillagerRegistry;
import mcp.mobius.waila.cbcore.LangUtil;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;

public enum VillagerProfessionProvider implements IEntityComponentProvider {
    INSTANCE;

    private static String[] vanillaVillagers = { "farmer", "librarian", "priest", "blacksmith", "butcher" };

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor) {
        if(accessor.getEntity() instanceof EntityVillager villager) {
            String name = getVillagerName(villager.getProfession());
            tooltip.child(ThemeHelper.INSTANCE.info(
                    LangUtil.translateG("hud.msg.wdmla.villager.profession." + name)
            ));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return VanillaIdentifiers.VILLAGER_PROFESSION;
    }

    //copied from Wawla as this is the only way to get villager name dynamically
    public static String getVillagerName(int id) {
        ResourceLocation skin = VillagerRegistry.getVillagerSkin(id, null);
        if (id >= 0 && id <= 4) {
            return vanillaVillagers[id];
        }

        if (skin != null) {
            String path = skin.getResourcePath();
            return skin.getResourceDomain() + "." + path.substring(path.lastIndexOf("/") + 1, path.length() - 4);
        }

        return "nodata";
    }
}
