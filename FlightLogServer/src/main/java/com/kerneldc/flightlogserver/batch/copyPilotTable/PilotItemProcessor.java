package com.kerneldc.flightlogserver.batch.copyPilotTable;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.batch.item.ItemProcessor;

import com.kerneldc.flightlogserver.domain.pilot.Pilot;

public class PilotItemProcessor implements ItemProcessor<Pilot, Pilot> {

	@Override
	public Pilot process(Pilot item) throws Exception {
		Pilot result = new Pilot();
		BeanUtils.copyProperties(/* destination */result, /* source */item);
		return result;
	}
}

