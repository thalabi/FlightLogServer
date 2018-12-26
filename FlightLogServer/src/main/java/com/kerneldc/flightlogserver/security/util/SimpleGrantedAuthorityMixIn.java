package com.kerneldc.flightlogserver.security.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityMixIn {

	public SimpleGrantedAuthorityMixIn(@JsonProperty("authority") String role) {
	}

}
