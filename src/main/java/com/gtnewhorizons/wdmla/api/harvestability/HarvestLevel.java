package com.gtnewhorizons.wdmla.api.harvestability;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.Map;

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

    public String getName() {
        String unlocalized = "hud.msg.wdmla.harvestlevel" + (value + 1);

        if (StatCollector.canTranslate(unlocalized)) {
            return StatCollector.translateToLocal(unlocalized);
        }
        else {
            return String.valueOf(value);
        }
    }

    public ItemStack getIconFromMap(Map<Integer, ItemStack> levelIconMap) {
        return levelIconMap.get(value);
    }

    public boolean doesSatisfy(HarvestLevel otherHarvestLevel) {
        return value >= otherHarvestLevel.value;
    }
}
