package com.kerneldc.flightlogserver.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kerneldc.flightlogserver.domain.EntityEnum;
import com.kerneldc.flightlogserver.security.config.JwtAuthenticationFilter;
import com.kerneldc.flightlogserver.security.config.UnauthorizedHandler;
import com.kerneldc.flightlogserver.security.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//	securedEnabled = true,
//	jsr250Enabled = true,
//	prePostEnabled = true
//)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private UnauthorizedHandler unauthorizedHandler;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    
	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
		//httpSecurity.authorizeRequests().mvcMatchers("/**").permitAll();

		httpSecurity
			.cors()
				.and()
        	.csrf().disable()
        	.exceptionHandling()
	            .authenticationEntryPoint(unauthorizedHandler)
	            .and()
            .sessionManagement()
            	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            	.and()
            .authorizeRequests()
                //.mvcMatchers(/*"/", "/home",*/"/StudentNotesService/getVersion", "/StudentNotesService/Security/authenticate").permitAll()
                .mvcMatchers("/appInfoController/*", "/authenticationController/authenticate").permitAll()
                .mvcMatchers("/appInfoController/*", "/authenticationController/changePassword").authenticated()

                //.mvcMatchers("/**").permitAll()
                //.anyRequest().authenticated()
                //.anyRequest().denyAll()
            ;
		for (EntityEnum entityEnum : EntityEnum.values()) {
			String entityName = entityEnum.getEntityName();
			String tableName = entityEnum.getTableName();
			httpSecurity
	            .authorizeRequests()
		            .mvcMatchers(HttpMethod.GET, "/"+entityName+"Controller/findAll/*").hasAuthority(tableName+" read")
		            .mvcMatchers(HttpMethod.GET, "/"+entityName+"Controller/count").hasAuthority(tableName+" read")
		            .mvcMatchers(HttpMethod.GET, "/"+entityName+"s").hasAuthority(tableName+" read")
		            .mvcMatchers(HttpMethod.GET, "/"+entityName+"s/**").hasAuthority(tableName+" read")
		            
		            .mvcMatchers(HttpMethod.POST, "/"+entityName+"s").hasAuthority(tableName+" write")
		            .mvcMatchers(HttpMethod.PUT, "/"+entityName+"s/**").hasAuthority(tableName+" write")
		            .mvcMatchers(HttpMethod.DELETE, "/"+entityName+"s/**").hasAuthority(tableName+" write")
		            
		            .mvcMatchers(HttpMethod.GET, "/replicationController/getTableReplicationStatus/"+entityName).hasAuthority(tableName+" read")
		            .mvcMatchers(HttpMethod.GET, "/replicationController/setTableReplicationStatus/"+entityName).hasAuthority(tableName+" write")
		            .mvcMatchers(HttpMethod.GET, "/jobLauncherController/copy"+StringUtils.capitalize(entityName+"Table")).hasAuthority(tableName+" sync")
		            ;
		}
		
		httpSecurity.authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/flightLogMonthlyTotalVs/**").hasAuthority("summary")
            .mvcMatchers(HttpMethod.GET, "/flightLogYearlyTotalVs/**").hasAuthority("summary")
            .mvcMatchers(HttpMethod.GET, "/flightLogLastXDaysTotalVs/**").hasAuthority("summary");

		httpSecurity.authorizeRequests().anyRequest().denyAll();

		
		// Add our jwtAuthenticationFilter
		httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
