package com.gtnewhorizons.wdmla.plugin.vanilla;

import net.minecraft.util.ResourceLocation;

public class VanillaIdentifiers {

    // provider Uid & component tag
    public static final ResourceLocation SILVERFISH_HEADER = MC("silverfish_header");
    public static final ResourceLocation REDSTONE_WIRE_HEADER = MC("redstone_wire_header");
    public static final ResourceLocation REDSTONE_WIRE = MC("redstone_wire");
    public static final ResourceLocation GROWABLE_HEADER = MC("growable_header");
    public static final ResourceLocation GROWTH_RATE = MC("growth_rate");
    public static final ResourceLocation REDSTONE_ORE_HEADER = MC("redstone_ore");
    public static final ResourceLocation DOUBLE_PLANT_HEADER = MC("double_plant_header");
    public static final ResourceLocation DROPPED_ITEM_HEADER = MC("dropped_item_header");
    public static final ResourceLocation CUSTOM_META_HEADER = MC("custom_meta_header");
    public static final ResourceLocation REDSTONE_STATE = MC("redstone_state");
    public static final ResourceLocation MOB_SPAWNER_HEADER = MC("mob_spawner_header");
    public static final ResourceLocation FURNACE = MC("furnace");
    public static final ResourceLocation PLAYER_HEAD_HEADER = MC("player_head_header");
    public static final ResourceLocation BEACON = MC("beacon");
    public static final ResourceLocation BED = MC("bed");
    public static final ResourceLocation PET = MC("pet");
    public static final ResourceLocation ANIMAL = MC("animal");
    public static final ResourceLocation ANIMAL_GROWTH = MC("animal_growth");
    public static final ResourceLocation ANIMAL_BREED = MC("animal_breed");
    public static final ResourceLocation HORSE = MC("horse");
    public static final ResourceLocation PRIMED_TNT = MC("primed_tnt");
    public static final ResourceLocation ITEM_FRAME = MC("item_frame");
    public static final ResourceLocation VILLAGER_PROFESSION = MC("villager_profession");
    public static final ResourceLocation COMMAND_BLOCK = MC("command_block");
    public static final ResourceLocation FALLING_BLOCK_HEADER = MC("falling_block_header");
    public static final ResourceLocation FLOWER_POT_HEADER = MC("flower_pot_header");
    public static final ResourceLocation JUKEBOX = MC("jukebox");
    public static final ResourceLocation MOB_SPAWNER = MC("mob_spawner");
    public static final ResourceLocation CHICKEN = MC("chicken");
    public static final ResourceLocation NOTE_BLOCK = MC("note_block");
    public static final ResourceLocation PAINTING = MC("painting");
    public static final ResourceLocation TOTAL_ENCHANTMENT_POWER = MC("total_enchantment_power");
    public static final ResourceLocation ZOMBIE_VILLAGER = MC("zombie_villager");
    public static final ResourceLocation ZOMBIE_VILLAGER_HEADER = MC("zombie_villager_header");
    public static final ResourceLocation TE_CUSTOM_NAME_HEADER = MC("te_custom_name_header");
    public static final ResourceLocation CAULDRON = MC("cauldron");
    public static final ResourceLocation MINECART_COMMAND_BLOCK = MC("minecart_command_block");
    public static final ResourceLocation MINECART_FURNACE = MC("minecart_furnace");
    public static final ResourceLocation SNOW_LAYER = MC("snow_layer");
    public static final ResourceLocation ENDER_DRAGON_HEADER = MC("ender_dragon_header");
    public static final ResourceLocation END_PORTAL_HEADER = MC("end_portal_header");

    public static final String NAMESPACE_MINECRAFT = "minecraft";

    private static ResourceLocation MC(String path) {
        return new ResourceLocation(path);
    }
}
