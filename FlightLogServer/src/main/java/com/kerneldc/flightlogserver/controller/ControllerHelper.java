package com.kerneldc.flightlogserver.controller;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kerneldc.flightlogserver.domain.SearchCriteria;

public class ControllerHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static List<SearchCriteria> searchStringToSearchCriteriaList(String search) {
    	List<SearchCriteria> searchCriteriaList = new ArrayList<>();
    	Pattern pattern = Pattern.compile("(\\w+?)(<=|>=|=|<|>)(.+?),");
    	Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
        	LOGGER.debug("matcher.group(1): {} matcher.group(2): {} matcher.group(3): {}", matcher.group(1), matcher.group(2), matcher.group(3));
        	searchCriteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
        }
    	return searchCriteriaList;
    }
}
