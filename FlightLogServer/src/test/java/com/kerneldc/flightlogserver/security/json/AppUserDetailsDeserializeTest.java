package com.kerneldc.flightlogserver.security.json;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kerneldc.flightlogserver.security.bean.AppUserDetails;
import com.kerneldc.flightlogserver.security.util.SimpleGrantedAuthorityMixIn;

public class AppUserDetailsDeserializeTest {

	@Test
	public void testDeserialize() throws Exception {
		AppUserDetails appUserDetails = new AppUserDetails();
    	appUserDetails.setId(7l);
    	String username = "thalabi";
    	appUserDetails.setUsername(username);
    	appUserDetails.setPassword("secret");
    	appUserDetails.setFirstName(username + " first name");
    	appUserDetails.setLastName(username + " last name");
    	appUserDetails.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("authority1"), new SimpleGrantedAuthority("authority2"), new SimpleGrantedAuthority("authority3")));
    	appUserDetails.setToken("token");
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	String result = objectMapper.writeValueAsString(appUserDetails);
    	
    	objectMapper.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixIn.class);
    	AppUserDetails u = new AppUserDetails();
    	//objectMapper.convertValue(result, AppUserDetails.class);
    	u = objectMapper.readerForUpdating(u).readValue(result);
    	System.out.println(u);
	}
}
