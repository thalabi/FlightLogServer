package com.kerneldc.flightlogserver.security.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

import org.springframework.stereotype.Component;

import lombok.Getter;
@Component
public class SecretKeyProvider {

	@Getter
	private Key secretKey;
	
	public SecretKeyProvider() throws NoSuchAlgorithmException {
		secretKey = KeyGenerator.getInstance("HmacSHA256").generateKey();
		// Uncomment for testing
//		byte[] keyByteArray = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2};
//		secretKey = new SecretKeySpec(keyByteArray, "HmacSHA256");

	}
}
