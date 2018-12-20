package com.kerneldc.flightlogserver.security.util;

import java.lang.invoke.MethodHandles;
import java.security.Key;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.kerneldc.flightlogserver.security.bean.AppUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Value("${security.jwtTokenProvider.jwtExpirationInMs}")
    private int jwtExpirationInMs;
	
	@Autowired
	private SecretKeyProvider secretKeyProvider;

	private Key secretKey;
	
	@PostConstruct
	public void init() {
		secretKey = secretKeyProvider.getSecretKey();
		LOGGER.debug("secretKey: {}", secretKey);
	}

	public String generateToken(Authentication authentication) {
		AppUserDetails appUserDetails = (AppUserDetails)authentication.getPrincipal();
		Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        
        
        // TODO change to setSubject to appUserDetails object
		return Jwts.builder()
                .setSubject(Long.toString(appUserDetails.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
	}

	public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
        	LOGGER.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
        	LOGGER.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
        	LOGGER.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
        	LOGGER.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
        	LOGGER.error("JWT claims string is empty.");
        }
        return false;
    }
	
	// TODO remove after setSubject above is changed
	public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
}
