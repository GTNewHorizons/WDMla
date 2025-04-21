package com.gtnewhorizons.wdmla.api.ui;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.wdmla.api.ui.sizer.IPadding;
import com.gtnewhorizons.wdmla.api.ui.sizer.ISize;

/**
 * The interface for the advanced component which can append child components.<br>
 * Also provides quick shortcuts to append simple child elements of this component which does not require theme import.
 */
public interface ITooltip extends IComponent {

    ITooltip text(String text, IPadding padding);

    ITooltip text(String text);

    ITooltip vertical();

    ITooltip horizontal();

    ITooltip item(ItemStack stack, IPadding padding, ISize size);

    ITooltip item(ItemStack stack);

    ITooltip progress(long current, long max, String progressText);

    // spotless:off
    /**
     * Appends any component to this tooltip as child.<br>
     * Child component behaves same as normal component, except it can be searched from parent with
     * {@link ITooltip#getChildWithTag(ResourceLocation)}.<br>
     * Its position might be affected by the parent position and setting.<br>
     * Any tooltip is (mostly) the child of {@link com.gtnewhorizons.wdmla.impl.ui.component.RootComponent}. <br>
     * It can be chained like this:
     * 
     * <pre>
     * {@code
     *             tooltip.child(new HPanelComponent().text("example1").item(new ItemStack(Items.apple)))
     *                     .child(new ProgressComponent(10, 100))
     *                     .child(new ArmorComponent(10));
     * }
     * </pre>
     * 
     * @param child The component which will be the child of this component
     * @return this tooltip instance
     */
    //spotless:on
    ITooltip child(@NotNull IComponent child);

    /**
     * @return the count of children object appended to this tooltip
     */
    int childrenSize();

    /**
     * Clears all children appended to this tooltip.
     */
    ITooltip clear();

    ITooltip tag(ResourceLocation tag);

    /**
     * Search children inside this component with a tag. The target must have a tag applied with
     * {@link ITooltip#tag(ResourceLocation)}.
     * 
     * @param tag the identifier to find a child
     * @return the first component found
     */
    @Nullable
    IComponent getChildWithTag(ResourceLocation tag);

    /**
     * Replace a child inside this component with a specific tag.
     * 
     * @param tag      the identifier to find a child
     * @param newChild the new child to replace. I strongly recommend give it the same tag with previous one
     * @return The replacement was succeeded or not. If the child doesn't exist it will always return false
     */
    boolean replaceChildWithTag(ResourceLocation tag, IComponent newChild);
}
