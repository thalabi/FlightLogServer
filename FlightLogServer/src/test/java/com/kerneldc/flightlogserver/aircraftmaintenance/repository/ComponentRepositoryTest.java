package com.kerneldc.flightlogserver.aircraftmaintenance.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.domain.FlightLogEntityEnum;
import com.kerneldc.flightlogserver.repository.BaseEntityRepository;
import com.kerneldc.flightlogserver.repository.EntityRepositoryFactory;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ComponentRepositoryTest {

	@Autowired
    private TestEntityManager testEntityManager;
	@Autowired
    private PartRepository partRepository;
	@Autowired
    private ComponentRepository componentRepository;
	//@Autowired
	//private EntityRepositoryFactory<?, ?> entityRepositoryFactory;

	private static final String PART1_NAME = "Oil";
	//private static final String PART2_NAME = "Oil Filter";
	private static final String COMPONENT_NAME = "Component Oil Change";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Test
	void testFind_Part_Success() {
		Part part1 = new Part();
		part1.setName(PART1_NAME);
		testEntityManager.persist(part1);
		List<Part> partList = partRepository.findAll();
		assertThat(partList, hasSize(1));
	}
	@Test
	void testFind_Component_Success() {
		Part part1 = new Part();
		part1.setName(PART1_NAME);
		Part savedPart1 = testEntityManager.persist(part1);
		Component component1 = Component.builder().name(COMPONENT_NAME).deleted(false).part(savedPart1).build();
		testEntityManager.persist(component1);
		List<Component> componentList = componentRepository.findAll();
		assertThat(componentList, hasSize(1));
	}
	
	@Test
	void testGetRepository_Component_Success() {
		var entityRepositoryFactory = new EntityRepositoryFactory<>(List.of(componentRepository));
		BaseEntityRepository<Component, Long> repository = entityRepositoryFactory.getRepository(FlightLogEntityEnum.COMPONENT);
		assertThat(repository, notNullValue());
	}
	@Test
	void testFind_ComponentWithHistory_Success() {
		Part part1 = new Part();
		part1.setName(PART1_NAME);
		Part savedPart1 = testEntityManager.persist(part1);
		Component component1 = Component.builder().name(COMPONENT_NAME).deleted(false).part(savedPart1).build();
		
		ComponentHistory ch1 = ComponentHistory.builder().workPerformed("Changed").hoursPerformed(50f)
				.datePerformed(new Date()).build();
		ComponentHistory savedComponentHistory = testEntityManager.persist(ch1);
		
		component1.getComponentHistorySet().add(savedComponentHistory);
		
		testEntityManager.persist(component1);
		testEntityManager.flush();
		List<Component> componentList = componentRepository.findAll();
		assertThat(componentList, hasSize(1));
		assertThat(componentList.get(0).getComponentHistorySet(), hasSize(1));
		componentList.get(0).getComponentHistorySet().forEach(ch2 -> {
			System.out.println(ch2.getWorkPerformed());
		});
	}

	@Test
	void testFindUsingEntityRepositoryFactory_ComponentWithHistory_Success() throws ParseException {
		Part part1 = new Part();
		part1.setName(PART1_NAME);
		Part savedPart1 = testEntityManager.persist(part1);
		Component component1 = Component.builder().name(COMPONENT_NAME).deleted(false).part(savedPart1).build();
		
		ComponentHistory ch1 = ComponentHistory.builder().workPerformed("Changed at 50 hrs").hoursPerformed(50f)
				.datePerformed(DATE_FORMAT.parse("2024-06-29")).build();
		ComponentHistory savedComponentHistory1 = testEntityManager.persist(ch1);
		ComponentHistory ch2 = ComponentHistory.builder().workPerformed("Changed at 100 hrs").hoursPerformed(100f)
				.datePerformed(DATE_FORMAT.parse("2024-12-29")).build();
		ComponentHistory savedComponentHistory2 = testEntityManager.persist(ch2);
		
		component1.getComponentHistorySet().add(savedComponentHistory1);
		component1.getComponentHistorySet().add(savedComponentHistory2);
		
		testEntityManager.persist(component1);
		testEntityManager.flush();
		var entityRepositoryFactory = new EntityRepositoryFactory<>(List.of(componentRepository));
		
		List<Component> componentList = entityRepositoryFactory.getRepository(FlightLogEntityEnum.COMPONENT).findAll();
		
		assertThat(componentList, hasSize(1));
		System.out.println(String.format("component: [%s]", componentList.get(0)));
		assertThat(componentList.get(0).getComponentHistorySet(), hasSize(2));
		componentList.get(0).getComponentHistorySet().forEach(ch -> {
			System.out.println(ch.getWorkPerformed());
		});
	}
	
	@Test
	void testDelete_CheckComponentHistoryDeleted_Success() {
		
		Part part = Part.builder().name("Champion HD1408").build();
		part = testEntityManager.persist(part);
		assertThat(part.getId(), notNullValue());
		Component component = Component.builder().name("Oil Filter").part(part).workPerformed("Replaced")
				.datePerformed(new Date()).hoursPerformed(1000f).deleted(false).build();
		ComponentHistory componentHistory = ComponentHistory.builder().workPerformed("Replaced")
				.datePerformed(new Date()).hoursPerformed(9950f).build();
		component.setComponentHistorySet(Collections.singleton(componentHistory));
		componentRepository.save(component);
		assertThat(component.getId(), notNullValue());
		
		List<Component> savedComponentList = componentRepository.findAll();
		assertThat(savedComponentList.isEmpty(), equalTo(false));
		Component savedComponent = savedComponentList.get(0);
		
		componentRepository.delete(savedComponent);
		componentRepository.flush();
		
		ComponentHistory deletedComponetHistory = testEntityManager.find(ComponentHistory.class, componentHistory.getId());
		assertThat(deletedComponetHistory, nullValue());
	}

}
