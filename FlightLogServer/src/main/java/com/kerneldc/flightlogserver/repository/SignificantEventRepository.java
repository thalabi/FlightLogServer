package com.kerneldc.flightlogserver.repository;

import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.IEntityEnum;
import com.kerneldc.flightlogserver.domain.significantEvent.SignificantEvent;

public interface SignificantEventRepository extends BaseTableRepository<SignificantEvent, Long> {
//	List<SignificantEvent> findAllByOrderByEventDate();
	
	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.SIGNIFICANT_EVENT;
	}

}
