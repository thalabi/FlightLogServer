package com.kerneldc.flightlogserver.domain.airport;

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
@Getter @Setter
public class Airport extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	public Airport() {
	}

	@Setter(AccessLevel.NONE)
	@NotNull
	private String identifier;
	private String name;
	private String province;
	private String city;
	private String country;
	private Float latitude;
	private Float longitude;
	private String upperWindsStationId;
	
	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date created;
	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date modified;
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;

		if (getId() == null) {
			created = new Date();
		}
		modified = new Date();

		
		setLogicalKeyHolder();
	}
	
	protected void setLogicalKeyHolder() {
		var logicalKeyHolder = LogicalKeyHolder.build(identifier);
		setLogicalKeyHolder(logicalKeyHolder);
	}

}
