package com.kerneldc.flightlogserver.domain.converter;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BooleanConverter implements AttributeConverter<Boolean, Character> {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public Character convertToDatabaseColumn(Boolean attribute) {
		//LOGGER.debug("convertToDatabaseColumn, attribute: {}", attribute);

	    if (attribute != null) {
            if (attribute) {
                return 'y';
            } else {
                return 'n';
            }
        }
        return null;
	}

	@Override
	public Boolean convertToEntityAttribute(Character dbData) {
		//LOGGER.debug("convertToEntityAttribute, dbData: {}", dbData);
	    if (dbData != null) {
            return dbData.equals('y');
        }
        return false;	
    }
}
