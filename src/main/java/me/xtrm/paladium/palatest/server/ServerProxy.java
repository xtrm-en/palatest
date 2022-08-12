package me.xtrm.paladium.palatest.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import lombok.Getter;
import me.xtrm.paladium.palatest.PalaTest;
import me.xtrm.paladium.palatest.common.AbstractProxy;
import me.xtrm.paladium.palatest.common.recipe.type.GrinderRecipe;
import me.xtrm.paladium.palatest.server.config.ServerConfiguration;
import me.xtrm.paladium.palatest.server.database.DatabaseConnector;
import me.xtrm.paladium.palatest.server.database.dao.impl.GrinderCraftService;
import me.xtrm.paladium.palatest.server.database.dao.model.GrinderCraftModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;
import org.sql2o.Connection;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

/**
 * The server's sided proxy, mainly handles Database connections.
 *
 * @see AbstractProxy
 *
 * @author xtrm
 */
@Getter
public class ServerProxy extends AbstractProxy {
    private static final Gson GSON = new GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create();

    private final DatabaseConnector databaseConnector;
    private ServerConfiguration configuration;

    public ServerProxy() {
        this.databaseConnector = new DatabaseConnector();
    }

    public void logCraft(EntityPlayer player, World worldObj, GrinderRecipe recipe) {
        if (!databaseConnector.isInitialized())
            return;
        try (Connection connection = databaseConnector.getSql2o().open()) {
            GrinderCraftService.INSTANCE.insert(
                new GrinderCraftModel(
                    player,
                    recipe.output,
                    Instant.now(),
                    worldObj
                )
            );
        } catch (Throwable throwable) {
            PalaTest.INSTANCE.getLogger().error(
                "Error while logging craft of {} for {} in {}.",
                recipe.output.getItem().toString(),
                player.getCommandSenderName(),
                worldObj.getWorldInfo().getWorldName(),
                throwable
            );
        }
    }

    @Override
    public void onServerStarted(FMLServerStartedEvent event) {
        Logger logger = PalaTest.INSTANCE.getLogger();

        this.configFile = new File(
            configFile.getParentFile(),
            configFile.getName().replace(".cfg", ".json")
        );
        if (this.configFile.exists()) {
            try {
                configuration = GSON.fromJson(
                    new FileReader(this.configFile),
                    ServerConfiguration.class
                );
            } catch (IOException exception) {
                logger.error(
                    "Couldn't read server configuration file.",
                    exception
                );
            }
        } else {
            logger.error(
                "Couldn't find server configuration file."
            );
        }
        if (!this.configFile.exists()
            || configuration == null
            || !configuration.isValid()
        ) {
            String defaultValues = GSON.toJson(new ServerConfiguration(
                "jdbc:mysql://",
                "127.0.0.1",
                3306,
                "grinderCrafts",
                "username",
                "password"
            ));
            try (FileWriter fileWriter = new FileWriter(this.configFile)) {
                fileWriter.write(defaultValues);
            } catch (IOException exception) {
                logger.error(
                    "Error writing default server configuration to \"{}\".",
                    this.configFile.getAbsolutePath(),
                    exception
                );
                return;
            }
            logger.info(
                "Wrote default server configuration to \"{}\".",
                this.configFile.getAbsolutePath()
            );
        }

        if (configuration != null && configuration.isValid()) {
            try {
                this.databaseConnector.initializeConnector(configuration);
                this.databaseConnector.initializeTables();
            } catch (Throwable throwable) {
                logger.error(
                    "Error while setting-up SQL connection.",
                    throwable
                );
                try {
                    this.databaseConnector.shutdown();
                } catch (Throwable ignored) {
                }
            }
        }

        logger.info("Initialization complete!");
    }

    @Override
    public void onServerStopped(FMLServerStoppedEvent event) {
        this.databaseConnector.shutdown();
    }
}
