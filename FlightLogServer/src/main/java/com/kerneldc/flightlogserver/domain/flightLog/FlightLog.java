package com.kerneldc.flightlogserver.domain.flightLog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kerneldc.flightlogserver.domain.AbstractEntity;
import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;
import com.kerneldc.flightlogserver.domain.LogicalKeyHolder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
//@SequenceGenerator(name = "default_seq_gen", sequenceName = "flight_log_seq", allocationSize = 1)
@Getter @Setter
public class FlightLog extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.DATE)
	private Date flightDate;
	private String makeModel;
	private String registration;
	private String pic;
	private String coPilot;
	@Setter(AccessLevel.NONE)
	private String routeFrom;
	@Setter(AccessLevel.NONE)
	private String routeTo;
	private String remarks;
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
	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date created;
	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date modified;
	
	public void setFlightDate(Date flightDate) {
		this.flightDate = flightDate;
		
		if (getId() == null) {
			created = new Date();
		}
		modified = new Date();
		
		setLogicalKeyHolder();
	}

	public void setRouteFrom(String routeFrom) {
		this.routeFrom = routeFrom;
		setLogicalKeyHolder();
	}

	public void setRouteTo(String routeTo) {
		this.routeTo = routeTo;
		setLogicalKeyHolder();
	}


	@Override
	protected void setLogicalKeyHolder() {
		var logicalKeyHolder = LogicalKeyHolder.build(routeFrom, routeTo, flightDate, AbstractEntity.DATE_TIME_FORMAT.format(modified));
		setLogicalKeyHolder(logicalKeyHolder);
	}
}
