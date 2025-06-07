package com.kerneldc.flightlogserver.springBootConfig;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.kerneldc.flightlogserver.domain.EntityEnum;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {
	
	@Value("${application.security.disableSecurity:false}")
	private boolean disableSecurity;
	@Value("${application.security.actuator.username}")
	private String actuatorUsername;
	@Value("${application.security.actuator.password}")
	private String actuatorPassword;

	public static final String AUTHORITY_PREFIX = "ROLE_realm_";
	public static final String READ_TABLE_SUFFIX = " read";
	public static final String WRITE_TABLE_SUFFIX = " write";

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, /* CorsConfigurationSource corsConfigurationSource, */ KeycloakJwtRolesConverter keycloakJwtRolesConverter) throws Exception {

		DelegatingJwtGrantedAuthoritiesConverter authoritiesConverter =
				// Using the delegating converter multiple converters can be combined
				new DelegatingJwtGrantedAuthoritiesConverter(
						// First add the default converter
						new JwtGrantedAuthoritiesConverter(),
						// Second add our custom Keycloak specific converter
						keycloakJwtRolesConverter);

		// Set up http security to use the JWT converter defined above
		httpSecurity.oauth2ResourceServer((oauth2) -> oauth2.jwt((jwt) -> jwt
		.jwtAuthenticationConverter(jwtConv -> new JwtAuthenticationToken(jwtConv, authoritiesConverter.convert(jwtConv), keycloakJwtRolesConverter.getUsername(jwtConv)))));

		//return httpSecurity.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll())
				// .authorizeRequests(authorizeRequests ->
				// authorizeRequests.anyRequest().authenticated())
				// .authorizeRequests(authorizeRequests ->
				// authorizeRequests.anyRequest().hasRole("kerneldc-realm-user-role"))
				// .authorizeRequests(authorizeRequests ->
				// authorizeRequests.anyRequest().hasRole("SCOPE_PROFILE"))

//		httpSecurity.authorizeRequests()
//		.mvcMatchers("/sandboxController/noBearerTokenPing", "/actuator/*").permitAll()
//		.mvcMatchers("/protected/sandboxController/getUserInfo").hasRole("realm_sso-app-user-role")
//		//.mvcMatchers("/noBearerTokenPing").hasRole("realm_sso2-app-admin-role")
//		;
		if (disableSecurity) {
			LOGGER.warn("*** appliction security is currently disabled ***");
			LOGGER.warn("*** to enable set application.security.disableSecurity to false ***");
			httpSecurity.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.anyRequest().permitAll());
			// to allow h2 console to display using frames
			httpSecurity.headers().frameOptions().sameOrigin();
		} else {
			
			defineHttpAuthorizedRequests(httpSecurity);
			
		}
		
		httpSecurity.exceptionHandling(
						exception -> exception.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
								.accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

		httpSecurity.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
				;
				
		httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		return httpSecurity.build();
	}

	private void defineHttpAuthorizedRequests(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				// spring security 5.6
				.antMatchers("/appInfoController/getBuildInfo", "/appInfoController/*", "/pingController/*").permitAll()
				.antMatchers("/protected/securityController/getUserInfo", "/protected/externalAirportController/*").authenticated()
				.antMatchers(HttpMethod.GET, "/protected/simpleController/findAll").hasAuthority(AUTHORITY_PREFIX + "pilot" + READ_TABLE_SUFFIX)
				);
		httpSecurity.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.antMatchers("/actuator/*").hasRole("ACTUATOR")).httpBasic();
				// spring security 6.1
				//.requestMatchers("/appInfoController/*", "/pingController/*", "/actuator/*").permitAll()
				//.requestMatchers("/protected/securityController/getUserInfo").authenticated()
		//.antMatchers(HttpMethod.GET, "/protected/genericEntityController/findAll?tableName=flight_log_totals_v**")
		//.antMatchers(HttpMethod.GET, "/protected/genericEntityController/findAll*/**")				

		for (EntityEnum entityEnum : EntityEnum.values()) {
			var entityName = entityEnum.getEntityName();
			var tableName = entityEnum.getTableName();
			var tableAuthorityPrefix = AUTHORITY_PREFIX + tableName;
			LOGGER.info("entityName: [{}], tableName: [{}], tableAuthorityPrefix: [{}]", entityName, tableName, tableAuthorityPrefix);
			httpSecurity.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
					
					.regexMatchers(HttpMethod.GET, "/protected/genericEntityController/findAll\\?tableName="+tableName+".*")
						.hasAuthority(AUTHORITY_PREFIX+tableName+READ_TABLE_SUFFIX)
					.regexMatchers(HttpMethod.GET, "/protected/genericEntityController/countAll\\?tableName="+tableName)
						.hasAuthority(AUTHORITY_PREFIX+tableName+READ_TABLE_SUFFIX)

					
					.antMatchers(HttpMethod.GET, "/protected/"+entityName+"Controller/findAll/**").hasAuthority(tableAuthorityPrefix+READ_TABLE_SUFFIX)
		            
		            .antMatchers(HttpMethod.GET, "/protected/"+entityName+"Controller/count").hasAuthority(tableAuthorityPrefix+READ_TABLE_SUFFIX)
		            .antMatchers(HttpMethod.GET, "/protected/data-rest/"+entityName+"s").hasAuthority(tableAuthorityPrefix+READ_TABLE_SUFFIX)
		            .antMatchers(HttpMethod.GET, "/protected/data-rest/"+entityName+"s/**").hasAuthority(tableAuthorityPrefix+READ_TABLE_SUFFIX)
		            
		            .antMatchers(HttpMethod.POST, "/protected/data-rest/"+entityName+"s").hasAuthority(tableAuthorityPrefix+WRITE_TABLE_SUFFIX)
//		            .antMatchers(HttpMethod.PUT, "/protected/data-rest/"+entityName+"s/**").hasAuthority(tableAuthorityPrefix+WRITE_TABLE_SUFFIX)
		            .antMatchers(HttpMethod.PATCH, "/protected/data-rest/"+entityName+"s/**").hasAuthority(tableAuthorityPrefix+WRITE_TABLE_SUFFIX)
		            .antMatchers(HttpMethod.DELETE, "/protected/data-rest/"+entityName+"s/**").hasAuthority(tableAuthorityPrefix+WRITE_TABLE_SUFFIX)

		            .antMatchers(HttpMethod.POST, "/protected/"+entityName+"Controller/add").hasAuthority(tableAuthorityPrefix+WRITE_TABLE_SUFFIX)
		            .antMatchers(HttpMethod.PUT, "/protected/"+entityName+"Controller/modify*").hasAuthority(tableAuthorityPrefix+WRITE_TABLE_SUFFIX)
		            .antMatchers(HttpMethod.DELETE, "/protected/"+entityName+"Controller/delete").hasAuthority(tableAuthorityPrefix+WRITE_TABLE_SUFFIX)

		            .antMatchers(HttpMethod.GET, "/protected/replicationController/getTableReplicationStatus/"+entityName).hasAuthority(tableAuthorityPrefix+READ_TABLE_SUFFIX)
		            .antMatchers(HttpMethod.PUT, "/protected/replicationController/setTableReplicationStatus/"+entityName).hasAuthority(tableAuthorityPrefix+WRITE_TABLE_SUFFIX)
		            .antMatchers(HttpMethod.GET, "/protected/jobLauncherController/copy"+StringUtils.capitalize(entityName+"Table")).hasAuthority(tableAuthorityPrefix+" sync")
		            );
		}
		
		httpSecurity.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.antMatchers(HttpMethod.GET, "/protected/data-rest/flightLogMonthlyTotalVs/**",
						"/protected/data-rest/flightLogYearlyTotalVs/**",
						"/protected/data-rest/flightLogLastXDaysTotalVs/**")
				.hasAuthority(AUTHORITY_PREFIX + "summary"));

		httpSecurity.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
			.antMatchers(HttpMethod.GET, "/protected/aircraftMaintenancePrintController/*").hasAuthority(AUTHORITY_PREFIX + "component read"));
		
		httpSecurity.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.antMatchers(HttpMethod.POST, "/protected/flightLogPendingController/addFlightLogPending/*").hasAuthority(AUTHORITY_PREFIX + "flight_log_pending write"));

		httpSecurity.authorizeHttpRequests().anyRequest().denyAll();
	}

	@Bean
	public UserDetailsService userDetailsService() {
	    UserDetails admin = User.withUsername(actuatorUsername)
	        .password(passwordEncoder().encode(actuatorPassword))
	        .roles("ACTUATOR")  // this adds ROLE_ADMIN
	        .build();

	    return new InMemoryUserDetailsManager(admin);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder(); // or NoOpPasswordEncoder for testing only
	}	
	
	
}
