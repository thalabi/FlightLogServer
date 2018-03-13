package com.kerneldc.flightlogserver.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "sequence_generator", sequenceName = "airport_seq")
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

}
