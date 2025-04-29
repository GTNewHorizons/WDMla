package com.gtnewhorizons.wdmla.impl.ui.drawable;

import com.gtnewhorizons.wdmla.config.PluginsConfig;
import com.gtnewhorizons.wdmla.overlay.GuiDraw;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.BossStatus;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.wdmla.api.ui.IDrawable;
import com.gtnewhorizons.wdmla.api.ui.sizer.IArea;

import mcp.mobius.waila.Waila;

public class EntityDrawable implements IDrawable {

    private final @NotNull Entity entity;

    public EntityDrawable(@NotNull Entity entity) {
        this.entity = entity;
    }

    @Override
    public void draw(IArea area) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        // TODO: support float
        try {
            if(entity instanceof EntityLiving living) {
                String bossName = BossStatus.bossName;
                int bossTimeout = BossStatus.statusBarTime;
                boolean bossHasColorModifier = BossStatus.hasColorModifier;
                float renderTagRange = RendererLivingEntity.NAME_TAG_RANGE;
                float renderTagRangeSneaking = RendererLivingEntity.NAME_TAG_RANGE_SNEAK;
                // editing entity custom name directly will trigger DataWatcher
                RendererLivingEntity.NAME_TAG_RANGE = 0;
                RendererLivingEntity.NAME_TAG_RANGE_SNEAK = 0;
                GuiInventory.func_147046_a(
                        (int) area.getX(),
                        (int) (area.getY() + area.getH()),
                        (int) area.getW(),
                        135,
                        0,
                        living);
                RendererLivingEntity.NAME_TAG_RANGE = renderTagRange;
                RendererLivingEntity.NAME_TAG_RANGE_SNEAK = renderTagRangeSneaking;
                BossStatus.bossName = bossName;
                BossStatus.statusBarTime = bossTimeout;
                BossStatus.hasColorModifier = bossHasColorModifier;
            }
            else {
                GuiDraw.drawNonLivingEntity(
                        (int) area.getX(),
                        (int) (area.getY() + area.getH() - area.getW() / 2), //offset
                        (int) area.getW(),
                        135 + (entity.ticksExisted * PluginsConfig.core.defaultEntity.rendererRotationSpeed) % 360,
                        -0,
                        entity
                );
            }
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        } catch (Exception e) {
            Waila.log.error("Error rendering instance of entity", e);
        }
    }
}
