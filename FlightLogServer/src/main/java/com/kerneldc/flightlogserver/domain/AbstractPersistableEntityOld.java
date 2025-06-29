package com.kerneldc.flightlogserver.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
//@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
public abstract class AbstractPersistableEntityOld implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public AbstractPersistableEntityOld() {
        super();
    }
	
	@Version
	@Column(name = "version")
	private Long version;

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
