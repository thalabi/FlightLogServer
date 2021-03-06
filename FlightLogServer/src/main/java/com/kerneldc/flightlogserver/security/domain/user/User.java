package com.kerneldc.flightlogserver.security.domain.user;

import java.util.Date;
import java.util.Set;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;
import com.kerneldc.flightlogserver.domain.converter.HashingConverter;
import com.kerneldc.flightlogserver.security.domain.Permission;
import com.kerneldc.flightlogserver.security.domain.group.Group;

import lombok.Getter;
import lombok.Setter;

@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(
		name = "userGroupSetGraph",
		attributeNodes = @NamedAttributeNode(value = "groupSet")),
	@NamedEntityGraph(
		name = "userGroupSetPermissionSetGraph",
		attributeNodes = @NamedAttributeNode(value = "groupSet", subgraph = "permissions"), 
			subgraphs = @NamedSubgraph(name = "permissions", attributeNodes = @NamedAttributeNode("permissionSet")))
})
@Getter @Setter
public class User extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_GROUP_SET = "groupSet";

	public static final Function<User, Object> idExtractor = User::getId;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
	@SequenceGenerator(name = "user_seq_gen", sequenceName = "user_seq", allocationSize = 1)
	private Long id;

	@Column(unique = true)
    private String username;
	@Column
	@Convert(converter = HashingConverter.class)
    private String password;
    private Boolean enabled;
    private String firstName; 
    private String lastName;

    @ManyToMany
    @JoinTable(name = "user_group", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn( name="group_id")) 
    private Set<Group> groupSet;// = new HashSet<>(); 

    @Transient 
    private Set<Permission> permissionSet; 

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

}
