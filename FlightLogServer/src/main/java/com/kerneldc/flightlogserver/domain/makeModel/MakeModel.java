package com.kerneldc.flightlogserver.domain.makeModel;

import java.util.Date;

import javax.persistence.Entity;
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
//@SequenceGenerator(name = "default_seq_gen", sequenceName = "make_model_seq", allocationSize = 1)
@Getter @Setter
public class MakeModel extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	@Setter(AccessLevel.NONE)
	@NotNull
	private String makeModel;
	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date created;
	@Setter(AccessLevel.NONE)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date modified;

	public void setMakeModel(String makeModel) {
		this.makeModel = makeModel;
		
		if (getId() == null) {
			created = new Date();
		}
		modified = new Date();

		setLogicalKeyHolder();
	}

	@Override
	protected void setLogicalKeyHolder() {
		var logicalKeyHolder = LogicalKeyHolder.build(makeModel);
		setLogicalKeyHolder(logicalKeyHolder);
	}

}
