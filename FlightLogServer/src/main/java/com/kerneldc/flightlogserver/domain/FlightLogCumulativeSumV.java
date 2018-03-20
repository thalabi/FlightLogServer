package com.kerneldc.flightlogserver.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.Setter;

@Entity(name="flight_log_cumulative_sum_v")
@Immutable
@Getter @Setter
public class FlightLogCumulativeSumV {

	public FlightLogCumulativeSumV() {
	}

	@Id
	private Long id;
	private Date flightDate;
	private Float dayDualSum;
	private Float daySoloSum;
	private Float nightDualSum;
	private Float nightSoloSum;
//	private Float instrumentSimulated;
//	private Float instrumentFlightSim;
//	@Column(name = "x_country_day")
//	private Float xcountryDay;
//	@Column(name = "x_country_night")
//	private Float xcountryNight;
//	private Float instrumentImc;
//	private Integer instrumentNoIfrAppr;
//	private Integer tosLdgsDay;
//	private Integer tosLdgsNight;

}
