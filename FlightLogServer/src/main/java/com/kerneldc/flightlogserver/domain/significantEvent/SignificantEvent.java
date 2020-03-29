package com.kerneldc.flightlogserver.domain.significantEvent;

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
public class SignificantEvent extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	public static final Function<SignificantEvent, Object> idExtractor = SignificantEvent::getId;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "significant_event_seq_gen")
	@SequenceGenerator(name = "significant_event_seq_gen", sequenceName = "significant_event_seq", allocationSize = 1)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date eventDate;
	private String eventDescription;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;
}
