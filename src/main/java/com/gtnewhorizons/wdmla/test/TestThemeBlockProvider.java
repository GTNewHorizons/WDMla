package com.gtnewhorizons.wdmla.test;

import java.util.Arrays;

import com.gtnewhorizons.wdmla.api.ui.ColorPalette;
import com.gtnewhorizons.wdmla.impl.ui.component.AmountComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.VPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.style.AmountStyle;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.impl.ui.component.TextComponent;

import static com.gtnewhorizons.wdmla.impl.ui.component.TooltipComponent.DEFAULT_AMOUNT_TEXT_PADDING;

public enum TestThemeBlockProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public ResourceLocation getUid() {
        return Identifiers.TEST_THEME_BLOCK;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        tooltip.text("normal");
        tooltip.child(ThemeHelper.INSTANCE.info("This is info"));
        tooltip.child(ThemeHelper.INSTANCE.title("This is title"));
        tooltip.child(ThemeHelper.INSTANCE.success("This is success"));
        tooltip.child(ThemeHelper.INSTANCE.warning("This is warning"));
        tooltip.child(ThemeHelper.INSTANCE.danger("This is danger"));
        tooltip.child(ThemeHelper.INSTANCE.failure("This is failure"));
        tooltip.child(new AmountComponent(6,10)
                .style(new AmountStyle().filledColor(ColorPalette.PROGRESS_FILLED).alternateFilledColor(ColorPalette.PROGRESS_FILLED_ALTERNATE))
                .child(new VPanelComponent().padding(DEFAULT_AMOUNT_TEXT_PADDING).child(new TextComponent("Test Progress: 6 / 10"))));
        tooltip.child(new AmountComponent(8,10)
                .style(new AmountStyle().filledColor(ColorPalette.ENERGY_FILLED).alternateFilledColor(ColorPalette.ENERGY_FILLED_ALTERNATE))
                .child(new VPanelComponent().padding(DEFAULT_AMOUNT_TEXT_PADDING).child(new TextComponent("Test Energy: 8μI / 10μI"))));

        tooltip.child(ThemeHelper.INSTANCE.value("The answer", "42"));
        tooltip.child(
                ThemeHelper.INSTANCE.furnaceLikeProgress(
                        Arrays.asList(new ItemStack(Items.egg)),
                        Arrays.asList(new ItemStack(Items.chicken)),
                        1,
                        8,
                        accessor.showDetails(),
                        new TextComponent("1 / 8")));
    }
}
