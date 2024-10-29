package com.kerneldc.flightlogserver.security.util;

import java.lang.invoke.MethodHandles;
import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kerneldc.flightlogserver.security.bean.AppUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

//@Component
public class JwtTokenProviderOld {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private static final String APP_USER_DETAILS = "appUserDetails";

	//@Value("${security.jwtTokenProvider.jwtExpirationInMs}")
    private int jwtExpirationInMs;
	
	//@Autowired
	private SecretKeyProviderOld secretKeyProviderOld;

	private Key secretKey;
	
	//@PostConstruct
	public void init() {
		secretKey = secretKeyProviderOld.getSecretKey();
		LOGGER.debug("secretKey.getEncoded(): {}", secretKey.getEncoded());
	}

	public String generateJwt(Authentication authentication) {
		Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        
        Claims claims = Jwts.claims();
        claims.setSubject(authentication.getName());
        claims.put(APP_USER_DETAILS, authentication.getPrincipal());
        
		return Jwts.builder()
				.setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(secretKey)
                .compact();
	}

	public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
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
	
	public UserDetails getAppUserDetailsFromJwt(String token) {
    	Claims claims = Jwts.parser()
            	.setSigningKey(secretKey)
            	.parseClaimsJws(token)
            	.getBody();
    	LOGGER.debug("claims.get(APP_USER_DETAILS): {}", claims.get(APP_USER_DETAILS));
    	ObjectMapper objectMapper = new ObjectMapper(); 
    	objectMapper.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixIn.class);
    	return objectMapper.convertValue(claims.get(APP_USER_DETAILS), AppUserDetails.class);
    }
}
