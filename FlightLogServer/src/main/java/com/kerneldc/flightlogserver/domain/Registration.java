package com.kerneldc.flightlogserver.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "sequence_generator", sequenceName = "registration_seq")
@Getter @Setter
public class Registration extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	public Registration() {
	}

	private String registration;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

}
