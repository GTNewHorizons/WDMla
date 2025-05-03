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
            @Config.DefaultEnum("ALL")
            @Config.Comment("None: Always render fake itemStack, ALL: Always render 3D block, \n " +
                    "FALLBACK: Render 3D block if the block has no Item variant")
            public Core.fancyRendererMode fancyRenderer;

            @Config.LangKey("option.wdmla.core.renderer.rotation.speed")
            @Config.DefaultInt(1)
            @Config.RangeInt(min = 1, max = 10000)
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
            @Config.RangeInt(min = 1, max = 10000)
            public int rendererRotationSpeed;

            @Config.LangKey("option.wdmla.core.entity.icon.auto.scale")
            @Config.DefaultBoolean(true)
            public boolean iconAutoScale;

            @Config.LangKey("option.wdmla.core.entity.icon.default.scale")
            @Config.DefaultFloat(1.2f)
            @Config.RangeFloat(min = 0.1f, max = 100f)
            public float iconDefaultScale;
        }

        public static enum fancyRendererMode {
            NONE,
            FALLBACK,
            ALL
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
            @Config.RangeInt(min = 0, max = 10000)
            public int normalAmount;

            @Config.LangKey("option.wdmla.universal.detailed.amount")
            @Config.DefaultInt(54)
            @Config.RangeInt(min = 0, max = 10000)
            public int detailedAmount;

            @Config.LangKey("option.wdmla.universal.items.per.line")
            @Config.DefaultInt(9)
            @Config.RangeInt(min = 0, max = 10000)
            public int itemsPerLine;

            @Config.LangKey("option.wdmla.universal.show.name.amount")
            @Config.DefaultInt(4)
            @Config.RangeInt(min = 0, max = 10000)
            public int showNameAmount;
        }

        @Config.LangKey("provider.wdmla.universal.fluid.storage")
        public static class FluidStorage {

            @Config.LangKey("option.wdmla.universal.fluid.mode")
            @Config.DefaultEnum("GAUGE")
            public Mode mode;

            @Config.LangKey("option.wdmla.universal.fluid.normal.amount")
            @Config.DefaultInt(4)
            @Config.RangeInt(min = 0, max = 10000)
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
            @Config.RangeInt(min = 0, max = 10000)
            public int maxCommandLength;
        }
    }

    @Config.LangKey("provider.wdmla.harvestability.category")
    public static class Harvestability {

        public final TinkersConstruct tinkersConstruct = new TinkersConstruct();

        public final IguanaTweaks iguanaTweaks = new IguanaTweaks();

        public final Icon icon = new Icon();

        public final Text text = new Text();

        public final Condition condition = new Condition();

        @Config.Comment("IDs of the TiC effective pickaxe material corresponding to the harvest level.")
        @Config.LangKey("provider.wdmla.harvestability.tinkersconstruct")
        public static class TinkersConstruct {
            @Config.DefaultInt(0)
            @Config.Comment("default: wood")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel0;

            @Config.DefaultInt(1)
            @Config.Comment("default: stone")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel1;

            @Config.DefaultInt(2)
            @Config.Comment("default: iron")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel2;

            @Config.DefaultInt(6)
            @Config.Comment("default: obsidian")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel3;

            @Config.DefaultInt(10)
            @Config.Comment("default: cobalt")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel4;

            @Config.DefaultInt(12)
            @Config.Comment("default: manyullyn")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel5;
        }

        @Config.Comment("IDs of the IguanaTweaks effective pickaxe material corresponding to the harvest level.\n"
                + "It will override TiC config if the mod is loaded")
        @Config.LangKey("provider.wdmla.harvestability.iguanatweaks")
        public static final class IguanaTweaks {
            @Config.DefaultInt(0)
            @Config.Comment("default: wood")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel0;

            @Config.DefaultInt(13)
            @Config.Comment("default: copper")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel1;

            @Config.DefaultInt(2)
            @Config.Comment("default: iron")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel2;

            @Config.DefaultInt(14)
            @Config.Comment("default: tin")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel3;

            @Config.DefaultInt(16)
            @Config.Comment("default: redstone")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel4;

            @Config.DefaultInt(6)
            @Config.Comment("default: obsidian")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel5;

            @Config.DefaultInt(11)
            @Config.Comment("default: ardite")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel6;

            @Config.DefaultInt(10)
            @Config.Comment("default: cobalt")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel7;

            @Config.DefaultInt(12)
            @Config.Comment("default: manyullyn")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel8;

            @Config.DefaultInt(12)
            @Config.Comment("default: manyullynplus")
            @Config.RangeInt(min = 1, max = 100)
            public int harvestLevel9;
        }

        @Config.LangKey("provider.wdmla.harvestability.icon")
        public static final class Icon {
            @Config.LangKey("option.wdmla.harvestability.currentlyHarvestable.icon")
            @Config.DefaultBoolean(true)
            @Config.Comment("Shows the line of Currently Harvestable icons")
            public boolean currentlyHarvestableIcon;

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

            @Config.LangKey("option.wdmla.harvestability.effectivetool.icon")
            @Config.DefaultBoolean(true)
            @Config.Comment("Shows the Effective Tool icon along with Currently Harvestable icon")
            public boolean effectiveToolIcon;

            @Config.LangKey("option.wdmla.harvestability.shearability.icon")
            @Config.DefaultBoolean(true)
            @Config.Comment("Shows the Shearability icon when holding the respective tool")
            public boolean showShearabilityIcon;

            @Config.LangKey("option.wdmla.harvestability.silktouchability.icon")
            @Config.DefaultBoolean(true)
            @Config.Comment("Shows the Silktouchabiity icon when holding the respective tool")
            public boolean showSilkTouchabilityIcon;

            @Config.LangKey("option.wdmla.harvestability.coloriconwitheffectiveness")
            @Config.DefaultBoolean(false)
            @Config.Comment("Colors the Currently Harvestable icon with held tool effectiveness")
            public boolean colorIconWithEffectiveness;

            @Config.LangKey("option.wdmla.harvestability.always.show.additional.tools")
            @Config.DefaultBoolean(false)
            @Config.Comment("Shows additional tools like Silktouchabiity even if you aren't holding the respective tool")
            public boolean alwaysShowAdditionalTools;
        }

        @Config.LangKey("provider.wdmla.harvestability.text")
        public static final class Text {
            @Config.LangKey("option.wdmla.harvestability.harvestlevelnum")
            @Config.DefaultBoolean(false)
            @Config.Comment("Shows the Harvest Level number as text")
            public boolean harvestLevelNum;

            @Config.LangKey("option.wdmla.harvestability.harvestlevelname")
            @Config.DefaultBoolean(false)
            @Config.Comment("Shows the Harvest Level name as text, if it is different than number")
            public boolean harvestLevelName;

            @Config.LangKey("option.wdmla.harvestability.effectivetool.line")
            @Config.DefaultBoolean(false)
            @Config.Comment("Shows the Effective Tool line")
            public boolean effectiveToolLine;

            @Config.LangKey("option.wdmla.harvestability.currentlyharvestable.line")
            @Config.DefaultBoolean(false)
            @Config.Comment("Shows the Currently Harvestable line")
            public boolean currentlyHarvestableLine;
        }

        @Config.LangKey("provider.wdmla.harvestability.condition")
        public static final class Condition {
            @Config.LangKey("option.wdmla.harvestability.oresonly")
            @Config.DefaultBoolean(false)
            @Config.Comment("Only shows tooltip when the block is ore")
            public boolean oresOnly;

            @Config.LangKey("option.wdmla.harvestability.textdetailsonly")
            @Config.DefaultBoolean(false)
            @Config.Comment("Only shows the text part of the tooltip when pressing details key")
            public boolean textDetailsOnly;

            @Config.LangKey("option.wdmla.harvestability.unharvestableonly")
            @Config.DefaultBoolean(false)
            @Config.Comment("Only shows tooltip when the block cannot be harvested")
            public boolean unHarvestableOnly;

            @Config.LangKey("option.wdmla.harvestability.toolrequiredonly")
            @Config.DefaultBoolean(false)
            @Config.Comment("Only shows tooltip when a tool is required to harvest")
            public boolean toolRequiredOnly;
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
