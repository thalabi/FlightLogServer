package com.kerneldc.flightlogserver.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.kerneldc.flightlogserver.security.domain.User;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	@EntityGraph("userGraph")
	List<User> findByUsernameAndEnabled(String username, Boolean enabled);
	@EntityGraph("userGraph")
	List<User> findByUsername(String username);
}
