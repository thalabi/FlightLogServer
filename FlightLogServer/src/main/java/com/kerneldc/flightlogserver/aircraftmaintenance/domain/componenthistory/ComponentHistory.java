package com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory;

import java.util.Date;
import java.util.function.Function;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;
import com.kerneldc.flightlogserver.domain.LogicalKeyHolder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@SequenceGenerator(name = "component_history_seq_gen", sequenceName = "component_history_seq", allocationSize = 1)
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class ComponentHistory extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_PART = "part";

	public static final Function<ComponentHistory, Object> idExtractor = ComponentHistory::getId;

	@Setter(AccessLevel.NONE)
	@NotNull
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "part_id")
	@Setter(AccessLevel.NONE)
    private Part part;
	@Setter(AccessLevel.NONE)
	@NotNull
    private String workPerformed;
	@Setter(AccessLevel.NONE)
	@NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePerformed;
    private Float hoursPerformed;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDue;
    private Float hoursDue;

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

	public void setPart(Part part) {
		this.part = part;
		setLogicalKeyHolder();
	}

	public void setWorkPerformed(String workPerformed) {
		this.workPerformed = workPerformed;
		setLogicalKeyHolder();
	}

	public void setDatePerformed(Date datePerformed) {
		this.datePerformed = datePerformed;
		setLogicalKeyHolder();
	}

	@Override
	protected void setLogicalKeyHolder() {
		var logicalKeyHolder = LogicalKeyHolder.build(name, datePerformed, workPerformed,
				part != null ? part.getId() : StringUtils.EMPTY);
		setLogicalKeyHolder(logicalKeyHolder);
	}

}
