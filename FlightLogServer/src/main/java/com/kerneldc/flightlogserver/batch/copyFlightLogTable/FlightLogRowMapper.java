package com.kerneldc.flightlogserver.batch.copyFlightLogTable;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.kerneldc.flightlogserver.domain.flightLog.FlightLog;

public class FlightLogRowMapper implements RowMapper<FlightLog> {

	public static final String ID_COLUMN = "id";
	public static final String VERSION_COLUMN = "version";
	public static final String CO_PILOT_COLUMN = "co_pilot";
	public static final String CREATED_COLUMN = "created";
	public static final String DAY_DUAL_COLUMN = "day_dual";
	public static final String DAY_SOLO_COLUMN = "day_solo";
	public static final String FLIGHT_DATE_COLUMN = "flight_date";
	public static final String INSTRUMENT_FLIGHT_SIM_COLUMN = "instrument_flight_sim";
	public static final String INSTRUMENT_IMC_COLUMN = "instrument_imc";
	public static final String INSTRUMENT_NO_IFR_APPR_COLUMN = "instrument_no_ifr_appr";
	public static final String INSTRUMENT_SIMULATED_COLUMN = "instrument_simulated";
	public static final String MAKE_MODEL_COLUMN = "make_model";
	public static final String MODIFIED_COLUMN = "modified";
	public static final String NIGHT_DUAL_COLUMN = "night_dual";
	public static final String NIGHT_SOLO_COLUMN = "night_solo";
	public static final String PIC_COLUMN = "pic";
	public static final String REGISTRATION_COLUMN = "registration";
	public static final String REMARKS_COLUMN = "remarks";
	public static final String ROUTE_FROM_COLUMN = "route_from";
	public static final String ROUTE_TO_COLUMN = "route_to";
	public static final String TOS_LDGS_DAY_COLUMN = "tos_ldgs_day";
	public static final String TOS_LDGS_NIGHT_COLUMN = "tos_ldgs_night";
	public static final String X_COUNTRY_DAY_COLUMN = "x_country_day";
	public static final String X_COUNTRY_NIGHT_COLUMN = "x_country_night";
	
	@Override
	public FlightLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		FlightLog flightLog = new FlightLog();
		flightLog.setId(rs.getLong(ID_COLUMN));
		flightLog.setVersion(rs.getLong(VERSION_COLUMN));
		flightLog.setCoPilot(rs.getString(CO_PILOT_COLUMN));
		flightLog.setCreated(rs.getDate(CREATED_COLUMN));
		flightLog.setDayDual(rs.getFloat(DAY_DUAL_COLUMN));
		flightLog.setDaySolo(rs.getFloat(DAY_SOLO_COLUMN));
		flightLog.setFlightDate(rs.getDate(FLIGHT_DATE_COLUMN));
		flightLog.setInstrumentFlightSim(rs.getFloat(INSTRUMENT_FLIGHT_SIM_COLUMN));
		flightLog.setInstrumentImc(rs.getFloat(INSTRUMENT_IMC_COLUMN));
		flightLog.setInstrumentNoIfrAppr(rs.getInt(INSTRUMENT_NO_IFR_APPR_COLUMN));
		flightLog.setInstrumentSimulated(rs.getFloat(INSTRUMENT_SIMULATED_COLUMN));
		flightLog.setMakeModel(rs.getString(MAKE_MODEL_COLUMN));
		flightLog.setModified(rs.getDate(MODIFIED_COLUMN));
		flightLog.setNightDual(rs.getFloat(NIGHT_DUAL_COLUMN));
		flightLog.setNightSolo(rs.getFloat(NIGHT_SOLO_COLUMN));
		flightLog.setPic(rs.getString(PIC_COLUMN));
		flightLog.setRegistration(rs.getString(REGISTRATION_COLUMN));
		flightLog.setRemarks(rs.getString(REMARKS_COLUMN));
		flightLog.setRouteFrom(rs.getString(ROUTE_FROM_COLUMN));
		flightLog.setRouteTo(rs.getString(ROUTE_TO_COLUMN));
		flightLog.setTosLdgsDay(rs.getInt(TOS_LDGS_DAY_COLUMN));
		flightLog.setTosLdgsNight(rs.getInt(TOS_LDGS_NIGHT_COLUMN));
		flightLog.setXCountryDay(rs.getFloat(X_COUNTRY_DAY_COLUMN));
		flightLog.setXCountryNight(rs.getFloat(X_COUNTRY_NIGHT_COLUMN));
		return flightLog;
	}

}
