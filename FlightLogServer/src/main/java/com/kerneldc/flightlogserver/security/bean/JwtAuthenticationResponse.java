package com.kerneldc.flightlogserver.security.bean;

import com.kerneldc.flightlogserver.security.constants.SecurityConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType = SecurityConstants.AUTH_HEADER_SCHEMA;

	public JwtAuthenticationResponse(String accessToken) {
		this.accessToken = accessToken;
	}

}
