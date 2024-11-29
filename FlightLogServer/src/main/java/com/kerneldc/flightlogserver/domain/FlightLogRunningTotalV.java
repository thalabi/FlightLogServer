package com.kerneldc.flightlogserver.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Entity(name="flight_log_running_total_v")
@Immutable
@Getter @Setter
public class FlightLogRunningTotalV {

	public FlightLogRunningTotalV() {
	}

	@Id
	private Long id;
	private String yearMonth;
	private Float dayDual;
	private Float daySolo;
	private Float nightDual;
	private Float nightSolo;
	private Float instrumentSimulated;
	private Float instrumentFlightSim;
	@Column(name = "x_country_day")
	private Float xcountryDay;
	@Column(name = "x_country_night")
	private Float xcountryNight;
	private Float instrumentImc;
	private Integer instrumentNoIfrAppr;
	private Integer tosLdgsDay;
	private Integer tosLdgsNight;
	
	private Float totalDayDual;
	private Float totalDaySolo;
	private Float totalNightDual;
	private Float totalNightSolo;
	private Float totalInstrumentSimulated;
	private Float totalInstrumentFlightSim;
	@Column(name = "total_x_country_day")
	private Float totalXcountryDay;
	@Column(name = "total_x_country_night")
	private Float totalXcountryNight;
	private Float totalInstrumentImc;
	private Integer totalInstrumentNoIfrAppr;
	private Integer totalTosLdgsDay;
	private Integer totalTosLdgsNight;
	
	private Float totalToDate;
}
