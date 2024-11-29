package com.kerneldc.flightlogserver.domain.makeModel;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "default_seq_gen", sequenceName = "make_model_seq", allocationSize = 1)
@Getter @Setter
public class MakeModel extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	private String makeModel;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	@Override
	protected void setLogicalKeyHolder() {
		// TODO need to add lk column to table and implement this method
	}

}
