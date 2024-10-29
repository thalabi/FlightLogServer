package com.kerneldc.flightlogserver.security.repository;

import java.util.List;

import com.kerneldc.flightlogserver.security.domain.Permission;

public interface PermissionRepository /*extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission>*/ {
	List<Permission> findAllByOrderByName();
}
