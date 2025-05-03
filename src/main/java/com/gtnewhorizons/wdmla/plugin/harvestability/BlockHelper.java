package com.gtnewhorizons.wdmla.plugin.harvestability;

import java.util.Random;

import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.IShearable;

import com.gtnewhorizons.wdmla.config.PluginsConfig;
import net.minecraftforge.oredict.OreDictionary;

public class BlockHelper {

    public static HarvestabilityInfo.AdditionalToolInfo getShearability(EntityPlayer player, Block block, int meta,
                                                                        MovingObjectPosition position) {
        if ((block instanceof IShearable || block == Blocks.deadbush
                || (block == Blocks.double_plant && block.getItemDropped(meta, new Random(), 0) == null))) {
            ItemStack itemHeld = player.getHeldItem();
            boolean isHoldingShears = itemHeld != null && itemHeld.getItem() instanceof ItemShears;
            boolean canShear = isHoldingShears && ((IShearable) block)
                    .isShearable(itemHeld, player.worldObj, position.blockX, position.blockY, position.blockZ);
            String[] parts = PluginsConfig.harvestability.icon.shearabilityItem.split(":");
            if (parts.length == 2) {
                return new HarvestabilityInfo.AdditionalToolInfo(GameRegistry.findItemStack(parts[0], parts[1], 1), canShear);
            }
        }
        return null;
    }

    public static HarvestabilityInfo.AdditionalToolInfo getSilktouchAbility(EntityPlayer player, Block block, int meta,
                                                                                MovingObjectPosition position) {
        if (block.canSilkHarvest(player.worldObj, player, position.blockX, position.blockY, position.blockZ, meta)) {
            Item itemDropped = block.getItemDropped(meta, new Random(), 0);
            boolean silkTouchMatters = (itemDropped instanceof ItemBlock && itemDropped != Item.getItemFromBlock(block))
                    || block.quantityDropped(new Random()) <= 0;
            boolean canSilkTouch = silkTouchMatters && EnchantmentHelper.getSilkTouchModifier(player);
            String[] parts = PluginsConfig.harvestability.icon.silkTouchabilityItem.split(":");
            if (parts.length == 2) {
                return new HarvestabilityInfo.AdditionalToolInfo(GameRegistry.findItemStack(parts[0], parts[1], 1), canSilkTouch);
            }
        }
        return null;
    }

    public static boolean isBlockAnOre(Block block, int metadata) {
        return isItemAnOre(new ItemStack(block, 1, metadata));
    }

    public static boolean isItemAnOre(ItemStack itemStack) {
        // check the ore dictionary to see if it starts with "ore"
        int[] oreIDs = OreDictionary.getOreIDs(itemStack);
        for (int oreID : oreIDs) {
            if (OreDictionary.getOreName(oreID).startsWith("ore")) return true;
        }

        // ore in the display name (but not part of another word)
        if (itemStack.getDisplayName().matches(".*(^|\\s)([oO]re)($|\\s).*")) return true;

        // ore as the start of the unlocalized name
        if (itemStack.getUnlocalizedName().startsWith("ore")) return true;

        return false;
    }
}
