package com.kerneldc.flightlogserver.service.http;

import lombok.Getter;

public enum HttpRequestTypeEnum {
	FLS_JWT_TOKEN("Fls OAuth2 JWT Access Token Service"),
	AIRCRAFT_DETAILS("Aircraft Details Service");
	
	@Getter
	String serviceDescription;
	
	HttpRequestTypeEnum(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

}