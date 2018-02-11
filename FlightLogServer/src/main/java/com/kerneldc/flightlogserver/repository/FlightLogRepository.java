package com.kerneldc.flightlogserver.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.kerneldc.flightlogserver.domain.FlightLog;

@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
public interface FlightLogRepository extends JpaRepository<FlightLog, Long>, JpaSpecificationExecutor<FlightLog> {
	List<FlightLog> findByFlightDate(@Param("flightDate") Date flightDate);
}
