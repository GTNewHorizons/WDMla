package com.gtnewhorizons.wdmla.test;

import static com.gtnewhorizons.wdmla.impl.ui.component.TooltipComponent.DEFAULT_PROGRESS_DESCRIPTION_PADDING;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerDataProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.api.ui.sizer.IPadding;
import com.gtnewhorizons.wdmla.impl.ui.component.ProgressComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.TextComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.VPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Padding;
import com.gtnewhorizons.wdmla.impl.ui.sizer.Size;
import com.gtnewhorizons.wdmla.impl.ui.style.PanelStyle;

import mcp.mobius.waila.overlay.DisplayUtil;

public enum TestNBTBlockProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    @Override
    public ResourceLocation getUid() {
        return Identifiers.TEST_NBT_BLOCK;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.BODY + 10;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        int cookTime = accessor.getServerData().getShort("CookTime");
        cookTime = Math.round(cookTime / 20.0f);

        ItemStack[] items = new ItemStack[3];
        NBTTagList itemsTag = accessor.getServerData().getTagList("Items", 10);

        boolean allEmpty = true;
        for (int i = 0; i < itemsTag.tagCount(); i++) {
            NBTTagCompound itemTag = itemsTag.getCompoundTagAt(i);
            byte slot = itemTag.getByte("Slot");

            if (slot >= 0 && slot < items.length) {
                items[slot] = ItemStack.loadItemStackFromNBT(itemTag);
                if (items[slot] != null) {
                    allEmpty = false;
                }
            }
        }

        if (!accessor.showDetails()) {
            return;
        }

        int random = accessor.getServerData().getInteger("random");
        tooltip.child(new TextComponent("Recieved Server Data: " + random));

        if (cookTime != 0) {
            ITooltip amountTooltip = new ProgressComponent(cookTime, 10).child(
                    new VPanelComponent().padding(DEFAULT_PROGRESS_DESCRIPTION_PADDING)
                            .child(new TextComponent("Smelting: " + cookTime + " / 10 s")));
            tooltip.child(amountTooltip);
        }

        int burnTime = accessor.getServerData().getInteger("BurnTime") / 20;
        if (burnTime > 0) {
            tooltip.horizontal().text("Burn").item(new ItemStack(Blocks.fire), new Padding(), new Size(8, 8))
                    .text(": " + burnTime + " " + "Seconds Remaining");
        }

        if (!allEmpty) {
            IPadding itemPadding = new Padding().vertical(2);
            ITooltip itemSection = new VPanelComponent().style(new PanelStyle().borderColor(0xff00ffff));
            if (items[0] != null) {
                itemSection.horizontal().text("In: ", itemPadding).item(items[0], new Padding(), new Size(10, 10))
                        .text(" " + DisplayUtil.itemDisplayNameShort(items[0]), itemPadding);
            }

            if (items[2] != null) {
                itemSection.horizontal().text("Out: ", itemPadding).item(items[2], new Padding(), new Size(10, 10))
                        .text(" " + DisplayUtil.itemDisplayNameShort(items[2]), itemPadding);
            }

            if (items[1] != null) {
                itemSection.horizontal().text("Fuel: ", itemPadding).item(items[1], new Padding(), new Size(10, 10))
                        .text(" " + DisplayUtil.itemDisplayNameShort(items[1]), itemPadding);
            }
            tooltip.child(itemSection);
        }
    }

    @Override
    public void appendServerData(NBTTagCompound data, BlockAccessor accessor) {
        ItemStack[] dummyItemStacks = { new ItemStack(Items.potato), new ItemStack(Items.coal),
                new ItemStack(Items.baked_potato) };
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < 3; ++i) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte) i);
            dummyItemStacks[i].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
        }
        data.setTag("Items", nbttaglist);

        data.setShort("CookTime", (short) 100);
        data.setInteger("BurnTime", 5);
        data.setInteger("random", new Random().nextInt(11));
    }
}
