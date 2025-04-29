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
        float autoScale = Math.max(PluginsConfig.core.defaultEntity.iconDefaultScale, 0.1f) / Math.max(entity.width, entity.height);
        GL11.glPushMatrix();
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
                GL11.glTranslatef(area.getX(), area.getY() + area.getH(), 0);
                if(PluginsConfig.core.defaultEntity.iconAutoScale) {
                    GL11.glScalef(autoScale, autoScale, 1.0f);
                }
                GuiInventory.func_147046_a(
                        0,
                        0,
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
                // yOffset
                GL11.glTranslatef(area.getX(), (area.getY() + area.getH() - area.getW() / 2), 0);
                if(PluginsConfig.core.defaultEntity.iconAutoScale) {
                    GL11.glScalef(autoScale, autoScale, 1.0f);
                }
                GuiDraw.drawNonLivingEntity(
                        0,
                        0,
                        (int) area.getW(),
                        135 + (entity.ticksExisted * PluginsConfig.core.defaultEntity.rendererRotationSpeed) % 360,
                        -0,
                        entity
                );
            }
        } catch (Exception e) {
            Waila.log.error("Error rendering instance of entity", e);
        }
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
}
