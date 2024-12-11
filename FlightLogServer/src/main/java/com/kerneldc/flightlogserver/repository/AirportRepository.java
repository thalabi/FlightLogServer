package com.kerneldc.flightlogserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kerneldc.flightlogserver.domain.airport.Airport;

public interface AirportRepository extends JpaRepository<Airport, Long>, JpaSpecificationExecutor<Airport> {
//	List<Airport> findAllByOrderByIdentifier();
//	List<Airport> findAllByOrderByName();
//	List<Airport> findByIdentifierContainingIgnoreCaseOrNameContainingIgnoreCase(@Param("identifier") String identifier, @Param("name") String name);
}
