package com.gtnewhorizons.wdmla.impl.ui;

import static mcp.mobius.waila.api.SpecialChars.ITALIC;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.Theme;
import com.gtnewhorizons.wdmla.api.ui.IComponent;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.api.ui.MessageType;
import com.gtnewhorizons.wdmla.config.General;
import com.gtnewhorizons.wdmla.config.PluginsConfig;
import com.gtnewhorizons.wdmla.impl.format.TimeFormattingPattern;
import com.gtnewhorizons.wdmla.impl.ui.component.EntityComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.HPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.IconComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.ItemComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.TextComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.VPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Padding;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;
import com.gtnewhorizons.wdmla.impl.ui.style.TextStyle;
import com.gtnewhorizons.wdmla.overlay.WDMlaUIIcons;
import com.gtnewhorizons.wdmla.plugin.vanilla.VanillaIdentifiers;
import com.gtnewhorizons.wdmla.util.Color;
import com.gtnewhorizons.wdmla.util.FormatUtil;

import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.ModIdentification;

/**
 * Use this class to unify common layout settings
 */
public class ThemeHelper {

    public static final ThemeHelper INSTANCE = new ThemeHelper();

    private static final int ITEM_SIZE = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;

    private ThemeHelper() {

    }

    @Deprecated
    public void overrideTooltipIcon(ITooltip root, ItemStack newItemStack) {
        overrideTooltipIcon(root, newItemStack, false);
    }

    public void overrideTooltipIcon(ITooltip root, ItemStack newItemStack, boolean overrideFancyRenderer) {
        if (!overrideFancyRenderer
                && PluginsConfig.core.defaultBlock.fancyRenderer == PluginsConfig.Core.fancyRendererMode.ALL) {
            return;
        }

        root.replaceChildWithTag(
                Identifiers.ITEM_ICON,
                new ItemComponent(newItemStack).doDrawOverlay(false).tag(Identifiers.ITEM_ICON));
    }

    public void overrideTooltipTitle(ITooltip root, ItemStack newItemStack) {
        String strippedName = DisplayUtil.itemDisplayNameShortFormatted(newItemStack);
        overrideTooltipTitle(root, strippedName);
    }

    public void overrideTooltipTitle(ITooltip root, String formattedNewName) {
        Theme theme = General.currentTheme.get();
        IComponent replacedName = new HPanelComponent().child(
                new TextComponent(formattedNewName).style(new TextStyle().color(theme.textColor(MessageType.TITLE))))
                .tag(Identifiers.ITEM_NAME);
        root.replaceChildWithTag(Identifiers.ITEM_NAME, replacedName);
    }

    public void overrideEntityTooltipTitle(ITooltip root, String newName, @Nullable Entity entityMayHaveCustomName) {
        Theme theme = General.currentTheme.get();
        if (entityMayHaveCustomName instanceof EntityLiving living && living.hasCustomNameTag()) {
            newName = FormatUtil.formatNameByPixelCount(living.getCustomNameTag());
        } else {
            newName = FormatUtil.formatNameByPixelCount(newName);
        }
        IComponent replacedName = new HPanelComponent()
                .child(new TextComponent(newName).style(new TextStyle().color(theme.textColor(MessageType.TITLE))))
                .tag(Identifiers.ENTITY_NAME);
        root.replaceChildWithTag(Identifiers.ENTITY_NAME, replacedName);
    }

    public void overrideEntityTooltipIcon(ITooltip root, @Nullable Entity newEntity) {
        if (PluginsConfig.core.defaultEntity.showEntity) {
            root.replaceChildWithTag(Identifiers.ENTITY, entityIcon(newEntity).tag(Identifiers.ENTITY));
        }
    }

    public IComponent entityIcon(@Nullable Entity entity) {
        if (entity instanceof EntityPainting) {
            if (PluginsConfig.core.defaultEntity.paintingAsItemIcon) {
                return new ItemComponent(new ItemStack(Items.painting)).doDrawOverlay(false);
            }
            return new EntityComponent(entity).padding(new Padding()).size(new Size(24, 24));
        }
        if (entity == null || (!PluginsConfig.core.defaultEntity.fancyRenderer && !(entity instanceof EntityLiving))) {
            return new HPanelComponent();
        }
        return new EntityComponent(entity).padding(new Padding(6, 0, 10, 0)).size(new Size(12, 12));
    }

    public void overrideTooltipModName(ITooltip root, ItemStack newItemStack) {
        overrideTooltipModName(root, ModIdentification.nameFromStack(newItemStack));
    }

    public void overrideTooltipModName(ITooltip root, String newName) {
        IComponent replacedModName = modName(newName).tag(Identifiers.MOD_NAME);
        root.replaceChildWithTag(Identifiers.MOD_NAME, replacedModName);
    }

    public void overrideTooltipHeader(ITooltip root, ItemStack newItemStack) {
        overrideTooltipIcon(root, newItemStack, false);
        overrideTooltipTitle(root, newItemStack);
        overrideTooltipModName(root, newItemStack);
    }

    public IComponent modName(String modName) {
        String content = modName == null ? "" : modName;
        if (General.modName.hudItalic && !content.isEmpty()) {
            content = ITALIC + content;
        }
        return new TextComponent(content).style(new TextStyle().color(resolveModNameColor()));
    }

    private int resolveModNameColor() {
        int fallback = General.currentTheme.get().textColor(MessageType.MOD_NAME);
        return Color.parseColor(General.modName.hudColorOverride, fallback);
    }

    public IComponent info(String content) {
        return color(content, MessageType.INFO);
    }

    public IComponent title(String content) {
        return color(content, MessageType.TITLE);
    }

    public IComponent success(String content) {
        return color(content, MessageType.SUCCESS);
    }

    public IComponent warning(String content) {
        return color(content, MessageType.WARNING);
    }

    public IComponent danger(String content) {
        return color(content, MessageType.DANGER);
    }

    public IComponent failure(String content) {
        return color(content, MessageType.FAILURE);
    }

    public IComponent color(String content, MessageType type) {
        Theme theme = General.currentTheme.get();
        return new TextComponent(content).style(new TextStyle().color(theme.textColor(type)));
    }

    public IComponent furnaceLikeProgress(List<ItemStack> input, List<ItemStack> output, int currentProgress,
            int maxProgress, boolean showDetails) {
        return furnaceLikeProgress(input, output, currentProgress, maxProgress, showDetails, null);
    }

    /**
     * Provides Minecraft furnace progress arrow and item display.
     * 
     * @param input             the items on the left side of arrow
     * @param output            the items on the right side of arrow
     * @param currentProgress   ticks elapsed
     * @param maxProgress       the length of ticks to fill the arrow
     * @param showDetails       is Show Details button pressed or not (for controlling legacy text)
     * @param legacyProcessText The text displayed instead of arrow and ItemStacks in legacy mode. If null, it will be
     *                          auto generated.
     * @return built component
     */
    public IComponent furnaceLikeProgress(List<ItemStack> input, List<ItemStack> output, int currentProgress,
            int maxProgress, boolean showDetails, @Nullable IComponent legacyProcessText) {
        if (!General.forceLegacy) {
            HPanelComponent hPanel = new HPanelComponent();
            for (ItemStack inputStack : input) {
                if (inputStack != null) {
                    hPanel.item(inputStack);
                }
            }
            float ratio = (float) currentProgress / maxProgress;
            hPanel.padding(new Padding().horizontal(2)).child(
                    new IconComponent(WDMlaUIIcons.FURNACE_BG, WDMlaUIIcons.FURNACE_BG.texPath).padding(new Padding())
                            .child(
                                    new IconComponent(WDMlaUIIcons.FURNACE, WDMlaUIIcons.FURNACE.texPath)
                                            .clip(0f, 0f, ratio, 1f).padding(new Padding())));
            for (ItemStack outputStack : output) {
                if (outputStack != null) {
                    hPanel.item(outputStack);
                }
            }
            return hPanel;
        } else {
            ITooltip vPanel = new VPanelComponent();
            if (showDetails) {
                for (ItemStack inputStack : input) {
                    if (inputStack != null) {
                        vPanel.horizontal()
                                .text(String.format("%s: ", StatCollector.translateToLocal("hud.msg.wdmla.in"))).child(
                                        ThemeHelper.INSTANCE.info(
                                                String.format(
                                                        "%dx %s",
                                                        inputStack.stackSize,
                                                        DisplayUtil.itemDisplayNameShortFormatted(inputStack))));
                    }
                }
                for (ItemStack outputStack : output) {
                    if (outputStack != null) {
                        vPanel.horizontal()
                                .text(String.format("%s: ", StatCollector.translateToLocal("hud.msg.wdmla.out"))).child(
                                        ThemeHelper.INSTANCE.info(
                                                String.format(
                                                        "%dx %s",
                                                        outputStack.stackSize,
                                                        DisplayUtil.itemDisplayNameShortFormatted(outputStack))));
                    }
                }
            }

            if (currentProgress != 0 && maxProgress != 0 && legacyProcessText == null) {
                legacyProcessText = ThemeHelper.INSTANCE.value(
                        StatCollector.translateToLocal("hud.msg.wdmla.progress"),
                        TimeFormattingPattern.ALWAYS_TICK.tickFormatter.apply(currentProgress) + " / "
                                + TimeFormattingPattern.ALWAYS_TICK.tickFormatter.apply(maxProgress));
            }

            if (legacyProcessText != null) {
                vPanel.child(legacyProcessText);
            }

            if (vPanel.childrenSize() != 0) {
                return vPanel;
            } else {
                return null;
            }
        }
    }

    public IComponent value(String entry, String value) {
        return new HPanelComponent().text(String.format("%s: ", entry)).child(info(value));
    }

    /**
     * Provides an ItemComponent with has size of default text height
     * 
     * @param itemStack Base ItemStack to display
     */
    public ITooltip smallItem(ItemStack itemStack) {
        return new ItemComponent(itemStack).doDrawOverlay(false).size(new Size(ITEM_SIZE, ITEM_SIZE));
    }

    /**
     * Constructs a component to display an ItemStack in "(icon) 3x Apple" format
     */
    public IComponent itemStackFullLine(ItemStack stack) {
        String strippedName = DisplayUtil.stripSymbols(DisplayUtil.itemDisplayNameShortFormatted(stack));
        TextComponent name = new TextComponent(strippedName);
        ITooltip hPanel = new HPanelComponent().child(smallItem(stack));
        String s = String.valueOf(stack.stackSize); // TODO: unit format
        return hPanel.text(s).text(StatCollector.translateToLocal("hud.msg.wdmla.item.count") + StringUtils.EMPTY)
                .child(name);
    }

    /**
     * display any crop's growth value with percentage
     * 
     * @param growthValue growth value (0 ~ 1)
     */
    public IComponent growthValue(float growthValue) {
        if (growthValue < 1) {
            return ThemeHelper.INSTANCE.value(
                    StatCollector.translateToLocal("hud.msg.wdmla.growth"),
                    FormatUtil.PERCENTAGE_STANDARD.format(growthValue)).tag(VanillaIdentifiers.GROWTH_RATE);
        } else {
            return new HPanelComponent()
                    .text(String.format("%s: ", StatCollector.translateToLocal("hud.msg.wdmla.growth")))
                    .child(
                            ThemeHelper.INSTANCE.success(
                                    String.format("%s", StatCollector.translateToLocal("hud.msg.wdmla.mature"))))
                    .tag(VanillaIdentifiers.GROWTH_RATE);
        }
    }
}
