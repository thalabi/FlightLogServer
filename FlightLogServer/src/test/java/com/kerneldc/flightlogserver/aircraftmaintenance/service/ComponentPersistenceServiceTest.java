package com.kerneldc.flightlogserver.aircraftmaintenance.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;

import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentRequest;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.ComponentRepository;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.PartRepository;
import com.kerneldc.flightlogserver.exception.ApplicationException;

@RunWith(MockitoJUnitRunner.class)
public class ComponentPersistenceServiceTest {

	@InjectMocks
	private ComponentPersistenceService componentPersistenceService;

	@Mock
    private ComponentRepository componentRepository;
	@Mock
    private PartRepository partRepository;

	@Test
	public void testParseAndFindComponent_ValidComponentUri_Success() throws ApplicationException {
		String componentUri = "http://localhost:6001/components/7";
		Part part = Part.builder().id(25l).name("part 25").build();
		Component component = Component.builder().id(7l).part(part).name("component 7").build();
		Optional<Component> optionalComponent = Optional.of(component);
		when(componentRepository.findById(7l)).thenReturn(optionalComponent);
		
		Component resultComponent = componentPersistenceService.parseAndFindComponent(componentUri);
		
		assertThat(resultComponent.getId(), equalTo(component.getId()));
		assertThat(resultComponent.getName(), equalTo(component.getName()));
		assertThat(resultComponent.getPart().getId(), equalTo(component.getPart().getId()));
		assertThat(resultComponent.getPart().getName(), equalTo(component.getPart().getName()));
	}
	
	
	@Test(expected = ApplicationException.class)
	public void testParseAndFindComponent_InvalidComponentUri_Failure() throws ApplicationException {
		String componentUri = "http://localhost:6001/componentsXXX/7";
		
		componentPersistenceService.parseAndFindComponent(componentUri);
	}
	
	//@Ignore
	@Test
	public void testupdateComponentAndHistory_ModifyComponentWithNoHistory_Success() throws ApplicationException {
		ComponentRequest componentRequest = new ComponentRequest();
		componentRequest.setComponentUri("http://localhost:6001/components/7");
		componentRequest.setName("OilFilter");
		componentRequest.setDescription("Champion CH48110");
		componentRequest.setWorkPerformed("replaced");
		componentRequest.setDatePerformed(new Date());
		componentRequest.setHoursPerformed(1000f);
		componentRequest.setDateDue(new Date());
		componentRequest.setHoursDue(1500f);
		componentRequest.setPartUri("http://localhost:6001/parts/258");
		
		Part oldPart = Part.builder().id(25l).build();
		Part newPart = Part.builder().id(258l).name("part 258 name").build();
		Component oldComponent = Component.builder().id(7l).part(oldPart).name("old name").build();
		Component newComponent = new Component();
		BeanUtils.copyProperties(componentRequest, newComponent);
		newComponent.setId(7l);
		newComponent.setPart(newPart);

		//when(componentPersistenceService.parseAndFindPart(componentRequest.getPartUri())).thenReturn(newPart);
		
		Optional<Component> optionalComponent = Optional.of(oldComponent);
		when(componentRepository.findById(7l)).thenReturn(optionalComponent);
		Optional<Part> optionalPart = Optional.of(newPart);
		when(partRepository.findById(258l)).thenReturn(optionalPart);
		
		//when(componentPersistenceService.parseAndFindComponent(componentRequest.getComponentUri())).thenReturn(oldComponent);

		componentPersistenceService.updateComponentAndHistory(componentRequest);
		
		verify(componentRepository).save(argThat(component -> component.getId().equals(7l) && component.getName().equals("OilFilter")));
	}

}
