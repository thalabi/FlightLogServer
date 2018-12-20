package com.kerneldc.flightlogserver.security.config;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kerneldc.flightlogserver.security.constants.SecurityConstants;
import com.kerneldc.flightlogserver.security.service.CustomUserDetailsService;
import com.kerneldc.flightlogserver.security.util.JwtTokenProvider;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
    private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		LOGGER.debug("Begin ...");
		LOGGER.debug("url: {}", request.getRequestURL() + "?" + request.getQueryString());
		Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            LOGGER.debug("header: {}, value: {}",key, value);
        }
		String jwt = getJwtFromRequest(request);
		LOGGER.debug("jwt:{}", jwt);
		if (StringUtils.isNotEmpty(jwt) && jwtTokenProvider.validateToken(jwt)) {
			Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
			LOGGER.debug("userId:{}", userId);
			UserDetails userDetails = customUserDetailsService.loadUserById(userId);
			LOGGER.debug("userDetails:{}", userDetails);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		LOGGER.debug("End ...");
        filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(SecurityConstants.AUTH_HEADER_NAME);
        LOGGER.debug("authorizationHeader: {}", authorizationHeader);
        String prefix = SecurityConstants.AUTH_HEADER_SCHEMA+StringUtils.SPACE;
        if (StringUtils.isNotEmpty(authorizationHeader) && authorizationHeader.startsWith(prefix)) {
            return authorizationHeader.substring(prefix.length(), authorizationHeader.length());
        } else {
        	return null;
        }
    }
}
