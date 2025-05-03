package com.gtnewhorizons.wdmla.api.harvestability;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class HarvestLevel {

    public static final HarvestLevel NO_TOOL = new HarvestLevel(-1);

    protected final int value;

    public HarvestLevel(int level) {
        this.value = level;
    }

    public HarvestLevel(HarvestLevel level) {
        this.value = level.value;
    }

    public boolean isToolRequired() {
        return value != -1;
    }

    public String getDisplayNum() {
        return isToolRequired() ? String.valueOf(value) : null;
    }

    public String getName() {
        String unlocalized = "hud.msg.wdmla.harvestlevel" + (value + 1);

        if (StatCollector.canTranslate(unlocalized)) {
            return StatCollector.translateToLocal(unlocalized);
        }
        else {
            return String.valueOf(value);
        }
    }

    public ItemStack getIconFromList(List<ItemStack> iconList) {
        if (iconList == null || iconList.isEmpty()) {
            return null;
        }
        if (iconList.size() <= value) {
            return iconList.get(iconList.size() - 1);
        }
        return iconList.get(value);
    }

    public boolean doesSatisfy(HarvestLevel otherHarvestLevel) {
        return value >= otherHarvestLevel.value;
    }
}
