package com.kerneldc.flightlogserver.aircraftmaintenance.domain.component;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.hateoas.Identifiable;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(
		name = "componentPartGraph",
		attributeNodes = @NamedAttributeNode(value = "part")),
	@NamedEntityGraph(
		name = "componentFullGraph",
		attributeNodes = {
			@NamedAttributeNode(value = "part"),
			@NamedAttributeNode(value = "componentHistorySet", subgraph = "componentHistoryPart")},
		subgraphs = @NamedSubgraph(name = "componentHistoryPart", attributeNodes = @NamedAttributeNode("part"))
	)
})
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class Component extends AbstractPersistableEntity implements Identifiable<Long> {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_PART = "part";
	public static final String PROPERTY_COMPONENT_HISTORY_SET = "componentHistorySet";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "component_seq_gen")
	@SequenceGenerator(name = "component_seq_gen", sequenceName = "component_seq", allocationSize = 1)
	private Long id;

	@Column(unique = true)
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;
    private String workPerformed;
    @Temporal(TemporalType.DATE)
    private Date datePerformed;
    private Float hoursPerformed;
    @Temporal(TemporalType.DATE)
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


	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

}
