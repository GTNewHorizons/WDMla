package com.gtnewhorizons.wdmla.impl.ui.drawable;

import net.minecraft.client.Minecraft;

import com.gtnewhorizons.wdmla.api.ui.IDrawable;
import com.gtnewhorizons.wdmla.api.ui.sizer.IArea;
import com.gtnewhorizons.wdmla.config.PluginsConfig;
import com.gtnewhorizons.wdmla.overlay.GuiBlockDraw;

import mcp.mobius.waila.overlay.OverlayConfig;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.gtnewhorizons.wdmla.overlay.GuiDraw;

public class BlockDrawable implements IDrawable {

    private static final float SIZE_MULTIPLIER = 1.5f;

    protected static float rotationPitch = 30f;
    protected static long lastTime;

    private final int blockX;
    private final int blockY;
    private final int blockZ;

    public BlockDrawable(int blockX, int blockY, int blockZ) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }

    @Override
    public void draw(IArea area) {
        // TODO: get RenderPartialTick
        rotationPitch += (Minecraft.getMinecraft().theWorld.getTotalWorldTime() - lastTime)
                * PluginsConfig.core.defaultBlock.rendererRotationSpeed;
        // custom viewport is unaffected by GLScalef
        boolean rendered = GuiBlockDraw.drawWorldBlock(
                (int) ((area.getX() - area.getW() * (SIZE_MULTIPLIER - 1) / 2) * OverlayConfig.scale),
                (int) ((area.getY() - area.getH() * (SIZE_MULTIPLIER - 1) / 2) * OverlayConfig.scale),
                (int) (area.getW() * OverlayConfig.scale * SIZE_MULTIPLIER),
                (int) (area.getH() * OverlayConfig.scale * SIZE_MULTIPLIER),
                blockX,
                blockY,
                blockZ,
                30f,
                rotationPitch);

        if (!rendered) {
            // If the fancy world-block preview produced no geometry/TESR, fall back to the item icon.
            Minecraft mc = Minecraft.getMinecraft();
            Block b = mc.theWorld.getBlock(blockX, blockY, blockZ);
            if (b == null) b = Blocks.air;
            int meta = mc.theWorld.getBlockMetadata(blockX, blockY, blockZ);
            Item it = Item.getItemFromBlock(b);
            ItemStack stack = null;
            if (it != null) {
                try {
                    int dmg = b.damageDropped(meta);
                    stack = new ItemStack(it, 1, dmg);
                } catch (Throwable t) {
                    stack = new ItemStack(it, 1, meta);
                }
            }
            if (stack == null) stack = new ItemStack(Blocks.air);
            GuiDraw.renderStack(area, stack, false, null);
        }
        lastTime = Minecraft.getMinecraft().theWorld.getTotalWorldTime();
    }
}