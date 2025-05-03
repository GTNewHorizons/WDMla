package com.gtnewhorizons.wdmla.plugin.harvestability;

import net.minecraft.util.ResourceLocation;

public class HarvestabilityIdentifiers {

    // component tag
    public static final ResourceLocation HARVESTABILITY_ICON = Harvestability("harvestability_icon");
    public static final ResourceLocation HARVESTABILITY_TEXT = Harvestability("harvestability_text");

    // provider Uid
    public static final ResourceLocation HARVESTABILITY = Harvestability("modern");
//    public static final ResourceLocation LEGACY_HARVESTABILITY = Harvestability("legacy");

    // handler uid
    public static final ResourceLocation BASE_LOGIC = Harvestability("base_logic");
    public static final ResourceLocation VANILLA = Harvestability("vanilla");
    public static final ResourceLocation LIQUID = Harvestability("liquid");
    public static final ResourceLocation GREGTECH = Harvestability("gregtech");
    public static final ResourceLocation TINKERSCONSTRUCT = Harvestability("tinkersconstruct");
    public static final ResourceLocation IGUANATWEAKS = Harvestability("iguanatweaks");

    public static final String NAMESPACE_HARVESTABILITY = "harvestability";

    public static ResourceLocation Harvestability(String path) {
        return new ResourceLocation(NAMESPACE_HARVESTABILITY, path);
    }
}
