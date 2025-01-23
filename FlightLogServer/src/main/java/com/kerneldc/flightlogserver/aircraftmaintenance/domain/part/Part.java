package com.kerneldc.flightlogserver.aircraftmaintenance.domain.part;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;
import com.kerneldc.flightlogserver.domain.LogicalKeyHolder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "default_seq_gen", sequenceName = "part_seq", allocationSize = 1)
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Part extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	@Setter(AccessLevel.NONE)
	@NotNull
	@Column(unique = true)
    private String name;
    private String description;

	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date created;
	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date modified;

	public void setName(String name) {
		this.name = name;
		
		if (getId() == null) {
			created = new Date();
		}
		modified = new Date();

		setLogicalKeyHolder();
	}

	@Override
	protected void setLogicalKeyHolder() {
		var logicalKeyHolder = LogicalKeyHolder.build(name);
		setLogicalKeyHolder(logicalKeyHolder);
	}

}
