package com.kerneldc.flightlogserver.controller.util;

import java.sql.SQLException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.kerneldc.flightlogserver.exception.ApplicationException;

import oracle.jdbc.OracleDatabaseException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	//ApplicationException
	@ExceptionHandler(value = {ApplicationException.class})
	protected ResponseEntity<Object> handleNullPointerException(ApplicationException applicationException, WebRequest request) {
		applicationException.printStackTrace();
		ExceptionBean exceptionBean = initExceptionBean(applicationException);
		return handleExceptionInternal(applicationException, exceptionBean, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = {SQLException.class})
	protected ResponseEntity<Object> handleSqlException(SQLException sqlException, WebRequest request) {
		sqlException.printStackTrace();
		ExceptionBean exceptionBean = initExceptionBean(sqlException);
		if (sqlException.getCause() instanceof OracleDatabaseException) {
			OracleDatabaseException oracleDatabaseException = (OracleDatabaseException)sqlException.getCause();
			exceptionBean.setOracleSqlError(oracleDatabaseException.getOracleErrorNumber());
			exceptionBean.setOracleSqlMessage(oracleDatabaseException.getMessage());
			exceptionBean.setOriginalSqlStatement(oracleDatabaseException.getOriginalSql());
			exceptionBean.setSqlStatement(oracleDatabaseException.getSql());
		}
		return handleExceptionInternal(sqlException, exceptionBean, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = {NullPointerException.class})
	protected ResponseEntity<Object> handleNullPointerException(NullPointerException nullPointerException, WebRequest request) {
		nullPointerException.printStackTrace();
		ExceptionBean exceptionBean = initExceptionBean(nullPointerException);
		return handleExceptionInternal(nullPointerException, exceptionBean, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	private ExceptionBean initExceptionBean(Exception exception) {
		ExceptionBean exceptionBean = new ExceptionBean();
		exceptionBean.setMessage(exception.getMessage());
		exceptionBean.setStackTrace(ExceptionUtils.getStackTrace(exception));
		return exceptionBean;
	}
}
