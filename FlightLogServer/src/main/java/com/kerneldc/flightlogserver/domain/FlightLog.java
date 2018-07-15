package com.kerneldc.flightlogserver.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.hateoas.Identifiable;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class FlightLog extends AbstractPersistableEntity implements Identifiable<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_log_seq_gen")
	@SequenceGenerator(name = "flight_log_seq_gen", sequenceName = "flight_log_seq", allocationSize = 1)
	private Long id;
	
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
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

}
