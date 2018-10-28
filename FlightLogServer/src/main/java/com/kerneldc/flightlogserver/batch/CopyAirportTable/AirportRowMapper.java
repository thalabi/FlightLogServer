package com.kerneldc.flightlogserver.batch.CopyAirportTable;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.kerneldc.flightlogserver.domain.airport.Airport;

public class AirportRowMapper implements RowMapper<Airport> {

    public static final String ID_COLUMN = "id";

    public static final String IDENTIFIER = "identifier";
	public static final String NAME = "name";
	public static final String PROVINCE = "province";
	public static final String CITY = "city";
	public static final String COUNTRY = "country";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String UPPER_WINDS_STATION_ID = "upper_winds_station_id";

    public static final String CREATED_COLUMN = "created";
    public static final String MODIFIED_COLUMN = "modified";
    public static final String VERSION_COLUMN = "version";
    
	@Override
	public Airport mapRow(ResultSet rs, int rowNum) throws SQLException {
		Airport airport = new Airport();
		airport.setId(rs.getLong(ID_COLUMN));
		
		airport.setIdentifier(rs.getString(IDENTIFIER));
		airport.setName(rs.getString(NAME));
		airport.setProvince(rs.getString(PROVINCE));
		airport.setCity(rs.getString(CITY));
		airport.setCountry(rs.getString(COUNTRY));
		airport.setLatitude(rs.getFloat(LATITUDE));
		airport.setLongitude(rs.getFloat(LONGITUDE));
		airport.setUpperWindsStationId(rs.getString(UPPER_WINDS_STATION_ID));

		airport.setCreated(rs.getDate(CREATED_COLUMN));
		airport.setModified(rs.getDate(MODIFIED_COLUMN));
		airport.setVersion(rs.getLong(VERSION_COLUMN));
		return airport;
	}

}
