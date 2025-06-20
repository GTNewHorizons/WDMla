package com.gtnewhorizons.wdmla.plugin.harvestability;

import java.util.List;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityInfoImpl;
import com.gtnewhorizons.wdmla.api.harvestability.HarvestabilityTestPhase;
import com.gtnewhorizons.wdmla.api.provider.HarvestHandler;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.IComponent;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.config.General;
import com.gtnewhorizons.wdmla.config.PluginsConfig;
import com.gtnewhorizons.wdmla.impl.ObjectDataCenter;
import com.gtnewhorizons.wdmla.impl.WDMlaClientRegistration;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.impl.ui.component.HPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.ItemComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.VPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Padding;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;
import com.gtnewhorizons.wdmla.plugin.harvestability.proxy.ProxyCreativeBlocks;

import mcp.mobius.waila.overlay.DisplayUtil;

public enum HarvestToolProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public ResourceLocation getUid() {
        return HarvestabilityIdentifiers.HARVESTABILITY;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.HARVESTABILITY_OVERRIDE;
    }

    @Override
    public boolean isPriorityFixed() {
        return true;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        if (General.forceLegacy) {
            tooltip.text("The legacy harvestability support").text("will come back in the next version!");
            return;
        }

        if (ProxyCreativeBlocks.isCreativeBlock(accessor.getBlock(), accessor.getMetadata())) {
            ObjectDataCenter.setHarvestabilityInfo(null);
            return;
        }

        List<HarvestHandler> handlers = WDMlaClientRegistration.instance()
                .getHarvestHandlers(accessor.getBlock(), interactionHandler -> true);

        Block effectiveBlock = handlers.stream()
                .map(
                        handler -> handler.getEffectiveBlock(
                                accessor.getPlayer(),
                                accessor.getBlock(),
                                accessor.getItemForm(),
                                accessor.getMetadata(),
                                accessor.getHitResult()))
                .filter(Objects::nonNull).findFirst().orElse(accessor.getBlock());
        int effectiveMeta = handlers.stream()
                .map(
                        handler -> handler.getEffectiveMeta(
                                accessor.getPlayer(),
                                accessor.getBlock(),
                                accessor.getItemForm(),
                                accessor.getMetadata(),
                                accessor.getHitResult()))
                .filter(Objects::nonNull).findFirst().orElse(accessor.getMetadata());

        HarvestabilityInfo info = getHarvestability(
                accessor.getPlayer(),
                effectiveBlock,
                effectiveMeta,
                accessor.getHitResult(),
                handlers);

        ObjectDataCenter.setHarvestabilityInfo(info);

        if (PluginsConfig.harvestability.condition.oresOnly
                && !BlockHelper.isBlockAnOre(effectiveBlock, effectiveMeta)) {
            return;
        }

        if ((PluginsConfig.harvestability.condition.unHarvestableOnly && info.isCurrentlyHarvestable())
                || (PluginsConfig.harvestability.condition.toolRequiredOnly && !info.getEffectiveTool().isValid())) {
            return;
        }

        IComponent itemNameRow = tooltip.getChildWithTag(Identifiers.TARGET_NAME_ROW);
        if (!(itemNameRow instanceof ITooltip)) {
            return;
        }

        IComponent harvestabilityIcon = assembleHarvestabilityIcon(info);

        IComponent harvestabilityText = null;
        if (!PluginsConfig.harvestability.condition.textDetailsOnly || accessor.showDetails()) {
            harvestabilityText = assembleHarvestabilityText(info);
        }

        ((ITooltip) itemNameRow).child(harvestabilityIcon);
        if (harvestabilityText != null) {
            tooltip.child(harvestabilityText);
        }
    }

    /**
     * @return element1: harvest tool icon to append after item name
     *         <p>
     *         element2: harvestability String if the harvest level is greater than 0
     */
    public HarvestabilityInfo getHarvestability(EntityPlayer player, Block block, int meta,
            MovingObjectPosition position, List<HarvestHandler> handlers) {
        // needed to stop array index out of bounds exceptions on mob spawners
        // block.getHarvestLevel/getHarvestTool are only 16 elements big
        if (meta >= 16) meta = 0;

        HarvestabilityInfo info = new HarvestabilityInfoImpl();
        if (!fireHarvestTest(
                HarvestabilityTestPhase.EFFECTIVE_TOOL_NAME,
                player,
                block,
                meta,
                position,
                handlers,
                info)) {
            return info;
        }

        if (!fireHarvestTest(HarvestabilityTestPhase.HARVEST_LEVEL, player, block, meta, position, handlers, info)) {
            return info;
        }

        if (!fireHarvestTest(
                HarvestabilityTestPhase.ADDITIONAL_TOOLS_ICON,
                player,
                block,
                meta,
                position,
                handlers,
                info)) {
            return info;
        }

        if (!fireHarvestTest(
                HarvestabilityTestPhase.CURRENTLY_HARVESTABLE,
                player,
                block,
                meta,
                position,
                handlers,
                info)) {
            return info;
        }

        fireHarvestTest(HarvestabilityTestPhase.IS_HELD_TOOL_EFFECTIVE, player, block, meta, position, handlers, info);

        return info;
    }

    private static boolean fireHarvestTest(HarvestabilityTestPhase phase, EntityPlayer player, Block block, int meta,
            MovingObjectPosition position, List<HarvestHandler> handlers, HarvestabilityInfo info) {
        for (HarvestHandler handler : handlers) {
            if (!handler.testHarvest(info, phase, player, block, meta, position)) {
                return false;
            }
        }

        return true;
    }

    private static @NotNull ITooltip assembleHarvestabilityIcon(HarvestabilityInfo info) {
        ITooltip harvestabilityComponent = new HPanelComponent().tag(HarvestabilityIdentifiers.HARVESTABILITY_ICON);

        if (!PluginsConfig.harvestability.icon.currentlyHarvestableIcon) {
            return harvestabilityComponent;
        }

        // TODO: resize CHECK text
        IComponent currentlyHarvestableIcon;
        if (info.isCurrentlyHarvestable()) {
            currentlyHarvestableIcon = !PluginsConfig.harvestability.icon.colorIconWithEffectiveness
                    || info.isHeldToolEffective()
                    || !info.getEffectiveTool().isValid()
                            ? ThemeHelper.INSTANCE.success(PluginsConfig.harvestability.icon.currentlyHarvestableString)
                            : ThemeHelper.INSTANCE.info(PluginsConfig.harvestability.icon.currentlyHarvestableString);
        } else {
            currentlyHarvestableIcon = ThemeHelper.INSTANCE
                    .failure(PluginsConfig.harvestability.icon.notCurrentlyHarvestableString);
        }

        ItemStack effectiveToolIcon = info.getEffectiveTool().getIcon(info.getHarvestLevel());
        if (effectiveToolIcon != null) {
            ITooltip effectiveToolIconComponent = new ItemComponent(effectiveToolIcon).doDrawOverlay(false)
                    .size(new Size(10, 10));

            if (PluginsConfig.harvestability.icon.effectiveToolIcon) {
                effectiveToolIconComponent.child(
                        new HPanelComponent().padding(new Padding().left(5).top(6)).child(currentlyHarvestableIcon));
                harvestabilityComponent.child(effectiveToolIconComponent);
            } else {
                harvestabilityComponent.child(currentlyHarvestableIcon);
            }
        } else {
            harvestabilityComponent.child(currentlyHarvestableIcon);
        }

        for (HarvestabilityInfo.AdditionalToolInfo additionalTool : info.getAdditionalToolsInfo()) {
            if (additionalTool.isHolding || PluginsConfig.harvestability.icon.alwaysShowAdditionalTools) {
                harvestabilityComponent
                        .child(new ItemComponent(additionalTool.icon).doDrawOverlay(false).size(new Size(10, 10)));
            }
        }
        return harvestabilityComponent;
    }

    private static @Nullable IComponent assembleHarvestabilityText(HarvestabilityInfo info) {
        ITooltip lines = new VPanelComponent();
        if (info.getHarvestLevel().isToolRequired() && (PluginsConfig.harvestability.text.harvestLevelNum
                || PluginsConfig.harvestability.text.harvestLevelName)) {
            String harvestLevelString = PluginsConfig.harvestability.text.harvestLevelName
                    ? DisplayUtil.stripSymbols(info.getHarvestLevel().getName())
                    : String.valueOf(info.getHarvestLevel().getDisplayNum());
            IComponent harvestLevelText = new HPanelComponent().tag(HarvestabilityIdentifiers.HARVESTABILITY_TEXT)
                    .text(String.format("%s: ", StatCollector.translateToLocal("hud.msg.wdmla.harvestlevel"))).child(
                            info.isCurrentlyHarvestable() ? ThemeHelper.INSTANCE.success(harvestLevelString)
                                    : ThemeHelper.INSTANCE.failure(harvestLevelString));
            lines.child(harvestLevelText);
        }

        if (PluginsConfig.harvestability.text.effectiveToolLine && info.getEffectiveTool().isValid()) {
            ITooltip effectiveToolPanel = new HPanelComponent();
            effectiveToolPanel.text(StatCollector.translateToLocal("hud.msg.wdmla.effectivetool") + ": ");
            if (info.isHeldToolEffective()) {
                effectiveToolPanel.child(ThemeHelper.INSTANCE.success(info.getEffectiveTool().getLocalizedName()));
            } else {
                effectiveToolPanel.child(ThemeHelper.INSTANCE.failure(info.getEffectiveTool().getLocalizedName()));
            }
            lines.child(effectiveToolPanel);
        }

        if (PluginsConfig.harvestability.text.currentlyHarvestableLine) {
            ITooltip currentlyHarvestable = new HPanelComponent();
            if (info.isCurrentlyHarvestable()) {
                String icon = PluginsConfig.harvestability.icon.currentlyHarvestableString;
                currentlyHarvestable.child(ThemeHelper.INSTANCE.success(icon));
            } else {
                String icon = PluginsConfig.harvestability.icon.notCurrentlyHarvestableString;
                currentlyHarvestable.child(ThemeHelper.INSTANCE.failure(icon));
            }
            String suffix = StatCollector.translateToLocal("hud.msg.wdmla.currentlyharvestable");
            lines.child(currentlyHarvestable.text(suffix));
        }

        return lines.childrenSize() > 0 ? lines : null;
    }
}
