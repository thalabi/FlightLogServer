package com.kerneldc.flightlogserver.domain.pilot;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;
import com.kerneldc.flightlogserver.domain.LogicalKeyHolder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "default_seq_gen", sequenceName = "pilot_seq", allocationSize = 1)
@Getter @Setter
public class Pilot extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	@Setter(AccessLevel.NONE)
	@NotNull
	private String pilot;
	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date created;
	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date modified;
	
	public void setPilot(String pilot) {
		this.pilot = pilot;

		if (getId() == null) {
			created = new Date();
		}
		modified = new Date();

		setLogicalKeyHolder();
	}
	
	@Override
	protected void setLogicalKeyHolder() {
		var logicalKeyHolder = LogicalKeyHolder.build(pilot);
		super.setLogicalKeyHolder(logicalKeyHolder);
	}
}
