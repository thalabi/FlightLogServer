package com.kerneldc.flightlogserver.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kerneldc.flightlogserver.FlightLogServerApplication;
import com.kerneldc.flightlogserver.domain.SearchCriteria;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlightLogServerApplication.class)
public class FlightLogControllerTests {

	public FlightLogControllerTests() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private FlightLogController fixture;

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
