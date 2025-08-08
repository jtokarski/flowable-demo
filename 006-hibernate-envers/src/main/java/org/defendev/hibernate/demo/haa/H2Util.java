package org.defendev.hibernate.demo.haa;

import org.h2.tools.Server;

import java.sql.SQLException;



public class H2Util {

    public static Server createH2TcpServer() throws SQLException {
        final String[] h2Args = new String[] {
            "-trace",
            "-tcpPort", "9092",
            "-tcpAllowOthers",
            "-ifExists"
        };
        /*
         * There is no possibility to select which H2 datasources will be exposed by this Server.
         * So apparently it exposes all that are started by particular JVM process, and stored
         * somehow statically.
         */
        final Server h2Server = Server.createTcpServer(h2Args);
        h2Server.start();
        return h2Server;
    }

}
