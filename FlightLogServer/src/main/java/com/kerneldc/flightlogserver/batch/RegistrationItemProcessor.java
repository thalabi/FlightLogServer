package com.kerneldc.flightlogserver.batch;

import org.springframework.batch.item.ItemProcessor;

import com.kerneldc.flightlogserver.domain.Registration;

public class RegistrationItemProcessor implements ItemProcessor<Registration, Registration> {

	@Override
	public Registration process(Registration item) throws Exception {
		Registration result = new Registration();
		result.setId(item.getId());
		result.setRegistration(item.getRegistration());
		result.setCreated(item.getCreated());
		result.setModified(item.getModified());
		result.setVersion(item.getVersion());
		return result;
	}

}

