package com.kerneldc.flightlogserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kerneldc.flightlogserver.domain.FlightLogRunningTotalV;

public interface FlightLogRunningTotalVRepository extends JpaRepository<FlightLogRunningTotalV, Long>, JpaSpecificationExecutor<FlightLogRunningTotalV> {
	List<FlightLogRunningTotalV> findAllByOrderById();
}
