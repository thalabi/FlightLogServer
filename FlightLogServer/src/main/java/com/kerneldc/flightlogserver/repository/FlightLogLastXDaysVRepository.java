package com.kerneldc.flightlogserver.repository;

import java.util.List;

import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.FlightLogLastXDaysTotalV;
import com.kerneldc.flightlogserver.domain.IEntityEnum;

public interface FlightLogLastXDaysVRepository extends BaseViewRepository<FlightLogLastXDaysTotalV, Long> {
	List<FlightLogLastXDaysTotalV> findAllByOrderById();

	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.FLIGHT_LOG_LAST_X_DAYS_TOTAL_V;
	}
}
