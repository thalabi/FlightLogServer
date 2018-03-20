package com.kerneldc.flightlogserver.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.Test;

import com.kerneldc.flightlogserver.domain.SearchCriteria;

public class FlightLogControllerTests {

	public FlightLogControllerTests() {
		// TODO Auto-generated constructor stub
	}

	@Test
	public void testSearchStringToSearchCriteriaList_GreaterOrEqual_Success() {
		FlightLogController fixture = new FlightLogController(null, null, null);
		String search = "daySolo>=7,";
    	List<SearchCriteria> searchCriteriaList = fixture.searchStringToSearchCriteriaList(search);
		assertThat(searchCriteriaList.size(), equalTo(1));
	}
	@Test
	public void testSearchStringToSearchCriteriaList_Equal_Success() {
		FlightLogController fixture = new FlightLogController(null, null, null);
		String search = "routeTo=CYHU,";
    	List<SearchCriteria> searchCriteriaList = fixture.searchStringToSearchCriteriaList(search);
		assertThat(searchCriteriaList.size(), equalTo(1));
	}
}
