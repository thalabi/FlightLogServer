package com.kerneldc.flightlogserver.domain;

import java.util.Date;

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
public class Pilot extends AbstractPersistableEntity implements Identifiable<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pilot_seq_gen")
	@SequenceGenerator(name = "pilot_seq_gen", sequenceName = "pilot_seq", allocationSize = 1)
	private Long id;

	private String pilot;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

}
