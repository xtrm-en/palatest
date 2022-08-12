package me.xtrm.paladium.palatest.server.database;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import me.xtrm.paladium.palatest.Constants;
import me.xtrm.paladium.palatest.server.config.ServerConfiguration;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.io.InputStreamReader;
import java.net.URL;

@Log4j2(topic = "DatabaseConnector")
@SideOnly(Side.SERVER)
@SuppressWarnings("SameParameterValue")
public @Data class DatabaseConnector {
    private boolean initialized;
    private Sql2o sql2o;

    public void initializeConnector(ServerConfiguration configuration) {
        log.info("Loading-up server SQL Database connection...");

        this.sql2o = new Sql2o(
            configuration.getProtocol()
                + configuration.getDatabaseIp()
                + ":" + configuration.getDatabasePort()
                + "/" + configuration.getDatabaseName(),
            configuration.getDatabaseUser(),
            configuration.getDatabasePassword()
        );
    }

    public void initializeTables() {
        log.info("Initializing tables...");
        runScript("0-create-tables");
        initialized = true;
    }

    private void runScript(String scriptName) {
        runScript(scriptName, null);
    }

    @SneakyThrows
    private void runScript(String scriptName, Runnable successCallback) {
        boolean success = false;
        try (Connection connection = this.sql2o.open()) {
            URL url = getClass().getResource(
                String.format(
                    "/assets/%s/server/sql/%s.sql",
                    Constants.MODID,
                    scriptName
                )
            );
            assert url != null;

            try (InputStreamReader isr = new InputStreamReader(url.openStream())) {
                ScriptRunner scriptRunner = new ScriptRunner(connection.getJdbcConnection(), false, true);
                scriptRunner.setLogWriter(null);
                scriptRunner.runScript(isr);
            }

            success = true;
        } catch (MySQLSyntaxErrorException mySQLSyntaxErrorException) {
            if (!mySQLSyntaxErrorException.getMessage().contains("already exists")) {
                throw new RuntimeException("Error while executing script.", mySQLSyntaxErrorException);
            }
        }
        if (success && successCallback != null) {
            successCallback.run();
        }
    }

    public void shutdown() {
    }

}
