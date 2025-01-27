package com.kerneldc.flightlogserver.aircraftmaintenance.domain.component;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
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
//@SequenceGenerator(name = "default_seq_gen", sequenceName = "component_seq", allocationSize = 1)
@NamedEntityGraph(
	name = "componentPartGraph",
	attributeNodes = @NamedAttributeNode(value = "part")
)
@NamedEntityGraph(
	name = "componentFullGraph",
	attributeNodes = {
		@NamedAttributeNode(value = "part"),
		@NamedAttributeNode(value = "componentHistorySet", subgraph = "componentHistoryPart")
	},
	subgraphs = @NamedSubgraph(name = "componentHistoryPart", attributeNodes = @NamedAttributeNode("part"))
)

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class Component extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_PART = "part";
	public static final String PROPERTY_COMPONENT_HISTORY_SET = "componentHistorySet";

	public static final Function<Component, Object> idExtractor = Component::getId;

	@Setter(AccessLevel.NONE)
	@NotNull
	@Column(unique = true)
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
    private Boolean deleted;
    
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(
		name = "component_component_history",
		joinColumns = @JoinColumn(name = "component_id"),
		inverseJoinColumns = @JoinColumn(name="component_history_id"))
	@OrderBy(value = "datePerformed desc") // show component history ordered by datePerformed in descending
	@Builder.Default // to make builder initialize componentHistorySet with empty set
	private Set<ComponentHistory> componentHistorySet = new LinkedHashSet<>(); // the no-args constructor won't get this default value


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
