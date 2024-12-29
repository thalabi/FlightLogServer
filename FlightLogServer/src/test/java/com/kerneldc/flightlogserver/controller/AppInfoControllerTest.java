package com.kerneldc.flightlogserver.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.kerneldc.flightlogserver.AbstractBaseTest;
import com.kerneldc.flightlogserver.springBootConfig.WebSecurityConfig;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AppInfoController.class)
@Import(WebSecurityConfig.class)
@Slf4j
class AppInfoControllerTest extends AbstractBaseTest {


	private static final String BASE_URI = "/appInfoController";
	
	@Autowired
	private MockMvc mockMvc;
    @MockBean
    private JwtDecoder jwtDecoder;

	@Value("${build.version}_${build.timestamp}")
	private String buildTimestamp;

	private MediaType contentType = new MediaType(MediaType.TEXT_PLAIN.getType(), MediaType.TEXT_PLAIN.getSubtype(), Charset.forName("utf8"));

	@Test
	void testGetBuildTimestamp( ) throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(BASE_URI + "/getBuildInfo"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(content().string(equalTo(buildTimestamp)))
			.andDo(print())
			.andReturn();
		LOGGER.debug("mvcResult.getResponse(): {}", mvcResult.getResponse().getContentAsString());
	}
}
