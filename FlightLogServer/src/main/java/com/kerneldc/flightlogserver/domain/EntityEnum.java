package com.kerneldc.flightlogserver.domain;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;

public enum EntityEnum {
	FLIGHT_LOG("flightLog", "flight_log"),
	AIRPORT("airport", "airport"),
	PILOT("pilot", "pilot"),
	REGISTRATION("registration", "registration"),
	SIGNIFICAT_EVENT("significantEvent", "significant_event"),
	MAKE_MODEL("makeModel", "make_model");
	
	@Getter
	private String entityName;
	@Getter
	private String tableName;
	private EntityEnum(String entityName, String tableName) {
		this.entityName = entityName;
		this.tableName = tableName;
	}

	public static EntityEnum getEnumByEntityName(String entityName) {
		Optional<EntityEnum> optionalEntityEnum = Arrays.asList(EntityEnum.values()).stream().filter(entityEnum -> entityEnum.getEntityName().equals(entityName)).findFirst();
		if (optionalEntityEnum.isPresent()) {
			return optionalEntityEnum.get();
		} else {
			return null;
		}
	}
}
