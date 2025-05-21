package com.kerneldc.flightlogserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kerneldc.flightlogserver.domain.flightlogpending.FlightLogPending;

public interface FlightLogPendingRepository extends JpaRepository<FlightLogPending, Long>, JpaSpecificationExecutor<FlightLogPending> {
//	List<FlightLog> findByFlightDate(@Param("flightDate") Date flightDate);
}
