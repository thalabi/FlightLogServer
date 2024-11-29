package com.kerneldc.flightlogserver.aircraftmaintenance.domain.part;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;

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

	@Column(unique = true)
    private String name;
    private String description;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	@Override
	protected void setLogicalKeyHolder() {
		// TODO need to add lk column to table and implement this method
	}

}
