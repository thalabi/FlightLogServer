package com.kerneldc.flightlogserver.aircraftmaintenance.bean;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ComponentRequestNew {

	private String componentUri;

    private String name;
    private String description;
    private String workPerformed;
    private Date datePerformed;
    private Float hoursPerformed;
    private Date dateDue;
    private Float hoursDue;

    private String partUri;

    private Date created;
	private Date modified;

	private Set<ComponentHistoryVo> componentHistoryRequestSet = new LinkedHashSet<>(); 

    @Getter @Setter
    @ToString
	class ComponentHistoryVo {
    	
    	private String componentHistoryUri;
    	
	    private String name;
	    private String description;
	    private String workPerformed;
	    private Date datePerformed;
	    private Float hoursPerformed;
	    private Date dateDue;
	    private Float hoursDue;

	    private String partUri;

	    private Date created;
		private Date modified;
	}
}