package com.gtnewhorizons.wdmla.plugin.core;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.IComponent;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.config.General;
import com.gtnewhorizons.wdmla.config.PluginsConfig;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.impl.ui.component.BlockComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.HPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.ItemComponent;
import com.gtnewhorizons.wdmla.util.FormatUtil;
import com.gtnewhorizons.wdmla.wailacompat.RayTracingCompat;

import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public enum DefaultBlockInfoProvider implements IBlockComponentProvider {

    INSTANCE;

private static boolean isNitorBlock(BlockAccessor accessor) {
    if (accessor == null) return false;
    net.minecraft.block.Block b = accessor.getBlock();
    if (b == null) return false;

    int meta = accessor.getMetadata();

    // Thaumcraft 4 Nitor is placed as a block variant (commonly BlockAiry / "blockAiry") with meta 1.
    // Detect by registry/unlocalized/class name + metadata to avoid false positives.
    Object regNameObj = net.minecraft.block.Block.blockRegistry.getNameForObject(b);
    String regName = regNameObj != null ? regNameObj.toString() : "";
    String unloc = b.getUnlocalizedName();
    String cls = b.getClass().getName();
    String s = (regName + " " + unloc + " " + cls).toLowerCase(java.util.Locale.ROOT);

    if (meta == 1 && (s.contains("blockairy") || s.contains("airy") || s.contains("nitor"))) {
        // Prefer exact airy variant match when available, but keep it permissive across TC addons.
        return true;
    }

    // Fallback: some packs expose a registry name containing 'nitor' directly.
    return s.contains("nitor");
}

private static net.minecraft.item.ItemStack getThaumcraftNitorStack() {
    try {
        Object obj = net.minecraft.item.Item.itemRegistry.getObject("Thaumcraft:ItemResource");
        if (!(obj instanceof net.minecraft.item.Item)) obj = net.minecraft.item.Item.itemRegistry.getObject("thaumcraft:ItemResource");
        if (!(obj instanceof net.minecraft.item.Item)) obj = net.minecraft.item.Item.itemRegistry.getObject("thaumcraft:itemResource");
        if (obj instanceof net.minecraft.item.Item) {
            return new net.minecraft.item.ItemStack((net.minecraft.item.Item) obj, 1, 1);
        }
    } catch (Throwable t) {
        // ignore
    }
    return null;
}
    @Override
    public ResourceLocation getUid() {
        return Identifiers.DEFAULT_BLOCK;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.DEFAULT_INFO;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        // step 1: check whether waila has custom Wailastack or not
        ItemStack overrideStack = RayTracingCompat.INSTANCE.getWailaStack(accessor.getHitResult());

        // step 2: construct an actual icon
        ITooltip row = tooltip.horizontal();
        ItemStack itemStack = overrideStack != null ? overrideStack : accessor.getItemForm();
        if (itemStack == null) {
            // Some blocks/items return null in strange modded cases. Fall back to "block -> item" mapping.
            try {
                net.minecraft.item.Item it = net.minecraft.item.Item.getItemFromBlock(accessor.getBlock());
                if (it != null) itemStack = new ItemStack(it, 1, accessor.getMetadata());
            } catch (Throwable t) {
                // ignore
            }
        }
        if (isNitorBlock(accessor)) {
            ItemStack nitor = getThaumcraftNitorStack();
            if (nitor != null) itemStack = nitor;
        }
        if (PluginsConfig.core.defaultBlock.showIcon) {
            final ItemStack safeStack = itemStack != null ? itemStack : new ItemStack(Blocks.air);

            if (forceItemIcon(accessor) || itemStack == null) {
                row.child(new ItemComponent(safeStack).doDrawOverlay(false).tag(Identifiers.ITEM_ICON));
            } else {
                PluginsConfig.Core.fancyRendererMode mode = PluginsConfig.core.defaultBlock.fancyRenderer;

                // ALL: always try fancy.
                // FALLBACK: only use fancy when the item icon isn't usable.
                boolean wantsFancy = mode == PluginsConfig.Core.fancyRendererMode.ALL
                        || (mode == PluginsConfig.Core.fancyRendererMode.FALLBACK
                                && (itemStack.getItem() == null));

                // If the in-world preview can't reasonably render (invisible blocks, pure ISBRH-only blocks, etc.),
                // always fall back to the item icon.
                boolean canFancy = wantsFancy && canUseFancyBlockPreview(accessor);

                if (canFancy) {
                    row.child(
                            new BlockComponent(
                                    accessor.getHitResult().blockX,
                                    accessor.getHitResult().blockY,
                                    accessor.getHitResult().blockZ).tag(Identifiers.ITEM_ICON));
                } else {
                    row.child(new ItemComponent(safeStack).doDrawOverlay(false).tag(Identifiers.ITEM_ICON));
                }
            }
        }

        ITooltip row_vertical = row.vertical();
        if (PluginsConfig.core.defaultBlock.showBlockName) {
            String itemName;
            if (accessor.getServerData().hasKey("CustomName")) {
                String rawName = accessor.getServerData().getString("CustomName");
                itemName = EnumChatFormatting.ITALIC + FormatUtil.formatNameByPixelCount(rawName);
            } else {
                itemName = DisplayUtil.itemDisplayNameShortFormatted(itemStack != null ? itemStack : new ItemStack(Blocks.air));
            }
            ITooltip title = row_vertical.horizontal();
            IComponent nameComponent = ThemeHelper.INSTANCE.title(itemName).tag(Identifiers.ITEM_NAME);
            title.child(nameComponent).child(new HPanelComponent() {

                @Override
                public void tick(float x, float y) {
                    if (General.alignIconRightTop) {
                        IComponent icon = row.getChildWithTag(Identifiers.ITEM_ICON);
                        IComponent name = title.getChildWithTag(Identifiers.ITEM_NAME);
                        // align right
                        x += Math.max(
                                tooltip.getWidth() - (icon != null ? icon.getWidth() : 0)
                                        - (name != null ? name.getWidth() : 0)
                                        - getWidth()
                                        - General.currentTheme.get().panelStyle.getSpacing() * 2,
                                0);
                    }
                    super.tick(x, y);
                }
            }.tag(Identifiers.TARGET_NAME_ROW));
        }
        String modName = ModIdentification.nameFromStack(itemStack != null ? itemStack : new ItemStack(Blocks.air));
        if (PluginsConfig.core.defaultBlock.showModName && General.modName.hudShow) {
            row_vertical.child(ThemeHelper.INSTANCE.modName(modName).tag(Identifiers.MOD_NAME));
        }
    }

    @Override
    public boolean isPriorityFixed() {
        return true;
    }

    /**
     * Some mod blocks are not safe/reliable to render via the in-world block preview (custom render types,
     * invisible blocks, etc.). In those cases we fall back to the item icon even when fancy rendering is enabled.
     */
    private static boolean canUseFancyBlockPreview(BlockAccessor accessor) {
        if (accessor == null) return false;
        Block b = accessor.getBlock();
        if (b == null || b == Blocks.air) return false;

        // Absolutely invisible / special portal surfaces render poorly (or not at all) in the HUD preview.
        if (b == Blocks.portal || b == Blocks.end_portal) return false;

        int rt;
        try {
            rt = b.getRenderType();
        } catch (Throwable t) {
            return false;
        }

        // Invisible / no-render blocks.
        if (rt < 0) return false;

        // TESR blocks are fine; the preview renderer handles TESR separately.
        try {
            if (accessor.getTileEntity() != null
                    && net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.instance
                            .hasSpecialRenderer(accessor.getTileEntity())) {
                return true;
            }
        } catch (Throwable t) {
        }
        return true;
    }
private static boolean forceItemIcon(BlockAccessor accessor) {
    Block b = accessor.getBlock();
    if (b == null) return false;

    // Thaumcraft Nitor: always show as item icon (Thaumcraft:ItemResource:1).
        if (isNitorBlock(accessor)) return true;

    // Always show ladders as the item icon (vanilla + modded ladders)
    if (b == Blocks.ladder || b instanceof net.minecraft.block.BlockLadder) return true;
    String unloc = b.getUnlocalizedName();
    if (unloc != null && unloc.toLowerCase(java.util.Locale.ROOT).contains("ladder")) return true;

    return false;
}


}
