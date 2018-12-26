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
	}
}
