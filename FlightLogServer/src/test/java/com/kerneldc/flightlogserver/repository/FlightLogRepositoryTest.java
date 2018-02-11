package com.kerneldc.flightlogserver.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.test.context.junit4.SpringRunner;

import com.kerneldc.flightlogserver.FlightLogServerApplication;
import com.kerneldc.flightlogserver.domain.FlightLog;
import com.kerneldc.flightlogserver.domain.FlightLogSpecification;
import com.kerneldc.flightlogserver.domain.SearchCriteria;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlightLogServerApplication.class)
@Transactional
public class FlightLogRepositoryTest {

	@Autowired
    private FlightLogRepository flightLogRepository;
	
	@Test
	public void testFindAll_Registration_Success() {
		FlightLogSpecification spec = new FlightLogSpecification(new SearchCriteria("registration", ":", "GYAX"));
		List<FlightLog> results = flightLogRepository.findAll(Specifications.where(spec));
		System.out.println(results);
		System.out.println(results.size());
	}

	@Test
	public void testFindAll_DaySolo_Success() {
		FlightLogSpecification spec = new FlightLogSpecification(new SearchCriteria("daySolo", ">", "4"));
		List<FlightLog> results = flightLogRepository.findAll(Specifications.where(spec));
		System.out.println(results);
		System.out.println(results.size());
	}
}
