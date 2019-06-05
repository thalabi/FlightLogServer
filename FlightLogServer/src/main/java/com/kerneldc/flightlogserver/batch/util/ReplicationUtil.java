package com.kerneldc.flightlogserver.batch.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
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
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		if (isOracleDatabase(dataSource)) {
			List<Integer> lastNumberList = jdbcTemplate.queryForList(
					String.format("select last_number from all_sequences where sequence_owner = user and lower(sequence_name) = '%s'", sequenceName),
					Integer.class);
			if (CollectionUtils.isEmpty(lastNumberList) || lastNumberList.get(0).compareTo(1) == 0) {
				return;
			}
			jdbcTemplate.execute(String.format("alter sequence %s increment by %d nocache nominvalue", sequenceName, -1*(lastNumberList.get(0)-1)));
			jdbcTemplate.execute(String.format("select %s.nextval from dual", sequenceName));
			jdbcTemplate.execute(String.format("alter sequence %s increment by 1", sequenceName));
		} else {
			if (sequenceExists(dataSource, sequenceName)) {
				jdbcTemplate.execute(String.format("drop sequence %s", sequenceName));
				jdbcTemplate.execute(String.format("create sequence %s", sequenceName));
			}
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
	
	public static boolean sequenceExists(DataSource dataSource, String sequenceName) throws SQLException {
		String sql;
		if (isOracleDatabase(dataSource)) {
			sql = "select count(*) from user_sequences where sequence_name = upper(?)";
		} else {
			sql = "select count(*) from information_schema.sequences where sequence_name = upper(?)";
		}
		Integer sequenceCount = new JdbcTemplate(dataSource).queryForObject(sql, new Object[] { sequenceName }, Integer.class);
		return sequenceCount.equals(1);
	}
	
	public static void executeSqlStatement(DataSource dataSource, String sqlStatement) throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.execute(sqlStatement);
	}
}
