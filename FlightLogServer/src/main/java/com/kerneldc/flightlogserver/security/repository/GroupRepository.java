package com.kerneldc.flightlogserver.security.repository;

import java.util.List;

import com.kerneldc.flightlogserver.security.domain.group.Group;

public interface GroupRepository /*extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group>*/ {
	List<Group> findAllByOrderByName();
}
