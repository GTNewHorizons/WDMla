package com.gtnewhorizons.wdmla.api.harvestability;

import joptsimple.internal.Strings;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class EffectiveTool {

    public static final EffectiveTool NO_TOOL = new EffectiveTool(null, null);
    public static final EffectiveTool CANNOT_HARVEST = new EffectiveTool("unbreakable", null);

    protected final String value;
    /**
     * icons of the tool indexed by harvest level (0~)
     */
    private final @Nullable List<ItemStack> iconList;

    public EffectiveTool(String name, @Nullable List<ItemStack> iconList) {
        this.value = name;
        this.iconList = iconList;
    }

    public boolean isValid() {
        return !Strings.isNullOrEmpty(value) && value.length() > 1;
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
        return iconList != null;
    }

    public ItemStack getIcon(HarvestLevel harvestLevel) {
        if (!harvestLevel.isToolRequired() || !isValid()) {
            return null;
        }
        else {
            ItemStack icon = harvestLevel.getIconFromList(iconList);
            if (icon == null && !isSameTool(CANNOT_HARVEST)) {
                icon = new ItemStack(Blocks.iron_bars);
            }
            return icon;
        }
    }

    public String getLocalizedName() {
        if (StatCollector.canTranslate("hud.msg.wdmla.toolclass." + value)) {
            return StatCollector.translateToLocal("hud.msg.wdmla.toolclass." + value);
        }
        else if (isValid()) {
            return value.substring(0, 1).toUpperCase() + value.substring(1);
        }
        else {
            return null;
        }
    }
}
