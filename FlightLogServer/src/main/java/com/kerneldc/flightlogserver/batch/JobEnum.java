package com.kerneldc.flightlogserver.batch;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

import lombok.Getter;

public enum JobEnum {
	COPY_FLIGHT_LOG_TABLE ("flight_log", JobTypeEnum.COPY_TABLE),
	COPY_MAKE_MODEL_TABLE ("make_model", JobTypeEnum.COPY_TABLE),
	COPY_PILOT_TABLE ("pilot", JobTypeEnum.COPY_TABLE),
	COPY_REGISTRATION_TABLE ("registration", JobTypeEnum.COPY_TABLE),
	COPY_SIGNIFICANT_EVENT_TABLE ("significant_event", JobTypeEnum.COPY_TABLE),
	COPY_AIRPORT_TABLE ("airport", JobTypeEnum.COPY_TABLE),
	DISABLE_FLIGHT_LOG_TRIGGERS ("flight_log", JobTypeEnum.TRIGGER_CHANGE_STATUS, ImmutableMap.of("triggerStatus", false)),
	ENABLE_FLIGHT_LOG_TRIGGERS ("flight_log", JobTypeEnum.TRIGGER_CHANGE_STATUS, ImmutableMap.of("triggerStatus", true)),
	DISABLE_MAKE_MODEL_TRIGGERS ("make_model", JobTypeEnum.TRIGGER_CHANGE_STATUS, ImmutableMap.of("triggerStatus", false)),
	ENABLE_MAKE_MODEL_TRIGGERS ("make_model", JobTypeEnum.TRIGGER_CHANGE_STATUS, ImmutableMap.of("triggerStatus", true)),
	DISABLE_SIGNIFICANT_EVENT_TRIGGERS ("significant_event", JobTypeEnum.TRIGGER_CHANGE_STATUS, ImmutableMap.of("triggerStatus", false)),
	ENABLE_SIGNIFICANT_EVENT_TRIGGERS ("significant_event", JobTypeEnum.TRIGGER_CHANGE_STATUS, ImmutableMap.of("triggerStatus", true)),
	COPY_AIRCRAFT_MAINTENANCE_TABLES (StringUtils.EMPTY, JobTypeEnum.COPY_TABLE),
	;
	
	@Getter
	private String tableName;
	@Getter
	private JobTypeEnum jobTypeEnum;
	@Getter
	private Map<String, Object> jobConfigMap;
	
	private JobEnum (String tableName, JobTypeEnum jobTypeEnum) {
		this.tableName = tableName;
		this.jobTypeEnum = jobTypeEnum;
		this.jobConfigMap = null;
	}
	private JobEnum (String tableName, JobTypeEnum jobTypeEnum, Map<String, Object> jobConfigMap) {
		this.tableName = tableName;
		this.jobTypeEnum = jobTypeEnum;
		this.jobConfigMap = jobConfigMap;
	}
	
}
