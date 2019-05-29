package com.kerneldc.flightlogserver.aircraftmaintenance.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTest {

	@Autowired
    private TestEntityManager entityManager;
	@Autowired
    private PartRepository partRepository;
	@Autowired
    private ComponentRepository componentRepository;

	private static final String PART1_NAME = "Oil";
	private static final String PART2_NAME = "Oil Filter";
	private static final String COMPONENT_NAME = "Component 1";

	@Test
	public void testFind_Part_Success() {
		Part part1 = new Part();
		part1.setName(PART1_NAME);
		entityManager.persist(part1);
		List<Part> partList = partRepository.findAll();
		assertThat(partList, hasSize(1));
	}
	@Test
	public void testFind_Component_Success() {
		Part part1 = new Part();
		part1.setName(PART1_NAME);
		Part savedPart1 = entityManager.persist(part1);
		Component component1 = Component.builder().name(COMPONENT_NAME).deleted(false).part(savedPart1).build();
		entityManager.persist(component1);
		List<Component> componentList = componentRepository.findAll();
		assertThat(componentList, hasSize(1));
	}
	@Test
	public void testFind_Component_With_History_Success() {
		Part part1 = new Part();
		part1.setName(PART1_NAME);
		Part savedPart1 = entityManager.persist(part1);
		Component component1 = Component.builder().name(COMPONENT_NAME).deleted(false).part(savedPart1).build();
		
		ComponentHistory ch = ComponentHistory.builder().workPerformed("Changed").hoursPerformed(50f)
				.datePerformed(new Date()).build();
		ComponentHistory savedComponentHistory = entityManager.persist(ch);
		
		component1.getComponentHistorySet().add(savedComponentHistory);
		
		entityManager.persist(component1);
		entityManager.flush();
		List<Component> componentList = componentRepository.findAll();
		assertThat(componentList, hasSize(1));
		assertThat(componentList.get(0).getComponentHistorySet(), hasSize(1));
		componentList.get(0).getComponentHistorySet().forEach(ch2 -> {
			System.out.println(ch2.getWorkPerformed());
		});
	}
}
