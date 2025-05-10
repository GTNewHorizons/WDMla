package com.gtnewhorizons.wdmla;

import com.gtnewhorizons.wdmla.api.Identifiers;
import net.minecraft.launchwrapper.Launch;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;
import com.gtnewhorizons.wdmla.api.config.General;
import com.gtnewhorizons.wdmla.api.config.PluginsConfig;
import com.gtnewhorizons.wdmla.example.TestMode;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import mcp.mobius.waila.Tags;

/**
 * The Main mod class.<br>
 * The mod name is supposed to be "WDMla" (the first three letter is upper case) since the full mod name is "What
 * DreamMaster looks at".
 */
@Mod(
        modid = Identifiers.MODID,
        name = "WDMla",
        version = Tags.GRADLETOKEN_VERSION,
        dependencies = " after:Waila;" + " required-after:gtnhlib;",
        acceptableRemoteVersions = "*",
        acceptedMinecraftVersions = "[1.7.10]",
        guiFactory = "com.gtnewhorizons.wdmla.gui.GuiFactory")
public class WDMla {

    static {
        try {
            ConfigurationManager.registerConfig(General.class);
            ConfigurationManager.registerConfig(PluginsConfig.class);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

    @SidedProxy(clientSide = "com.gtnewhorizons.wdmla.ClientProxy", serverSide = "com.gtnewhorizons.wdmla.CommonProxy")
    public static CommonProxy proxy;

    /**
     * Edit this on dev env to switch test mode
     */
    public static TestMode testMode = TestMode.WDMla;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    public static boolean isDevEnv() {
        return (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }
}
