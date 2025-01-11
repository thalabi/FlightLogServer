package com.kerneldc.flightlogserver.aircraftmaintenance.controller;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.ComponentModelAssembler;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.ComponentRepository;
import com.kerneldc.flightlogserver.aircraftmaintenance.service.ComponentPersistenceService;
import com.kerneldc.flightlogserver.exception.ApplicationException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/protected/componentController")
//@ExposesResourceFor(Component.class) // needed for unit test to create entity links
@RequiredArgsConstructor
public class ComponentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
//	private static final UriTemplate PART_URI_TEMPLATE = new UriTemplate("{protocol}://{host}:{port}/parts/{id}");
//	private static final UriTemplate COMPONENT_URI_TEMPLATE = new UriTemplate("{protocol}://{host}:{port}/components/{id}");

    private final ComponentRepository componentRepository;
    private final ComponentPersistenceService componentPersistenceService;
	private final ComponentModelAssembler componentModelAssembler;
	private final EntityManager entityManager;

//    public ComponentController(ComponentRepository componentRepository, /*PartRepository partRepository,*/ ComponentPersistenceService componentPersistenceService, ComponentModelAssembler componentModelAssembler) throws JsonProcessingException {
//        this.componentRepository = componentRepository;
////        this.partRepository = partRepository;
//        this.componentPersistenceService = componentPersistenceService;
//        this.componentModelAssembler = componentModelAssembler;
//    }

//    @GetMapping("/findAllOld")
//	public PagedModel<ComponentModel> findAllOld(
//			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<Component> pagedResourcesAssembler) {
//    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
//    	EntitySpecificationsBuilder<Component> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
//        Page<Component> componentPage = componentRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
//        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ComponentController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
//		return pagedResourcesAssembler.toModel(componentPage, componentModelAssembler, link);
//    }
//    @GetMapping("/findAll")
//	public PagedModel<ComponentModel> findAll(
//			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<Component> pagedResourcesAssembler) {
//    	
//    	var entityMetamodel = entityManager.getMetamodel().entity(Component.class);
//    	Specification<Component> entitySpecification = new EntitySpecification<>(entityMetamodel, search);
//    	
//        Page<Component> componentPage = componentRepository.findAll(entitySpecification, pageable);
//        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ComponentController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
//		return pagedResourcesAssembler.toModel(componentPage, componentModelAssembler, link);
//    }
    
    @Transactional
    @PostMapping("/add")
    public ResponseEntity<String> add(@Valid @RequestBody ComponentRequest componentRequest) throws ApplicationException {
    	LOGGER.debug("addComponentRequest: {}", componentRequest);
    	Part part = componentPersistenceService.parseAndFindPart(componentRequest.getPartUri());
    	 
    	Component component = new Component();
    	BeanUtils.copyProperties(componentRequest, component);
    	component.setPart(part);
    	component.setDeleted(false);
    	componentRepository.save(component);
    	LOGGER.info("component: {}", component);
    	return ResponseEntity.ok(StringUtils.EMPTY);
    }
    
//    @Transactional
//    @PutMapping("/modify")
//    public ResponseEntity<String> modify(@Valid @RequestBody ComponentRequest componentRequest) throws ApplicationException {
//    	LOGGER.debug("componentRequest: {}", componentRequest);
//    	Component component = componentPersistenceService.parseAndFindComponent(componentRequest.getComponentUri());
//    	ComponentHistory componentHistory = null;
//    	if (componentRequest.getCreateHistoryRecord()) {
//    		componentHistory = ComponentHistory.builder()
//    				.name(component.getName())
//    				.description(component.getDescription())
//    				.part(component.getPart())
//    				.workPerformed(component.getWorkPerformed())
//    				.datePerformed(component.getDatePerformed()).hoursPerformed(component.getHoursPerformed())
//    				.dateDue(component.getDateDue()).hoursDue(component.getHoursDue())
//    				.created(componentRequest.getModified()).modified(componentRequest.getModified()).build();
//    	}
//    	Part part = componentPersistenceService.parseAndFindPart(componentRequest.getPartUri());
//    	BeanUtils.copyProperties(componentRequest, component);
//    	component.setPart(part);
//    	if (componentHistory != null) {
//    		component.getComponentHistorySet().add(componentHistory);
//    	}
//    	component.setDeleted(false);
//    	componentRepository.save(component);
//    	LOGGER.info("component: {}", component);
//    	return ResponseEntity.ok(StringUtils.EMPTY);
//    }
    
    @Transactional
    @PutMapping("/modifyComponentAndHistory")
    public ResponseEntity<String> modifyComponentAndHistory(@Valid @RequestBody ComponentRequest componentRequest) throws ApplicationException, IllegalAccessException, InvocationTargetException {
    	componentPersistenceService.updateComponentAndHistory(componentRequest);
    	return ResponseEntity.ok(StringUtils.EMPTY);
    }
    
    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam(value = "componentUri") String componentUri, @RequestParam(value = "deleteHistoryRecords") Boolean deleteHistoryRecords) throws ApplicationException {
    	LOGGER.debug("componentUri: {}, deleteHistoryRecords: {}", deleteHistoryRecords, deleteHistoryRecords);
    	Component component = componentPersistenceService.parseAndFindComponent(componentUri);
		Set<ComponentHistory> componentHistorySet = component.getComponentHistorySet();
    	if (deleteHistoryRecords || CollectionUtils.isEmpty(componentHistorySet)) {
    		componentRepository.delete(component);
    	} else {
    		LOGGER.info("componentHistorySet: {}", componentHistorySet);
    		component.setName(componentHistorySet.iterator().next().getName());
    		component.setDescription(componentHistorySet.iterator().next().getDescription());
    		component.setPart(componentHistorySet.iterator().next().getPart());
    		component.setWorkPerformed(componentHistorySet.iterator().next().getWorkPerformed());
    		component.setDatePerformed(componentHistorySet.iterator().next().getDatePerformed());
    		component.setHoursPerformed(componentHistorySet.iterator().next().getHoursPerformed());
    		component.setDateDue(componentHistorySet.iterator().next().getDateDue());
    		component.setHoursDue(componentHistorySet.iterator().next().getHoursDue());
    		component.setCreated(componentHistorySet.iterator().next().getCreated());
    		component.setModified(componentHistorySet.iterator().next().getModified());
    		componentHistorySet.remove(componentHistorySet.iterator().next());

    		componentRepository.save(component);
    	}
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
