package com.kerneldc.flightlogserver.security.service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kerneldc.flightlogserver.security.bean.AppUserDetails;
import com.kerneldc.flightlogserver.security.domain.Permission;
import com.kerneldc.flightlogserver.security.domain.group.Group;
import com.kerneldc.flightlogserver.security.domain.user.User;
import com.kerneldc.flightlogserver.security.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		LOGGER.debug("Begin ...");
		List<User> oneUserList = userRepository.findByUsernameAndEnabled(username, true);
		if (oneUserList.isEmpty()) {
			throw new UsernameNotFoundException("User not found with username or email : " + username);
		}

		User user = oneUserList.get(0);
		
		Set<Permission> permissionSet = user.getGroupSet().stream().map(Group::getPermissionSet).flatMap(Set::stream).collect(Collectors.toSet());
        user.setPermissionSet(permissionSet);
		
		LOGGER.debug("End ...");
		return appUserDetailsFromUser(user);
	}

	private AppUserDetails appUserDetailsFromUser(User user) {
		AppUserDetails appUserDetails = new AppUserDetails();
		appUserDetails.setId(user.getId());
		appUserDetails.setUsername(user.getUsername());
		//appUserDetails.setPassword(passwordEncoder.encode(user.getPassword()));
		appUserDetails.setPassword(user.getPassword());
		appUserDetails.setFirstName(user.getFirstName());
		appUserDetails.setLastName(user.getLastName());
		List<GrantedAuthority> authorities = new ArrayList<>();
		user.getPermissionSet().stream().forEach(permission->authorities.add(new SimpleGrantedAuthority(permission.getName())));
		appUserDetails.setAuthorities(authorities);
		return appUserDetails;
	}
}
