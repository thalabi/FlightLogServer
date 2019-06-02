package com.kerneldc.flightlogserver.aircraftmaintenance.bean;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ComponentRequest {

	private String componentUri;
    private String name;
    private String description;
    private String workPerformed;
    private Date datePerformed;
    private Float hoursPerformed;
    private Date dateDue;
    private Float hoursDue;
    
    private String partUri;
    
    private Boolean createHistoryRecord;
    
	private Date created;
	private Date modified;
}