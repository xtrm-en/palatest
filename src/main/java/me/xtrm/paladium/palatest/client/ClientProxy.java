package me.xtrm.paladium.palatest.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import me.xtrm.paladium.palatest.client.ui.notification.NotificationManager;
import me.xtrm.paladium.palatest.common.AbstractProxy;
import me.xtrm.paladium.palatest.client.ui.font.FontManager;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends AbstractProxy {
    @Override
    public void onPostInitialization(FMLPostInitializationEvent event) {
        super.onPostInitialization(event);
        FMLCommonHandler.instance().bus().register(NotificationManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(NotificationManager.INSTANCE);

        FontManager.INSTANCE.initialize();
    }
}
