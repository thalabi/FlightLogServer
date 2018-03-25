package com.kerneldc.flightlogserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.kerneldc.flightlogserver.domain.FlightLogMonthlyTotalV;

@RepositoryRestResource
public interface FlightLogMonthlyTotalVRepository extends JpaRepository<FlightLogMonthlyTotalV, Long>, JpaSpecificationExecutor<FlightLogMonthlyTotalV> {
	List<FlightLogMonthlyTotalV> findAllByOrderById();
}
