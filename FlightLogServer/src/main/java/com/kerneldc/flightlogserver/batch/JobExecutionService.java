package com.kerneldc.flightlogserver.batch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

import com.kerneldc.flightlogserver.batch.bean.JobExecutionBean;
import com.kerneldc.flightlogserver.batch.bean.StepExecutionBean;
import com.kerneldc.flightlogserver.batch.util.JobUtil;

//@Service
public class JobExecutionService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobUtil jobUtil;
    
    public JobExecutionBean runJob(JobEnum jobEnum) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
    	JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
    	JobParameters jobParameters = jobParametersBuilder.addLong("sysTime", new Date().getTime()).toJobParameters();
    	
    	if (jobEnum.getJobTypeEnum().equals(JobTypeEnum.TRIGGER_CHANGE_STATUS)) {
    		jobParameters = new JobParametersBuilder(jobParameters)
    				.addString("tableName", jobEnum.getTableName())
    				.addString("triggerStatus", String.valueOf((boolean)jobEnum.getJobConfigMap().get("triggerStatus")))
    				.toJobParameters();
    	}
        JobExecution jobExecution = jobLauncher.run(jobUtil.getJob(jobEnum), jobParameters);
        return getJobExecutionBean(jobExecution);
    }

	private JobExecutionBean getJobExecutionBean(JobExecution jobExecution) {

		JobExecutionBean jobExecutionBean = JobExecutionBean.builder()
				.jobName(jobExecution.getJobInstance().getJobName())
				.jobExitStatus(jobExecution.getExitStatus().getExitCode()).stepExecutionList(new ArrayList<>())
				.startTime(jobExecution.getStartTime()).endTime(jobExecution.getEndTime()).build();
        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
        for (StepExecution stepExecution: stepExecutions) {
			StepExecutionBean stepExecutionBean = StepExecutionBean.builder().stepName(stepExecution.getStepName())
					.stepExitStatus(stepExecution.getExitStatus().getExitCode()).readCount((int) stepExecution.getReadCount())
					.readSkipCount((int) stepExecution.getReadSkipCount()).writeCount((int) stepExecution.getWriteCount())
					.writeSkipCount((int) stepExecution.getWriteSkipCount()).startTime(stepExecution.getStartTime())
					.endTime(stepExecution.getStartTime()).failureExceptionList(new ArrayList<>()).build();
        	stepExecution.getStartTime();
//        	if (! /* not */ stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
        		List<String> failureExceptionList = stepExecutionBean.getFailureExceptionList();
        		for (Throwable throwable: stepExecution.getFailureExceptions()) {
        			failureExceptionList.add(ExceptionUtils.getStackTrace(throwable));
        		}
//        	}
        	jobExecutionBean.getStepExecutionList().add(stepExecutionBean);
        }
		return jobExecutionBean;
	}

}
