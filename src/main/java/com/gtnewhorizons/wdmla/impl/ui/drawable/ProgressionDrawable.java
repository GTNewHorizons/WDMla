package com.gtnewhorizons.wdmla.impl.ui.drawable;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.wdmla.api.ui.IDrawable;
import com.gtnewhorizons.wdmla.api.ui.IFilledProgress;
import com.gtnewhorizons.wdmla.api.ui.sizer.IArea;
import com.gtnewhorizons.wdmla.overlay.GuiDraw;

public class ProgressionDrawable implements IDrawable {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final ResourceLocation texture = new ResourceLocation("waila", "textures/sprites.png");
    private final @NotNull IFilledProgress progress;

    public ProgressionDrawable(@NotNull IFilledProgress progress) {
        this.progress = progress;
    }

    @Override
    public void draw(IArea area) {
        int progressPixel = (int) ((progress.getCurrent() * area.getW()) / progress.getMax());

        this.mc.getTextureManager().bindTexture(texture);

        GuiDraw.drawTexturedModelRect(area.getX() + 4, area.getY(), 4, 16, area.getW(), area.getH(), 28, 16);
        GuiDraw.drawTexturedModelRect(
                area.getX() + 4,
                area.getY(),
                4,
                0,
                progressPixel + 1,
                area.getH(),
                progressPixel + 1,
                16);
    }
}
