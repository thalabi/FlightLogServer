package com.kerneldc.flightlogserver.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kerneldc.flightlogserver.AbstractBaseTest;
import com.kerneldc.flightlogserver.domain.SearchCriteria;

@ExtendWith(SpringExtension.class)
class ControllerHelperTest extends AbstractBaseTest {

	@Test
	void testSearchStringToSearchCriteriaList_GreaterOrEqual_Success() {
		
		String search = "daySolo>=7,";
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
		assertThat(searchCriteriaList.size(), equalTo(1));
	}

	@Test
	void testSearchStringToSearchCriteriaList_Equal_Success() {
		String search = "routeTo=CYHU,";
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
		assertThat(searchCriteriaList.size(), equalTo(1));
	}
}
