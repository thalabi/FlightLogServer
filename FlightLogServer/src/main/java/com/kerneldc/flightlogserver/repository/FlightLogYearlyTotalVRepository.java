package com.kerneldc.flightlogserver.repository;

import java.util.List;

import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.FlightLogYearlyTotalV;
import com.kerneldc.flightlogserver.domain.IEntityEnum;

public interface FlightLogYearlyTotalVRepository extends BaseViewRepository<FlightLogYearlyTotalV, Long> {
	List<FlightLogYearlyTotalV> findAllByOrderById();

	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.FLIGHT_LOG_YEARLY_TOTAL_V;
	}

}
