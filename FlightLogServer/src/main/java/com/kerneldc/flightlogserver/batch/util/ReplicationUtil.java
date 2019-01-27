package com.kerneldc.flightlogserver.batch.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

public class ReplicationUtil {
	
	private ReplicationUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static boolean isOracleDatabase(DataSource outputDataSource) throws SQLException {
		try (Connection connection = outputDataSource.getConnection()) {
			return connection.getMetaData().getDatabaseProductName().equals("Oracle");	
		}
	}
	
	public static void enableTriggers(DataSource dataSource, String tableName) throws SQLException {
		alterTriggers(dataSource, tableName, true);
	}
	
	public static void disableTriggers(DataSource dataSource, String tableName) throws SQLException {
		alterTriggers(dataSource, tableName, false);
	}
	
	/**
	 * 'soft' alter of table triggers. Set the status in common.trigger_status.
	 * @param dataSource
	 * @param tableName
	 * @param enable
	 * @throws DataAccessException
	 * @throws SQLException
	 */
	private static void alterTriggers(DataSource dataSource, String tableName, boolean enable) throws SQLException {
		if (isOracleDatabase(dataSource)) {
			new JdbcTemplate(dataSource).execute(String.format("call common.trigger_status_pkg.set_trigger_status('%s',%d)", tableName, enable ? 1 : 0));
		}
	}
	
	public static void resetSequence(DataSource dataSource, String tableName) throws SQLException {
		String sequenceName = String.format("%s_seq", tableName );
		if (isOracleDatabase(dataSource)) {
			List<Integer> lastNumberList = new JdbcTemplate(dataSource).queryForList(
					String.format("select last_number from all_sequences where sequence_owner = user and lower(sequence_name) = '%s'", sequenceName),
					Integer.class);
			if (lastNumberList.get(0).compareTo(Integer.valueOf(1)) == 0) {
				return;
			}
			new JdbcTemplate(dataSource).execute(String.format("alter sequence %s increment by %d nocache nominvalue", sequenceName, -1*(lastNumberList.get(0)-1)));
			new JdbcTemplate(dataSource).execute(String.format("select %s.nextval from dual", sequenceName));
			new JdbcTemplate(dataSource).execute(String.format("alter sequence %s increment by 1", sequenceName));
		} else {
			new JdbcTemplate(dataSource).execute(String.format("drop sequence %s", sequenceName));
			new JdbcTemplate(dataSource).execute(String.format("create sequence %s", sequenceName));
		}
	}
	
	public static int tableReplicationStatus(DataSource dataSource, String legacySchemaName, String outputSchemaName, String tableName) throws SQLException {
		if (! /* not */ isOracleDatabase(dataSource)) {
			return -1;
		}

		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource)
				.withSchemaName("common").withCatalogName("trigger_status_pkg").withFunctionName("get_trigger_status");
		BigDecimal legacyTableTriggerStatus = simpleJdbcCall.executeFunction(BigDecimal.class, legacySchemaName, tableName);
		BigDecimal outputTableTriggerStatus = simpleJdbcCall.executeFunction(BigDecimal.class, outputSchemaName, tableName);
		return legacyTableTriggerStatus.add(outputTableTriggerStatus).intValue();
	}
}
