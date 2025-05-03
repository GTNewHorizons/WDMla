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

    public static final EffectiveTool NO_TOOL = new EffectiveTool(null, null);

    protected final String value;
    protected final boolean isValid;
    //TODO: make it List
    private final @Nullable Map<Integer, ItemStack> iconMap;

    public EffectiveTool(String value, @Nullable Map<Integer, ItemStack> iconMap) {
        this.value = value;
        this.isValid = !Strings.isNullOrEmpty(value) && value.length() > 1;
        this.iconMap = iconMap;
    }

    public boolean isValid() {
        return isValid;
    }

    public HarvestLevel getHarvestLevel(Block block, int meta) {
        int rawLevel = block.getHarvestLevel(meta);
        if (isValid() && rawLevel < 0) rawLevel = 0;
        return new HarvestLevel(rawLevel);
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

    public ItemStack getIcon(HarvestLevel harvestLevel) {
        if (!harvestLevel.isToolRequired() || !isValid) {
            return null;
        }
        else {
            ItemStack icon = iconMap != null ? harvestLevel.getIconFromMap(iconMap) : null;
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
