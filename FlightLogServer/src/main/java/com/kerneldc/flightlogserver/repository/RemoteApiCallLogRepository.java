package com.kerneldc.flightlogserver.repository;

import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.IEntityEnum;
import com.kerneldc.flightlogserver.domain.remoteapicalllog.RemoteApiCallLog;

public interface RemoteApiCallLogRepository extends BaseTableRepository<RemoteApiCallLog, Long>{

	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.REMOTE_API_CALL_LOG;
	}

}
