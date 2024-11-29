package com.kerneldc.flightlogserver.domain.significantEvent;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "default_seq_gen", sequenceName = "significant_event_seq", allocationSize = 1)
@Getter @Setter
public class SignificantEvent extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	private Date eventDate;
	private String eventDescription;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	@Override
	protected void setLogicalKeyHolder() {
		// TODO need to add lk column to table and implement this method
	}

}
