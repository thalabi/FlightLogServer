package com.kerneldc.flightlogserver.controller.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionBean {

	private String message;
	private String stackTrace;
	private Integer oracleSqlError;
	private String oracleSqlMessage;
	private String originalSqlStatement;
	private String sqlStatement;
}
