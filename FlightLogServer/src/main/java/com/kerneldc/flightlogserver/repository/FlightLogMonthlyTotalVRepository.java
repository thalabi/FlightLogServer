package com.kerneldc.flightlogserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kerneldc.flightlogserver.domain.FlightLogMonthlyTotalV;

public interface FlightLogMonthlyTotalVRepository extends JpaRepository<FlightLogMonthlyTotalV, Long>, JpaSpecificationExecutor<FlightLogMonthlyTotalV> {
	List<FlightLogMonthlyTotalV> findAllByOrderById();
}
