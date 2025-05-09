package com.gtnewhorizons.wdmla.impl.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.wdmla.api.ui.HighlightState;
import com.gtnewhorizons.wdmla.api.ui.IComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.HPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.IconComponent;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Padding;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;
import com.gtnewhorizons.wdmla.overlay.WDMlaUIIcons;

/**
 * Use this class to unify common object status
 */
public class StatusHelper {

    public static final StatusHelper INSTANCE = new StatusHelper();

    public static final int ICON_SIZE = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;

    private StatusHelper() {

    }

    public IComponent structureIncomplete() {
        return new HPanelComponent().child(
                new IconComponent(WDMlaUIIcons.ERROR, WDMlaUIIcons.ERROR.texPath).size(new Size(ICON_SIZE, ICON_SIZE)))
                .child(
                        ThemeHelper.INSTANCE
                                .failure(StatCollector.translateToLocal("hud.msg.wdmla.incomplete.structure")));
    }

    public IComponent hasProblem() {
        return new HPanelComponent().child(
                new IconComponent(WDMlaUIIcons.ERROR, WDMlaUIIcons.ERROR.texPath).size(new Size(ICON_SIZE, ICON_SIZE)))
                .child(ThemeHelper.INSTANCE.failure(StatCollector.translateToLocal("hud.msg.wdmla.has.problem")));
    }

    public IComponent runningFine() {
        return new HPanelComponent().child(
                new IconComponent(WDMlaUIIcons.START, WDMlaUIIcons.START.texPath).size(new Size(ICON_SIZE, ICON_SIZE)))
                .child(ThemeHelper.INSTANCE.success(StatCollector.translateToLocal("hud.msg.wdmla.running.fine")));
    }

    public IComponent idle() {
        return new HPanelComponent().child(
                new IconComponent(WDMlaUIIcons.IDLE, WDMlaUIIcons.IDLE.texPath).size(new Size(ICON_SIZE, ICON_SIZE)))
                .child(ThemeHelper.INSTANCE.info(StatCollector.translateToLocal("hud.msg.wdmla.idle")));
    }

    public IComponent workingDisabled() {
        return new HPanelComponent().child(
                new IconComponent(WDMlaUIIcons.PAUSE, WDMlaUIIcons.PAUSE.texPath).size(new Size(ICON_SIZE, ICON_SIZE)))
                .child(ThemeHelper.INSTANCE.info(StatCollector.translateToLocal("hud.msg.wdmla.working.disabled")));
    }

    public IComponent insufficientEnergy() {
        return new HPanelComponent().child(
                new IconComponent(WDMlaUIIcons.WARNING, WDMlaUIIcons.WARNING.texPath)
                        .size(new Size(ICON_SIZE, ICON_SIZE)))
                .child(
                        ThemeHelper.INSTANCE
                                .warning(StatCollector.translateToLocal("hud.msg.wdmla.insufficient.energy")));
    }

    public IComponent insufficientFuel() {
        return new HPanelComponent()
                .child(
                        new IconComponent(WDMlaUIIcons.WARNING, WDMlaUIIcons.WARNING.texPath)
                                .size(new Size(ICON_SIZE, ICON_SIZE)))
                .child(ThemeHelper.INSTANCE.warning(StatCollector.translateToLocal("hud.msg.wdmla.insufficient.fuel")));
    }

    public IComponent locked() {
        return locked(HighlightState.ACTIVE);
    }

    public IComponent locked(HighlightState highlightState) {
        switch (highlightState) {
            case ACTIVATING -> {
                return new HPanelComponent().child(
                        new IconComponent(WDMlaUIIcons.LOCK, WDMlaUIIcons.LOCK.texPath)
                                .size(new Size(ICON_SIZE, ICON_SIZE)))
                        .child(
                                ThemeHelper.INSTANCE
                                        .success(StatCollector.translateToLocal("hud.msg.wdmla.locked") + "!"));
            }
            case ACTIVE -> {
                return new HPanelComponent()
                        .child(
                                new IconComponent(WDMlaUIIcons.LOCK, WDMlaUIIcons.LOCK.texPath)
                                        .size(new Size(ICON_SIZE, ICON_SIZE)))
                        .child(ThemeHelper.INSTANCE.info(StatCollector.translateToLocal("hud.msg.wdmla.locked")));
            }
            case DEACTIVATING -> {
                return new HPanelComponent()
                        .child(
                                new IconComponent(WDMlaUIIcons.LOCK, WDMlaUIIcons.LOCK.texPath)
                                        .size(new Size(ICON_SIZE, ICON_SIZE)).child(
                                                new HPanelComponent().padding(new Padding(2, 0, 1.5f, 0))
                                                        .child(ThemeHelper.INSTANCE.failure("✕"))))
                        .child(ThemeHelper.INSTANCE.failure(StatCollector.translateToLocal("hud.msg.wdmla.locked")));
            }
            default -> {
                return new HPanelComponent();
            }
        }
    }

    /**
     * Indicates the input item/fluid is voided on overflow. This concept is shared between multiple storage mods
     * (drawers, barrels, super chests...)
     */
    public IComponent voidOverflow() {
        return voidOverflow(HighlightState.ACTIVE);
    }

    public IComponent voidOverflow(HighlightState highlightState) {
        switch (highlightState) {
            case ACTIVATING -> {
                return new HPanelComponent()
                        .child(
                                new IconComponent(WDMlaUIIcons.VOID, WDMlaUIIcons.VOID.texPath)
                                        .size(new Size(ICON_SIZE, ICON_SIZE)))
                        .child(
                                ThemeHelper.INSTANCE
                                        .success(StatCollector.translateToLocal("hud.msg.wdmla.void.overflow") + "!"));
            }
            case ACTIVE -> {
                return new HPanelComponent().child(
                        new IconComponent(WDMlaUIIcons.VOID, WDMlaUIIcons.VOID.texPath)
                                .size(new Size(ICON_SIZE, ICON_SIZE)))
                        .child(
                                ThemeHelper.INSTANCE
                                        .info(StatCollector.translateToLocal("hud.msg.wdmla.void.overflow")));
            }
            case DEACTIVATING -> {
                return new HPanelComponent()
                        .child(
                                new IconComponent(WDMlaUIIcons.VOID, WDMlaUIIcons.VOID.texPath)
                                        .size(new Size(ICON_SIZE, ICON_SIZE)).child(
                                                new HPanelComponent().padding(new Padding(2, 0, 1.5f, 0))
                                                        .child(ThemeHelper.INSTANCE.failure("✕"))))
                        .child(
                                ThemeHelper.INSTANCE
                                        .failure(StatCollector.translateToLocal("hud.msg.wdmla.void.overflow")));
            }
            default -> {
                return new HPanelComponent();
            }
        }
    }
}
