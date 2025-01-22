package com.kerneldc.flightlogserver.aircraftmaintenance.service;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentHistoryVo;
import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentRequest;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.ComponentRepository;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.PartRepository;
import com.kerneldc.flightlogserver.domain.AbstractEntity;
import com.kerneldc.flightlogserver.exception.ApplicationException;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ComponentPersistenceServiceJpaTest {

	@Autowired
    private TestEntityManager testEntityManager;
	@Autowired
	private PartRepository partRepository;
	@Autowired
    private ComponentRepository componentRepository;
	
	private static String PART_BRAKE_LINING_NAME = "Brake Lining";
	private static String PART_BRAKE_LINING_DESCRIPTION = "P/N 66-105";
	private static String PART_BRAKE_DISK_NAME = "Brake Disk";
	private static String PART_BRAKE_DISK_DESCRIPTION = "P/N RA 64-02000";
	private static String COMPONENT_BRAKE_NAME = "Brake Component";
	private static String COMPONENT_BRAKE_DESCRIPTION = "Brake component";
	private static String COMPONENT_BRAKE_WORK_PERFORMED = "Replaced New";
	private static String COMPONENT_BRAKE_DATE_PERFORMED = "2025-01-18";
	private static float COMPONENT_BRAKE_HOURS_PERFORMED = 5F;
	
	
	private static String COMPONENT_HISTORY_BRAKE_NAME = "Brake Component";
	private static String COMPONENT_HISTORY_BRAKE_DESCRIPTION = "Brake component history 1";
	private static String COMPONENT_HISTORY_BRAKE_WORK_PERFORMED = "Replaced New";
	private static String COMPONENT_HISTORY_BRAKE_DATE_PERFORMED = "2025-01-17";
	private static float COMPONENT_HISTORY_BRAKE_HOURS_PERFORMED = 4F;

	private final String BASE_URI = "http://localhost:6001/protected/data-rest";
	
	private Part partBrakeLining() {
		return Part.builder().name(PART_BRAKE_LINING_NAME).description(PART_BRAKE_LINING_DESCRIPTION).build();
	}
	private Part partBrakeDisk() {
		return Part.builder().name(PART_BRAKE_DISK_NAME).description(PART_BRAKE_DISK_DESCRIPTION).build();
	}
	private Component componentBrakes() {
		var partBrakeDisk = partBrakeDisk();
		partRepository.save(partBrakeDisk);
		return Component.builder().name(COMPONENT_BRAKE_NAME).description(COMPONENT_BRAKE_DESCRIPTION).part(partBrakeDisk)
				.workPerformed(COMPONENT_BRAKE_WORK_PERFORMED).datePerformed(dateOf(COMPONENT_BRAKE_DATE_PERFORMED))
				.hoursPerformed(COMPONENT_BRAKE_HOURS_PERFORMED).deleted(false)
				.build();
	}
	private Component componentBrakesWithOneHistory() {
		var component = componentBrakes();
		var partBrakeLining = partBrakeLining();
		partRepository.save(partBrakeLining);
		var componentHistory = ComponentHistory.builder().name(COMPONENT_HISTORY_BRAKE_NAME).description(COMPONENT_HISTORY_BRAKE_DESCRIPTION).part(partBrakeLining)
				.workPerformed(COMPONENT_HISTORY_BRAKE_WORK_PERFORMED).datePerformed(dateOf(COMPONENT_HISTORY_BRAKE_DATE_PERFORMED))
				.hoursPerformed(COMPONENT_HISTORY_BRAKE_HOURS_PERFORMED)
				.build();
		var componentHistorySet = new LinkedHashSet<ComponentHistory>();
		componentHistorySet.add(componentHistory);
		component.setComponentHistorySet(componentHistorySet);
		return component;
	}

	@Test
	void testSaveComponentBrakes() {
		var savedComponent = componentRepository.saveAndFlush(componentBrakes());
		
		assertThat(savedComponent.getId(), notNullValue());
		assertThat(savedComponent.getPart().getId(), notNullValue());
	}
	@Test
	void testSaveComponentBrakes_withOneHistory_Row() {
		var savedComponent = componentRepository.saveAndFlush(componentBrakesWithOneHistory());

		assertThat(savedComponent.getId(), notNullValue());
		assertThat(savedComponent.getPart().getId(), notNullValue());
		assertThat(savedComponent.getComponentHistorySet().isEmpty(), is(false));
		assertThat(savedComponent.getComponentHistorySet().iterator().next().getId(), notNullValue());
		assertThat(savedComponent.getComponentHistorySet().iterator().next().getPart().getId() , notNullValue());
	}
	@Test
	void testParseAndFindComponent_ValidComponentUri_Success() throws ApplicationException {
		var savedComponent = componentRepository.saveAndFlush(componentBrakes());
		String componentUri = BASE_URI + "/components/" + savedComponent.getId();
		var componentPersistenceService = new ComponentPersistenceService(partRepository, componentRepository);
		Component resultComponent = componentPersistenceService.parseAndFindComponent(componentUri);
		
		assertThat(resultComponent.getId(), equalTo(savedComponent.getId()));
		assertThat(resultComponent.getName(), equalTo(savedComponent.getName()));
		assertThat(resultComponent.getPart().getId(), equalTo(savedComponent.getPart().getId()));
		assertThat(resultComponent.getPart().getName(), equalTo(savedComponent.getPart().getName()));
	}
	@Test
	void testParseAndFindComponent_InvalidComponentUri_Failure() throws ApplicationException {
		String componentUri = BASE_URI + "/componentsXXX/7";
		var componentPersistenceService = new ComponentPersistenceService(partRepository, componentRepository);
		
		assertThrows(ApplicationException.class,
				() -> componentPersistenceService.parseAndFindComponent(componentUri));
	}
	
	@Test
	void testUpdateComponentAndHistory_ModifyComponentWithNoHistory_Success() throws ApplicationException {
		var component = componentRepository.saveAndFlush(componentBrakes());
		var savedComponentVersion = component.getVersion();
		
		var newDescription = "Brake component new description";
		ComponentRequest componentRequest = ComponentRequest.builder()
				.componentUri(BASE_URI + "/components/" + component.getId()).name(COMPONENT_BRAKE_NAME)
				.description(newDescription)
				.workPerformed(COMPONENT_BRAKE_WORK_PERFORMED).datePerformed(dateOf(COMPONENT_BRAKE_DATE_PERFORMED))
				.hoursPerformed(COMPONENT_BRAKE_HOURS_PERFORMED).partUri(BASE_URI + "/parts/" + component.getPart().getId())
				.build();
		
		var componentPersistenceService = new ComponentPersistenceService(partRepository, componentRepository);
		componentPersistenceService.updateComponentAndHistory(componentRequest);
		testEntityManager.flush();
		
		assertThat(component.getVersion(), is(savedComponentVersion+1));
		assertThat(component.getDescription(), is(newDescription));
		assertThat(component.getComponentHistorySet().isEmpty(), is(true));
	}

	@Test
	void testUpdateComponentAndHistory_ModifyComponentAndAddHistoryRecord_Success() throws ApplicationException {
		var component = componentRepository.saveAndFlush(componentBrakes());
		var savedComponentVersion = component.getVersion();
		// Set up request component
		ComponentRequest componentRequest = ComponentRequest.builder()
				.componentUri(BASE_URI + "/components/" + component.getId()).name(COMPONENT_BRAKE_NAME)
				.description(COMPONENT_BRAKE_DESCRIPTION)
				.workPerformed(COMPONENT_BRAKE_WORK_PERFORMED).datePerformed(dateOf(COMPONENT_BRAKE_DATE_PERFORMED))
				.hoursPerformed(COMPONENT_BRAKE_HOURS_PERFORMED).partUri(BASE_URI + "/parts/" + component.getPart().getId())
				.build();
		// Set up request history record
		var partBrakeLining = partBrakeLining();
		partRepository.save(partBrakeLining);
		ComponentHistoryVo componentHistoryVo = ComponentHistoryVo.builder().name(COMPONENT_HISTORY_BRAKE_NAME)
				.description(COMPONENT_HISTORY_BRAKE_DESCRIPTION).workPerformed(COMPONENT_HISTORY_BRAKE_WORK_PERFORMED).datePerformed(dateOf(COMPONENT_HISTORY_BRAKE_DATE_PERFORMED))
				.hoursPerformed(COMPONENT_HISTORY_BRAKE_HOURS_PERFORMED).partUri(BASE_URI + "/parts/" + partBrakeLining.getId()).build();
		componentRequest.getHistoryRequestSet().add(componentHistoryVo);
		
		var componentPersistenceService = new ComponentPersistenceService(partRepository, componentRepository);
		componentPersistenceService.updateComponentAndHistory(componentRequest);
		testEntityManager.flush();
		
		assertThat(component.getVersion(), is(savedComponentVersion+1));
		assertThat(component.getComponentHistorySet().isEmpty(), is(false));
		assertThat(component.getComponentHistorySet().iterator().next().getId(), is(not(0)));
	}

	@Test
	void testUpdateComponentAndHistory_ModifyComponentAndModifyHistoryRecord_Success() throws ApplicationException {
		var component = componentRepository.saveAndFlush(componentBrakesWithOneHistory());
		var savedComponentVersion = component.getVersion();
		
		// Set up request component
		var newComponentDescription = "Brake component new description";
		ComponentRequest componentRequest = ComponentRequest.builder()
				.componentUri(BASE_URI + "/components/" + component.getId()).name(COMPONENT_BRAKE_NAME)
				.description(newComponentDescription)
				.workPerformed(COMPONENT_BRAKE_WORK_PERFORMED).datePerformed(dateOf(COMPONENT_BRAKE_DATE_PERFORMED))
				.hoursPerformed(COMPONENT_BRAKE_HOURS_PERFORMED).partUri(BASE_URI + "/parts/" + component.getPart().getId())
				.build();
		// Set up request history record
		var newComponenHistoryDescription = "Brake component history new description";
		var componentHistory = component.getComponentHistorySet().iterator().next();
		var componentHistoryPart = componentHistory.getPart();
		ComponentHistoryVo componentHistoryVo = ComponentHistoryVo.builder()
				.historyUri(BASE_URI + "/componentHistories/" + componentHistory.getId()).name(COMPONENT_HISTORY_BRAKE_NAME)
				.description(newComponenHistoryDescription)
				.workPerformed(COMPONENT_HISTORY_BRAKE_WORK_PERFORMED).datePerformed(dateOf(COMPONENT_HISTORY_BRAKE_DATE_PERFORMED))
				.hoursPerformed(COMPONENT_HISTORY_BRAKE_HOURS_PERFORMED).partUri(BASE_URI + "/parts/" + componentHistoryPart.getId()).build();
		componentRequest.getHistoryRequestSet().add(componentHistoryVo);

		var componentPersistenceService = new ComponentPersistenceService(partRepository, componentRepository);		
		componentPersistenceService.updateComponentAndHistory(componentRequest);
		testEntityManager.flush();
		
		assertThat(component.getVersion(), is(savedComponentVersion+1));
		assertThat(component.getDescription(), is(newComponentDescription));
		assertThat(component.getComponentHistorySet().size(), is(1));
		assertThat(component.getComponentHistorySet().iterator().next().getDescription(), is(newComponenHistoryDescription));
	}

	@Test
	void testUpdateComponentAndHistory_ModifyComponentAndDeleteHistoryRecord_Success() throws ApplicationException {
		var component = componentRepository.saveAndFlush(componentBrakesWithOneHistory());
		var savedComponentVersion = component.getVersion();
		// Set up request component with no history record
		ComponentRequest componentRequest = ComponentRequest.builder()
				.componentUri(BASE_URI + "/components/" + component.getId()).name(COMPONENT_BRAKE_NAME)
				.description(COMPONENT_BRAKE_DESCRIPTION)
				.workPerformed(COMPONENT_BRAKE_WORK_PERFORMED).datePerformed(dateOf(COMPONENT_BRAKE_DATE_PERFORMED))
				.hoursPerformed(COMPONENT_BRAKE_HOURS_PERFORMED).partUri(BASE_URI + "/parts/" + component.getPart().getId())
				.build();
		
		var componentPersistenceService = new ComponentPersistenceService(partRepository, componentRepository);		
		componentPersistenceService.updateComponentAndHistory(componentRequest);
		testEntityManager.flush();
		
		assertThat(component.getVersion(), is(savedComponentVersion+1));
		assertThat(component.getComponentHistorySet().isEmpty(), is(true));
	}

	@Test
	void testUpdateComponentAndHistory_ModifyComponentAndDeleteComponentRecord_Success() throws ApplicationException {
		var component = componentRepository.saveAndFlush(componentBrakesWithOneHistory());
		var savedComponentVersion = component.getVersion();
		var savedComponentHistoryPartId = component.getComponentHistorySet().iterator().next().getPart().getId();
		// Set up request component with no history record
		ComponentRequest componentRequest = ComponentRequest.builder()
				.componentUri(BASE_URI + "/components/" + component.getId()).name(COMPONENT_HISTORY_BRAKE_NAME)
				.description(COMPONENT_HISTORY_BRAKE_DESCRIPTION)
				.workPerformed(COMPONENT_HISTORY_BRAKE_WORK_PERFORMED).datePerformed(dateOf(COMPONENT_HISTORY_BRAKE_DATE_PERFORMED))
				.hoursPerformed(COMPONENT_HISTORY_BRAKE_HOURS_PERFORMED)
				.partUri(BASE_URI + "/parts/" + component.getComponentHistorySet().iterator().next().getPart().getId())
				.build();
		
		var componentPersistenceService = new ComponentPersistenceService(partRepository, componentRepository);		
		componentPersistenceService.updateComponentAndHistory(componentRequest);
		testEntityManager.flush();
		
		assertThat(component.getVersion(), is(savedComponentVersion+1));
		assertThat(component.getComponentHistorySet().isEmpty(), is(true));
		assertThat(component.getPart().getId(), is(savedComponentHistoryPartId));
	}

	private Date dateOf(String dateString) {
		try {
			return AbstractEntity.DATE_FORMAT.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
