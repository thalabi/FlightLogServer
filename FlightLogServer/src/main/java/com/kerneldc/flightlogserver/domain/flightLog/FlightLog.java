package com.kerneldc.flightlogserver.domain.flightLog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "default_seq_gen", sequenceName = "flight_log_seq", allocationSize = 1)
@Getter @Setter
public class FlightLog extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

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
	
	@Override
	protected void setLogicalKeyHolder() {
		// TODO need to add lk column to table and implement this method
	}

}
