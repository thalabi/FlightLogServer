package com.kerneldc.flightlogserver.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Entity(name="flight_log_yearly_total_v")
@Immutable
@Getter @Setter
public class FlightLogYearlyTotalV {

	public FlightLogYearlyTotalV() {
	}

	@Id
	private Long id;
	private Long year;
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
	
	private Float totalYear;
}
