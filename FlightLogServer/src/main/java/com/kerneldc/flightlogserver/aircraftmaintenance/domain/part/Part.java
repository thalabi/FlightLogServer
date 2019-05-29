package com.kerneldc.flightlogserver.aircraftmaintenance.domain.part;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.hateoas.Identifiable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Part extends AbstractPersistableEntity implements Identifiable<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "part_seq_gen")
	@SequenceGenerator(name = "part_seq_gen", sequenceName = "part_seq", allocationSize = 1)
	private Long id;

	@Column(unique = true)
    private String name;
    private String description;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

}
