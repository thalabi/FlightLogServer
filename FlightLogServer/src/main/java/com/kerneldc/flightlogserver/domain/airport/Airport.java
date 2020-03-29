package com.kerneldc.flightlogserver.domain.airport;

import java.util.Date;
import java.util.function.Function;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Airport extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	public Airport() {
	}

	public static final Function<Airport, Object> idExtractor = Airport::getId;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airport_seq_gen")
	@SequenceGenerator(name = "airport_seq_gen", sequenceName = "airport_seq", allocationSize = 1)
	private Long id;

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
