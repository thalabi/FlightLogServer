package com.kerneldc.flightlogserver.aircraftmaintenance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;

public interface PartRepository extends JpaRepository<Part, Long>, JpaSpecificationExecutor<Part> {

	List<Part> findAllByOrderByName();

}
