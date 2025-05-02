package com.gtnewhorizons.wdmla.api.harvestability;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO: encapsulation
public class HarvestabilityInfo {
    @NotNull
    public Block block = Blocks.air;
    public int meta = 0;

    public @NotNull EffectiveTool effectiveTool = new EffectiveTool(null, null);
    /**
     * if -1: hand(no tool)
     */
    public int harvestLevel = -1;

    public String harvestLevelName = "";

    /**
     * the primary harvest tool (pickaxe for hopper, shovel for dirt...)
     */
    //pending removal
    public ItemStack effectiveToolIcon = null;

    /**
     * can the block be harvested by player currently?
     */
    public boolean canHarvest = false;

    /**
     * the secondary harvest tool (wrench for hopper, shears for leaves)
     */
    public List<Map.Entry<ItemStack, Boolean>> additionalToolsIcon = new ArrayList<>();

    public boolean stopFurtherTesting = false;

    public boolean isHeldToolEffective = false;
}
