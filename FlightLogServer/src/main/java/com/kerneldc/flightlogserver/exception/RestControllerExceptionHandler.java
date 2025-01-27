package com.kerneldc.flightlogserver.exception;

import java.sql.SQLException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestControllerExceptionHandler /*extends ResponseEntityExceptionHandler*/ {

	private static final String LOG_MESSAGE_PREFIX = "Exception handled by RestControllerExceptionHandler.";
	protected static final String MESSAGE_SEPERATOR = "; ";
	protected record ErrorBody(String message, String stackTrace) {}

	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<ErrorBody> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException, WebRequest request) {
		illegalArgumentException.printStackTrace();

		LOGGER.info(LOG_MESSAGE_PREFIX + "handleIllegalArgumentException()");
		return new ResponseEntity<>(new ErrorBody(illegalArgumentException.getMessage(),
				ExceptionUtils.getStackTrace(illegalArgumentException)), HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(ApplicationException.class)
	protected ResponseEntity<ErrorBody> handleApplicationException(ApplicationException applicationException) {
		applicationException.printStackTrace();
		
		LOGGER.info(LOG_MESSAGE_PREFIX + "handleApplicationException()");
		return new ResponseEntity<>(new ErrorBody(String.join(MESSAGE_SEPERATOR, applicationException.getMessageList()),
				ExceptionUtils.getStackTrace(applicationException)), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(SQLException.class)
	protected ResponseEntity<ErrorBody> handleSqlException(SQLException sqlException) {
		sqlException.printStackTrace();
		//ExceptionBean exceptionBean = createExceptionBean(sqlException);

		LOGGER.info(LOG_MESSAGE_PREFIX + "handleSqlException()");
		ErrorBody eb;
//		if (sqlException.getCause() instanceof OracleDatabaseException oracleDatabaseException) {
//			//enrichSqlExceptionBean(exceptionBean, (OracleDatabaseException)sqlException.getCause());
//			eb = new ErrorBody(String.join(MESSAGE_SEPERATOR, 
//					sqlException.getMessage(),
//					"OracleErrorNumber: " + oracleDatabaseException.getOracleErrorNumber()+"",
//					"OracleSqlMessage: " + oracleDatabaseException.getMessage(),
//					"OriginalSqlStatement: " + oracleDatabaseException.getOriginalSql(),
//					"SqlStatement" + oracleDatabaseException.getSql()), ExceptionUtils.getStackTrace(sqlException));
//		} else {
			eb = new ErrorBody(sqlException.getMessage(), ExceptionUtils.getStackTrace(sqlException));			
//		}
		//return handleExceptionInternal(sqlException, exceptionBean, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
		return new ResponseEntity<>(eb, HttpStatus.INTERNAL_SERVER_ERROR);
		//return null;
	}

	@ExceptionHandler(NullPointerException.class)
	protected ResponseEntity<ErrorBody> handleNullPointerException(NullPointerException nullPointerException) {
		nullPointerException.printStackTrace();
		//ExceptionBean exceptionBean = createExceptionBean(nullPointerException);
//		return handleExceptionInternal(nullPointerException, exceptionBean, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

		LOGGER.info(LOG_MESSAGE_PREFIX + "handleNullPointerException()");
		return new ResponseEntity<>(
				new ErrorBody(nullPointerException.getMessage(), ExceptionUtils.getStackTrace(nullPointerException)),
				HttpStatus.BAD_REQUEST);
	}

//	@Override
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException, HttpHeaders headers, HttpStatus status, WebRequest request) {
//		methodArgumentNotValidException.printStackTrace();
//		ExceptionBean exceptionBean = createExceptionBean(methodArgumentNotValidException);
//		return handleExceptionInternal(methodArgumentNotValidException, exceptionBean, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
//	}

//	private ExceptionBean createExceptionBean(Exception exception) {
//		return ExceptionBean.builder().message(exception.getMessage())
//				.stackTrace(ExceptionUtils.getStackTrace(exception)).build();
//	}
//	private void enrichSqlExceptionBean(ExceptionBean sqlExceptionBean, OracleDatabaseException oracleDatabaseException) {
//		sqlExceptionBean.setOracleSqlError(oracleDatabaseException.getOracleErrorNumber());
//		sqlExceptionBean.setOracleSqlMessage(oracleDatabaseException.getMessage());
//		sqlExceptionBean.setOriginalSqlStatement(oracleDatabaseException.getOriginalSql());
//		sqlExceptionBean.setSqlStatement(oracleDatabaseException.getSql());
//	}
	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<ErrorBody> handleRuntimeException(RuntimeException runtimeException) {
		runtimeException.printStackTrace();

		LOGGER.info(LOG_MESSAGE_PREFIX + "handleRuntimeException()");
		return new ResponseEntity<>(
				new ErrorBody(runtimeException.getMessage(), ExceptionUtils.getStackTrace(runtimeException)),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
