package com.kerneldc.flightlogserver.security.service;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kerneldc.flightlogserver.exception.ApplicationException;
import com.kerneldc.flightlogserver.security.domain.user.User;
import com.kerneldc.flightlogserver.security.repository.UserRepository;

@Service
public class SecurityPersistenceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
    private UserRepository userRepository;

	//@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional()
	public void changePassword(String username, String oldPassword, String newPassword) throws ApplicationException {
		LOGGER.debug("Begin ...");
		List<User> oneUserList = userRepository.findByUsername(username);
		if (oneUserList.isEmpty()) {
			throw new ApplicationException(String.format("Cannot find user with username: %s", username));
		}
		User user = oneUserList.get(0); 
		if (! /* not */ passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new ApplicationException("Old password does not match one on record");
		}
		user.setPassword(newPassword);
		user.setModified(new Date());
		userRepository.save(user);
		LOGGER.debug("End ...");

	}

	@Transactional()
	public User copyUser(String fromUsername, String toUsername) throws ApplicationException {
		LOGGER.debug("Begin ...");
    	List<User> fromUserListOfOne = userRepository.findByUsername(fromUsername);
    	if (CollectionUtils.isEmpty(fromUserListOfOne)) {
    		throw new ApplicationException(String.format("Username [%s] not found", fromUsername), new EntityNotFoundException());
    	}
    	List<User> toUserListOfOne = userRepository.findByUsername(toUsername);
    	if (! /* not */ CollectionUtils.isEmpty(toUserListOfOne)) {
    		throw new ApplicationException(String.format("Username [%s] already exists", toUsername), new EntityExistsException());
    	}
		User toUser = SerializationUtils.clone(fromUserListOfOne.get(0));
		toUser.setId(null);
		toUser.setUsername(toUsername);
		toUser.setPassword("******");
		userRepository.save(toUser);
		LOGGER.debug("End ...");
		return toUser;
	}

}
