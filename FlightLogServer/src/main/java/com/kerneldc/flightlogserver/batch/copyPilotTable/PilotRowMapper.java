package com.kerneldc.flightlogserver.batch.copyPilotTable;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.kerneldc.flightlogserver.domain.pilot.Pilot;

public class PilotRowMapper implements RowMapper<Pilot> {

    public static final String ID_COLUMN = "id";
    public static final String PILOT_COLUMN = "pilot";
    public static final String CREATED_COLUMN = "created";
    public static final String MODIFIED_COLUMN = "modified";
    public static final String VERSION_COLUMN = "version";

	@Override
	public Pilot mapRow(ResultSet rs, int rowNum) throws SQLException {
		Pilot pilot = new Pilot();
		pilot.setId(rs.getLong(ID_COLUMN));
		pilot.setPilot(rs.getString(PILOT_COLUMN));
//		pilot.setCreated(rs.getDate(CREATED_COLUMN));
//		pilot.setModified(rs.getDate(MODIFIED_COLUMN));
		pilot.setVersion(rs.getLong(VERSION_COLUMN));
		return pilot;
	}

}
