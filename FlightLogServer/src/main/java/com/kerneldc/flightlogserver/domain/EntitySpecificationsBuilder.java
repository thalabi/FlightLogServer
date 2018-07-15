package com.kerneldc.flightlogserver.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

public class EntitySpecificationsBuilder<T> {

	private final List<SearchCriteria> params;
	 
    public EntitySpecificationsBuilder() {
        params = new ArrayList<>();
    }
 
    public EntitySpecificationsBuilder<T> with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }
 
    public EntitySpecificationsBuilder<T> with(List<SearchCriteria> searchCriteriaList) {
        params.addAll(searchCriteriaList);
        return this;
    }

    public Specification<T> build() {
        if (params.isEmpty()) {
            return null;
        }
 
        List<Specification<T>> specs = new ArrayList<>();
        for (SearchCriteria param : params) {
            specs.add(new EntitySpecification<T>(param));
        }
 
        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
        	result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
