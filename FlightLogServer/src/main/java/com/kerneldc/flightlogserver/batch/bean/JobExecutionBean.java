package com.kerneldc.flightlogserver.batch.bean;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class JobExecutionBean {

	private String jobName;
	private String jobExitStatus;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private List<StepExecutionBean> stepExecutionList;
}
