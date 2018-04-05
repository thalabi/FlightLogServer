package com.kerneldc.flightlogserver.batch;

import java.lang.invoke.MethodHandles;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.kerneldc.flightlogserver.domain.Registration;

public class RegistrationItemProcessor implements ItemProcessor<Registration, Registration> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private int count;
	
	@Override
	public Registration process(Registration item) throws Exception {
		Registration result = new Registration();
		BeanUtils.copyProperties(/* destination */result, /* source */item);
		count++;
		LOGGER.debug("count: {}", count);
		return result;
	}
}

