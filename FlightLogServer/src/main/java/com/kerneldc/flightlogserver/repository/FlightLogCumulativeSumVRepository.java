package com.kerneldc.flightlogserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.kerneldc.flightlogserver.domain.FlightLogCumulativeSumV;

@RepositoryRestResource
public interface FlightLogCumulativeSumVRepository extends JpaRepository<FlightLogCumulativeSumV, Long>, JpaSpecificationExecutor<FlightLogCumulativeSumV> {
	List<FlightLogCumulativeSumV> findAllByOrderById();
}
