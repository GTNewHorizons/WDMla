package com.gtnewhorizons.wdmla.overlay;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.config.Configuration;

import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.utils.Constants;

/**
 * RayTracing class from Waila.<br>
 * Important Note: WailaStack and WailaEntity are no longer considered when retrieving results!(Because they are way too
 * outdated)
 */
public class RayTracing {

    private static RayTracing _instance;

    private RayTracing() {}

    public static RayTracing instance() {
        if (_instance == null) _instance = new RayTracing();
        return _instance;
    }

    private MovingObjectPosition target = null;
    private final Minecraft mc = Minecraft.getMinecraft();

    public void fire() {
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
                && !shouldHidePlayer(mc.objectMouseOver.entityHit)) {
            this.target = mc.objectMouseOver;
            return;
        }

        EntityLivingBase viewpoint = mc.renderViewEntity;
        if (viewpoint == null) return;

        this.target = this.rayTrace(viewpoint, mc.playerController.getBlockReachDistance(), 0);
    }

    private static boolean shouldHidePlayer(Entity targetEnt) {
        // Check if entity is player with invisibility effect
        if (targetEnt instanceof EntityPlayer thePlayer) {
            boolean shouldHidePlayerSetting = !ConfigHandler.instance().getConfig("vanilla.show_invisible_players");
            return shouldHidePlayerSetting && thePlayer.isInvisible();
        }
        return false;
    }

    public MovingObjectPosition getTarget() {
        return this.target;
    }

    public ItemStack getTargetStack() {
        return this.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK ? this.getIdentifierStack() : null;
    }

    public Entity getTargetEntity() {
        return this.target.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY ? this.getIdentifierEntity()
                : null;
    }

    public MovingObjectPosition rayTrace(EntityLivingBase entity, double par1, float par3) {
        Vec3 vec3 = entity.getPosition(par3);
        Vec3 vec31 = entity.getLook(par3);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * par1, vec31.yCoord * par1, vec31.zCoord * par1);

        if (ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_LIQUID, true))
            return entity.worldObj.rayTraceBlocks(vec3, vec32, true);
        else return entity.worldObj.rayTraceBlocks(vec3, vec32, false);
    }

    public ItemStack getIdentifierStack() {
        ArrayList<ItemStack> items = this.getIdentifierItems();

        if (items.isEmpty()) return null;

        items.sort((stack0, stack1) -> stack1.getItemDamage() - stack0.getItemDamage());

        return items.get(0);
    }

    public Entity getIdentifierEntity() {
        return this.target.entityHit;
    }

    public ArrayList<ItemStack> getIdentifierItems() {
        ArrayList<ItemStack> items = new ArrayList<>();

        if (this.target == null) return items;

        World world = mc.theWorld;

        int x = this.target.blockX;
        int y = this.target.blockY;
        int z = this.target.blockZ;

        Block mouseoverBlock = world.getBlock(x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (mouseoverBlock == null) return items;

        if (tileEntity == null) {
            try {
                ItemStack block = new ItemStack(mouseoverBlock, 1, world.getBlockMetadata(x, y, z));

                if (block.getItem() != null) items.add(block);

            } catch (Exception ignored) {}
        }

        if (!items.isEmpty()) return items;

        try {
            ItemStack pick = mouseoverBlock.getPickBlock(this.target, world, x, y, z);
            if (pick != null) items.add(pick);
        } catch (Exception ignored) {}

        if (!items.isEmpty()) return items;

        if (mouseoverBlock instanceof IShearable shearable) {
            if (shearable.isShearable(new ItemStack(Items.shears), world, x, y, z)) {
                items.addAll(shearable.onSheared(new ItemStack(Items.shears), world, x, y, z, 0));
            }
        }

        if (items.isEmpty()) items.add(0, new ItemStack(mouseoverBlock, 1, world.getBlockMetadata(x, y, z)));

        return items;
    }

}
