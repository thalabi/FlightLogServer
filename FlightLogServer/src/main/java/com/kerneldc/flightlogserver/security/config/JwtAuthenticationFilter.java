package com.kerneldc.flightlogserver.security.config;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

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
import com.kerneldc.flightlogserver.security.util.JwtTokenProvider;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		LOGGER.debug("Begin ...");
		LOGGER.debug("url: {}", request.getRequestURL() + "?" + request.getQueryString());
		String jwt = getJwtFromRequest(request);
		if (StringUtils.isNotEmpty(jwt) && jwtTokenProvider.validateToken(jwt)) {
			UserDetails userDetails = jwtTokenProvider.getAppUserDetailsFromJwt(jwt);
			
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
		LOGGER.debug("End ...");
        filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(SecurityConstants.AUTH_HEADER_NAME);
        LOGGER.debug("authorizationHeader: {}", authorizationHeader);
        String prefix = SecurityConstants.AUTH_HEADER_SCHEMA+StringUtils.SPACE;
        if (StringUtils.isNotEmpty(authorizationHeader) && authorizationHeader.startsWith(prefix)) {
            return authorizationHeader.substring(prefix.length());
        } else {
        	return null;
        }
    }
}
