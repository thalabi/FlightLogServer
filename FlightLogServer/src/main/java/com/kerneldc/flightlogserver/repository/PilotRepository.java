package com.kerneldc.flightlogserver.repository;

import java.util.List;

import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.IEntityEnum;
import com.kerneldc.flightlogserver.domain.pilot.Pilot;

public interface PilotRepository extends BaseTableRepository<Pilot, Long> {
	List<Pilot> findAllByOrderByPilot();
	
	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.PILOT;
	}

}
