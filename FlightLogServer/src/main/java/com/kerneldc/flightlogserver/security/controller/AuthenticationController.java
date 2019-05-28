package com.kerneldc.flightlogserver.security.controller;

import java.lang.invoke.MethodHandles;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.exception.ApplicationException;
import com.kerneldc.flightlogserver.security.bean.AppUserDetails;
import com.kerneldc.flightlogserver.security.bean.ChangePasswordRequest;
import com.kerneldc.flightlogserver.security.bean.LoginRequest;
import com.kerneldc.flightlogserver.security.service.SecurityPersistenceService;
import com.kerneldc.flightlogserver.security.util.JwtTokenProvider;

@RestController
@RequestMapping("authenticationController")
public class AuthenticationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
    private SecurityPersistenceService securityPersistenceService;

	@PostMapping("/authenticate")
	public ResponseEntity<AppUserDetails> auhenticate(@Valid @RequestBody LoginRequest loginRequest) {
		LOGGER.debug("Begin ...");
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		LOGGER.debug("authentication: {}", authentication);

		AppUserDetails appUserDetails = (AppUserDetails)authentication.getPrincipal();
		appUserDetails.setPassword(StringUtils.EMPTY);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtTokenProvider.generateJwt(authentication);
		appUserDetails.setToken(jwt);
		LOGGER.debug("End ...");
		return ResponseEntity.ok(appUserDetails);
	}

	@PostMapping("/changePassword")
	public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) throws ApplicationException {
		LOGGER.debug("Begin ...");
		securityPersistenceService.changePassword(changePasswordRequest.getUsernameOrEmail(), changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
		LOGGER.debug("End ...");
		return ResponseEntity.ok(StringUtils.EMPTY);
	}
}
