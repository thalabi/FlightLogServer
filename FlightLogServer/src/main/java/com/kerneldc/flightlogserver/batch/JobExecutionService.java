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
import org.springframework.stereotype.Service;

import com.kerneldc.flightlogserver.batch.bean.JobExecutionBean;
import com.kerneldc.flightlogserver.batch.bean.StepExecutionBean;
import com.kerneldc.flightlogserver.batch.util.JobUtil;

@Service
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

		JobExecutionBean jobExecutionBean = new JobExecutionBean(jobExecution.getJobInstance().getJobName(), jobExecution.getExitStatus().getExitCode(), new ArrayList<>());
        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
        for (StepExecution stepExecution: stepExecutions) {
        	StepExecutionBean stepExecutionBean = new StepExecutionBean(stepExecution.getStepName(), stepExecution.getExitStatus().getExitCode(),
					stepExecution.getReadCount(), stepExecution.getReadSkipCount(),
					stepExecution.getWriteCount(), stepExecution.getWriteSkipCount(), new ArrayList<>());
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
