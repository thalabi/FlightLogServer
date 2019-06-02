package com.kerneldc.flightlogserver.springBootConfig;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

	@Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2ddlAuto;
	
	@Value("${hibernate.physical_naming_strategy}")
    private String hibernatePhysicalNamingStrategy;
	
    @Bean
    @Primary // primary for Spring boot
    @ConfigurationProperties("output.datasource")
    public DataSource outputDataSource() {
    	return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("input.datasource.oracle")
    public DataSource inputDataSource() {
    	return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("input.datasource.h2")
    public DataSource inputDataSourceH2() {
    	return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
       LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
       localContainerEntityManagerFactoryBean.setDataSource(outputDataSource());
       localContainerEntityManagerFactoryBean.setPackagesToScan("com.kerneldc.flightlogserver.domain", "com.kerneldc.flightlogserver.security.domain", "com.kerneldc.flightlogserver.aircraftmaintenance.domain");
  
       JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
       localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
       localContainerEntityManagerFactoryBean.setJpaProperties(additionalProperties());
  
       return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
  
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }    
    
	private Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
		properties.setProperty("hibernate.physical_naming_strategy", hibernatePhysicalNamingStrategy);
		return properties;
	}

}
