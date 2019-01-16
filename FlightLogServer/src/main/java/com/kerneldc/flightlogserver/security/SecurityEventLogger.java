package com.kerneldc.flightlogserver.security;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SecurityEventLogger {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@EventListener
	public void eventLogger(AuditApplicationEvent auditApplicationEvent) {
		AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();
		LOGGER.info("User: {}, Event type: {}", auditEvent.getPrincipal(), auditEvent.getType());
	}

}
