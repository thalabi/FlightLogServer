package com.kerneldc.flightlogserver.domain.airport;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "default_seq_gen", sequenceName = "airport_seq", allocationSize = 1)
@Getter @Setter
public class Airport extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	public Airport() {
	}

	private String identifier;
	private String name;
	private String province;
	private String city;
	private String country;
	private Float latitude;
	private Float longitude;
	private String upperWindsStationId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	protected void setLogicalKeyHolder() {
		// TODO need to add lk column to table and implement this method
	}

}
