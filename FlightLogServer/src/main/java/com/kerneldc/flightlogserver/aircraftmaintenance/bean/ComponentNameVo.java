package com.kerneldc.flightlogserver.aircraftmaintenance.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor  @AllArgsConstructor
@Getter @Setter
@Builder
@ToString
public class ComponentNameVo {

	private String name;
}
