package com.kerneldc.flightlogserver.springBootConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;

@Configuration
public class JacksonHibernateConfig {

	@Bean
    public Hibernate6Module hibernate6Module() {
        return new Hibernate6Module();
    }
}
