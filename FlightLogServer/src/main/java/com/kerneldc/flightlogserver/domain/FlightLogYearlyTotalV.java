package com.kerneldc.flightlogserver.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity(name="flight_log_yearly_total_v")
@Immutable
@Getter @Setter
public class FlightLogYearlyTotalV extends AbstractImmutableEntity {

	private Date year;
	private Float dayDual;
	private Float daySolo;
	private Float nightDual;
	private Float nightSolo;
	private Float instrumentSimulated;
	private Float instrumentFlightSim;
	@Column(name = "x_country_day")
	@JsonProperty("xCountryDay")
	private Float xcountryDay;
	@Column(name = "x_country_night")
	@JsonProperty("xCountryNight")
	private Float xcountryNight;
	private Float instrumentImc;
	private Integer instrumentNoIfrAppr;
	private Integer tosLdgsDay;
	private Integer tosLdgsNight;
	
	private Float totalYear;
}
