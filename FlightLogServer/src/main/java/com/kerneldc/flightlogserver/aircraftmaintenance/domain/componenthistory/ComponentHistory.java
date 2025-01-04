package com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory;

import java.util.Date;
import java.util.function.Function;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "component_history_seq_gen", sequenceName = "component_history_seq", allocationSize = 1)
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class ComponentHistory extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_PART = "part";

	public static final Function<ComponentHistory, Object> idExtractor = ComponentHistory::getId;

    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;
    private String workPerformed;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePerformed;
    private Float hoursPerformed;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDue;
    private Float hoursDue;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	@Override
	protected void setLogicalKeyHolder() {
		// TODO need to add lk column to table and implement this method
	}

}
