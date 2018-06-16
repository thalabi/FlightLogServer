package com.kerneldc.flightlogserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.kerneldc.flightlogserver.domain.SignificantEvent;

@RepositoryRestResource
public interface SignificantEventRepository extends JpaRepository<SignificantEvent, Long>, JpaSpecificationExecutor<SignificantEvent> {
	List<SignificantEvent> findAllByOrderByEventDate();
}