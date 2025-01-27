package com.kerneldc.flightlogserver.batch.copyAircraftMaintenanceTables;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

import lombok.extern.slf4j.Slf4j;

//@Component
@Slf4j
public class ComponentItemListener extends StepExecutionListenerSupport {

	private PartCachingService partCachingService;

	public ComponentItemListener(PartCachingService partCachingService) {
		this.partCachingService = partCachingService;
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		LOGGER.info("Initializing parts cache");
		partCachingService.getNameToPartIdMap();
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("Evicting parts cache");
		partCachingService.clearCache();
		return null;
	}

}
