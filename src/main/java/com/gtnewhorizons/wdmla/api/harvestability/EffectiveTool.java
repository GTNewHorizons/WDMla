package com.gtnewhorizons.wdmla.api.harvestability;

import joptsimple.internal.Strings;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class EffectiveTool {

    private final String value;
    private final boolean isValid;
    private final @Nullable Map<Integer, ItemStack> iconMap;

    public EffectiveTool(String value, @Nullable Map<Integer, ItemStack> iconMap) {
        this.value = value;
        this.isValid = !Strings.isNullOrEmpty(value) && value.length() > 1;
        this.iconMap = iconMap;
    }

    public boolean isValid() {
        return isValid;
    }

    public int getHarvestLevel(Block block, int meta) {
        int harvestLevel = block.getHarvestLevel(meta);
        if (isValid() && harvestLevel < 0) harvestLevel = 0;
        return harvestLevel;
    }

    public boolean isToolInstance(ItemStack tool) {
        return tool.getItem().getToolClasses(tool).contains(value);
    }

    public boolean isSameTool(EffectiveTool anotherTool) {
        return Objects.equals(value, anotherTool.value);
    }

    public boolean hasIconRegistered() {
        return iconMap != null;
    }

    public ItemStack getIcon(int harvestLevel) {
        if (harvestLevel == -1 || !isValid) {
            return null;
        }
        else {
            ItemStack icon = iconMap != null ? iconMap.get(harvestLevel) : null;
            if (icon == null) {
                icon = new ItemStack(Blocks.iron_bars);
            }
            return icon;
        }
    }

    public String getLocalizedName() {
        if (StatCollector.canTranslate("hud.msg.wdmla.toolclass." + value)) {
            return StatCollector.translateToLocal("hud.msg.wdmla.toolclass." + value);
        }
        else if (isValid) {
            return value.substring(0, 1).toUpperCase() + value.substring(1);
        }
        else {
            return null;
        }
    }
}
