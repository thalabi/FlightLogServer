package com.kerneldc.flightlogserver.security.controller;

import java.lang.invoke.MethodHandles;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.exception.ApplicationException;
import com.kerneldc.flightlogserver.security.bean.CopyUserRequest;
import com.kerneldc.flightlogserver.security.domain.user.User;
import com.kerneldc.flightlogserver.security.service.SecurityPersistenceService;

@RestController
@RequestMapping("copyUserController")
@ExposesResourceFor(User.class) // needed for unit test to create entity links
public class CopyUserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private SecurityPersistenceService securityPersistenceService;
	
    public CopyUserController(SecurityPersistenceService securityPersistenceService) {
        this.securityPersistenceService = securityPersistenceService;
    }

    @PostMapping("/copyUser")
	public ResponseEntity<String> copyUser(@Valid @RequestBody CopyUserRequest copyUserRequest) {
    	LOGGER.debug("copyUserRequest: {}", copyUserRequest);
		try {
			securityPersistenceService.copyUser(copyUserRequest.getFromUsername(), copyUserRequest.getToUsername());
		} catch (ApplicationException e) {
			if (e.getCause() instanceof EntityNotFoundException) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
			}
			if (e.getCause() instanceof EntityExistsException) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
			}
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok("");
    }

}
