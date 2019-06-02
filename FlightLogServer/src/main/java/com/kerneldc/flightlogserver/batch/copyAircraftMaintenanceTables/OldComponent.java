package com.kerneldc.flightlogserver.batch.copyAircraftMaintenanceTables;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class OldComponent {

	private String partName;
	private String activityPerformed;
	private Date datePerformed;
	private Float hoursPerformed;
	private Date dateDue;
	private Float hoursDue;
}
