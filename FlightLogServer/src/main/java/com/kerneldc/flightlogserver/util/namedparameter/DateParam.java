package com.kerneldc.flightlogserver.util.namedparameter;

import java.util.Date;

public record DateParam(String name, Date value) implements NamedParameter {

	@Override
	public Class<?> getType() {
		return Date.class;
	}

}
