package com.kerneldc.flightlogserver.batch.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JobExecutionBean {

	private String jobName;
	private String jobExitStatus;
	private List<StepExecutionBean> stepExecutionList;
	
	public JobExecutionBean() {
	}

	public JobExecutionBean(String jobName, String jobExitStatus, List<StepExecutionBean> stepExecutionList) {
		this.jobName = jobName;
		this.jobExitStatus = jobExitStatus;
		this.stepExecutionList = stepExecutionList;
	}
}
