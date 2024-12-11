package com.kerneldc.flightlogserver.repository;

import java.util.List;

import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.FlightLogMonthlyTotalV;
import com.kerneldc.flightlogserver.domain.IEntityEnum;

public interface FlightLogMonthlyTotalVRepository extends BaseViewRepository<FlightLogMonthlyTotalV, Long> {
	List<FlightLogMonthlyTotalV> findAllByOrderById();

	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.FLIGHT_LOG_MONTHLY_TOTAL_V;
	}
}
