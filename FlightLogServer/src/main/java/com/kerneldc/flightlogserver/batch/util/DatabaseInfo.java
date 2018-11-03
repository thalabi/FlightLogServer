package com.kerneldc.flightlogserver.batch.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseInfo {
	
	private DatabaseInfo() {
		throw new IllegalStateException("Utility class");
	}

	public static boolean isOracleDatabase(DataSource outputDataSource) throws SQLException {
		try (Connection connection = outputDataSource.getConnection()) {
			return connection.getMetaData().getDatabaseProductName().equals("Oracle");	
		}
	}
	
	public static void enableTriggers(DataSource dataSource, String tableName) throws DataAccessException, SQLException {
		alterTriggers(dataSource, tableName, true);
	}
	
	public static void disableTriggers(DataSource dataSource, String tableName) throws DataAccessException, SQLException {
		alterTriggers(dataSource, tableName, false);
	}
	
	private static void alterTriggers(DataSource dataSource, String tableName, boolean enable) throws DataAccessException, SQLException {
		if (isOracleDatabase(dataSource)) {
			List<String> triggerNameList = new JdbcTemplate(dataSource).queryForList(
					String.format("select trigger_name from all_triggers where owner = user and lower(table_name) = '%s'", tableName),
					String.class);
			String enableDisable = enable ? "enable" : "disable";
			triggerNameList.forEach(triggerName -> {
				new JdbcTemplate(dataSource).execute(String.format("alter trigger %s %s", triggerName, enableDisable));});
		}
	}
}
