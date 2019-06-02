package com.kerneldc.flightlogserver.batch.copyAircraftMaintenanceTables;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class OldPartRowMapper implements RowMapper<OldPart> {

	public static final String NAME = "name";
    
	@Override
	public OldPart mapRow(ResultSet rs, int rowNum) throws SQLException {
		return OldPart.builder().name(rs.getString(NAME)).build();
	}

}
