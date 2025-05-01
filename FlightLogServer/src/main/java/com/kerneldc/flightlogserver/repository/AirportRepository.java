package com.kerneldc.flightlogserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.IEntityEnum;
import com.kerneldc.flightlogserver.domain.airport.Airport;

public interface AirportRepository extends BaseTableRepository<Airport, Long> {

	List<Airport> findByIdentifierContainingIgnoreCaseOrNameContainingIgnoreCase(@Param("identifier") String identifier,
			@Param("name") String name);
	
	@Query(value = "select identifier from airport", nativeQuery = true)
	List<String> getListOfIdentifiers();
	
	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.AIRPORT;
	}

}
