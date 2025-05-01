package com.gtnewhorizons.wdmla.plugin.harvestability.helpers;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;
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

public class BlockHelper {

    public static Map.Entry<ItemStack, Boolean> getShearability(EntityPlayer player, Block block, int meta,
                                                                MovingObjectPosition position) {
        boolean isSneaking = player.isSneaking();
        boolean showShearability = PluginsConfig.harvestability.legacy.shearability
                && (!PluginsConfig.harvestability.legacy.shearabilitySneakingOnly || isSneaking);

        if (showShearability && (block instanceof IShearable || block == Blocks.deadbush
                || (block == Blocks.double_plant && block.getItemDropped(meta, new Random(), 0) == null))) {
            ItemStack itemHeld = player.getHeldItem();
            boolean isHoldingShears = itemHeld != null && itemHeld.getItem() instanceof ItemShears;
            boolean canShear = isHoldingShears && ((IShearable) block)
                    .isShearable(itemHeld, player.worldObj, position.blockX, position.blockY, position.blockZ);
            String[] parts = PluginsConfig.harvestability.modern.shearabilityItem.split(":");
            if (parts.length == 2) {
                return Maps.immutableEntry(GameRegistry.findItemStack(parts[0], parts[1], 1), canShear);
            }
        }
        return null;
    }

    public static Map.Entry<ItemStack, Boolean> getSilktouchAbility(EntityPlayer player, Block block, int meta,
                                                                    MovingObjectPosition position) {
        boolean isSneaking = player.isSneaking();
        boolean showSilkTouchability = PluginsConfig.harvestability.legacy.silkTouchability
                && (!PluginsConfig.harvestability.legacy.silkTouchabilitySneakingOnly || isSneaking);

        if (showSilkTouchability && block
                .canSilkHarvest(player.worldObj, player, position.blockX, position.blockY, position.blockZ, meta)) {
            Item itemDropped = block.getItemDropped(meta, new Random(), 0);
            boolean silkTouchMatters = (itemDropped instanceof ItemBlock && itemDropped != Item.getItemFromBlock(block))
                    || block.quantityDropped(new Random()) <= 0;
            boolean canSilkTouch = silkTouchMatters && EnchantmentHelper.getSilkTouchModifier(player);
            String[] parts = PluginsConfig.harvestability.modern.silkTouchabilityItem.split(":");
            if (parts.length == 2) {
                return Maps.immutableEntry(GameRegistry.findItemStack(parts[0], parts[1], 1), canSilkTouch);
            }
        }
        return null;
    }
}
