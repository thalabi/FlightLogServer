package com.kerneldc.flightlogserver.aircraftmaintenance.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
    private ComponentRepository componentRepository;
	@Autowired
    private TestEntityManager testEntityManager;

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
