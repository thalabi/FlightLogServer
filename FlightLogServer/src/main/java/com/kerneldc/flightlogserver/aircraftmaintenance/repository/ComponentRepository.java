package com.kerneldc.flightlogserver.aircraftmaintenance.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;

public interface ComponentRepository extends JpaRepository<Component, Long>, JpaSpecificationExecutor<Component> {

//	@EntityGraph("componentPartGraph")
//	List<Component> findAllByOrderByName();
	
	@Override
	@EntityGraph("componentFullGraph")
	Page<Component> findAll(Specification<Component> spec, Pageable pageable);
	
	List<Component> findByNameOrderByName(String name);
}
