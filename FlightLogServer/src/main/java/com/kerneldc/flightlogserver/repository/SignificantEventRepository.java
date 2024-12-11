package com.kerneldc.flightlogserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kerneldc.flightlogserver.domain.significantEvent.SignificantEvent;

public interface SignificantEventRepository extends JpaRepository<SignificantEvent, Long>, JpaSpecificationExecutor<SignificantEvent> {
//	List<SignificantEvent> findAllByOrderByEventDate();
}
