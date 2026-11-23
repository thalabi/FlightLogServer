package com.kerneldc.flightlogserver.aircraftmaintenance.service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kerneldc.flightlogserver.domain.remoteapicall.RemoteApiCall;
import com.kerneldc.flightlogserver.domain.remoteapicalllog.RemoteApiCallLog.RetryStatusEnum;
import com.kerneldc.flightlogserver.exception.ApplicationException;
import com.kerneldc.flightlogserver.repository.RemoteApiCallLogRepository;
import com.kerneldc.flightlogserver.repository.RemoteApiCallRepository;
import com.kerneldc.flightlogserver.service.http.AbstractRemoteApiCallBase;
import com.kerneldc.flightlogserver.service.http.HttpRequestTypeEnum;
import com.kerneldc.flightlogserver.service.http.HttpService;
import com.kerneldc.flightlogserver.service.http.JwtTokenService;
import com.kerneldc.flightlogserver.util.EmailService;
import com.kerneldc.flightlogserver.util.namedparameter.NamedParameter;
import com.kerneldc.flightlogserver.util.namedparameter.StringParam;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AircrafDetailsService extends AbstractRemoteApiCallBase {
	
	private final RemoteApiCallRepository remoteApiCallRepository;
	private final ObjectMapper objectMapper;
	
	public AircrafDetailsService(RemoteApiCallRepository remoteApiCallRepository, RemoteApiCallLogRepository remoteApiCallLogRepository, JwtTokenService jwtTokenService,
			HttpService httpService, EmailService emailService, ObjectMapper objectMapper) {
		super(remoteApiCallLogRepository, jwtTokenService, httpService, emailService);
		this.remoteApiCallRepository = remoteApiCallRepository;
		this.objectMapper = objectMapper;
	}

	private static final String REGISTRATION = "registration";
	public record AircraftDetails (String registration, Float tsn) {}
	public record Tsn (Float value, TsnValueStateEnum valueStateEnum) {}
	public enum TsnValueStateEnum {CURRENT, STALE, MISSING}
	
	private Map<String, Float> tsnCache = new ConcurrentHashMap<>();
	
	public Tsn getAircraftTsn(String registration) throws ApplicationException {
		var now = OffsetDateTime.now();

		var remoteApiCall = new RemoteApiCall();
		var payload = Map.of(REGISTRATION, registration);
		try {
			remoteApiCall.setParameters(objectMapper.writeValueAsString(payload));
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage());
			throw new ApplicationException("ObjectMapper writeValueAsString() method failure", e);
		}
		remoteApiCall.setRequest(HttpRequestTypeEnum.FLS_JWT_TOKEN);
		remoteApiCall.setTimestamp(now);
		remoteApiCallRepository.save(remoteApiCall);

		try {
			
			var jwt = jwtTokenService.getJwtToken();
			
			Set<NamedParameter> namedParameterSet =	Set.of(new StringParam(REGISTRATION, registration));
	
			var returnParams = httpService.processRequest(HttpRequestTypeEnum.AIRCRAFT_DETAILS, namedParameterSet, jwt);
			writeLog(remoteApiCall, 1, RetryStatusEnum.SUCCESS, null, 0);
			
			Float tsn = returnParams.get("tsn", Float.class);

			tsnCache.put(registration, tsn);
			return new Tsn(tsn, TsnValueStateEnum.CURRENT);
			
		} catch (ApplicationException applicationException) {
			
			writeLog(remoteApiCall, 1, RetryStatusEnum.FAILED, applicationException, 0);
			LOGGER.error(applicationException.getMessage());
			// send email on failure
			LOGGER.info("Sending failure email");
			emailService.sendRemoteApiFailureEmail(HttpRequestTypeEnum.AIRCRAFT_DETAILS, applicationException);
			
			if (tsnCache.containsKey(registration)) {
				return new Tsn(tsnCache.get(registration), TsnValueStateEnum.STALE);
			} else {
				return new Tsn(tsnCache.get(registration), TsnValueStateEnum.MISSING);
			}
			
		}
	}
	
	@PreDestroy
	public void terminate() {
		tsnCache.clear();
	}

}
