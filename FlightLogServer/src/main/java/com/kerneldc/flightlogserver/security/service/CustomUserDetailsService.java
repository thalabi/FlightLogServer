package com.kerneldc.flightlogserver.security.service;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kerneldc.flightlogserver.security.bean.AppUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	public PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		LOGGER.debug("Begin ...");
		// TODO - implement a user lookup service
		
		// Let people login with either username or email
/*        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> 
                        new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
        );
*/		
		if (! /* not */ username.equals("thalabi")) {
			throw new UsernameNotFoundException("User not found with username or email : " + username);
		}
		
		LOGGER.debug("End ...");
		return appUserDetails1();
	}

	private AppUserDetails appUserDetails1() {
		String username = "thalabi";
		AppUserDetails appUserDetails = new AppUserDetails();
    	appUserDetails.setId(7l);
    	appUserDetails.setUsername(username);
    	String encodedPassword = passwordEncoder.encode("secret");
    	appUserDetails.setPassword(encodedPassword);
    	appUserDetails.setFirstName(username + " first name");
    	appUserDetails.setLastName(username + " last name");
    	appUserDetails.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("authority1"), new SimpleGrantedAuthority("authority2"), new SimpleGrantedAuthority("authority3")));
    	//newUser.setToken(jwtTokenUtil.generate(newUser.getUsername(), newUser.getAuthorities()));
		return appUserDetails;
	}
}
