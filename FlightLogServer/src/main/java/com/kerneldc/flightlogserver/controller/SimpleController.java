package com.kerneldc.flightlogserver.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.domain.pilot.Pilot;
import com.kerneldc.flightlogserver.repository.PilotRepository;

@RestController
@RequestMapping("simpleController")
public class SimpleController {

	private PilotRepository pilotRepository;
	
    public SimpleController(PilotRepository repository) {
        this.pilotRepository = repository;
    }

    @GetMapping("/findAll")
	public List<Pilot> findAll() {
    	return pilotRepository.findAll();
    }
}
