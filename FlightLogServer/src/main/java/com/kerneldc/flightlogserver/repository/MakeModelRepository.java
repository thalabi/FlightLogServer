package com.kerneldc.flightlogserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.kerneldc.flightlogserver.domain.makeModel.MakeModel;

public interface MakeModelRepository extends JpaRepository<MakeModel, Long>, JpaSpecificationExecutor<MakeModel> {
//	List<MakeModel> findAllByOrderByMakeModel();
}
