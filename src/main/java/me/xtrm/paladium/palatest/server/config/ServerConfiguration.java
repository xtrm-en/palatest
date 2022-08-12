package me.xtrm.paladium.palatest.server.config;

import lombok.Data;

public @Data class ServerConfiguration {
    // Database
    private final String protocol;
    private final String databaseIp;
    private final int databasePort;
    private final String databaseName;
    private final String databaseUser;
    private final String databasePassword;

    public boolean isValid() {
        return exists(protocol)
            && exists(databaseIp)
            && exists(databaseName)
            && exists(databaseUser)
            && exists(databasePassword)
            && databasePort != -1;
    }

    private static <T> boolean exists(T object) {
        if (object instanceof String) {
            return !((String) object).isEmpty();
        }
        return object != null;
    }
}
