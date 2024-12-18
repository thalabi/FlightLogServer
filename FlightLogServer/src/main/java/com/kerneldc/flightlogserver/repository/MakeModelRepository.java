package com.kerneldc.flightlogserver.repository;

import java.util.List;

import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.IEntityEnum;
import com.kerneldc.flightlogserver.domain.makeModel.MakeModel;

public interface MakeModelRepository extends BaseTableRepository<MakeModel, Long> {
	List<MakeModel> findAllByOrderByMakeModel();
	
	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.MAKE_MODEL;
	}

}
