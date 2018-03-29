package com.kerneldc.flightlogserver.springBootConfig;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

	@Bean
    @Primary // primary for Spring boot
    @ConfigurationProperties("output.datasource")
    public DataSourceProperties outputDataSourceProperties() {
    	return new DataSourceProperties();
    }

    @Bean
    @Primary // primary for Spring boot
    @ConfigurationProperties("output.datasource")
    public DataSource outputDataSource() {
    	return outputDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    @ConfigurationProperties("input.datasource")
    public DataSourceProperties inputDataSourceProperties() {
    	return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("input.datasource")
    public DataSource inputDataSource() {
    	return inputDataSourceProperties().initializeDataSourceBuilder().build();
    }

}
