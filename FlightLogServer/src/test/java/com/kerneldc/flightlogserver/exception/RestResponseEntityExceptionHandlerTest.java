package com.kerneldc.flightlogserver.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.startsWith;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import com.kerneldc.flightlogserver.exception.ApplicationException;
import com.kerneldc.flightlogserver.exception.ExceptionBean;
import com.kerneldc.flightlogserver.exception.RestResponseEntityExceptionHandler;

import oracle.jdbc.OracleDatabaseException;

public class RestResponseEntityExceptionHandlerTest {

	private RestResponseEntityExceptionHandler fixture;
	
	@Mock
	private WebRequest webRequest;
	
	@Before
	public void setup() {
		fixture = new RestResponseEntityExceptionHandler();
	}
	
	@Test
    public void handleApplicationExceptionTest() {
		String message = "Exception message";
		ApplicationException applicationException = new ApplicationException(message);
		
		ResponseEntity<Object> responseEntity = fixture.handleApplicationException(applicationException, webRequest);
		
		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
		assertThat(responseEntity.getBody(), instanceOf(ExceptionBean.class));
		ExceptionBean exceptionBean = (ExceptionBean)responseEntity.getBody();
		assertThat(exceptionBean.getMessage(), equalTo(message));
		assertThat(exceptionBean.getStackTrace(), startsWith(ApplicationException.class.getName()));
    }
	
	@Test
	public void handleSqlExceptionTest() {
		String message = "SQL exception message";
		SQLException sqlException = new SQLException(message);
		
		int errorPosition = 7;
		int oracleErrorNumber = 1403;
		String oracleErrorMessage = "NO_DATA_FOUND";
		String sql = "select attribute from table where id = 500;";
		String originalSql = "exec findId(500)";
		OracleDatabaseException OracleDatabaseException = new OracleDatabaseException(errorPosition, oracleErrorNumber, oracleErrorMessage, sql, originalSql);
		
		sqlException.initCause(OracleDatabaseException);
		
		ResponseEntity<Object> responseEntity = fixture.handleSqlException(sqlException, webRequest);

		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
		assertThat(responseEntity.getBody(), instanceOf(ExceptionBean.class));
		ExceptionBean exceptionBean = (ExceptionBean)responseEntity.getBody();
		assertThat(exceptionBean.getMessage(), equalTo(message));
		assertThat(exceptionBean.getStackTrace(), startsWith(SQLException.class.getName()));
		
		assertThat(exceptionBean.getOracleSqlError(), equalTo(oracleErrorNumber));
		assertThat(exceptionBean.getOracleSqlMessage(), equalTo(oracleErrorMessage));
		assertThat(exceptionBean.getSqlStatement(), equalTo(sql));
		assertThat(exceptionBean.getOriginalSqlStatement(), equalTo(originalSql));
	}
}
