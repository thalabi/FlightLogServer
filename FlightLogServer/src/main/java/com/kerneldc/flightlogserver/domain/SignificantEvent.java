package com.kerneldc.flightlogserver.domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class SignificantEvent extends AbstractPersistableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "significant_event_seq_gen")
	@SequenceGenerator(name = "significant_event_seq_gen", sequenceName = "significant_event_seq", allocationSize = 1)
	private Long id;

	//@Temporal(TemporalType.DATE)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date eventDate;
	private String eventDescription;
	//@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	//@Temporal(TemporalType.TIMESTAMP)
	private Date modified;
	
	public void setEventDate(Date eventDate) {
		Calendar cal = Calendar.getInstance(); // locale-specific
		cal.setTime(eventDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		this.eventDate = new Date(cal.getTimeInMillis());
	}

}
