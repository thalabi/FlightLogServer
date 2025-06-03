package com.kerneldc.flightlogserver.repository;

import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.IEntityEnum;
import com.kerneldc.flightlogserver.domain.flightlogpending.FlightLogPending;

public interface FlightLogPendingRepository extends BaseTableRepository<FlightLogPending, Long> {

	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.FLIGHT_LOG_PENDING;
	}

}
