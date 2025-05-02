package com.gtnewhorizons.wdmla.plugin.harvestability;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.gtnewhorizons.wdmla.api.HarvestabilityInfo;
import com.gtnewhorizons.wdmla.api.HarvestabilityTestPhase;
import com.gtnewhorizons.wdmla.api.provider.HarvestHandler;
import com.gtnewhorizons.wdmla.impl.WDMlaClientRegistration;
import com.gtnewhorizons.wdmla.impl.ui.component.VPanelComponent;
import mcp.mobius.waila.overlay.DisplayUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.IComponent;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.config.General;
import com.gtnewhorizons.wdmla.config.PluginsConfig;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.impl.ui.component.HPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.ItemComponent;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Padding;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;
import com.gtnewhorizons.wdmla.plugin.harvestability.proxy.ProxyCreativeBlocks;

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
            tooltip.text("The legacy harvestability support")
                    .text("will come back in the next version!");
            return;
        }

        if (ProxyCreativeBlocks.isCreativeBlock(accessor.getBlock(), accessor.getMetadata())) {
            return;
        }

        List<HarvestHandler> handlers = WDMlaClientRegistration.instance().getHarvestHandlers(
                accessor.getBlock(),
                interactionHandler -> true);

        Block effectiveBlock = handlers
                .stream().map(
                        handler -> handler.getEffectiveBlock(
                                accessor.getBlock(), accessor.getItemForm(), accessor.getMetadata()))
                .filter(Objects::nonNull).findFirst().orElse(accessor.getBlock());
        int effectiveMeta = handlers
                .stream().map(
                        handler -> handler.getEffectiveMeta(
                                accessor.getBlock(), accessor.getItemForm(), accessor.getMetadata()))
                .filter(Objects::nonNull).findFirst().orElse(accessor.getMetadata());

        if(PluginsConfig.harvestability.oresOnly && !BlockHelper.isBlockAnOre(effectiveBlock, effectiveMeta)) {
            return;
        }

        HarvestabilityInfo info = getHarvestability(
                accessor.getPlayer(),
                effectiveBlock,
                effectiveMeta,
                accessor.getHitResult(),
                handlers);

        if((PluginsConfig.harvestability.unHarvestableOnly && info.canHarvest)
                || (PluginsConfig.harvestability.toolRequiredOnly && info.effectiveTool == null)) {
            return;
        }

        IComponent itemNameRow = tooltip.getChildWithTag(Identifiers.ITEM_NAME_ROW);
        if (!(itemNameRow instanceof ITooltip)) {
            return;
        }

        IComponent harvestabilityIcon = assembleHarvestabilityIcon(info);

        IComponent harvestabilityText = null;
        if(!PluginsConfig.harvestability.textDetailsOnly || accessor.showDetails()) {
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

        HarvestabilityInfo info = new HarvestabilityInfo();
        if (!fireHarvestTest(HarvestabilityTestPhase.EFFECTIVE_TOOL_NAME,
                player, block, meta, position, handlers, info)) {
            return info;
        }

        if (!fireHarvestTest(HarvestabilityTestPhase.HARVEST_LEVEL,
                player, block, meta, position, handlers, info)) {
            return info;
        }

        if (!fireHarvestTest(HarvestabilityTestPhase.HARVEST_LEVEL_NAME,
                player, block, meta, position, handlers, info)) {
            return info;
        }

        if (!fireHarvestTest(HarvestabilityTestPhase.EFFECTIVE_TOOL_ICON,
                player, block, meta, position, handlers, info)) {
            return info;
        }

        if (info.effectiveTool != null && info.effectiveToolIcon == null) {
            info.effectiveToolIcon = new ItemStack(Blocks.iron_bars);
        }

        if (!fireHarvestTest(HarvestabilityTestPhase.ADDITIONAL_TOOLS_ICON,
                player, block, meta, position, handlers, info)) {
            return info;
        }

        if (!fireHarvestTest(HarvestabilityTestPhase.CURRENTLY_HARVESTABLE,
                player, block, meta, position, handlers, info)) {
            return info;
        }

        fireHarvestTest(HarvestabilityTestPhase.IS_HELD_TOOL_EFFECTIVE,
                player, block, meta, position, handlers, info);

        return info;
    }

    private static boolean fireHarvestTest(HarvestabilityTestPhase phase, EntityPlayer player, Block block, int meta,
                                           MovingObjectPosition position, List<HarvestHandler> handlers, HarvestabilityInfo info) {
        for (HarvestHandler handler : handlers) {
            if(info.stopFurtherTesting) {
                break;
            }
            handler.testHarvest(info, phase, player, block, meta, position);
        }

        return !info.stopFurtherTesting;
    }

    private static @NotNull ITooltip assembleHarvestabilityIcon(HarvestabilityInfo info) {
        ITooltip harvestabilityComponent = new HPanelComponent().tag(HarvestabilityIdentifiers.HARVESTABILITY_ICON);

        if (!PluginsConfig.harvestability.currentlyHarvestableIcon) {
            return harvestabilityComponent;
        }

        // TODO: resize CHECK text
        IComponent currentlyHarvestableIcon;
        if(info.canHarvest) {
            currentlyHarvestableIcon = !PluginsConfig.harvestability.colorIconWithEffectiveness || info.isHeldToolEffective
                    || info.effectiveTool == null
                    ? ThemeHelper.INSTANCE.success(PluginsConfig.harvestability.currentlyHarvestableString)
                    : ThemeHelper.INSTANCE.info(PluginsConfig.harvestability.currentlyHarvestableString);
        }
        else {
            currentlyHarvestableIcon = ThemeHelper.INSTANCE.failure(PluginsConfig.harvestability.notCurrentlyHarvestableString);
        }

        if (info.effectiveToolIcon != null) {
            ITooltip effectiveToolIconComponent = new ItemComponent(info.effectiveToolIcon).doDrawOverlay(false)
                    .size(new Size(10, 10));

            if (PluginsConfig.harvestability.effectiveToolIcon) {
                effectiveToolIconComponent.child(
                        new HPanelComponent().padding(new Padding().left(5).top(6)).child(currentlyHarvestableIcon));
                harvestabilityComponent.child(effectiveToolIconComponent);
            } else {
                harvestabilityComponent.child(currentlyHarvestableIcon);
            }
        }
        else {
            harvestabilityComponent.child(currentlyHarvestableIcon);
        }

        for (Map.Entry<ItemStack, Boolean> additionalTool : info.additionalToolsIcon) {
            if (additionalTool.getValue()) { //TODO: config to always show additional tools
                harvestabilityComponent.child(
                        new ItemComponent(additionalTool.getKey()).doDrawOverlay(false)
                                .size(new Size(10, 10)));
            }
        }
        return harvestabilityComponent;
    }

    private static @Nullable IComponent assembleHarvestabilityText(HarvestabilityInfo info) {
        ITooltip lines = new VPanelComponent();
        if (info.harvestLevel >= 1 &&
                (PluginsConfig.harvestability.harvestLevelNum || PluginsConfig.harvestability.harvestLevelName)) {
            String harvestLevelString = PluginsConfig.harvestability.harvestLevelName ?
                    DisplayUtil.stripSymbols(info.harvestLevelName)
                    : String.valueOf(info.harvestLevel);
            IComponent harvestLevelText = new HPanelComponent().tag(HarvestabilityIdentifiers.HARVESTABILITY_TEXT)
                    .text(String.format("%s: ", StatCollector.translateToLocal("hud.msg.wdmla.harvestlevel"))).child(
                            info.canHarvest ? ThemeHelper.INSTANCE.success(harvestLevelString)
                                    : ThemeHelper.INSTANCE.failure(harvestLevelString));
            lines.child(harvestLevelText);
        }

        if (PluginsConfig.harvestability.effectiveToolLine && info.effectiveTool != null) {
            String effectiveToolString;
            if (StatCollector.canTranslate("hud.msg.wdmla.toolclass." + info.effectiveTool)) {
                effectiveToolString = StatCollector.translateToLocal("hud.msg.wdmla.toolclass." + info.effectiveTool);
            }
            else {
                effectiveToolString = info.effectiveTool.substring(0, 1).toUpperCase() + info.effectiveTool.substring(1);
            }
            ITooltip effectiveToolPanel = new HPanelComponent();
            effectiveToolPanel.text(StatCollector.translateToLocal("hud.msg.wdmla.effectivetool") + ": ");
            if (info.isHeldToolEffective) {
                effectiveToolPanel.child(ThemeHelper.INSTANCE.success(effectiveToolString));
            } else {
                effectiveToolPanel.child(ThemeHelper.INSTANCE.failure(effectiveToolString));
            }
            lines.child(effectiveToolPanel);
        }

        if (PluginsConfig.harvestability.currentlyHarvestableLine) {
            ITooltip currentlyHarvestable = new HPanelComponent();
            if (info.canHarvest) {
                String icon = PluginsConfig.harvestability.currentlyHarvestableString;
                currentlyHarvestable.child(ThemeHelper.INSTANCE.success(icon));
            } else {
                String icon = PluginsConfig.harvestability.notCurrentlyHarvestableString;
                currentlyHarvestable.child(ThemeHelper.INSTANCE.failure(icon));
            }
            String suffix = StatCollector.translateToLocal("hud.msg.wdmla.currentlyharvestable");
            lines.child(currentlyHarvestable.text(suffix));
        }

        return lines.childrenSize() > 0 ? lines : null;
    }
}
