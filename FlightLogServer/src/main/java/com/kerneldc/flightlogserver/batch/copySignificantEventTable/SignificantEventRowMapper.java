package com.kerneldc.flightlogserver.batch.copySignificantEventTable;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.kerneldc.flightlogserver.domain.SignificantEvent;

public class SignificantEventRowMapper implements RowMapper<SignificantEvent> {

    public static final String ID_COLUMN = "id";
    public static final String EVENT_DATE_COLUMN = "event_date";
    public static final String EVENT_DESCRIPTION_COLUMN = "event_description";
    public static final String CREATED_COLUMN = "created";
    public static final String MODIFIED_COLUMN = "modified";
    public static final String VERSION_COLUMN = "version";

	@Override
	public SignificantEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
		SignificantEvent significantEvent = new SignificantEvent();
		significantEvent.setId(rs.getLong(ID_COLUMN));
		significantEvent.setEventDate(rs.getDate(EVENT_DATE_COLUMN));
		significantEvent.setEventDescription(rs.getString(EVENT_DESCRIPTION_COLUMN));
		significantEvent.setCreated(rs.getDate(CREATED_COLUMN));
		significantEvent.setModified(rs.getDate(MODIFIED_COLUMN));
		significantEvent.setVersion(rs.getLong(VERSION_COLUMN));
		return significantEvent;
	}

}
