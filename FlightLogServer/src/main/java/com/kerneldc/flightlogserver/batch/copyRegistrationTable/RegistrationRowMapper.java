package com.kerneldc.flightlogserver.batch.copyRegistrationTable;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.kerneldc.flightlogserver.domain.registration.Registration;

public class RegistrationRowMapper implements RowMapper<Registration> {

    public static final String ID_COLUMN = "id";
    public static final String REGISTRATION_COLUMN = "registration";
    public static final String CREATED_COLUMN = "created";
    public static final String MODIFIED_COLUMN = "modified";
    public static final String VERSION_COLUMN = "version";

	@Override
	public Registration mapRow(ResultSet rs, int rowNum) throws SQLException {
		Registration registration = new Registration();
		registration.setId(rs.getLong(ID_COLUMN));
		registration.setRegistration(rs.getString(REGISTRATION_COLUMN));
		registration.setCreated(rs.getDate(CREATED_COLUMN));
		registration.setModified(rs.getDate(MODIFIED_COLUMN));
		registration.setVersion(rs.getLong(VERSION_COLUMN));
		return registration;
	}

}
