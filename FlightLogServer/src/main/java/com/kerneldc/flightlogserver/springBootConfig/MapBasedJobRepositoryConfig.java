package com.kerneldc.flightlogserver.springBootConfig;

import javax.sql.DataSource;

//@Configuration
//@EnableBatchProcessing
public class MapBasedJobRepositoryConfig /*extends DefaultBatchConfigurer*/ {

    //@Override
    public void setDataSource(DataSource dataSource) {
        // override to do not set datasource even if a datasource exist.
        // initialize will use a Map based JobRepository (instead of database)
    }
}
