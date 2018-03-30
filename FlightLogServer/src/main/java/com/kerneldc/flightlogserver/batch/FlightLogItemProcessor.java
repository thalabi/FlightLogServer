package com.kerneldc.flightlogserver.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

import com.kerneldc.flightlogserver.domain.FlightLog;

public class FlightLogItemProcessor implements ItemProcessor<FlightLog, FlightLog> {

	@Override
	public FlightLog process(FlightLog item) throws Exception {
		FlightLog result = new FlightLog();
		BeanUtils.copyProperties(/* source */item, /* destination */result);
		return result;
	}

}

