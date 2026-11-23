package com.kerneldc.flightlogserver.util.namedparameter;

public record FloatParam(String name, Float value) implements NamedParameter {

	@Override
	public Class<?> getType() {
		return Float.class;
	}

}
