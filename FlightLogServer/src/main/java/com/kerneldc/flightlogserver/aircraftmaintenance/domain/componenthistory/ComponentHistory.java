package com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.hateoas.Identifiable;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@NamedEntityGraphs({
//	@NamedEntityGraph(
//		name = "componentPartGraph",
//		attributeNodes = @NamedAttributeNode(value = "part"))
//})
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class ComponentHistory extends AbstractPersistableEntity implements Identifiable<Long> {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_PART = "part";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "component_history_seq_gen")
	@SequenceGenerator(name = "component_history_seq_gen", sequenceName = "component_history_seq", allocationSize = 1)
	private Long id;

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

}
