package com.kerneldc.flightlogserver.aircraftmaintenance.controller;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.TextNode;
import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentRequest;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.ComponentResource;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.ComponentResourceAssembler;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.ComponentRepository;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.PartRepository;
import com.kerneldc.flightlogserver.controller.ControllerHelper;
import com.kerneldc.flightlogserver.domain.EntitySpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.exception.ApplicationException;

@RestController
@RequestMapping("componentController")
@ExposesResourceFor(Component.class) // needed for unit test to create entity links
public class ComponentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final UriTemplate PART_URI_TEMPLATE = new UriTemplate("{protocol}://{host}:{port}/parts/{id}");
	private static final UriTemplate COMPONENT_URI_TEMPLATE = new UriTemplate("{protocol}://{host}:{port}/components/{id}");

    private ComponentRepository componentRepository;
    private PartRepository partRepository;
	private ComponentResourceAssembler componentResourceAssembler;

    public ComponentController(ComponentRepository componentRepository, /*ComponentHistoryRepository componentHistoryRepository,*/ PartRepository partRepository, ComponentResourceAssembler componentResourceAssembler) throws JsonProcessingException {
        this.componentRepository = componentRepository;
        this.partRepository = partRepository;
        this.componentResourceAssembler = componentResourceAssembler;
    }

    @GetMapping("/findAll")
	public PagedResources<ComponentResource> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<Component> pagedResourcesAssembler) {
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<Component> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<Component> componentPage = componentRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(ComponentController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		return pagedResourcesAssembler.toResource(componentPage, componentResourceAssembler, link);
    }
    
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody ComponentRequest componentRequest) throws ApplicationException {
    	LOGGER.debug("addComponentRequest: {}", componentRequest);
    	Part part = parseAndFindPart(componentRequest.getPartUri()).get();
    	Component component = new Component();
    	BeanUtils.copyProperties(componentRequest, component);
    	component.setPart(part);
    	component.setDeleted(false);
    	componentRepository.save(component);
    	LOGGER.info("component: {}", component);
    	return ResponseEntity.ok(StringUtils.EMPTY);
    }
    
    @Transactional
    @PutMapping("/modify")
    public ResponseEntity<String> modify(@Valid @RequestBody ComponentRequest componentRequest) throws ApplicationException {
    	LOGGER.debug("modifyComponentRequest: {}", componentRequest);
    	Component component = parseAndFindComponent(componentRequest.getComponentUri()).get();
    	ComponentHistory componentHistory = null;
    	if (componentRequest.getCreateHistoryRecord().equals(true)) {
    		componentHistory = ComponentHistory.builder().workPerformed(component.getWorkPerformed())
    				.datePerformed(component.getDatePerformed()).hoursPerformed(component.getHoursPerformed())
    				.created(component.getCreated()).modified(component.getModified()).build();
    	}
    	Part part = parseAndFindPart(componentRequest.getPartUri()).get();
    	BeanUtils.copyProperties(componentRequest, component);
    	component.setPart(part);
    	if (componentHistory != null) {
    		component.getComponentHistorySet().add(componentHistory);
    	}
    	component.setDeleted(false);
    	componentRepository.save(component);
    	LOGGER.info("component: {}", component);
    	return ResponseEntity.ok(StringUtils.EMPTY);
    }
    
    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam(value = "componentUri") String componentUri) throws ApplicationException {
    	LOGGER.debug("componentUri: {}", componentUri);
    	Component component = parseAndFindComponent(componentUri).get();
    	componentRepository.delete(component);
    	return ResponseEntity.ok(StringUtils.EMPTY);
    }
    
    private Optional<Part> parseAndFindPart(String partUri) throws ApplicationException {
    	Long partId;
    	try {
    		partId = parseId(partUri, PART_URI_TEMPLATE);	
    	} catch (NumberFormatException e) {
    		throw new ApplicationException(String.format("Could not parse part ID from uri: %s", partUri));
    	}
    	Optional<Part> optionalPart = partRepository.findById(partId);
    	if (!optionalPart.isPresent()) {
    		throw new ApplicationException(String.format("Part ID: %d not found", partId));
    	}
    	return optionalPart;
    }
    
    private Optional<Component> parseAndFindComponent(String componentUri) throws ApplicationException {
    	Long componentId;
    	try {
    		componentId = parseId(componentUri, COMPONENT_URI_TEMPLATE);	
    	} catch (NumberFormatException e) {
    		throw new ApplicationException(String.format("Could not parse component ID from uri: %s", componentUri));
    	}
    	Optional<Component> optionalComponent = componentRepository.findById(componentId);
    	if (!optionalComponent.isPresent()) {
    		throw new ApplicationException(String.format("Component ID: %d not found", componentId));
    	}
    	return optionalComponent;
    }
    
    private Long parseId(String uri, UriTemplate uriTemplate) {
    	Map<String, String> parameterMap = uriTemplate.match(uri);
    	return Long.parseLong(parameterMap.get("id"));
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
