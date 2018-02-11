package com.kerneldc.flightlogserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.kerneldc.flightlogserver.domain.Airport;

@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
public interface AirportRepository extends JpaRepository<Airport, Long>, JpaSpecificationExecutor<Airport> {
	List<Airport> findAllByOrderByIdentifier();
	List<Airport> findByIdentifierContainingIgnoreCaseOrNameContainingIgnoreCase(@Param("identifier") String identifier, @Param("name") String name);
}
