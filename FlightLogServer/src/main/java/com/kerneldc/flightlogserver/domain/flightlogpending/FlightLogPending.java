package com.kerneldc.flightlogserver.domain.flightlogpending;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerneldc.flightlogserver.domain.AbstractEntity;
import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;
import com.kerneldc.flightlogserver.domain.LogicalKeyHolder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class FlightLogPending extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.DATE)
	private Date flightDate;
	
	@Setter(AccessLevel.NONE)
	private String routeFrom;
	
	@Setter(AccessLevel.NONE)
	private String routeTo;
	
	private Float flightTime;
	private String registration;
	private String makeModel;

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
