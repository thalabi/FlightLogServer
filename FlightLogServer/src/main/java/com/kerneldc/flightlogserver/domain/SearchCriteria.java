package com.kerneldc.flightlogserver.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter @Setter
public class SearchCriteria implements Serializable {

	private static final long serialVersionUID = 1L;

	private final @NonNull String key;
	private final @NonNull String operation;
	private final @NonNull Object value;
}
