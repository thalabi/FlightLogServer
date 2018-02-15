package com.kerneldc.flightlogserver.domain;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class FlightLogSpecification implements Specification<FlightLog> {

	private SearchCriteria criteria;
	
	public FlightLogSpecification(SearchCriteria criteria) {
		this.criteria = criteria;
	}
	
	@Override
	public Predicate toPredicate(Root<FlightLog> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThan(
              root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
		if (criteria.getOperation().equalsIgnoreCase(">=")) {
            return builder.greaterThanOrEqualTo(
              root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
		else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThan(
              root.<String> get(criteria.getKey()), criteria.getValue().toString());
        } 
		else if (criteria.getOperation().equalsIgnoreCase("<=")) {
            return builder.lessThanOrEqualTo(
              root.<String> get(criteria.getKey()), criteria.getValue().toString());
        } 
        else if (criteria.getOperation().equalsIgnoreCase("=")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(builder.upper(
                  root.<String>get(criteria.getKey())), "%" + criteria.getValue().toString().toUpperCase() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
	}

}
