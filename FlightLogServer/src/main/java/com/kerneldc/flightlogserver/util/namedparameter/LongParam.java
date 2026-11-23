package com.kerneldc.flightlogserver.util.namedparameter;

public record LongParam(String name, Long value) implements NamedParameter {

	@Override
	public Class<?> getType() {
		return Long.class;
	}

}
