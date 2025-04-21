package com.gtnewhorizons.wdmla.impl.ui.component;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.wdmla.api.ui.IComponent;
import com.gtnewhorizons.wdmla.api.ui.IDrawable;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.api.ui.sizer.IPadding;
import com.gtnewhorizons.wdmla.api.ui.sizer.ISize;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Area;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Padding;

public class TooltipComponent extends Component implements ITooltip {

    protected List<IComponent> children;

    public static final IPadding DEFAULT_PROGRESS_DESCRIPTION_PADDING = new Padding(3, 2, 2, 3);

    protected TooltipComponent(List<IComponent> children, IPadding padding, ISize size, IDrawable foreground) {
        super(padding, size, foreground);
        this.children = children;
    }

    @Override
    public TooltipComponent padding(@NotNull IPadding padding) {
        this.padding = padding;
        return this;
    }

    @Override
    public TooltipComponent size(@NotNull ISize size) {
        this.size = size;
        return this;
    }

    @Override
    public void tick(int x, int y) {
        foreground.draw(new Area(x + padding.getLeft(), y + padding.getTop(), size.getW(), size.getH()));

        for (IComponent child : children) {
            child.tick(x + padding.getLeft(), y + padding.getTop());
        }
    }

    @Override
    public ITooltip child(@NotNull IComponent child) {
        this.children.add(child);
        return this;
    }

    @Override
    public ITooltip text(String text, IPadding padding) {
        IComponent c = new TextComponent(text).padding(padding);
        this.children.add(c);
        return this;
    }

    @Override
    public ITooltip text(String text) {
        IComponent c = new TextComponent(text);
        this.children.add(c);
        return this;
    }

    @Override
    public ITooltip vertical() {
        ITooltip c = new VPanelComponent();
        this.children.add(c);
        return c;
    }

    @Override
    public ITooltip horizontal() {
        ITooltip c = new HPanelComponent();
        this.children.add(c);
        return c;
    }

    @Override
    public ITooltip item(ItemStack stack, IPadding padding, ISize size) {
        IComponent c = new ItemComponent(stack).padding(padding).size(size);
        this.children.add(c);
        return this;
    }

    @Override
    public ITooltip item(ItemStack stack) {
        IComponent c = new ItemComponent(stack);
        this.children.add(c);
        return this;
    }

    @Override
    public ITooltip progress(long current, long max, String progressText) {
        ITooltip c = new ProgressComponent(current, max)
                .child(new TextComponent(progressText).padding(DEFAULT_PROGRESS_DESCRIPTION_PADDING));
        this.children.add(c);
        return this;
    }

    @Override
    public int childrenSize() {
        return this.children.size();
    }

    @Override
    public ITooltip clear() {
        this.children.clear();
        return this;
    }

    @Override
    public ITooltip tag(ResourceLocation tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public IComponent getChildWithTag(ResourceLocation tag) {
        for (int i = 0; i < children.size(); i++) {
            IComponent child = children.get(i);
            if (tag.equals(child.getTag())) {
                return child;
            } else if (child instanceof ITooltip tooltipChild) {
                IComponent nestedChild = tooltipChild.getChildWithTag(tag);
                if (nestedChild != null) {
                    return nestedChild;
                }
            }
        }

        return null;
    }

    @Override
    public boolean replaceChildWithTag(@NotNull ResourceLocation tag, IComponent newChild) {
        for (int i = 0; i < children.size(); i++) {
            IComponent child = children.get(i);
            if (tag.equals(child.getTag())) {
                children.set(i, newChild);
                return true;
            } else if (child instanceof ITooltip tooltipChild) {
                boolean isReplaced = tooltipChild.replaceChildWithTag(tag, newChild);
                if (isReplaced) {
                    return true;
                }
            }
        }

        return false;
    }
}
