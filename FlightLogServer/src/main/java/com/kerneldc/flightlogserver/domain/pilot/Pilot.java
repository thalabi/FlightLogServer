package com.kerneldc.flightlogserver.domain.pilot;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "default_seq_gen", sequenceName = "pilot_seq", allocationSize = 1)
@Getter @Setter
public class Pilot extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	private String pilot;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	@Override
	protected void setLogicalKeyHolder() {
		// TODO need to add lk column to table and implement this method
	}

}
