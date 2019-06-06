package com.kerneldc.flightlogserver.batch.bean;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class StepExecutionBean {

	private String stepName;
	private String stepExitStatus;
	private Integer readCount;
	private Integer readSkipCount;
	private Integer writeCount;
	private Integer writeSkipCount;
	private Date startTime;
	private Date endTime;
	private List<String> failureExceptionList;
}
