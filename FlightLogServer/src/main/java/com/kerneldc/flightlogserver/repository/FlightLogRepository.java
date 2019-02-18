package com.kerneldc.flightlogserver.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import com.kerneldc.flightlogserver.domain.flightLog.FlightLog;

public interface FlightLogRepository extends JpaRepository<FlightLog, Long>, JpaSpecificationExecutor<FlightLog> {
	List<FlightLog> findByFlightDate(@Param("flightDate") Date flightDate);
}
