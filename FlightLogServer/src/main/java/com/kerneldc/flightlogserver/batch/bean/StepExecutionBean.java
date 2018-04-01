package com.kerneldc.flightlogserver.batch.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StepExecutionBean {

	private String jobName;
	private String jobExitStatus;
	private Integer readCount;
	private Integer readSkipCount;
	private Integer writeCount;
	private Integer writeSkipCount;
	private List<String> failureExceptionList;
	
	public StepExecutionBean() {
	}
	
	public StepExecutionBean(String jobName, String jobExitStatus, Integer readCount, Integer readSkipCount,
			Integer writeCount, Integer writeSkipCount, List<String> failureExceptionList) {
		this.jobName = jobName;
		this.jobExitStatus = jobExitStatus;
		this.readCount = readCount;
		this.readSkipCount = readSkipCount;
		this.writeCount = writeCount;
		this.writeSkipCount = writeSkipCount;
		this.failureExceptionList = failureExceptionList;
	}

}
