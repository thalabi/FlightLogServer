package com.kerneldc.flightlogserver.service.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kerneldc.flightlogserver.exception.ApplicationException;
import com.kerneldc.flightlogserver.util.namedparameter.FloatParam;
import com.kerneldc.flightlogserver.util.namedparameter.LongParam;
import com.kerneldc.flightlogserver.util.namedparameter.NamedParameter;
import com.kerneldc.flightlogserver.util.namedparameter.NamedParameterSet;
import com.kerneldc.flightlogserver.util.namedparameter.StringParam;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HttpService {

	private final boolean urlLoggingEnabled;
	private final String flsOauth2ServerUrl;
	private final String aircraftServiceApiUrl;

	public HttpService(@Value("${httpservice.url.logging.enabled:false}") boolean urlLoggingEnabled,
			@Value("${fls.oauth2.server.url}") String flsOauth2ServerUrl,
			@Value("${aircraft.service.url}") String aircraftServiceApiUrl) {
		this.urlLoggingEnabled = urlLoggingEnabled;
		this.flsOauth2ServerUrl = flsOauth2ServerUrl;
		this.aircraftServiceApiUrl = aircraftServiceApiUrl;
	}

	public NamedParameterSet processRequest(HttpRequestTypeEnum httpRequestTypeEnum, Set<NamedParameter> data) throws ApplicationException {
		return processRequest(httpRequestTypeEnum, data, StringUtils.EMPTY);
	}
	public NamedParameterSet processRequest(HttpRequestTypeEnum httpRequestTypeEnum, String jwt) throws ApplicationException {
		return processRequest(httpRequestTypeEnum, Set.of(), jwt);
	}
	
	public NamedParameterSet processRequest(HttpRequestTypeEnum httpRequestTypeEnum, Set<NamedParameter> data, String jwt) throws ApplicationException {
		var parameterSet = new NamedParameterSet(data);
		switch (httpRequestTypeEnum) {
			case FLS_JWT_TOKEN -> {
				return fetchJwtAccessToken(parameterSet);
			}
			case AIRCRAFT_DETAILS -> {
				return getAircraftDetails(parameterSet, jwt);
			}
		}
		return null;
	}
	
	private NamedParameterSet getAircraftDetails(NamedParameterSet parameterSet, String jwt) throws ApplicationException  {
		
		var registration = parameterSet.get("registration", String.class);
		var encodedRegistration = URLEncoder.encode(registration, StandardCharsets.UTF_8);
		var fullUrlWithParams = aircraftServiceApiUrl + "?registration=" + encodedRegistration;

		if (urlLoggingEnabled) LOGGER.info("Hitting url: [{}]", fullUrlWithParams);
		
		var client = HttpClient.newHttpClient();
		var httpRequest = HttpRequest.newBuilder()
		    .uri(URI.create(fullUrlWithParams))
		    .GET()
			.header("Authorization", "Bearer " + jwt)
			.build();

		HttpResponse<String> response = null;
		try {
			response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			if (e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			var message = String.format("Fetching contents of URL [%s] failed. ", aircraftServiceApiUrl);
			throw new ApplicationException(message, e);
		}
		var httpStatusCode = response.statusCode();
		if (httpStatusCode != HttpURLConnection.HTTP_OK) {
			var message = String.format("Fetching contents of URL [%s], returned Http status code [%d]. ",
					aircraftServiceApiUrl, httpStatusCode);
			if (response != null && StringUtils.isNotEmpty(response.body())) {
				message += String.format("Site message: [%s]", response.body());
			}
			throw new ApplicationException(message);
		}

		var jsonResponse = response.body();
		var mapper = new ObjectMapper();
		JsonNode root;
		try {
			root = mapper.readTree(jsonResponse);
		} catch (JsonProcessingException e) {
			var message = String.format("Exception when parsing json response: %s", jsonResponse);
			LOGGER.error(message, e);
			throw new ApplicationException(message);
		}
		LOGGER.debug(root.toPrettyString());
		var returnedRegistration = root.path("registration").asText();
		var tsn = (float)root.path("tsn").asDouble();
		
		return new NamedParameterSet(Set.of(new StringParam("registration", returnedRegistration), new FloatParam("tsn", tsn)));
	}

	private NamedParameterSet fetchJwtAccessToken(NamedParameterSet parameters) throws ApplicationException {
		
		if (urlLoggingEnabled) LOGGER.info("Hitting url: [{}]", flsOauth2ServerUrl);
		
		var oauth2ClientId = parameters.get("oauth2clientId", String.class);
		var oauth2ClientSecret = parameters.get("oauth2ClientSecret", String.class);
		
		var client = HttpClient.newHttpClient();
		var request = HttpRequest.newBuilder()
		    .uri(URI.create(flsOauth2ServerUrl))
		    .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString())
			.POST(BodyPublishers.ofString("client_id=" + oauth2ClientId + "&client_secret=" + oauth2ClientSecret
					+ "&grant_type=client_credentials"))
		    .build();

		HttpResponse<String> response = null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			if (e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			var message = String.format("Fetching contents of URL [%s] failed. ", flsOauth2ServerUrl);
			throw new ApplicationException(message, e);
		}
		var httpStatusCode = response.statusCode();
		if (httpStatusCode != HttpURLConnection.HTTP_OK) {
			var message = String.format("Fetching contents of URL [%s], returned Http status code [%d]. ",
					flsOauth2ServerUrl, httpStatusCode);
			if (response != null && StringUtils.isNotEmpty(response.body())) {
				message += String.format("Site message: [%s]", response.body());
			}
			throw new ApplicationException(message);
		}
		var jsonResponse = response.body();
		var mapper = new ObjectMapper();
		JsonNode root;
		try {
			root = mapper.readTree(jsonResponse);
		} catch (JsonProcessingException e) {
			var message = String.format("Exception when parsing json response: %s", jsonResponse);
			LOGGER.error(message, e);
			throw new ApplicationException(message);
		}
		LOGGER.debug(root.toPrettyString());
		
		var jwt = root.path("access_token").asText();
		var expiresIn = root.path("expires_in").asLong();
		return new NamedParameterSet(Set.of(new StringParam("jwt", jwt), new LongParam("expiresIn", expiresIn)));
	}

}
