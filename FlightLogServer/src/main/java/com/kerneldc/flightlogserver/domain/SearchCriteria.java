package com.kerneldc.flightlogserver.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter @Setter
public class SearchCriteria {

	private final @NonNull String key;
	private final @NonNull String operation;
	private final @NonNull Object value;
}
