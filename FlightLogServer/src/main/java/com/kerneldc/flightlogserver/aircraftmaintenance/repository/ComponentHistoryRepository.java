package com.kerneldc.flightlogserver.aircraftmaintenance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;

public interface ComponentHistoryRepository extends JpaRepository<ComponentHistory, Long>, JpaSpecificationExecutor<ComponentHistory> {

}
