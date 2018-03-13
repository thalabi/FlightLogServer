package com.kerneldc.flightlogserver.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "sequence_generator", sequenceName = "pilot_seq")
@Getter @Setter
public class Pilot extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	public Pilot() {
	}

	private String pilot;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

}
