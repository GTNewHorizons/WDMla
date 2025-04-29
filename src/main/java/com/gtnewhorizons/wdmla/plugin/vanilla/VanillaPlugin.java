package com.gtnewhorizons.wdmla.plugin.vanilla;

import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockNote;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

import com.gtnewhorizons.wdmla.api.IWDMlaClientRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaCommonRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaPlugin;
import com.gtnewhorizons.wdmla.api.Identifiers;
import com.gtnewhorizons.wdmla.api.TooltipPosition;
import com.gtnewhorizons.wdmla.api.WDMlaPlugin;
import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.accessor.EntityAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.provider.IEntityComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.config.WDMlaConfig;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.plugin.universal.ItemStorageProvider;

/**
 * Registers providers depend on specific vanilla Minecraft mechanics with no mod interaction.
 */
@SuppressWarnings("unused")
@WDMlaPlugin(uid = VanillaIdentifiers.NAMESPACE_MINECRAFT)
public class VanillaPlugin implements IWDMlaPlugin {

    @Override
    public void registerClient(IWDMlaClientRegistration registration) {
        registration.registerBlockComponent(SilverFishBlockHeaderProvider.INSTANCE, BlockSilverfish.class);
        registration.registerBlockComponent(RedstoneWireHeaderProvider.INSTANCE, BlockRedstoneWire.class);
        registration.registerBlockComponent(RedstoneWireProvider.INSTANCE, BlockRedstoneWire.class);
        registration.registerBlockComponent(GrowableHeaderProvider.INSTANCE, BlockCrops.class);
        registration.registerBlockComponent(GrowableHeaderProvider.INSTANCE, BlockStem.class);
        registration.registerBlockComponent(GrowthRateProvider.INSTANCE, BlockCrops.class);
        registration.registerBlockComponent(GrowthRateProvider.INSTANCE, BlockStem.class);
        registration.registerBlockComponent(GrowthRateProvider.INSTANCE, BlockCocoa.class);
        registration.registerBlockComponent(GrowthRateProvider.INSTANCE, BlockNetherWart.class);
        registration.registerBlockComponent(RedstoneOreHeaderProvider.INSTANCE, BlockRedstoneOre.class);
        registration.registerBlockComponent(DoublePlantHeaderProvider.INSTANCE, BlockDoublePlant.class);
        registration.registerBlockComponent(DroppedItemHeaderProvider.INSTANCE, BlockAnvil.class);
        registration.registerBlockComponent(DroppedItemHeaderProvider.INSTANCE, BlockSapling.class);
        registration.registerBlockComponent(DroppedItemHeaderProvider.INSTANCE, BlockStoneSlab.class);
        registration.registerBlockComponent(DroppedItemHeaderProvider.INSTANCE, BlockWoodSlab.class);
        registration.registerBlockComponent(CustomMetaDataHeaderProvider.INSTANCE, BlockLeaves.class);
        registration.registerBlockComponent(CustomMetaDataHeaderProvider.INSTANCE, BlockLog.class);
        registration.registerBlockComponent(CustomMetaDataHeaderProvider.INSTANCE, BlockQuartz.class);
        registration.registerBlockComponent(RedstoneStateProvider.INSTANCE, BlockLever.class);
        registration.registerBlockComponent(RedstoneStateProvider.INSTANCE, BlockRedstoneRepeater.class);
        registration.registerBlockComponent(RedstoneStateProvider.INSTANCE, BlockRedstoneComparator.class);
        registration.registerBlockComponent(MobSpawnerHeaderProvider.INSTANCE, BlockMobSpawner.class);
        registration.registerBlockComponent(FurnaceProvider.INSTANCE, BlockFurnace.class);
        registration.registerBlockComponent(PlayerHeadHeaderProvider.INSTANCE, BlockSkull.class);
        registration.registerBlockComponent(BeaconProvider.INSTANCE, BlockBeacon.class);
        registration.registerBlockComponent(BedProvider.INSTANCE, BlockBed.class);
        registration.registerBlockComponent(CommandBlockProvider.INSTANCE, BlockCommandBlock.class);
        registration.registerBlockComponent(FlowerPotHeaderProvider.INSTANCE, BlockFlowerPot.class);
        registration.registerBlockComponent(JukeboxProvider.INSTANCE, BlockJukebox.class);
        registration.registerBlockComponent(MobSpawnerProvider.INSTANCE, BlockMobSpawner.class);
        registration.registerBlockComponent(NoteBlockProvider.INSTANCE, BlockNote.class);
        registration.registerBlockComponent(TotalEnchantmentPowerProvider.INSTANCE, BlockEnchantmentTable.class);
        registration.registerBlockComponent(RedstoneWireProvider.INSTANCE, BlockDaylightDetector.class);
        registration.registerBlockComponent(CauldronProvider.INSTANCE, BlockCauldron.class);

        registration.registerEntityComponent(PetProvider.INSTANCE, EntityTameable.class);
        registration.registerEntityComponent(AnimalProvider.INSTANCE, EntityAnimal.class);
        registration.registerEntityComponent(HorseProvider.INSTANCE, EntityHorse.class);
        registration.registerEntityComponent(PrimedTNTProvider.INSTANCE, EntityTNTPrimed.class);
        registration.registerEntityComponent(VillagerProfessionProvider.INSTANCE, EntityVillager.class);
        registration.registerEntityComponent(FallingBlockHeaderProvider.INSTANCE, EntityFallingBlock.class);
        registration.registerEntityComponent(ChickenProvider.INSTANCE, EntityChicken.class);
        registration.registerEntityComponent(PaintingProvider.INSTANCE, EntityPainting.class);
        registration.registerEntityComponent(ZombieVillagerProvider.INSTANCE, EntityZombie.class);
        registration.registerEntityComponent(ZombieVillagerHeaderProvider.INSTANCE, EntityZombie.class);
        registration.registerEntityComponent(MinecartCommandBlockProvider.INSTANCE, EntityMinecartCommandBlock.class);
        registration.registerEntityComponent(MinecartFurnaceProvider.INSTANCE, EntityMinecartFurnace.class);

        registration.registerItemStorageClient(ItemFrameProvider.INSTANCE);

        WDMlaConfig.instance().getCategory(
                Identifiers.CONFIG_AUTOGEN + Configuration.CATEGORY_SPLITTER + VanillaIdentifiers.NAMESPACE_MINECRAFT)
                .setLanguageKey("provider.wdmla.minecraft.category");
    }

    @Override
    public void register(IWDMlaCommonRegistration registration) {
        registration.registerBlockDataProvider(FurnaceProvider.INSTANCE, BlockFurnace.class);
        registration.registerBlockDataProvider(BeaconProvider.INSTANCE, BlockBeacon.class);
        registration.registerBlockDataProvider(CommandBlockProvider.INSTANCE, BlockCommandBlock.class);
        registration.registerBlockDataProvider(JukeboxProvider.INSTANCE, BlockJukebox.class);
        registration.registerBlockDataProvider(MobSpawnerProvider.INSTANCE, BlockMobSpawner.class);
        registration.registerBlockDataProvider(NoteBlockProvider.INSTANCE, BlockNote.class);

        registration.registerBlockDataProvider(TECustomNameHeaderProvider.INSTANCE, BlockChest.class);
        registration.registerBlockDataProvider(TECustomNameHeaderProvider.INSTANCE, BlockFurnace.class);
        registration.registerBlockDataProvider(TECustomNameHeaderProvider.INSTANCE, BlockHopper.class);
        registration.registerBlockDataProvider(TECustomNameHeaderProvider.INSTANCE, BlockBrewingStand.class);
        registration.registerBlockDataProvider(TECustomNameHeaderProvider.INSTANCE, BlockDispenser.class);
        registration.registerBlockDataProvider(TECustomNameHeaderProvider.INSTANCE, BlockDropper.class);
        registration.registerBlockDataProvider(TECustomNameHeaderProvider.INSTANCE, BlockEnchantmentTable.class);
        registration.registerBlockDataProvider(TECustomNameHeaderProvider.INSTANCE, BlockCommandBlock.class);

        registration.registerEntityDataProvider(PetProvider.INSTANCE, EntityTameable.class);
        registration.registerEntityDataProvider(PrimedTNTProvider.INSTANCE, EntityTNTPrimed.class);
        registration.registerEntityDataProvider(ChickenProvider.INSTANCE, EntityChicken.class);
        registration.registerEntityDataProvider(ZombieVillagerProvider.INSTANCE, EntityZombie.class);
        registration
                .registerEntityDataProvider(MinecartCommandBlockProvider.INSTANCE, EntityMinecartCommandBlock.class);
        registration.registerEntityDataProvider(MinecartFurnaceProvider.INSTANCE, EntityMinecartFurnace.class);

        registration.registerItemStorage(ItemStorageProvider.Extension.INSTANCE, BlockChest.class);
        registration.registerItemStorage(ItemStorageProvider.Extension.INSTANCE, BlockEnderChest.class);
        registration.registerItemStorage(ItemStorageProvider.Extension.INSTANCE, EntityMinecartChest.class);
        registration.registerItemStorage(ItemStorageProvider.Extension.INSTANCE, EntityMinecartHopper.class);
        registration.registerItemStorage(ItemStorageProvider.Extension.INSTANCE, BlockHopper.class);
        registration.registerItemStorage(ItemStorageProvider.Extension.INSTANCE, BlockBrewingStand.class);
        registration.registerItemStorage(ItemStorageProvider.Extension.INSTANCE, BlockDispenser.class);
        registration.registerItemStorage(ItemStorageProvider.Extension.INSTANCE, BlockDropper.class);
        registration.registerItemStorage(ItemStorageProvider.Extension.INSTANCE, EntityHorse.class);

        registration.registerItemStorage(ItemFrameProvider.INSTANCE, EntityItemFrame.class);
    }

    // misc providers section

    public enum RedstoneWireHeaderProvider implements IBlockComponentProvider {

        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
            ThemeHelper.INSTANCE.overrideTooltipIcon(tooltip, new ItemStack(Items.redstone), false);
        }

        @Override
        public ResourceLocation getUid() {
            return VanillaIdentifiers.REDSTONE_WIRE_HEADER;
        }

        @Override
        public int getDefaultPriority() {
            return TooltipPosition.HEAD;
        }
    }

    public enum RedstoneWireProvider implements IBlockComponentProvider {

        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
            tooltip.child(
                    ThemeHelper.INSTANCE.value(
                            StatCollector.translateToLocal("hud.msg.wdmla.power"),
                            String.format("%s", accessor.getMetadata())));
        }

        @Override
        public ResourceLocation getUid() {
            return VanillaIdentifiers.REDSTONE_WIRE;
        }
    }

    public enum RedstoneOreHeaderProvider implements IBlockComponentProvider {

        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
            // override lit redstone
            ItemStack redstoneOre = new ItemStack(Blocks.redstone_ore);
            ThemeHelper.INSTANCE.overrideTooltipHeader(tooltip, redstoneOre);
        }

        @Override
        public ResourceLocation getUid() {
            return VanillaIdentifiers.REDSTONE_ORE_HEADER;
        }

        @Override
        public int getDefaultPriority() {
            return TooltipPosition.HEAD;
        }
    }

    public enum ZombieVillagerHeaderProvider implements IEntityComponentProvider {

        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, EntityAccessor accessor) {
            if (accessor.getEntity() instanceof EntityZombie zombie && zombie.isVillager()) {
                ThemeHelper.INSTANCE.overrideEntityTooltipTitle(
                        tooltip,
                        StatCollector.translateToLocal("entity.zombievillager.name"),
                        accessor.getEntity());
            }
        }

        @Override
        public ResourceLocation getUid() {
            return VanillaIdentifiers.ZOMBIE_VILLAGER_HEADER;
        }

        @Override
        public int getDefaultPriority() {
            return TooltipPosition.HEAD;
        }
    }
}
