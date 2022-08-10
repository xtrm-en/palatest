package me.xtrm.paladium.palatest;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.xtrm.paladium.palatest.client.ClientProxy;
import me.xtrm.paladium.palatest.common.AbstractProxy;
import me.xtrm.paladium.palatest.server.ServerProxy;

/**
 * The Mod's main class, only serves as a bouncer to the
 * sided {@link AbstractProxy} implementation.
 *
 * @see AbstractProxy
 * @see ClientProxy
 * @see ServerProxy
 *
 * @author xtrm
 */
@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION)
public class PalaTest {
    @Instance(Constants.MODID)
    public static PalaTest INSTANCE;

    @SidedProxy(
            clientSide = "me.xtrm.paladium.palatest.client.ClientProxy",
            serverSide = "me.xtrm.paladium.palatest.server.ServerProxy"
    )
    private static AbstractProxy proxy;

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        proxy.onPreInitialization(event);
    }

    @EventHandler
    public void onInit(FMLInitializationEvent event) {
        proxy.onInitialization(event);
    }

    @EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        proxy.onPostInitialization(event);
    }
}
