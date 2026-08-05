package mcp.mobius.waila;

import java.lang.reflect.Field;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.gtnewhorizons.wdmla.config.General;
import com.gtnewhorizons.wdmla.plugin.PluginScanner;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.client.KeyEvent;
import mcp.mobius.waila.network.NetworkHandler;
import mcp.mobius.waila.network.WailaPacketHandler;
import mcp.mobius.waila.overlay.DecoratorRenderer;
import mcp.mobius.waila.server.ProxyServer;
import mcp.mobius.waila.utils.ModIdentification;

@Mod(modid = "Waila", name = "Waila", version = "1.9.5", acceptableRemoteVersions = "*")
public class Waila {

    // The instance of your mod that Forge uses.
    @Instance("Waila")
    public static Waila instance;

    @SidedProxy(clientSide = "mcp.mobius.waila.client.ProxyClient", serverSide = "mcp.mobius.waila.server.ProxyServer")
    public static ProxyServer proxy;
    public static Logger log = LogManager.getLogger("Waila");
    public boolean serverPresent = false;

    /* INIT SEQUENCE */
    // Don't call wdmla in this phase as it does not exist
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.instance().loadDefaultConfig(event);
        MinecraftForge.EVENT_BUS.register(new DecoratorRenderer());
        WailaPacketHandler.INSTANCE.ordinal();
    }

    @EventHandler
    public void initialize(FMLInitializationEvent event) {
        try {
            Field eBus = FMLModContainer.class.getDeclaredField("eventBus");
            eBus.setAccessible(true);
            EventBus FMLbus = (EventBus) eBus.get(FMLCommonHandler.instance().findContainerFor(this));
            FMLbus.register(this);
        } catch (Throwable ignored) {}

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new DecoratorRenderer());
            FMLCommonHandler.instance().bus().register(new KeyEvent());
        }
        FMLCommonHandler.instance().bus().register(new NetworkHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.registerHandlers();
        ModIdentification.init();
    }

    @Subscribe
    public void loadComplete(FMLLoadCompleteEvent event) {
        proxy.registerLegacyMods();
        proxy.registerIMCs();
    }

    @EventHandler
    public void processIMC(FMLInterModComms.IMCEvent event) {
        for (IMCMessage imcMessage : event.getMessages()) {
            if (!imcMessage.isStringMessage()) continue;

            if (imcMessage.key.equalsIgnoreCase("addconfig")) {
                String[] params = imcMessage.getStringValue().split("\\$\\$");
                if (params.length != 3) {
                    Waila.log.warn(
                            String.format(
                                    "Error while parsing config option from [ %s ] for %s",
                                    imcMessage.getSender(),
                                    imcMessage.getStringValue()));
                    continue;
                }
                Waila.log.info(
                        String.format(
                                "Receiving config request from [ %s ] for %s",
                                imcMessage.getSender(),
                                imcMessage.getStringValue()));
                ConfigHandler.instance().addConfig(params[0], params[1], params[2]);
            }

            if (imcMessage.key.equalsIgnoreCase("register")) {
                Waila.log.info(
                        String.format(
                                "Receiving registration request from [ %s ] for method %s",
                                imcMessage.getSender(),
                                imcMessage.getStringValue()));
                if (General.overrideWailaTooltips && isRegistrationMethodBlacklisted(imcMessage.getStringValue())) {
                    continue;
                }
                ModuleRegistrar.instance().addIMCRequest(imcMessage.getStringValue(), imcMessage.getSender());
            }
        }
    }

    private boolean isRegistrationMethodBlacklisted(String method) {
        for (String blackListedRegistrationMethod : PluginScanner.INSTANCE.blackListedRegistrationMethods) {
            if (blackListedRegistrationMethod.equals(method)) {
                return true;
            }
        }
        return false;
    }
}
