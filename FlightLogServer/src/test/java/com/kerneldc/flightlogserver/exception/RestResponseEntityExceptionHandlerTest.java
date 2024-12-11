package com.kerneldc.flightlogserver.exception;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.startsWith;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kerneldc.flightlogserver.exception.RestControllerExceptionHandler.ErrorBody;

import oracle.jdbc.OracleDatabaseException;

class RestResponseEntityExceptionHandlerTest {

	private RestControllerExceptionHandler fixture;
	
//	@Mock
//	private WebRequest webRequest;
	
	@BeforeEach
	void setup() {
		fixture = new RestControllerExceptionHandler();
	}
	
	@Test
    void handleApplicationExceptionTest() {
		String message = "Exception message";
		ApplicationException applicationException = new ApplicationException(message);
		
		ResponseEntity<ErrorBody> responseEntity = fixture.handleApplicationException(applicationException);
		
		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
		assertThat(responseEntity.getBody(), instanceOf(ErrorBody.class));
		ErrorBody erroBody = responseEntity.getBody();
		assertThat(erroBody.message(), equalTo(message));
		assertThat(erroBody.stackTrace(), startsWith(ApplicationException.class.getName()));
    }
	@Test
    void handleApplicationExceptionTest_withMessageList() {
		String message = "Exception message";
		String message2 = "Exception message two";
		String message3 = "Exception message three";
		ApplicationException applicationException = new ApplicationException(message);
		applicationException.addMessage(message2);
		applicationException.addMessage(message3);
		
		ResponseEntity<ErrorBody> responseEntity = fixture.handleApplicationException(applicationException);
		
		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
		assertThat(responseEntity.getBody(), instanceOf(ErrorBody.class));
		ErrorBody errorBody = responseEntity.getBody();
		assertThat(errorBody.message(), equalTo(String.join(RestControllerExceptionHandler.MESSAGE_SEPERATOR, message, message2, message3)));
		assertThat(errorBody.stackTrace(), startsWith(ApplicationException.class.getName()));
    }
	
	@Test
	void handleSqlExceptionTest() {
		String message = "SQL exception message";
		SQLException sqlException = new SQLException(message);
		
		int errorPosition = 7;
		int oracleErrorNumber = 1403;
		String oracleErrorMessage = "NO_DATA_FOUND";
		String sql = "select attribute from table where id = 500;";
		String originalSql = "exec findId(500)";
		OracleDatabaseException OracleDatabaseException = new OracleDatabaseException(errorPosition, oracleErrorNumber, oracleErrorMessage, sql, originalSql);
		
		sqlException.initCause(OracleDatabaseException);
		
		ResponseEntity<ErrorBody> responseEntity = fixture.handleSqlException(sqlException);

		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
		assertThat(responseEntity.getBody(), instanceOf(ErrorBody.class));
		ErrorBody exceptionBean = responseEntity.getBody();
		assertThat(exceptionBean.message(), containsString(message));
		assertThat(exceptionBean.message(), containsString(oracleErrorNumber+""));
		assertThat(exceptionBean.stackTrace(), startsWith(SQLException.class.getName()));
		assertThat(exceptionBean.message(), containsString(oracleErrorMessage));
		assertThat(exceptionBean.message(), containsString(sql));
		assertThat(exceptionBean.message(), containsString(originalSql));
	}
}
