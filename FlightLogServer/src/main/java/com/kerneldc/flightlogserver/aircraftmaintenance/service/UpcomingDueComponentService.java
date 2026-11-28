package com.kerneldc.flightlogserver.aircraftmaintenance.service;

import java.util.Date;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.ComponentRepository;
import com.kerneldc.flightlogserver.exception.ApplicationException;
import com.kerneldc.flightlogserver.util.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpcomingDueComponentService {

	private final EmailService emailService;
	private final ComponentRepository componentRepository;
	
	public Boolean checkUpcomingDueComponentsNotification(Date dateDue, Float hoursDue) {
		try {
			LOGGER.info("Checking for upcoming due components.");
			var upcomingDueComponents = componentRepository.findByDateDueLessThanOrHoursDueLessThanOrderByDateDueDescHoursDueDesc(dateDue, hoursDue);
			LOGGER.info("upcomingDueComponents.size() [{}]", CollectionUtils.size(upcomingDueComponents));
			for (Component upcomingDueComponent : upcomingDueComponents) {
				LOGGER.info("componentName [{}]  componentDescription [{}] dateDue [{}] HoursDue [{}]", upcomingDueComponent.getName(), upcomingDueComponent.getDescription(), upcomingDueComponent.getDateDue(), upcomingDueComponent.getHoursDue());	
			}
			if (CollectionUtils.sizeIsEmpty(upcomingDueComponents)) {
				LOGGER.info("No due components found.");
				return false;
			}
			emailService.emailUpcomingDueComponentsNotification(upcomingDueComponents, dateDue,  hoursDue);
			return true;
		} catch (ApplicationException applicationException) {
			LOGGER.error("checkUpcomingDueComponentsNotification failed", applicationException);
			emailService.emailUpcomingDueComponentsFailure(applicationException);
			return Boolean.FALSE;
		}
	}
}
