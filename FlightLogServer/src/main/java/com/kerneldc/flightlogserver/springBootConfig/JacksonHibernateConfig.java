package com.kerneldc.flightlogserver.springBootConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@Configuration
public class JacksonHibernateConfig {

	@Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }
}
