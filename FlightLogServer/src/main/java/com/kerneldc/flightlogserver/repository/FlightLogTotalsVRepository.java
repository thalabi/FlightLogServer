package com.kerneldc.flightlogserver.repository;

import java.util.List;

import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.FlightLogTotalsV;
import com.kerneldc.flightlogserver.domain.IEntityEnum;

public interface FlightLogTotalsVRepository extends BaseViewRepository<FlightLogTotalsV, Long> {
	List<FlightLogTotalsV> findAllByOrderById();
	
	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.FLIGHT_LOG_TOTALS_V;
	}

}
