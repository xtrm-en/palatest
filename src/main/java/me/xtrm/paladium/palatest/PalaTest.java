package me.xtrm.paladium.palatest;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import lombok.Getter;
import me.xtrm.paladium.palatest.client.ClientProxy;
import me.xtrm.paladium.palatest.common.AbstractProxy;
import me.xtrm.paladium.palatest.server.ServerProxy;
import org.apache.logging.log4j.Logger;

/**
 * The Mod's main class, only serves as a bouncer to the
 * sided {@link AbstractProxy} implementation.
 *
 * @author xtrm
 * @see AbstractProxy
 * @see ClientProxy
 * @see ServerProxy
 */
@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION)
public class PalaTest {
    @Instance(Constants.MODID)
    public static PalaTest INSTANCE;

    @SidedProxy(
        clientSide = "me.xtrm.paladium.palatest.client.ClientProxy",
        serverSide = "me.xtrm.paladium.palatest.server.ServerProxy"
    )
    private static AbstractProxy sidedProxy;

    @Getter
    private Logger logger;

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        sidedProxy.onPreInitialization(event);
    }

    @EventHandler
    public void onInit(FMLInitializationEvent event) {
        sidedProxy.onInitialization(event);
    }

    @EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        sidedProxy.onPostInitialization(event);
    }

    @EventHandler
    public void onPreServerStart(FMLServerAboutToStartEvent event) {
        sidedProxy.onPreServerStart(event);
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        sidedProxy.onServerStart(event);
    }

    @EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        sidedProxy.onServerStarted(event);
    }

    @EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        sidedProxy.onServerStopping(event);
    }

    @EventHandler
    public void onServerStopped(FMLServerStoppedEvent event) {
        sidedProxy.onServerStopped(event);
    }

    public AbstractProxy getSidedProxy() {
        return PalaTest.sidedProxy;
    }
}
