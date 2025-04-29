package com.gtnewhorizons.wdmla.config;

import com.gtnewhorizon.gtnhlib.config.Config;
import com.gtnewhorizons.wdmla.WDMla;
import com.gtnewhorizons.wdmla.plugin.debug.RegistryDataProvider;

/**
 * List of WDMla plugins config.<br>
 * All static configuration entry of any plugin inside WDMla should go here.<br>
 * Note: we can't split config files for each config due to GTNHLib's limitation. Sorry for inconvenience.
 */
@Config(modid = WDMla.MODID, category = "plugins", configSubDirectory = "WDMla", filename = "plugins")
@Config.LangKey("option.wdmla.plugin.category")
public class PluginsConfig {

    public static final Core core = new Core();

    public static final Universal universal = new Universal();

    public static final Vanilla vanilla = new Vanilla();

    public static final Harvestability harvestability = new Harvestability();

    public static final Debug debug = new Debug();

    @Config.LangKey("provider.wdmla.core.category")
    public static class Core {

        public final DefaultBlock defaultBlock = new DefaultBlock();
        public final DefaultEntity defaultEntity = new DefaultEntity();

        @Config.LangKey("provider.wdmla.core.default.block")
        public static class DefaultBlock {

            @Config.LangKey("option.wdmla.core.show.blockicon")
            @Config.DefaultBoolean(true)
            public boolean showIcon;

            @Config.LangKey("option.wdmla.core.show.blockname")
            @Config.DefaultBoolean(true)
            public boolean showBlockName;

            @Config.LangKey("option.wdmla.core.show.modname")
            @Config.DefaultBoolean(true)
            public boolean showModName;

            @Config.LangKey("option.wdmla.core.fancy.renderer")
            @Config.DefaultBoolean(true)
            public boolean fancyRenderer;

            @Config.LangKey("option.wdmla.core.renderer.rotation.speed")
            @Config.DefaultInt(1)
            public int rendererRotationSpeed;
        }

        @Config.LangKey("provider.wdmla.core.default.entity")
        public static class DefaultEntity {

            @Config.LangKey("option.wdmla.core.show.entity")
            @Config.DefaultBoolean(true)
            public boolean showEntity;

            @Config.LangKey("option.wdmla.core.show.entityname")
            @Config.DefaultBoolean(true)
            public boolean showEntityName;

            @Config.LangKey("option.wdmla.core.show.modname")
            @Config.DefaultBoolean(true)
            public boolean showModName;

            @Config.LangKey("option.wdmla.core.fancy.renderer")
            @Config.DefaultBoolean(true)
            public boolean fancyRenderer;

            @Config.LangKey("option.wdmla.core.renderer.rotation.speed")
            @Config.DefaultInt(1)
            public int rendererRotationSpeed;

            @Config.LangKey("option.wdmla.core.entity.icon.auto.scale")
            @Config.DefaultBoolean(true)
            public boolean iconAutoScale;

            @Config.LangKey("option.wdmla.core.entity.icon.default.scale")
            @Config.DefaultFloat(1.2f)
            public float iconDefaultScale;
        }
    }

    @Config.LangKey("provider.wdmla.universal.category")
    public static class Universal {

        public final ItemStorage itemStorage = new ItemStorage();
        public final FluidStorage fluidStorage = new FluidStorage();
        public final EnergyStorage energyStorage = new EnergyStorage();

        @Config.LangKey("provider.wdmla.universal.item.storage")
        public static class ItemStorage {

            @Config.LangKey("option.wdmla.universal.normal.amount")
            @Config.DefaultInt(9)
            public int normalAmount;

            @Config.LangKey("option.wdmla.universal.detailed.amount")
            @Config.DefaultInt(54)
            public int detailedAmount;

            @Config.LangKey("option.wdmla.universal.items.per.line")
            @Config.DefaultInt(9)
            public int itemsPerLine;

            @Config.LangKey("option.wdmla.universal.show.name.amount")
            @Config.DefaultInt(4)
            public int showNameAmount;
        }

        @Config.LangKey("provider.wdmla.universal.fluid.storage")
        public static class FluidStorage {

            @Config.LangKey("option.wdmla.universal.fluid.mode")
            @Config.DefaultEnum("GAUGE")
            public Mode mode;

            @Config.LangKey("option.wdmla.universal.fluid.normal.amount")
            @Config.DefaultInt(4)
            public int normalAmount;

            @Config.LangKey("option.wdmla.universal.fluid.detailed")
            @Config.DefaultBoolean(false)
            public boolean detailed;

            public enum Mode {
                GAUGE,
                ICON_TEXT,
                TEXT
            }
        }

        @Config.LangKey("provider.wdmla.universal.energy.storage")
        public static class EnergyStorage {
            // reserved
        }
    }

    @Config.LangKey("provider.wdmla.minecraft.category")
    public static class Vanilla {

        public final RedstoneState redstoneState = new RedstoneState();
        public final Pet pet = new Pet();
        public final Animal animal = new Animal();
        public final CommandBlock commandBlock = new CommandBlock();

        @Config.LangKey("provider.wdmla.minecraft.redstone.state")
        public static class RedstoneState {

            @Config.LangKey("option.wdmla.vanilla.leverstate")
            @Config.DefaultBoolean(true)
            public boolean showLeverState;

            @Config.LangKey("option.wdmla.vanilla.repeater")
            @Config.DefaultBoolean(true)
            public boolean showRepeaterDelay;

            @Config.LangKey("option.wdmla.vanilla.comparator")
            @Config.DefaultBoolean(true)
            public boolean showComparatorMode;
        }

        @Config.LangKey("provider.wdmla.minecraft.pet")
        public static class Pet {

            @Config.LangKey("option.wdmla.vanilla.show.petsitting")
            @Config.DefaultBoolean(true)
            public boolean showPetSit;

            @Config.LangKey("option.wdmla.vanilla.show.petowner")
            @Config.DefaultBoolean(true)
            public boolean showPetOwner;
        }

        @Config.LangKey("provider.wdmla.minecraft.animal")
        public static class Animal {

            @Config.LangKey("option.wdmla.vanilla.show.animalgrowth")
            @Config.DefaultBoolean(true)
            public boolean showAnimalGrowth;

            @Config.LangKey("option.wdmla.vanilla.show.breedcooldown")
            @Config.DefaultBoolean(true)
            public boolean showBreedCooldown;
        }

        @Config.LangKey("provider.wdmla.minecraft.command.block")
        public static class CommandBlock {

            @Config.LangKey("option.wdmla.vanilla.max.command.length")
            @Config.DefaultInt(40)
            public int maxCommandLength;
        }
    }

    @Config.LangKey("provider.wdmla.harvestability.category")
    public static class Harvestability {

        public final Legacy legacy = new Legacy();
        public final Modern modern = new Modern();

        @Config.LangKey("provider.wdmla.harvestability.legacy")
        public static class Legacy {

            @Config.LangKey("option.wdmla.harvestability.harvestlevel")
            @Config.DefaultBoolean(true)
            public boolean harvestLevel;

            @Config.LangKey("option.wdmla.harvestability.harvestlevelnum")
            @Config.DefaultBoolean(false)
            public boolean harvestLevelNum;

            @Config.LangKey("option.wdmla.harvestability.effectivetool")
            @Config.DefaultBoolean(true)
            public boolean effectiveTool;

            @Config.LangKey("option.wdmla.harvestability.currentlyharvestable")
            @Config.DefaultBoolean(true)
            public boolean currentlyHarvestable;

            @Config.LangKey("option.wdmla.harvestability.harvestlevel.sneakingonly")
            @Config.DefaultBoolean(false)
            public boolean harvestLevelSneakingOnly;

            @Config.LangKey("option.wdmla.harvestability.harvestlevelnum.sneakingonly")
            @Config.DefaultBoolean(false)
            public boolean harvestLevelNumSneakingOnly;

            @Config.LangKey("option.wdmla.harvestability.effectivetool.sneakingonly")
            @Config.DefaultBoolean(false)
            public boolean effectiveToolSneakingOnly;

            @Config.LangKey("option.wdmla.harvestability.currentlyharvestable.sneakingonly")
            @Config.DefaultBoolean(false)
            public boolean currentlyHarvestableSneakingOnly;

            @Config.LangKey("option.wdmla.harvestability.oresonly")
            @Config.DefaultBoolean(false)
            public boolean oresOnly;

            @Config.LangKey("option.wdmla.harvestability.minimal")
            @Config.DefaultBoolean(false)
            public boolean minimal;

            @Config.LangKey("option.wdmla.harvestability.unharvestableonly")
            @Config.DefaultBoolean(false)
            public boolean unHarvestableOnly;

            @Config.LangKey("option.wdmla.harvestability.toolrequiredonly")
            @Config.DefaultBoolean(true)
            public boolean toolRequiredOnly;

            @Config.LangKey("option.wdmla.harvestability.shearability")
            @Config.DefaultBoolean(true)
            public boolean shearability;

            @Config.LangKey("option.wdmla.harvestability.shearability.sneakingonly")
            @Config.DefaultBoolean(false)
            public boolean shearabilitySneakingOnly;

            @Config.LangKey("option.wdmla.harvestability.silktouchability")
            @Config.DefaultBoolean(true)
            public boolean silkTouchability;

            @Config.LangKey("option.wdmla.harvestability.silktouchability.sneakingonly")
            @Config.DefaultBoolean(false)
            public boolean silkTouchabilitySneakingOnly;

            @Config.LangKey("option.wdmla.harvestability.minimalseparator.string")
            @Config.DefaultString(" : ")
            public String minimalSeparatorString;

            @Config.LangKey("option.wdmla.harvestability.currentlyharvestable.string")
            @Config.DefaultString("\u2714")
            public String currentlyHarvestableString;

            @Config.LangKey("option.wdmla.harvestability.notcurrentlyharvestable.string")
            @Config.DefaultString("\u2718")
            public String notcurrentlyHarvestableString;

            @Config.LangKey("option.wdmla.harvestability.shearability.string")
            @Config.DefaultString("\u2702")
            public String shearabilityString;

            @Config.LangKey("option.wdmla.harvestability.silktouchability.string")
            @Config.DefaultString("\u2712")
            public String silkTouchabilityString;
        }

        @Config.LangKey("provider.wdmla.harvestability.modern")
        public static class Modern {

            public final TinkersConstruct tinkersConstruct = new TinkersConstruct();

            @Config.Comment("IDs of the TiC effective pickaxe material corresponding to the harvest level.\n"
                    + "Note that the default values are tuned for GTNH Iguana tweaks (TiC itself only has the harvest level up to 6)")
            public static class TinkersConstruct {

                @Config.DefaultInt(0)
                @Config.Comment("default: wood")
                public int harvestLevel0;

                @Config.DefaultInt(13)
                @Config.Comment("default: copper")
                public int harvestLevel1;

                @Config.DefaultInt(2)
                @Config.Comment("default: iron")
                public int harvestLevel2;

                @Config.DefaultInt(14)
                @Config.Comment("default: tin")
                public int harvestLevel3;

                @Config.DefaultInt(16)
                @Config.Comment("default: redstone")
                public int harvestLevel4;

                @Config.DefaultInt(6)
                @Config.Comment("default: obsidian")
                public int harvestLevel5;

                @Config.DefaultInt(11)
                @Config.Comment("default: ardite")
                public int harvestLevel6;

                @Config.DefaultInt(10)
                @Config.Comment("default: cobalt")
                public int harvestLevel7;

                @Config.DefaultInt(12)
                @Config.Comment("default: manyullyn")
                public int harvestLevel8;

                @Config.DefaultInt(12)
                @Config.Comment("default: manyullynplus")
                public int harvestLevel9;
            }

            @Config.LangKey("option.wdmla.harvestability.currentlyharvestable.string")
            @Config.DefaultString("✔")
            @Config.Comment("The string below the Harvest Tool icon after the item name")
            public String currentlyHarvestableString;

            @Config.LangKey("option.wdmla.harvestability.notcurrentlyharvestable.string")
            @Config.DefaultString("✕")
            @Config.Comment("The string below the Harvest Tool icon after the item name")
            public String notCurrentlyHarvestableString;

            @Config.LangKey("option.wdmla.harvestability.shearability.item")
            @Config.DefaultString("minecraft:shears")
            @Config.Comment("The icon after an item represents that the item is shearable")
            public String shearabilityItem;

            @Config.LangKey("option.wdmla.harvestability.silktouchability.item")
            @Config.DefaultString("minecraft:grass")
            @Config.Comment("The icon after an item represents that the item can be harvested by silk touch")
            public String silkTouchabilityItem;

            @Config.LangKey("option.wdmla.harvestability.harvestlevelnum")
            @Config.DefaultBoolean(false)
            @Config.Comment("Shows the Harvest Level number text without enabling legacy mode")
            public boolean modernHarvestLevelNum;

            @Config.LangKey("option.wdmla.harvestability.currentlyHarvestable.icon")
            @Config.DefaultBoolean(true)
            public boolean modernCurrentlyHarvestableIcon;

            @Config.LangKey("option.wdmla.harvestability.effectivetool.icon")
            @Config.DefaultBoolean(true)
            public boolean modernEffectiveToolIcon;

            @Config.LangKey("option.wdmla.harvestability.shearability.icon")
            @Config.DefaultBoolean(true)
            public boolean modernShowShearabilityIcon;

            @Config.LangKey("option.wdmla.harvestability.silktouchability.icon")
            @Config.DefaultBoolean(true)
            public boolean modernShowSilkTouchabilityIcon;
        }
    }

    @Config.LangKey("provider.wdmla.debug.category")
    public static class Debug {

        public final RegistryData registryData = new RegistryData();

        @Config.LangKey("provider.wdmla.debug.registry.data")
        public static class RegistryData {

            @Config.LangKey("option.wdmla.debug.entity.registry.data")
            @Config.DefaultBoolean(false)
            public boolean entityRegistryData;

            @Config.LangKey("option.wdmla.debug.registry.data.display.mode")
            @Config.DefaultEnum("SHORT")
            public RegistryDataProvider.DisplayMode displayMode;
        }
    }
}
