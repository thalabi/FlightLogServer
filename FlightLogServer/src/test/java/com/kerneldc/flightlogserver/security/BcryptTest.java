package com.kerneldc.flightlogserver.security;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BcryptTest {

	@Test
	public void testEncodeDecode() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = "123456";
		for (int i=0; i<5; i++) {
			String encodedPassword = passwordEncoder.encode(password);
			System.out.println(encodedPassword);
			assertThat("Password doesn't match", passwordEncoder.matches(password, encodedPassword));
		}
	}
}
