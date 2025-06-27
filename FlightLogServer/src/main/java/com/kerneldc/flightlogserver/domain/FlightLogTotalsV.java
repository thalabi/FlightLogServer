package com.kerneldc.flightlogserver.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity(name="flight_log_totals_v")
@Immutable
@Getter @Setter
public class FlightLogTotalsV extends AbstractImmutableEntity {

	@Temporal(TemporalType.DATE)
	private Date flightDate;
	private String makeModel;
	private String registration;
	private String pic;
	private String coPilot;
	private String routeFrom;
	private String routeTo;
	private String remarks;
	private Float dayDual;
	private Float daySolo;
	private Float nightDual;
	private Float nightSolo;
	private Float instrumentImc;
	private Float instrumentSimulated;
	private Float instrumentFlightSim;
	private Integer instrumentNoIfrAppr;
	@Column(name = "x_country_day")
	@JsonProperty("xCountryDay")
	private Float xCountryDay;
	@Column(name = "x_country_night")
	@JsonProperty("xCountryNight")
	private Float xCountryNight;
	private Integer tosLdgsDay;
	private Integer tosLdgsNight;

	private Float toDateDayDual;
	private Float toDateDaySolo;
	private Float toDateNightDual;
	private Float toDateNightSolo;
	private Float toDateInstrumentImc;
	private Float toDateInstrumentSimulated;
	private Float toDateInstrumentFlightSim;
	private Integer toDateInstrumentNoIfrAppr;
	@Column(name = "to_date_x_country_day")
	private Float toDateXCountryDay;
	@Column(name = "to_date_x_country_night")
	private Float toDateXCountryNight;
	private Integer toDateTosLdgsDay;
	private Integer toDateTosLdgsNight;
	private Float toDateTotal;

	private Float monthDaySolo;
	private Float monthDayDual;
	private Float monthNightSolo;
	private Float monthNightDual;
	private Float monthInstrumentImc;
	private Float monthInstrumentSimulated;
	private Float monthInstrumentFlightSim;
	private Integer monthInstrumentNoIfrAppr;
	@Column(name = "month_x_country_day")
	private Float monthXCountryDay;
	@Column(name = "month_x_country_night")
	private Float monthXCountryNight;
	private Integer monthTosLdgsDay;
	private Integer monthTosLdgsNight;
	private Float monthTotal;

	private Float yearDaySolo;
	private Float yearDayDual;
	private Float yearNightSolo;
	private Float yearNightDual;
	private Float yearInstrumentImc;
	private Float yearInstrumentSimulated;
	private Float yearInstrumentFlightSim;
	private Integer yearInstrumentNoIfrAppr;
	@Column(name = "year_x_country_day")
	private Float yearXCountryDay;
	@Column(name = "year_x_country_night")
	private Float yearXCountryNight;
	private Integer yearTosLdgsDay;
	private Integer yearTosLdgsNight;
	private Float yearTotal;
	}
