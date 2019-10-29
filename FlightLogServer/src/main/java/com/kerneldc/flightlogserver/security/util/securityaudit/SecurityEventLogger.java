package com.kerneldc.flightlogserver.security.util.securityaudit;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityEventLogger {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@EventListener
	public void eventLogger(AuditApplicationEvent auditApplicationEvent) {

		AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();
		if (! /* not */ auditEvent.getType().equals(AuthorizationFailureListener.AUTHORIZATION_FAILURE)) {
			return;
		}

		LOGGER.warn(
				"Principal: {}, audit event type: {}, request url: {}, exceptionClassName: {}, exception message: {}",
				auditEvent.getPrincipal(), auditEvent.getType(),
				auditEvent.getData().get(AuthorizationFailureListener.REQUEST_URL_KEY),
				auditEvent.getData().get(AuthorizationFailureListener.EXCEPTION_CLASS_NAME_KEY),
				auditEvent.getData().get(AuthorizationFailureListener.EXCEPTION_MESSAGE_KEY));

		WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) auditEvent.getData().get(AuthorizationFailureListener.WEB_AUTHENTICATION_DETAILS_KEY);
		if (webAuthenticationDetails != null) {
			LOGGER.info("Remote address: {}, session id: {}", webAuthenticationDetails.getRemoteAddress(), webAuthenticationDetails.getSessionId());
		}
	}

}
