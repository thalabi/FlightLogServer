package com.kerneldc.flightlogserver.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.kerneldc.flightlogserver.AbstractBaseTest;
import com.kerneldc.flightlogserver.security.config.UnauthorizedHandler;
import com.kerneldc.flightlogserver.security.service.CustomUserDetailsService;
import com.kerneldc.flightlogserver.security.util.JwtTokenProvider;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AppInfoController.class)

public class AppInfoControllerTests extends AbstractBaseTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static final String BASE_URI = "/appInfoController";
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CustomUserDetailsService customUserDetailsService;
	@MockBean
	private UnauthorizedHandler unauthorizedHandler;
	@MockBean
	private JwtTokenProvider jwtTokenProvider;

	@Value("${build.version}_${build.timestamp}")
	private String buildTimestamp;

	private MediaType contentType = new MediaType(MediaType.TEXT_PLAIN.getType(), MediaType.TEXT_PLAIN.getSubtype(), Charset.forName("utf8"));

	@Test
	@WithMockUser
	public void testGetBuildTimestamp( ) throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/getBuildTimestamp"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(content().string(equalTo(buildTimestamp)))
			.andReturn();
		LOGGER.debug("mvcResult.getResponse(): {}", mvcResult.getResponse().getContentAsString());
	}
}
