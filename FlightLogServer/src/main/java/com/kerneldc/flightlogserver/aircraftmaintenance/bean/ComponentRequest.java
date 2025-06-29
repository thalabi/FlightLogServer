package com.kerneldc.flightlogserver.aircraftmaintenance.bean;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@Builder
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

    //private Date created;
	//private Date modified;

	@Builder.Default
	private Set<ComponentHistoryVo> historyRequestSet = new LinkedHashSet<>(); 
}
