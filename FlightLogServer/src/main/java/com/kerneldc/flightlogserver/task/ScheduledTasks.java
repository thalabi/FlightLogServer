package com.kerneldc.flightlogserver.task;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.aircraftmaintenance.service.UpcomingDueComponentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledTasks {

	private final UpcomingDueComponentService upcomingDueComponentService;

	@Scheduled(cron = "2 0 0 * * *") // at 00:02 daily
//	@Scheduled(cron = "0 2 15 * * *") // for testing
	public void upcomingDueComponentsNotification() {
		var airtime = 5039.2F; // TODO get
		
		var toDueDate = Date.from(Instant.now().plus(20, ChronoUnit.DAYS));
		var toHrsDue = airtime + 20;
		upcomingDueComponentService.checkUpcomingDueComponentsNotification(toDueDate, toHrsDue);
	}

}
