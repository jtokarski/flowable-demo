package org.defendev.spring.core.demo;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

import static java.util.Objects.isNull;



public class DataSourceChecker {

    private final DataSource dataSource;

    public DataSourceChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isH2Database(String dbName) {
        final String canonicalName = dataSource.getClass().getCanonicalName();
        if (dataSource instanceof JdbcDataSource h2DataSource) {
            final String h2Url = h2DataSource.getURL();
            if (isNull(h2Url)) {
                return false;
            }
            return h2Url.contains(dbName);
        } else {
            return false;
        }
    }

}
