package com.kerneldc.flightlogserver.batch.copyAircraftMaintenanceTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class OldComponentRowMapper implements RowMapper<OldComponent> {

	public static final String PART_NAME = "part_name";
	public static final String ACTIVITY_PERFORMED = "activity_performed";
	public static final String DATE_PERFORMED = "date_performed";
	public static final String HOURS_PERFORMED = "hours_performed";
	public static final String DATE_DUE = "date_due";
	public static final String HOURS_DUE = "hours_due";
    
	@Override
	public OldComponent mapRow(ResultSet rs, int rowNum) throws SQLException {
		return OldComponent.builder().partName(rs.getString(PART_NAME)).activityPerformed(rs.getString(ACTIVITY_PERFORMED))
				.datePerformed(rs.getDate(DATE_PERFORMED)).hoursPerformed(rs.getFloat(HOURS_PERFORMED))
				.dateDue(rs.getDate(DATE_DUE)).hoursDue(rs.getFloat(HOURS_DUE)).build();
	}

}
