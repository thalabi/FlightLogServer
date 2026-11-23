package com.kerneldc.flightlogserver.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.exception.ApplicationException;
import com.kerneldc.flightlogserver.service.http.HttpRequestTypeEnum;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender javaMailSender;
	private final Configuration freeMarkerConfiguration;
	
	@Value("${application.email.upcomingDueComponentsNotificationFrom}")
	private String upcomingDueComponentsNotificationFrom;
	@Value("${application.email.upcomingDueComponentsNotificationTo}")
	private String upcomingDueComponentsNotificationTo;
	
	@Value("${application.email.failureNotificationFrom}")
	private String failureNotificationFrom;
	@Value("${application.email.failureNotificationTo}")
	private String failureNotificationTo;

	
	private static final String UPCOMING_DUE_COMPONENTS_NOTIFICATION_SUBJECT = "Upcoming Due Component(s)";
	private static final String UPCOMING_DUE_COMPONENTS_NOTIFICATION_TEMPLATE = "upcomingDueComponentsNotification.ftlh";
	
	private static final String FAILURE_TEMPLATE = "processingFailure.ftlh";
	private static final String RERMOTE_API_FAILURE_SUBJECT = "Remote API Failure";

	
//	public EmailService(JavaMailSender emailSender, Configuration freeMarkerConfiguration) {
//		this.javaMailSender = emailSender;
//		this.freeMarkerConfiguration = freeMarkerConfiguration;
//	}
	
	public void emailUpcomingDueComponentsNotification(List<Component> upcominDueComponents, Date dateDue, Float hoursDue) throws ApplicationException {
		LOGGER.info("Sending upcoming due components notification email");
		var mimeMessage = javaMailSender.createMimeMessage();
		var mimeMessageHelper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
		try {
			mimeMessageHelper.setFrom(upcomingDueComponentsNotificationFrom);
			mimeMessageHelper.setTo(InternetAddress.parse(upcomingDueComponentsNotificationTo));
			mimeMessageHelper.setSubject(UPCOMING_DUE_COMPONENTS_NOTIFICATION_SUBJECT);
			mimeMessageHelper.setText(processUpcomingDueComponentsTemplate(upcominDueComponents, dateDue, hoursDue), true);
			javaMailSender.send(mimeMessage);
			LOGGER.info("Sent upcoming due components notification email to: {}", upcomingDueComponentsNotificationTo);
		} catch (MessagingException | IOException | TemplateException e) {
			var message = "Exception while sending upcoming due components notification email."; 
			LOGGER.error(message, e);
			throw new ApplicationException(message + " (" + e.getMessage() + ")");
		}
	}

	public void emailUpcomingDueComponentsFailure(ApplicationException priceHoldingsExceptions) {
		LOGGER.info("Sending upcoming due components notification failure email");
		try {
			sendFailureEmail(priceHoldingsExceptions, upcomingDueComponentsNotificationFrom,
					upcomingDueComponentsNotificationTo, UPCOMING_DUE_COMPONENTS_NOTIFICATION_SUBJECT);
			LOGGER.info("Sent failure email to: {}", upcomingDueComponentsNotificationTo);
		} catch (ApplicationException e) {
			e.printStackTrace();
			LOGGER.error("Failed to send failure email.", e);
		}
	}

	public void sendRemoteApiFailureEmail(HttpRequestTypeEnum httpRequestTypeEnum, ApplicationException remoteCallApiException) {
		LOGGER.info("Sending remote api call failure email");
		try {
			var subject = RERMOTE_API_FAILURE_SUBJECT + "(" + httpRequestTypeEnum + ")";
			sendFailureEmail(remoteCallApiException, failureNotificationFrom,
					failureNotificationTo, subject);
			LOGGER.info("Sent failure email to: {}", failureNotificationTo);
		} catch (ApplicationException e) {
			e.printStackTrace();
			LOGGER.error("Failed to send failure email.", e);
		}
	}

	private String processUpcomingDueComponentsTemplate(List<Component> upcominDueComponents, Date dateDue, Float hoursDue) throws IOException, TemplateException {
		Map<String, Object> templateModelMap = new HashMap<>();
		templateModelMap.put("upcominDueComponents", upcominDueComponents);
		templateModelMap.put("dateDue", dateDue);
		templateModelMap.put("hoursDue", hoursDue);

		return FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfiguration.getTemplate(UPCOMING_DUE_COMPONENTS_NOTIFICATION_TEMPLATE), templateModelMap);
	}

	
	private void sendFailureEmail(ApplicationException applicationExceptions, String emailFromAddress, String emailToAddress, String emailSubject) throws ApplicationException {
		var mimeMessage = javaMailSender.createMimeMessage();
		var mimeMessageHelper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
		try {
			mimeMessageHelper.setFrom(emailFromAddress);
			mimeMessageHelper.setTo(InternetAddress.parse(emailToAddress));
			mimeMessageHelper.setSubject(emailSubject);
			mimeMessageHelper.setText(processFailureTemplate(applicationExceptions), true);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException | IOException | TemplateException e) {
			var message = "Exception while sending failure email."; 
			LOGGER.error(message, e);
			throw new ApplicationException(message + " (" + e.getMessage() + ")");
			
		}
	}
	private String processFailureTemplate(ApplicationException priceHoldingsExceptions) throws IOException, TemplateException {
		Map<String, Object> templateModelMap = new HashMap<>();
		templateModelMap.put("priceHoldingsExceptions", priceHoldingsExceptions);
		return FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfiguration.getTemplate(FAILURE_TEMPLATE), templateModelMap);
	}

}
