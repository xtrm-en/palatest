package me.xtrm.paladium.palatest.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.xtrm.paladium.palatest.common.registry.BlockRegistry;

import java.io.File;

/**
 * @author xtrm
 * @since 1.0.0
 */
public class AbstractProxy {
    protected File configDir;
    protected File configFile;

    public void onPreInitialization(FMLPreInitializationEvent event) {
        configDir = event.getModConfigurationDirectory();
        configFile = event.getSuggestedConfigurationFile();

        BlockRegistry.INSTANCE.registerAll();
    }

    public void onInitialization(FMLInitializationEvent event) {
    }

    public void onPostInitialization(FMLPostInitializationEvent event) {

    }
}
