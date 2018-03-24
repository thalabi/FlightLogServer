package com.kerneldc.flightlogserver.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractPersistableEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public AbstractPersistableEntity() {
        super();
    }
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
//	private Long id;

	@Version
	@Column(name = "version")
	@XmlTransient
	private Long version;

//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}


    /**
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
//    public boolean equals(final Object object) {
//
//        return EqualsBuilder.reflectionEquals(this, object);
//    }
//    public int hashCode() {
//
//        return HashCodeBuilder.reflectionHashCode(this);
//    }

}
