package com.kerneldc.flightlogserver.security.util.securityaudit;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.security.AbstractAuthorizationAuditListener;
import org.springframework.security.access.event.AbstractAuthorizationEvent;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationFailureListener extends AbstractAuthorizationAuditListener {

	public static final String AUTHORIZATION_FAILURE = "AUTHORIZATION_FAILURE";
	protected static final String EXCEPTION_CLASS_NAME_KEY = "exceptionClassName";
	protected static final String EXCEPTION_MESSAGE_KEY = "exceptionMessage";
	protected static final String REQUEST_URL_KEY = "requestUrl";
	protected static final String WEB_AUTHENTICATION_DETAILS_KEY = "webAuthenticationDetails";

	@Override
	public void onApplicationEvent(AbstractAuthorizationEvent event) {
		if (event instanceof AuthorizationFailureEvent) {
			onAuthorizationFailureEvent((AuthorizationFailureEvent) event);
		}
	}

	private void onAuthorizationFailureEvent(AuthorizationFailureEvent event) {
		Map<String, Object> data = new HashMap<>();
		data.put(EXCEPTION_CLASS_NAME_KEY, event.getAccessDeniedException().getClass().getName());
		data.put(EXCEPTION_MESSAGE_KEY, event.getAccessDeniedException().getMessage());
		data.put(REQUEST_URL_KEY, ((FilterInvocation) event.getSource()).getRequestUrl());

		if (event.getAuthentication().getDetails() != null) {
			data.put(WEB_AUTHENTICATION_DETAILS_KEY, event.getAuthentication().getDetails());
		}
		publish(new AuditEvent(event.getAuthentication().getName(), AUTHORIZATION_FAILURE, data));
	}
}
