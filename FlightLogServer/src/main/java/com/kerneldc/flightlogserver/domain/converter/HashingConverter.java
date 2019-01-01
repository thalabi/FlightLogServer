package com.kerneldc.flightlogserver.domain.converter;

import java.lang.invoke.MethodHandles;

import javax.annotation.PostConstruct;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Converter
@Component
public class HashingConverter implements AttributeConverter<String, String> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static PasswordEncoder passwordEncoder;

	@PostConstruct
	public void init () {
		LOGGER.debug("passwordEncoder == null: {}", passwordEncoder == null);
	}

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		HashingConverter.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public String convertToDatabaseColumn(String attribute) {
		LOGGER.debug("passwordEncoder == null: {}", passwordEncoder == null);
		LOGGER.debug("attribute: {}", attribute);
        return passwordEncoder.encode(attribute);
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		LOGGER.debug("dbData: {}", dbData);
        return dbData;	
    }
}
