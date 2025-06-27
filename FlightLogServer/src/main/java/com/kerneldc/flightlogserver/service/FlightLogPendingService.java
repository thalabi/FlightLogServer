package com.kerneldc.flightlogserver.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import com.kerneldc.flightlogserver.domain.flightLog.FlightLog;
import com.kerneldc.flightlogserver.repository.FlightLogPendingRepository;
import com.kerneldc.flightlogserver.repository.FlightLogRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightLogPendingService {

	private static final UriTemplate FLIGHT_LOG_PENDING_URI_TEMPLATE = new UriTemplate("{protocol}://{host}:{port}/protected/data-rest/flightLogPendings/{id}");
	
	private final FlightLogPendingRepository flightLogPendingRepository;
	private final FlightLogRepository flightLogRepository;

	public record FlightLogPendingAddRequest (
			String flightLogPendingUri,
			FlightLog flightLog
			) {}
	
	@Transactional
	public void addFlightLog(FlightLogPendingAddRequest flightLogPendingAddRequest) {
		LOGGER.info("flightLogPendingAddRequest [{}]", flightLogPendingAddRequest);
		var flightLogPendingId = parseId(flightLogPendingAddRequest.flightLogPendingUri(), FLIGHT_LOG_PENDING_URI_TEMPLATE);
		LOGGER.info("flightLogPendingId [{}]", flightLogPendingId);
		flightLogRepository.save(flightLogPendingAddRequest.flightLog());
		flightLogPendingRepository.deleteById(flightLogPendingId);
	}
	
    private Long parseId(String uri, UriTemplate uriTemplate) {
    	Map<String, String> parameterMap = uriTemplate.match(uri);
    	return Long.parseLong(parameterMap.get("id"));
    }


}
