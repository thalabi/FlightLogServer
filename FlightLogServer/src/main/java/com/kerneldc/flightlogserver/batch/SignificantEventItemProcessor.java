package com.kerneldc.flightlogserver.batch;

import java.lang.invoke.MethodHandles;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.kerneldc.flightlogserver.domain.SignificantEvent;

public class SignificantEventItemProcessor implements ItemProcessor<SignificantEvent, SignificantEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private int count;
	
	@Override
	public SignificantEvent process(SignificantEvent item) throws Exception {
		SignificantEvent result = new SignificantEvent();
		BeanUtils.copyProperties(/* destination */result, /* source */item);
		count++;
		LOGGER.debug("count: {}", count);
		return result;
	}
}

