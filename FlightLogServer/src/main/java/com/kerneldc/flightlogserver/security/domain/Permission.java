package com.kerneldc.flightlogserver.security.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.hateoas.Identifiable;

import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;
import com.kerneldc.flightlogserver.security.domain.group.Group;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Permission extends AbstractPersistableEntity implements Identifiable<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_seq_gen")
	@SequenceGenerator(name = "permission_seq_gen", sequenceName = "permission_seq", allocationSize = 1)
	private Long id;

	@Column(unique = true)
    private String name; 
    private String description; 
    
    @ManyToMany(mappedBy = "permissionSet")
    private Set<Group> groupSet = new HashSet<>(); 


	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

}
