package com.kerneldc.flightlogserver;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.kerneldc.flightlogserver.domain.AbstractPersistableEntity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter @Setter
public class Car extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@NonNull
    private String name;
	private Date dateCreated;
	@ManyToOne(fetch = FetchType.LAZY)
	private Color color;
}
