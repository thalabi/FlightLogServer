package com.kerneldc.flightlogserver.task;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.aircraftmaintenance.service.AircrafDetailsService;
import com.kerneldc.flightlogserver.aircraftmaintenance.service.AircrafDetailsService.Tsn;
import com.kerneldc.flightlogserver.aircraftmaintenance.service.AircrafDetailsService.TsnValueStateEnum;
import com.kerneldc.flightlogserver.aircraftmaintenance.service.UpcomingDueComponentService;
import com.kerneldc.flightlogserver.exception.ApplicationException;
import com.kerneldc.flightlogserver.util.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledTasks {

	private final AircrafDetailsService aircrafDetailsService;
	private final UpcomingDueComponentService upcomingDueComponentService;
	private final EmailService emailService;
	
	private static final String REGISTRATION = "C-GQGD";
	private static final long MONTHS_TO_ADD = 1L;
	private static final float HRS_TO_ADD = 50L;
	
	@Scheduled(cron = "2 0 0 * * *") // at 00:02 daily
//	@Scheduled(cron = "0 35 17 * * *") // for testing
	public void upcomingDueComponentsNotification() {
		Tsn tsn;
		try {
			tsn = aircrafDetailsService.getAircraftTsn(REGISTRATION);
		} catch (ApplicationException applicationException) {
			var description = "Error getting aircraft TSN";
			LOGGER.error(description, applicationException);
			emailService.sendFailureEmail(description, applicationException);
			return;
		}
		
		var airtime= tsn.value();
		if (tsn.valueStateEnum().equals(TsnValueStateEnum.MISSING)) {
			var description = "TSN is missing, can't produce Upcoming Due Components report";
			LOGGER.error(description);
			emailService.sendFailureEmail(description, new ApplicationException());
			return;
		}
		
		var toDueDate = Date.from(ZonedDateTime.now(ZoneId.systemDefault()).plusMonths(MONTHS_TO_ADD).toInstant());
		var toHrsDue = airtime + HRS_TO_ADD;
		upcomingDueComponentService.checkUpcomingDueComponentsNotification(toDueDate, toHrsDue);
	}

}
