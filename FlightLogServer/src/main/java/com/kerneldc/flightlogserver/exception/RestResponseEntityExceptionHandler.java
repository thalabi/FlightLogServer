package com.kerneldc.flightlogserver.exception;

import java.sql.SQLException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import oracle.jdbc.OracleDatabaseException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	//ApplicationException
	@ExceptionHandler(value = {ApplicationException.class})
	protected ResponseEntity<Object> handleApplicationException(ApplicationException applicationException, WebRequest request) {
		applicationException.printStackTrace();
		ExceptionBean exceptionBean = createExceptionBean(applicationException);
		return handleExceptionInternal(applicationException, exceptionBean, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = {SQLException.class})
	protected ResponseEntity<Object> handleSqlException(SQLException sqlException, WebRequest request) {
		sqlException.printStackTrace();
		ExceptionBean exceptionBean = createExceptionBean(sqlException);
		if (sqlException.getCause() instanceof OracleDatabaseException) {
			enrichSqlExceptionBean(exceptionBean, (OracleDatabaseException)sqlException.getCause());
		}
		return handleExceptionInternal(sqlException, exceptionBean, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = {NullPointerException.class})
	protected ResponseEntity<Object> handleNullPointerException(NullPointerException nullPointerException, WebRequest request) {
		nullPointerException.printStackTrace();
		ExceptionBean exceptionBean = createExceptionBean(nullPointerException);
		return handleExceptionInternal(nullPointerException, exceptionBean, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException, HttpHeaders headers, HttpStatus status, WebRequest request) {
		methodArgumentNotValidException.printStackTrace();
		ExceptionBean exceptionBean = createExceptionBean(methodArgumentNotValidException);
		return handleExceptionInternal(methodArgumentNotValidException, exceptionBean, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	private ExceptionBean createExceptionBean(Exception exception) {
		return ExceptionBean.builder().message(exception.getMessage())
				.stackTrace(ExceptionUtils.getStackTrace(exception)).build();
	}
	private void enrichSqlExceptionBean(ExceptionBean sqlExceptionBean, OracleDatabaseException oracleDatabaseException) {
		sqlExceptionBean.setOracleSqlError(oracleDatabaseException.getOracleErrorNumber());
		sqlExceptionBean.setOracleSqlMessage(oracleDatabaseException.getMessage());
		sqlExceptionBean.setOriginalSqlStatement(oracleDatabaseException.getOriginalSql());
		sqlExceptionBean.setSqlStatement(oracleDatabaseException.getSql());
	}
}
