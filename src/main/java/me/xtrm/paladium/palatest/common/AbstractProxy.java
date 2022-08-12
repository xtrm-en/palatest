package me.xtrm.paladium.palatest.common;

import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import lombok.Getter;
import me.xtrm.paladium.palatest.PalaTest;
import me.xtrm.paladium.palatest.common.extended.PalaPlayerManager;
import me.xtrm.paladium.palatest.common.network.NetworkManager;
import me.xtrm.paladium.palatest.common.registry.impl.*;
import me.xtrm.paladium.palatest.common.ui.PaladiumGuiHandler;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.util.Arrays;

/**
 * @author xtrm
 */
@Getter
public class AbstractProxy {
    protected NetworkManager networkManager;

    protected File configDir;
    protected File configFile;

    public void onPreInitialization(FMLPreInitializationEvent event) {
        // Store configuration files.
        configDir = event.getModConfigurationDirectory();
        configFile = event.getSuggestedConfigurationFile();

        // Apprently java whines when you use lambda reference.
        //noinspection Convert2MethodRef
        Arrays.asList(
            BlockRegistry.INSTANCE,
            ItemRegistry.INSTANCE,
            EntityRegistry.INSTANCE
        ).forEach(it -> it.registerAll());
    }

    public void onInitialization(FMLInitializationEvent event) {
        this.networkManager = new NetworkManager();
        this.networkManager.registerAll();

        //noinspection Convert2MethodRef
        Arrays.asList(
            RecipeRegistry.INSTANCE,
            TileEntityRegistry.INSTANCE
        ).forEach(it -> it.registerAll());
    }

    public void onPostInitialization(FMLPostInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(
            PalaTest.INSTANCE,
            new PaladiumGuiHandler()
        );

        MinecraftForge.EVENT_BUS.register(PalaPlayerManager.INSTANCE);
    }

    public void onPreServerStart(FMLServerAboutToStartEvent event) {
    }

    public void onServerStart(FMLServerStartingEvent event) {
    }

    public void onServerStarted(FMLServerStartedEvent event) {
    }

    public void onServerStopping(FMLServerStoppingEvent event) {
    }

    public void onServerStopped(FMLServerStoppedEvent event) {
    }
}
