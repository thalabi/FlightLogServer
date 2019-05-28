package com.kerneldc.flightlogserver.domain.converter;

import java.lang.invoke.MethodHandles;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
