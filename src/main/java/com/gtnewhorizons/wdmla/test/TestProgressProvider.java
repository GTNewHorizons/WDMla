package com.gtnewhorizons.wdmla.test;

import java.util.Arrays;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.accessor.Accessor;
import com.gtnewhorizons.wdmla.api.provider.IClientExtensionProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerExtensionProvider;
import com.gtnewhorizons.wdmla.api.view.ClientViewGroup;
import com.gtnewhorizons.wdmla.api.view.ProgressView;
import com.gtnewhorizons.wdmla.api.view.ViewGroup;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.impl.ui.component.TextComponent;
import com.gtnewhorizons.wdmla.impl.ui.style.ProgressStyle;

public enum TestProgressProvider implements IServerExtensionProvider<ProgressView.Data>,
        IClientExtensionProvider<ProgressView.Data, ProgressView> {

    INSTANCE;

    @Override
    public ResourceLocation getUid() {
        return Identifiers.TEST_PROGRESS;
    }

    @Override
    public List<ClientViewGroup<ProgressView>> getClientGroups(Accessor accessor,
            List<ViewGroup<ProgressView.Data>> groups) {
        return ClientViewGroup.map(groups, ProgressView::read, (group, clientGroup) -> {
            ProgressView view = clientGroup.views.get(0);
            // view.style.filledColor(0xFFCC0000);
            view.description = ThemeHelper.INSTANCE.info("Testtttttttttttttttttttttttttttttttt");
            view.hasScale = true;

            view = clientGroup.views.get(1);
            view.style = new ProgressStyle().singleColor(0xFF00CC00);
            view.description = new TextComponent("Test");
        });
    }

    @Override
    public List<ViewGroup<ProgressView.Data>> getGroups(Accessor accessor) {
        World world = accessor.getWorld();
        int period = 40;
        ProgressView.Data progress1 = new ProgressView.Data((world.getTotalWorldTime() % period) + 1, period);
        period = 200;
        ProgressView.Data progress2 = new ProgressView.Data((world.getTotalWorldTime() % period) + 1, period);
        ViewGroup<ProgressView.Data> group = new ViewGroup<>(Arrays.asList(progress1, progress2));
        return Arrays.asList(group);
    }
}
