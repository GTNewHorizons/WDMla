package com.gtnewhorizons.wdmla.impl.ui.drawable;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.wdmla.api.ui.IDrawable;
import com.gtnewhorizons.wdmla.api.ui.sizer.IArea;
import org.jetbrains.annotations.Nullable;

public class ItemDrawable implements IDrawable {

    private final @NotNull ItemStack item;

    private boolean doDrawOverlay = true;
    @Nullable
    private String stackSizeOverride = null;

    public ItemDrawable(@NotNull ItemStack item) {
        this.item = item;
    }

    public ItemDrawable doDrawOverlay(boolean flag) {
        this.doDrawOverlay = flag;
        return this;
    }

    public ItemDrawable stackSizeOverride(String stackSizeOverride) {
        this.stackSizeOverride = stackSizeOverride;
        return this;
    }

    @Override
    public void draw(IArea area) {
        GuiDraw.renderStack(area, item, doDrawOverlay, stackSizeOverride);
    }
}
