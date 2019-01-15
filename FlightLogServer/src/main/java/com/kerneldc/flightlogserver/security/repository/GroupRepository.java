package com.kerneldc.flightlogserver.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.kerneldc.flightlogserver.security.domain.group.Group;

@RepositoryRestResource
public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {
	List<Group> findAllByOrderByName();
}
