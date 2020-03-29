package com.kerneldc.flightlogserver.domain.registration;

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
public class Registration extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	public static final Function<Registration, Object> idExtractor = Registration::getId;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registration_seq_gen")
	@SequenceGenerator(name = "registration_seq_gen", sequenceName = "registration_seq", allocationSize = 1)
	private Long id;

	private String registration;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

}
