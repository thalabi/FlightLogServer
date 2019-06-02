package com.kerneldc.flightlogserver.batch.copyAircraftMaintenanceTables;


import java.util.Map;

import org.springframework.batch.item.ItemProcessor;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComponentItemProcessor implements ItemProcessor<OldComponent, Component> {
	
	private PartCachingService partCachingService;

	public ComponentItemProcessor(PartCachingService partCachingService) {
		this.partCachingService = partCachingService;
		LOGGER.info("partCachingService.getNameToPartIdMap().size(): "+partCachingService.getNameToPartIdMap().size());
	}
	
	@Override
	public Component process(OldComponent item) throws Exception {
		Map<String, Long> nameToPartIdMap = partCachingService.getNameToPartIdMap();
		Long partId = nameToPartIdMap.get(item.getPartName());
		LOGGER.info("Looked up id {} for {}", partId, item.getPartName());
		Float hoursPerformed = item.getHoursPerformed() == 0 ? null : item.getHoursPerformed();
		Float hoursDue = item.getHoursDue() == 0 ? null : item.getHoursDue();
		return Component.builder().workPerformed(item.getActivityPerformed()).datePerformed(item.getDatePerformed())
				.hoursPerformed(hoursPerformed).dateDue(item.getDateDue()).hoursDue(hoursDue)
				.part(Part.builder().name(item.getPartName()).id(partId).build())
				.build();
	}
}

