package com.kerneldc.flightlogserver.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

public class FlightLogSpecificationsBuilder {

	private final List<SearchCriteria> params;
	 
    public FlightLogSpecificationsBuilder() {
        params = new ArrayList<SearchCriteria>();
    }
 
    public FlightLogSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }
 
    public FlightLogSpecificationsBuilder with(List<SearchCriteria> searchCriteriaList) {
        params.addAll(searchCriteriaList);
        return this;
    }

    public Specification<FlightLog> build() {
        if (params.isEmpty()) {
            return null;
        }
 
        List<Specification<FlightLog>> specs = new ArrayList<>();
        for (SearchCriteria param : params) {
            specs.add(new FlightLogSpecification(param));
        }
 
        Specification<FlightLog> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
