package com.kerneldc.flightlogserver.domain;

public class EntityEnumUtilities {
	
	private EntityEnumUtilities() {
	    throw new IllegalStateException("Utility class not meat to be instantiated");
	}

	public static IEntityEnum getEntityEnum(String tableName) {
		IEntityEnum tEnum;
		if (FlightLogEntityEnum.valueOf(tableName.toUpperCase()) != null) {
			tEnum = FlightLogEntityEnum.valueOf(tableName.toUpperCase());
		} else {
			throw new IllegalArgumentException(String.format("There is no corresponding entity enum for [%s]", tableName.toUpperCase()));
		}
		return tEnum;
	}

}
