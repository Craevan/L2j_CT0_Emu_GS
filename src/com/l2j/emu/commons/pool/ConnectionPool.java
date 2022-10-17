package com.l2j.emu.commons.pool;

import com.l2j.emu.Config;
import com.l2j.emu.commons.logging.CLogger;

import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static final CLogger logger = new CLogger(ConnectionPool.class.getName());
    private static MariaDbPoolDataSource source;

    public static void init() {
        try {
            source = new MariaDbPoolDataSource();
            source.setMaxPoolSize(Config.DATABASE_MAX_CONNECTIONS);
            source.setUrl(Config.DATABASE_URL);
            source.setUser(Config.DATABASE_LOGIN);
            source.setPassword(Config.DATABASE_PASSWORD);
            source.setStaticGlobal(true);
        } catch (SQLException e) {
            logger.error("Couldn't initialize connection pooler.", e);
        }
        logger.info("Initializing ConnectionPool.");
    }

    public static void shutdown() {
        if (source != null) {
            source.close();
            source = null;
        }
    }

    public static Connection getConnection() throws SQLException {
        return source.getConnection();
    }
}
