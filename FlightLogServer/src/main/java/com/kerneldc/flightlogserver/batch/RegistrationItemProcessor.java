package com.kerneldc.flightlogserver.batch;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.batch.item.ItemProcessor;

import com.kerneldc.flightlogserver.domain.Registration;

public class RegistrationItemProcessor implements ItemProcessor<Registration, Registration> {

	@Override
	public Registration process(Registration item) throws Exception {
		Registration result = new Registration();
		BeanUtils.copyProperties(/* destination */result, /* source */item);
		return result;
	}

}

