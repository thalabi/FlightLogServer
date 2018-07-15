package com.kerneldc.flightlogserver.domain;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class EntitySpecification<T> implements Specification<T> {

	private static final long serialVersionUID = 1L;
	private SearchCriteria criteria;
	
	public EntitySpecification(SearchCriteria criteria) {
		this.criteria = criteria;
	}
	
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria.getOperation().equals(">")) {
            return builder.greaterThan(
              root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
		if (criteria.getOperation().equals(">=")) {
            return builder.greaterThanOrEqualTo(
              root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
		else if (criteria.getOperation().equals("<")) {
            return builder.lessThan(
              root.<String> get(criteria.getKey()), criteria.getValue().toString());
        } 
		else if (criteria.getOperation().equals("<=")) {
            return builder.lessThanOrEqualTo(
              root.<String> get(criteria.getKey()), criteria.getValue().toString());
        } 
        else if (criteria.getOperation().equals("=")) {
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
