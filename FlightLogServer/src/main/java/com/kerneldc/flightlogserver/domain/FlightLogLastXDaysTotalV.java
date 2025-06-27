package com.kerneldc.flightlogserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity(name="flight_log_last_x_days_total_v")
@Immutable
@Getter @Setter
public class FlightLogLastXDaysTotalV extends AbstractImmutableEntity {

	private Long days;
	private Float dayDual;
	private Float daySolo;
	private Float nightDual;
	private Float nightSolo;
	private Float instrumentSimulated;
	private Float instrumentFlightSim;
	@Column(name = "x_country_day")
	@JsonProperty("xCountryDay")
	private Float xCountryDay;
	@Column(name = "x_country_night")
	@JsonProperty("xCountryNight")
	private Float xCountryNight;
	private Float instrumentImc;
	private Integer instrumentNoIfrAppr;
	private Integer tosLdgsDay;
	private Integer tosLdgsNight;

}
