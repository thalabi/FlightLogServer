package com.kerneldc.flightlogserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.kerneldc.flightlogserver.domain.Pilot;

@RepositoryRestResource
@CrossOrigin(origins = "http://localhost:4200")
public interface PilotRepository extends JpaRepository<Pilot, Long>, JpaSpecificationExecutor<Pilot> {
	List<Pilot> findAllByOrderByName();
}
