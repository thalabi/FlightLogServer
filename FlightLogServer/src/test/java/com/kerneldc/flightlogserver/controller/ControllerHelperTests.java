package com.kerneldc.flightlogserver.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.kerneldc.flightlogserver.domain.SearchCriteria;

@RunWith(SpringRunner.class)
public class ControllerHelperTests {

	@Test
	public void testSearchStringToSearchCriteriaList_GreaterOrEqual_Success() {
		
		String search = "daySolo>=7,";
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
		assertThat(searchCriteriaList.size(), equalTo(1));
	}

	@Test
	public void testSearchStringToSearchCriteriaList_Equal_Success() {
		String search = "routeTo=CYHU,";
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
		assertThat(searchCriteriaList.size(), equalTo(1));
	}
}
