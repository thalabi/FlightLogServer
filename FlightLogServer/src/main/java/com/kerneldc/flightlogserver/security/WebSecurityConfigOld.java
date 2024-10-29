package com.kerneldc.flightlogserver.security;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfigOld /*extends WebSecurityConfigurerAdapter*/ {

//	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
//
//	private static final String READ_TABLE_SUFFIX = " read";
//	private static final String WRITE_TABLE_SUFFIX = " write";
//	//@Autowired
//    private CustomUserDetailsServiceOld customUserDetailsServiceOld;
//	//@Autowired
//	private UnauthorizedHandler unauthorizedHandler;
//	
//	@Value("${application.disableSecurity}")
//	private boolean disableSecurity;
//    
//    //@Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//    //@Override
//    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder
//                .userDetailsService(customUserDetailsServiceOld)
//                .passwordEncoder(passwordEncoder());
//    }
//    //@Bean(BeanIds.AUTHENTICATION_MANAGER)
//    //@Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        //return super.authenticationManagerBean();
//        return null;
//    }
//    
//    //@Bean
//    public JwtAuthenticationFilterOld jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilterOld();
//    }
//    
//	//@Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//		if (disableSecurity) {
//			LOGGER.warn("*** appliction security is currently disabled ***");;
//			LOGGER.warn("*** to enable set application.disableSecurity to false ***");;
//			httpSecurity.authorizeRequests().mvcMatchers("/**").permitAll();
//		}
//
//		httpSecurity
//			.cors()
//				.and()
//        	.csrf().disable()
//        	.exceptionHandling()
//	            .authenticationEntryPoint(unauthorizedHandler)
//	            .and()
//            .sessionManagement()
//            	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            	.and()
//            .authorizeRequests()
//                //.mvcMatchers(/*"/", "/home",*/"/StudentNotesService/getVersion", "/StudentNotesService/Security/authenticate").permitAll()
//                .mvcMatchers("/appInfoController/*", "/authenticationController/authenticate").permitAll()
//                .mvcMatchers("/appInfoController/*", "/authenticationController/changePassword").authenticated()
//
//                //.mvcMatchers("/**").permitAll()
//                //.anyRequest().authenticated()
//                //.anyRequest().denyAll()
//            ;
//		for (EntityEnum entityEnum : EntityEnum.values()) {
//			String entityName = entityEnum.getEntityName();
//			String tableName = entityEnum.getTableName();
//			httpSecurity
//	            .authorizeRequests()
//		            .mvcMatchers(HttpMethod.GET, "/"+entityName+"Controller/findAll/**").hasAuthority(tableName+READ_TABLE_SUFFIX)
//		            .mvcMatchers(HttpMethod.GET, "/"+entityName+"Controller/count").hasAuthority(tableName+READ_TABLE_SUFFIX)
//		            .mvcMatchers(HttpMethod.GET, "/"+entityName+"s").hasAuthority(tableName+READ_TABLE_SUFFIX)
//		            .mvcMatchers(HttpMethod.GET, "/"+entityName+"s/**").hasAuthority(tableName+READ_TABLE_SUFFIX)
//		            
//		            .mvcMatchers(HttpMethod.POST, "/"+entityName+"s").hasAuthority(tableName+WRITE_TABLE_SUFFIX)
//		            .mvcMatchers(HttpMethod.PUT, "/"+entityName+"s/**").hasAuthority(tableName+WRITE_TABLE_SUFFIX)
//		            .mvcMatchers(HttpMethod.DELETE, "/"+entityName+"s/**").hasAuthority(tableName+WRITE_TABLE_SUFFIX)
//
//		            .mvcMatchers(HttpMethod.POST, "/"+entityName+"Controller/add").hasAuthority(tableName+WRITE_TABLE_SUFFIX)
//		            .mvcMatchers(HttpMethod.PUT, "/"+entityName+"Controller/modify*").hasAuthority(tableName+WRITE_TABLE_SUFFIX)
//		            .mvcMatchers(HttpMethod.DELETE, "/"+entityName+"Controller/delete").hasAuthority(tableName+WRITE_TABLE_SUFFIX)
//
//		            .mvcMatchers(HttpMethod.GET, "/replicationController/getTableReplicationStatus/"+entityName).hasAuthority(tableName+READ_TABLE_SUFFIX)
//		            .mvcMatchers(HttpMethod.PUT, "/replicationController/setTableReplicationStatus/"+entityName).hasAuthority(tableName+WRITE_TABLE_SUFFIX)
//		            .mvcMatchers(HttpMethod.GET, "/jobLauncherController/copy"+StringUtils.capitalize(entityName+"Table")).hasAuthority(tableName+" sync")
//		            ;
//		}
//		
//		httpSecurity.authorizeRequests()
//            .mvcMatchers(HttpMethod.GET, "/flightLogMonthlyTotalVs/**").hasAuthority("summary")
//            .mvcMatchers(HttpMethod.GET, "/flightLogYearlyTotalVs/**").hasAuthority("summary")
//            .mvcMatchers(HttpMethod.GET, "/flightLogLastXDaysTotalVs/**").hasAuthority("summary");
//
//		httpSecurity.authorizeRequests()
//			.mvcMatchers(HttpMethod.POST, "/copyUserController/copyUser").hasAuthority("copy user");
//
//		httpSecurity.authorizeRequests()
//		// TODO should have "part write" and "component write"
//			.mvcMatchers(HttpMethod.GET, "/jobLauncherController/copyAircraftMaintenanceTables").hasAuthority("component write");
//
//		httpSecurity.authorizeRequests()
//			.mvcMatchers(HttpMethod.GET, "/aircraftMaintenancePrintController/*").hasAuthority("component read");
//
//		httpSecurity.authorizeRequests().anyRequest().denyAll();
//
//		
//		// Add our jwtAuthenticationFilter
//		//httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//    }
}
