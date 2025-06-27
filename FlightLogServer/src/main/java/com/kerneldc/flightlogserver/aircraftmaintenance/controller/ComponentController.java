package com.kerneldc.flightlogserver.aircraftmaintenance.controller;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import jakarta.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.TextNode;
import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentRequest;
import com.kerneldc.flightlogserver.aircraftmaintenance.service.ComponentPersistenceService;
import com.kerneldc.flightlogserver.exception.ApplicationException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/protected/componentController")
@RequiredArgsConstructor
public class ComponentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ComponentPersistenceService componentPersistenceService;
    
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody ComponentRequest componentRequest) throws ApplicationException {
    	LOGGER.debug("addComponentRequest: {}", componentRequest);
    	componentPersistenceService.addComponent(componentRequest);
    	return ResponseEntity.ok(StringUtils.EMPTY);
    }

    @PutMapping("/modifyComponentAndHistory")
    public ResponseEntity<String> modifyComponentAndHistory(@Valid @RequestBody ComponentRequest componentRequest) throws ApplicationException, IllegalAccessException, InvocationTargetException {
    	componentPersistenceService.updateComponentAndHistory(componentRequest);
    	return ResponseEntity.ok(StringUtils.EMPTY);
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam(value = "componentUri") String componentUri, @RequestParam(value = "deleteHistoryRecords") Boolean deleteHistoryRecords) throws ApplicationException {
    	LOGGER.debug("componentUri: {}, deleteHistoryRecords: {}", deleteHistoryRecords, deleteHistoryRecords);
    	componentPersistenceService.deleteComponentAndHistory(componentUri, deleteHistoryRecords);
    	return ResponseEntity.ok(StringUtils.EMPTY);
    }

    @GetMapping("/testDate")
    public ResponseEntity<String> testDate(@RequestParam(value = "testDate") Date testDate) {
    	LOGGER.debug("testDate: {}", testDate);
    	return ResponseEntity.ok(testDate.toString());
    }
    @PostMapping("/testDateAsRequestBody")
    public ResponseEntity<String> testDateAsRequestBody(@RequestBody Date  testDate) {
    	LOGGER.debug("testDate: {}", testDate);
    	return ResponseEntity.ok(testDate.toString());
    }
    @PostMapping("/testStringAsRequestBody")
    public ResponseEntity<String> testStringAsRequestBody(@RequestBody TextNode  testString) {
    	LOGGER.debug("testString: {}", testString.asText());
    	return ResponseEntity.ok(testString.asText());
    }
}
