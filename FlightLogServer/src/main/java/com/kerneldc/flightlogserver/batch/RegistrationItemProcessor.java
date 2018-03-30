package com.kerneldc.flightlogserver.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

import com.kerneldc.flightlogserver.domain.Registration;

public class RegistrationItemProcessor implements ItemProcessor<Registration, Registration> {

	@Override
	public Registration process(Registration item) throws Exception {
		Registration result = new Registration();
		BeanUtils.copyProperties(/* source */item, /* destination */result);
		return result;
	}

}

