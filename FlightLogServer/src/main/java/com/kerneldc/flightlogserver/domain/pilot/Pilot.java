package com.kerneldc.flightlogserver.domain.pilot;

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
public class Pilot extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	public static final Function<Pilot, Object> idExtractor = Pilot::getId;

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
