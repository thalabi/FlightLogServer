package com.kerneldc.flightlogserver.batch.copyAirportTable;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.batch.item.ItemProcessor;

import com.kerneldc.flightlogserver.domain.airport.Airport;

public class AirportItemProcessor implements ItemProcessor<Airport, Airport> {
	
	@Override
	public Airport process(Airport item) throws Exception {
		Airport result = new Airport();
		BeanUtils.copyProperties(/* destination */result, /* source */item);
		return result;
	}
}

