package org.defendev.spring.core.demo;

import org.springframework.beans.factory.FactoryBean;

import javax.sql.DataSource;



public class DataSourceCheckerFactoryBean implements FactoryBean<DataSourceChecker> {

    private DataSource wsxDataSource;

    public DataSource getWsxDataSource() {
        return wsxDataSource;
    }

    public void setWsxDataSource(DataSource wsxDataSource) {
        this.wsxDataSource = wsxDataSource;
    }

    @Override
    public DataSourceChecker getObject() throws Exception {
        return new DataSourceChecker(wsxDataSource);
    }

    @Override
    public Class<?> getObjectType() {
        return DataSourceChecker.class;
    }

}
