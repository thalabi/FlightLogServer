package com.kerneldc.flightlogserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.kerneldc.flightlogserver.domain.FlightLogMonthlyTotalV;

@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
public interface FlightLogMonthlyTotalVRepository extends JpaRepository<FlightLogMonthlyTotalV, Long>, JpaSpecificationExecutor<FlightLogMonthlyTotalV> {
	List<FlightLogMonthlyTotalV> findAllByOrderById();
}
