package com.kerneldc.flightlogserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kerneldc.flightlogserver.domain.pilot.Pilot;

public interface PilotRepository extends JpaRepository<Pilot, Long>, JpaSpecificationExecutor<Pilot> {
//	List<Pilot> findAllByOrderByPilot();
}
