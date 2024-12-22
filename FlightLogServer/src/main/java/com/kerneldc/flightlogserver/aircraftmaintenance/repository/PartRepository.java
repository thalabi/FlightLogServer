package com.kerneldc.flightlogserver.aircraftmaintenance.repository;

import java.util.List;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.IEntityEnum;
import com.kerneldc.flightlogserver.repository.BaseTableRepository;

public interface PartRepository extends BaseTableRepository<Part, Long> {

	List<Part> findAllByOrderByName();

	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.PART;
	}

}
