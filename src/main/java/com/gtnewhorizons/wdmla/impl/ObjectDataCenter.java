package com.gtnewhorizons.wdmla.impl;

import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.wdmla.api.accessor.Accessor;
import com.gtnewhorizons.wdmla.api.accessor.AccessorClientHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Handles all static data required for WDMla.<br>
 * It sometimes has to be used along with {@link mcp.mobius.waila.api.impl.DataAccessorCommon}.<br>
 * TODO: full implementation
 */
@SideOnly(Side.CLIENT)
public final class ObjectDataCenter {

    public static final int RATE_LIMIT_MP = 250;
    public static final int RATE_LIMIT_SP = 1;
    /**
     * the current passive frequency of requesting server data
     */
    private static int rateLimiter = RATE_LIMIT_MP;
    public static long timeLastUpdate = System.currentTimeMillis();
    public static HarvestabilityInfo harvestabilityInfo;
    private static Accessor accessor;
    private static AccessorClientHandler<Accessor> clientHandler;
    private static NBTTagCompound serverData = new NBTTagCompound();
    private static MovingObjectPosition lastObject;

    private ObjectDataCenter() {}

    public static void set(@Nullable Accessor accessor) {
        ObjectDataCenter.accessor = accessor;
        if (accessor == null) {
            lastObject = null;
            clientHandler = null;
            return;
        }

        clientHandler = WDMlaClientRegistration.instance().getAccessorHandler(accessor.getAccessorType());
        MovingObjectPosition object = accessor.getHitResult();

        if (!equals(object, lastObject)) {
            lastObject = object;
            serverData = null;
            harvestabilityInfo = null;
            requestServerData();
        }
    }

    @Nullable
    public static Accessor get() {
        return accessor;
    }

    public static NBTTagCompound getServerData() {
        if (accessor == null || clientHandler == null || serverData == null) {
            return null;
        }
        if (accessor.verifyData(serverData)) {
            return serverData;
        }
        requestServerData();
        return null;
    }

    public static void setServerData(NBTTagCompound tag) {

        serverData = tag;

        // player no longer looks at object when the server responded
        if (accessor == null) {
            return;
        }

        if (accessor.verifyData(serverData)) {
            accessor.getServerData().func_150296_c().clear();
            for (String s : tag.func_150296_c()) {
                NBTBase nbt = tag.getTag(s);
                accessor.getServerData().setTag(s, nbt);
            }
        }
    }

    public static void requestServerData() {
        timeLastUpdate = System.currentTimeMillis() - rateLimiter;
    }

    public static boolean isRequestTimeElapsed() {
        return System.currentTimeMillis() - timeLastUpdate >= rateLimiter;
    }

    public static void resetTimer() {
        timeLastUpdate = System.currentTimeMillis();
        rateLimiter = Minecraft.getMinecraft().isSingleplayer() ? RATE_LIMIT_SP : RATE_LIMIT_MP;
    }

    /**
     * @return is ray cast result from different frames targeting the same object or not
     */
    private static boolean equals(MovingObjectPosition mop1, MovingObjectPosition mop2) {
        if (mop1 != null && mop1.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                && mop2 != null
                && mop2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            return mop1.blockX == mop2.blockX && mop1.blockY == mop2.blockY && mop1.blockZ == mop2.blockZ;
        }

        if (mop1 != null && mop1.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
                && mop2 != null
                && mop2.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            return mop1.entityHit.getEntityId() == mop2.entityHit.getEntityId();
        }

        return false;
    }

    public static void setHarvestabilityInfo(HarvestabilityInfo info) {
        harvestabilityInfo = info;
    }

    public static boolean canCurrentTargetBeHarvested() {
        return harvestabilityInfo == null || harvestabilityInfo.isCurrentlyHarvestable();
    }
}
