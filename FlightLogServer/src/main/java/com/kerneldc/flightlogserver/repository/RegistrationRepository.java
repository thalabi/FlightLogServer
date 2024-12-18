package com.kerneldc.flightlogserver.repository;

import java.util.List;

import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.IEntityEnum;
import com.kerneldc.flightlogserver.domain.registration.Registration;

public interface RegistrationRepository extends BaseTableRepository<Registration, Long> {
	List<Registration> findAllByOrderByRegistration();
	
	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.REGISTRATION;
	}

}
