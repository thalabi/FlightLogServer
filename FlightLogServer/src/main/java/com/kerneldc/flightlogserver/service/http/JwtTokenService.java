package com.kerneldc.flightlogserver.service.http;

import java.time.LocalDateTime;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kerneldc.flightlogserver.exception.ApplicationException;
import com.kerneldc.flightlogserver.util.namedparameter.StringParam;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtTokenService {

	private final String flsOauth2ClientId;
	private final String flsOauth2ClientSecret;
	
	private final HttpService httpService;
	
	private String jwt;
	private LocalDateTime jwtExpirationTime;

	public JwtTokenService(
			@Value("${fls.oauth2.client.id}") String flsOauth2ClientId,
			@Value("${fls.oauth2.client.secret}") String flsOauth2ClientSecret,
			HttpService httpService) {
		this.flsOauth2ClientId = flsOauth2ClientId;
		this.flsOauth2ClientSecret = flsOauth2ClientSecret;
		this.httpService = httpService;
	}

	public String getJwtToken() throws ApplicationException {
		if (StringUtils.isEmpty(jwt) || LocalDateTime.now().isAfter(jwtExpirationTime)) {
			LOGGER.info("No JWT token or it is expired, requesting one");
			fetchJwtAccessToken();
		} else {
			LOGGER.info("JWT token is still valid");
		}
		return jwt;
	}

	private void fetchJwtAccessToken() throws ApplicationException {
		var jwtTokenRequestTime = LocalDateTime.now();
		var returnParams = httpService.processRequest(HttpRequestTypeEnum.FLS_JWT_TOKEN,
				Set.of(new StringParam("oauth2clientId", flsOauth2ClientId),
						new StringParam("oauth2ClientSecret", flsOauth2ClientSecret)));
		jwt = returnParams.get("jwt", String.class);
		var expiresIn = returnParams.get("expiresIn", Long.class);
		LOGGER.debug("JWT token [{}]", jwt);
		jwtExpirationTime = jwtTokenRequestTime.plusSeconds(expiresIn);
		LOGGER.info("JWT token received and expires in [{}] seconds, at [{}]", expiresIn, jwtExpirationTime);
	}

}
