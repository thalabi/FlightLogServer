package com.kerneldc.flightlogserver.batch;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

import com.kerneldc.flightlogserver.domain.FlightLog;

public class FlightLogItemProcessor implements ItemProcessor<FlightLog, FlightLog> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private int count = 0;
	
	@Override
	public FlightLog process(FlightLog item) throws Exception {
		count++;
		FlightLog result = new FlightLog();
		BeanUtils.copyProperties(/* source */item, /* destination */result);
		if (count % 100 == 0) LOGGER.debug("count: {}: ", count);
		return result;
	}

}

