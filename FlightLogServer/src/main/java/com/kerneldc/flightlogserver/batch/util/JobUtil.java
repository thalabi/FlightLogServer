package com.kerneldc.flightlogserver.batch.util;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.batch.JobEnum;

@Component
public class JobUtil {

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

    @Autowired
    @Qualifier("alterTableTriggers")
    private Job alterTableTriggersJob;

    @Autowired
    @Qualifier("copyAircraftMaintenanceTables")
    private Job copyAircraftMaintenanceTablesJob;

    public Job getJob(JobEnum jobEnum) {
    	switch (jobEnum) {
    		case COPY_FLIGHT_LOG_TABLE:
    			return copyFlightLogTableJob;
    		case COPY_MAKE_MODEL_TABLE:
    			return copyMakeModelTableJob;
    		case COPY_PILOT_TABLE:
    			return copyPilotTableJob;
    		case COPY_REGISTRATION_TABLE:
    			return copyRegistrationTableJob;
    		case COPY_SIGNIFICANT_EVENT_TABLE:
    			return copySignificantEventTableJob;
    		case COPY_AIRPORT_TABLE:
    			return copyAirportTableJob;
    		case DISABLE_FLIGHT_LOG_TRIGGERS:
    		case ENABLE_FLIGHT_LOG_TRIGGERS:
    		case DISABLE_MAKE_MODEL_TRIGGERS:
    		case ENABLE_MAKE_MODEL_TRIGGERS:
    		case DISABLE_SIGNIFICANT_EVENT_TRIGGERS:
    		case ENABLE_SIGNIFICANT_EVENT_TRIGGERS:
    			return alterTableTriggersJob;
    		case COPY_AIRCRAFT_MAINTENANCE_TABLES:
    			return copyAircraftMaintenanceTablesJob;
    	}
    	return null;
    }
}
