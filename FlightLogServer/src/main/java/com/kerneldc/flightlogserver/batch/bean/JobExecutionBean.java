package com.kerneldc.flightlogserver.batch.bean;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class JobExecutionBean {

	private String jobName;
	private String jobExitStatus;
	private Date startTime;
	private Date endTime;
	private List<StepExecutionBean> stepExecutionList;
}
