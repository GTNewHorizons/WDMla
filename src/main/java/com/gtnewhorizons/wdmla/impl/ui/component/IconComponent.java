package com.gtnewhorizons.wdmla.impl.ui.component;

import java.util.ArrayList;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.impl.ui.drawable.IconDrawable;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Padding;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;

public class IconComponent extends TooltipComponent {

    public IconComponent(IIcon icon, ResourceLocation path) {
        super(
                new ArrayList<>(),
                new Padding(-1, 0, 0, 0),
                new Size(icon.getIconWidth(), icon.getIconHeight()),
                new IconDrawable(icon, path));
    }

    public IconComponent clip(float suRatio, float svRatio, float twRatio, float thRatio) {
        ((IconDrawable)foreground).clip(suRatio, svRatio, twRatio, thRatio);
        return this;
    }
}
