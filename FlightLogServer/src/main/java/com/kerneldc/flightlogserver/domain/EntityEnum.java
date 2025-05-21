package com.kerneldc.flightlogserver.domain;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;

public enum EntityEnum {
	FLIGHT_LOG("flightLog", "flight_log", null),
	FLIGHT_LOG_TOTALS_V("flightLogTotalsV", "flight_log_totals_v", null),
	AIRPORT("airport", "airport", null),
	PILOT("pilot", "pilot", null),
	REGISTRATION("registration", "registration", null),
	SIGNIFICAT_EVENT("significantEvent", "significant_event", null),
	MAKE_MODEL("makeModel", "make_model", null),
	
	USER("user", "user", null),
	GROUP("group", "group", null),
	PERMISSION("permission", "permission", null),
	
	PART("part", "part", "acm_bu_%s_%s_part"),
	COMPONENT("component", "component", "acm_bu_%s_%s_component"),
	COMPONENT_HISTORY("componentHistory", "component_history", "acm_bu_%s_%s_comp_hist"),
	COMPONENT_COMPONENT_HISTORY("componentComponentHistory", "component_component_history", "acm_bu_%s_%s_c_c_h"),
	FLIGHT_LOG_PENDING("flightLogPending", "flight_log_pending", null);
	
	@Getter
	private String entityName;
	@Getter
	private String tableName;
	@Getter
	private String backupTableNameTemplate;
	
	private EntityEnum(String entityName, String tableName, String backupTableNameTemplate) {
		this.entityName = entityName;
		this.tableName = tableName;
		this.backupTableNameTemplate = backupTableNameTemplate;
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
