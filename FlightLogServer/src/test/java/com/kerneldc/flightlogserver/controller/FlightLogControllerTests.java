package com.kerneldc.flightlogserver.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.kerneldc.flightlogserver.FlightLogServerApplication;
import com.kerneldc.flightlogserver.domain.FlightLog;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.repository.FlightLogRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlightLogServerApplication.class)
//@WebMvcTest(FlightLogController.class)
//@AutoConfigureTestDatabase
//@AutoConfigureMockMvc

public class FlightLogControllerTests {

	private static final String BASE_URI = "/flightLogController";
	
	private MockMvc mockMvc;
	
	@MockBean
	private FlightLogRepository flightLogRepository;

	@Autowired
    private WebApplicationContext webApplicationContext;

	@Autowired
	private FlightLogController fixture;
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		
		FlightLog flightLog = new FlightLog();
		flightLog.setId(7l);
		flightLog.setVersion(0l);
		flightLog.setRouteFrom("CYOO");
		flightLog.setRouteTo("CYPQ");
		flightLog.setPic("Tarif Halabi");
		flightLog.setDaySolo(1f);
		flightLog.setTosLdgsDay(1);
		
		Mockito.when(flightLogRepository.count())
			.thenReturn(1l);
	}

	@Test
	public void testCount( ) throws Exception {
		mockMvc.perform(get(BASE_URI + "/count"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.count", is(1)))
//        .andExpect(jsonPath("$.uri", is("http://bookmark.com/1/" + userName)))
//        .andExpect(jsonPath("$.description", is("A description")))
        ;

	}
	
	@Test
	public void testSearchStringToSearchCriteriaList_GreaterOrEqual_Success() {
		
		String search = "daySolo>=7,";
    	List<SearchCriteria> searchCriteriaList = fixture.searchStringToSearchCriteriaList(search);
		assertThat(searchCriteriaList.size(), equalTo(1));
	}

	@Test
	public void testSearchStringToSearchCriteriaList_Equal_Success() {
		String search = "routeTo=CYHU,";
    	List<SearchCriteria> searchCriteriaList = fixture.searchStringToSearchCriteriaList(search);
		assertThat(searchCriteriaList.size(), equalTo(1));
	}
}
