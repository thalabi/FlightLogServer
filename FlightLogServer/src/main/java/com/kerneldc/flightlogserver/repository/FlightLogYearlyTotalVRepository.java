package com.kerneldc.flightlogserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kerneldc.flightlogserver.domain.FlightLogYearlyTotalV;

public interface FlightLogYearlyTotalVRepository extends JpaRepository<FlightLogYearlyTotalV, Long>, JpaSpecificationExecutor<FlightLogYearlyTotalV> {
	List<FlightLogYearlyTotalV> findAllByOrderById();
}
