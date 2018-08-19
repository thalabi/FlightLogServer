package com.kerneldc.flightlogserver.batch.copySignificantEventTable;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.batch.item.ItemProcessor;

import com.kerneldc.flightlogserver.domain.significantEvent.SignificantEvent;

public class SignificantEventItemProcessor implements ItemProcessor<SignificantEvent, SignificantEvent> {
	
	@Override
	public SignificantEvent process(SignificantEvent item) throws Exception {
		SignificantEvent result = new SignificantEvent();
		BeanUtils.copyProperties(/* destination */result, /* source */item);
		return result;
	}
}

