package com.kerneldc.flightlogserver.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;

import com.kerneldc.flightlogserver.security.domain.user.User;

public interface UserRepository /*extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>*/ {

	List<User> findAllByOrderByUsername();

	@EntityGraph("userGroupSetPermissionSetGraph")
	List<User> findByUsernameAndEnabled(String username, Boolean enabled);

	@EntityGraph("userGroupSetGraph")
	List<User> findByUsername(String username);

//	@EntityGraph("userGroupSetPermissionSetGraph")
//	Page<User> findAll(@Nullable Specification<User> spec, Pageable pageable);

}
