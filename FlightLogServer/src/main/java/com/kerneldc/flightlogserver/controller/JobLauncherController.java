package com.kerneldc.flightlogserver.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.batch.core.Job;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.batch.bean.JobExecutionBean;
import com.kerneldc.flightlogserver.batch.bean.StepExecutionBean;

@RestController
@RequestMapping("jobLauncherController")
public class JobLauncherController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("copyFlightLogTable")
    private Job copyFlightLogTableJob;

    @Autowired
    @Lazy
    @Qualifier("copyMakeModelTable")
    private Job copyMakeModelTableJob;

    @Autowired
    @Lazy
    @Qualifier("copyPilotTable")
    private Job copyPilotTableJob;

    @Autowired
    @Lazy
    @Qualifier("copyRegistrationTable")
    private Job copyRegistrationTableJob;

    @Autowired
    @Qualifier("copySignificantEventTable")
    private Job copySignificantEventTableJob;

    @Autowired
    @Qualifier("copyAirportTable")
    private Job copyAirportTableJob;

    @GetMapping("/copyFlightLogTable")
    public JobExecutionBean copyFlightLogTable() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return runJob(copyFlightLogTableJob);
    }

    @GetMapping("/copyMakeModelTable")
    public JobExecutionBean copyMakeModelTable() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return runJob(copyMakeModelTableJob);
    }
    
    @GetMapping("/copyPilotTable")
    public JobExecutionBean copyPilotTable() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return runJob(copyPilotTableJob);
    }
    
    @GetMapping("/copyRegistrationTable")
    public JobExecutionBean copyRegistrationTable() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return runJob(copyRegistrationTableJob);
    }
    
    @GetMapping("/copySignificantEventTable")
    public JobExecutionBean copySignificantEventTable() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return runJob(copySignificantEventTableJob);
    }
    
    @GetMapping("/copyAirportTable")
    public JobExecutionBean copyAirportTable() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return runJob(copyAirportTableJob);
    }
    
    private JobExecutionBean runJob(Job job) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
    	JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
    	JobParameters jobParameters = jobParametersBuilder.addLong("sysTime", new Date().getTime()).toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
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
