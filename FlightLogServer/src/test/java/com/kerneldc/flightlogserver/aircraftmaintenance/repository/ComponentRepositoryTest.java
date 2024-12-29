package com.kerneldc.flightlogserver.aircraftmaintenance.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ComponentRepositoryTest {

	@Autowired
    private TestEntityManager testEntityManager;
	@Autowired
    private PartRepository partRepository;
	@Autowired
    private ComponentRepository componentRepository;

	private static final String PART1_NAME = "Oil";
	private static final String PART2_NAME = "Oil Filter";
	private static final String COMPONENT_NAME = "Component 1";

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
	void testFind_Component_With_History_Success() {
		Part part1 = new Part();
		part1.setName(PART1_NAME);
		Part savedPart1 = testEntityManager.persist(part1);
		Component component1 = Component.builder().name(COMPONENT_NAME).deleted(false).part(savedPart1).build();
		
		ComponentHistory ch = ComponentHistory.builder().workPerformed("Changed").hoursPerformed(50f)
				.datePerformed(new Date()).build();
		ComponentHistory savedComponentHistory = testEntityManager.persist(ch);
		
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
