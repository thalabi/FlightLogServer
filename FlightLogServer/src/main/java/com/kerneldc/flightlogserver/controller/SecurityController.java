package com.kerneldc.flightlogserver.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping("/protected/securityController")
@RequiredArgsConstructor
@Slf4j
public class SecurityController {

	private record UserInfo(String username, String firstName, String lastName, String email, List<String> roles, List<String> backEndAuthorities) {};

	@GetMapping("/getUserInfo")
	public UserInfo getUserInfo() {
		if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken jwtAuthenticationToken
				&& jwtAuthenticationToken.getPrincipal() instanceof Jwt jwt) {
			
			LOGGER.info("jwtAuthenticationToken: {}" ,jwtAuthenticationToken);
			LOGGER.info("jwtAuthenticationToken.name: {}" ,jwtAuthenticationToken.getName());
			
			//LOGGER.info("jwt id: {}, claims: {}", jwt.getId(), jwt.getClaims());
			LOGGER.info("jwt id: {}, claims: ", jwt.getId());
			for (Entry<String, Object> entry : jwt.getClaims().entrySet()) {
				LOGGER.info("\t{} = {}", entry.getKey(), entry.getValue());
				
			}
			Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");
			var roles = realmAccess.get("roles");
			roles.sort(null);

			var authorities = jwtAuthenticationToken.getAuthorities();
			var backEndAuthorities = authorities.stream().map(auth -> auth.getAuthority()).collect(Collectors.toList());
			backEndAuthorities.sort(null);
			
			var userInfo = new UserInfo(jwt.getClaims().get("preferred_username").toString(),
					jwt.getClaims().get("given_name").toString(), jwt.getClaims().get("family_name").toString(),
					jwt.getClaims().get("email").toString(), roles, backEndAuthorities);
			LOGGER.info("userInfo: {}", userInfo);
			
			return userInfo;
		} else {
			return new UserInfo("", "","","", List.of(""), List.of(""));
		}
	}
}
