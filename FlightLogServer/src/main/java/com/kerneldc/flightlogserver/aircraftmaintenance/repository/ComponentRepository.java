package com.kerneldc.flightlogserver.aircraftmaintenance.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componentandhistoryview.ComponentAndHistoryV;
import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.domain.IEntityEnum;
import com.kerneldc.flightlogserver.repository.BaseTableRepository;

public interface ComponentRepository extends BaseTableRepository<Component, Long> {

	@Override
	@EntityGraph("componentFullGraph")
	Page<Component> findAll(Specification<Component> spec, Pageable pageable);
	
	List<Component> findByNameOrderByName(String name);
	
	List<Component> findByDateDueLessThanOrHoursDueLessThanOrderByDateDueDescHoursDueDesc(Date dateDue, Float hoursDue);

	@Override
	default IEntityEnum canHandle() {
		return FlightLogEntityEnum.COMPONENT;
	}

}
