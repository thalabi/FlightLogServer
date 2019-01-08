package com.kerneldc.flightlogserver.security.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;
import com.kerneldc.flightlogserver.security.domain.user.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Group extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_seq_gen")
	@SequenceGenerator(name = "group_seq_gen", sequenceName = "group_seq", allocationSize = 1)
	private Long id;

	@Column(unique = true)
    private String name; 
    private String description; 
    
    
    @ManyToMany(mappedBy = "groupSet")
    private Set<User> userSet = new HashSet<>(); 
 
    @ManyToMany
    @JoinTable(name = "group_permission", 
        joinColumns = @JoinColumn(name = "group_id"), 
        inverseJoinColumns = @JoinColumn( name="permission_id")) 
    private Set<Permission> permissionSet = new HashSet<>(); 

    
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

}
