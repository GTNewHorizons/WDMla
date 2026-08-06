package com.gtnewhorizons.wdmla.impl.ui.drawable;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityPainting;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.wdmla.api.ui.IDrawable;
import com.gtnewhorizons.wdmla.api.ui.sizer.IArea;
import com.gtnewhorizons.wdmla.config.PluginsConfig;
import com.gtnewhorizons.wdmla.overlay.GuiDraw;

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
        float autoScale = Math.max(PluginsConfig.core.defaultEntity.iconDefaultScale, 0.1f)
                / Math.max(entity.width, entity.height);
        GL11.glPushMatrix();
        try {
            if (entity instanceof EntityLiving living) {
                String bossName = BossStatus.bossName;
                int bossTimeout = BossStatus.statusBarTime;
                boolean bossHasColorModifier = BossStatus.hasColorModifier;
                float renderTagRange = RendererLivingEntity.NAME_TAG_RANGE;
                float renderTagRangeSneaking = RendererLivingEntity.NAME_TAG_RANGE_SNEAK;
                // editing entity custom name directly will trigger DataWatcher
                RendererLivingEntity.NAME_TAG_RANGE = 0;
                RendererLivingEntity.NAME_TAG_RANGE_SNEAK = 0;
                GL11.glTranslatef(area.getX(), area.getY() + area.getH(), 0);
                if (PluginsConfig.core.defaultEntity.iconAutoScale) {
                    GL11.glScalef(autoScale, autoScale, 1.0f);
                }
                GuiInventory.func_147046_a(0, 0, (int) area.getW(), 135, 0, living);
                RendererLivingEntity.NAME_TAG_RANGE = renderTagRange;
                RendererLivingEntity.NAME_TAG_RANGE_SNEAK = renderTagRangeSneaking;
                BossStatus.bossName = bossName;
                BossStatus.statusBarTime = bossTimeout;
                BossStatus.hasColorModifier = bossHasColorModifier;
            } else {
                // yOffset
                float baseX = area.getX();
                float baseY = area.getY() + area.getH() - area.getW() / 2;
                if (entity instanceof EntityPainting) {
                    baseX = area.getX() + area.getW() / 2.0f;
                    baseY = area.getY() + area.getH() / 2.0f;
                }
                GL11.glTranslatef(baseX, baseY, 0);
                float scale = 1.0f;
                if (entity instanceof EntityPainting painting) {
                    float fitScale = getPaintingFitScale(area, painting);
                    scale = fitScale;
                } else if (PluginsConfig.core.defaultEntity.iconAutoScale) {
                    scale = autoScale;
                }
                if (scale != 1.0f) {
                    GL11.glScalef(scale, scale, 1.0f);
                }
                float yaw = 135 + (entity.ticksExisted * PluginsConfig.core.defaultEntity.rendererRotationSpeed) % 360;
                float pitch = -0;
                if (entity instanceof EntityPainting) {
                    yaw = 0.0f;
                    pitch = 0.0f;
                }
                GuiDraw.drawNonLivingEntity(0, 0, (int) area.getW(), yaw, pitch, entity);
            }
        } catch (Exception e) {
            Waila.log.error("Error rendering instance of entity", e);
        }
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    private static float getPaintingFitScale(IArea area, EntityPainting painting) {
        int artWidth = painting.art.sizeX;
        int artHeight = painting.art.sizeY;
        if (artWidth <= 0 || artHeight <= 0) {
            return 1.0f;
        }
        float areaW = Math.max(area.getW(), 1.0f);
        float areaH = Math.max(area.getH(), 1.0f);
        float fitScaleW = 16.0f / artWidth;
        float fitScaleH = (areaH / areaW) * (16.0f / artHeight);
        return Math.min(fitScaleW, fitScaleH);
    }
}
