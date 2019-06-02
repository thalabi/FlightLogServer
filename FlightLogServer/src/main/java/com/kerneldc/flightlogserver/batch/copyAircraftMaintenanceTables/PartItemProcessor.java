package com.kerneldc.flightlogserver.batch.copyAircraftMaintenanceTables;

import org.springframework.batch.item.ItemProcessor;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;

public class PartItemProcessor implements ItemProcessor<OldPart, Part> {
	
	@Override
	public Part process(OldPart item) throws Exception {
		return Part.builder().name(item.getName()).build();
	}
}

