package com.kerneldc.flightlogserver.controller;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.batch.JobEnum;
import com.kerneldc.flightlogserver.batch.JobExecutionService;
import com.kerneldc.flightlogserver.batch.bean.JobExecutionBean;

@RestController
@RequestMapping("/protected/jobLauncherController")
public class JobLauncherController {

	@Autowired
	private JobExecutionService jobExecutionService;

    @GetMapping("/copyFlightLogTable")
    public JobExecutionBean copyFlightLogTable() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return jobExecutionService.runJob(JobEnum.COPY_FLIGHT_LOG_TABLE);
    }

    @GetMapping("/copyMakeModelTable")
    public JobExecutionBean copyMakeModelTable() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return jobExecutionService.runJob(JobEnum.COPY_MAKE_MODEL_TABLE);
    }
    
    @GetMapping("/copyPilotTable")
    public JobExecutionBean copyPilotTable() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return jobExecutionService.runJob(JobEnum.COPY_PILOT_TABLE);
    }
    
    @GetMapping("/copyRegistrationTable")
    public JobExecutionBean copyRegistrationTable() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return jobExecutionService.runJob(JobEnum.COPY_REGISTRATION_TABLE);
    }
    
    @GetMapping("/copySignificantEventTable")
    public JobExecutionBean copySignificantEventTable() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return jobExecutionService.runJob(JobEnum.COPY_SIGNIFICANT_EVENT_TABLE);
    }
    
    @GetMapping("/copyAirportTable")
    public JobExecutionBean copyAirportTable() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return jobExecutionService.runJob(JobEnum.COPY_AIRPORT_TABLE);
    }

    @GetMapping("/disableFlightLogTriggers")
    public JobExecutionBean disableFlightLogTriggers() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return jobExecutionService.runJob(JobEnum.DISABLE_FLIGHT_LOG_TRIGGERS);
    }
    
    @GetMapping("/enableFlightLogTriggers")
    public JobExecutionBean enableFlightLogTriggers() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return jobExecutionService.runJob(JobEnum.ENABLE_FLIGHT_LOG_TRIGGERS);
    }
    
    @GetMapping("/disableMakeModelTriggers")
    public JobExecutionBean disableMakeModelTriggers() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return jobExecutionService.runJob(JobEnum.DISABLE_MAKE_MODEL_TRIGGERS);
    }
    
    @GetMapping("/enableMakeModelTriggers")
    public JobExecutionBean enableMakeModelTriggers() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return jobExecutionService.runJob(JobEnum.ENABLE_MAKE_MODEL_TRIGGERS);
    }
    
    @GetMapping("/disableSignificantEventTriggers")
    public JobExecutionBean disableSignificantEventTriggers() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return jobExecutionService.runJob(JobEnum.DISABLE_SIGNIFICANT_EVENT_TRIGGERS);
    }
    
    @GetMapping("/enableSignificantEventTriggers")
    public JobExecutionBean enableSignificantEventTriggers() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return jobExecutionService.runJob(JobEnum.ENABLE_SIGNIFICANT_EVENT_TRIGGERS);
    }
    
    @GetMapping("/copyAircraftMaintenanceTables")
    public JobExecutionBean copyAircraftMaintenanceTables() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        return jobExecutionService.runJob(JobEnum.COPY_AIRCRAFT_MAINTENANCE_TABLES);
    }
    
}
