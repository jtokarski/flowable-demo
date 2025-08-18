package org.defendev.hibernate.demo.haa;

import org.h2.tools.Server;

import java.sql.SQLException;



public class H2Util {

    private static final String[] h2Args = new String[] {
        "-trace",
        "-web",
        "-webAllowOthers",
        "-webPort", "8082",
        "-tcpPort", "9092",
        "-tcpAllowOthers",
        "-ifExists"
    };

    public static Server createH2TcpServer() throws SQLException {
        /*
         * There is no possibility to select which H2 datasources will be exposed by this Server.
         * So apparently it exposes all that are started by particular JVM process, and stored
         * somehow statically.
         */
        return Server.createTcpServer(h2Args).start();
    }

    public static Server createH2WebServer() throws SQLException {
        return Server.createWebServer(h2Args).start();
    }

}
