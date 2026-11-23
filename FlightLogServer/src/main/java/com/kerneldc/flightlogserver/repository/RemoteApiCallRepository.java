package com.kerneldc.flightlogserver.repository;

import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.IEntityEnum;
import com.kerneldc.flightlogserver.domain.remoteapicall.RemoteApiCall;

public interface RemoteApiCallRepository extends BaseTableRepository<RemoteApiCall, Long>{

	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.REMOTE_API_CALL;
	}

}
