package com.kerneldc.flightlogserver.domain.significantEvent;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;
import com.kerneldc.flightlogserver.domain.LogicalKeyHolder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
//@SequenceGenerator(name = "default_seq_gen", sequenceName = "significant_event_seq", allocationSize = 1)
@Getter @Setter
public class SignificantEvent extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@Setter(AccessLevel.NONE)
	@NotNull
	private Date eventDate;
	@Setter(AccessLevel.NONE)
	@NotNull
	private String eventDescription;
	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date created;
	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date modified;

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
		
		if (getId() == null) {
			created = new Date();
		}
		modified = new Date();
		
		setLogicalKeyHolder();
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
		setLogicalKeyHolder();
	}

	@Override
	protected void setLogicalKeyHolder() {
		var logicalKeyHolder = LogicalKeyHolder.build(eventDate, eventDescription);
		setLogicalKeyHolder(logicalKeyHolder);
	}
}
