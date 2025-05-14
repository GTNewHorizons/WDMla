package com.gtnewhorizons.wdmla.plugin.architecturecraft;

import com.gtnewhorizons.wdmla.api.identifier.CommonPluginIDs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.IComponent;
import com.gtnewhorizons.wdmla.util.FormatUtil;

import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.BlockCompatUtils;
import gcewing.architecture.util.Utils;

public enum BlockShapeProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(IComponent tooltip, BlockAccessor accessor) {
        if (accessor.getTileEntity() instanceof TileShape shape) {
            if (shape.baseBlockState != null) {
                String hardcodedName = Utils.displayNameOfBlock(
                        shape.baseBlockState.getBlock(),
                        BlockCompatUtils.getMetaFromBlockState(shape.baseBlockState));
                String subStringedName = FormatUtil.formatNameByPixelCount(hardcodedName.substring(9));
                tooltip.text(StatCollector.translateToLocal("hud.msg.wdmla.cut.from") + " " + subStringedName);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return CommonPluginIDs.AC_BLOCK_SHAPE;
    }
}
