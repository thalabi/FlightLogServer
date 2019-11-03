package com.kerneldc.flightlogserver.aircraftmaintenance.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentHistoryVo;
import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentRequest;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
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
	
	@Test
	public void testUpdateComponentAndHistory_ModifyComponentWithNoHistory_Success() throws ApplicationException {
		ComponentRequest componentRequest = ComponentRequest.builder()
				.componentUri("http://localhost:6001/components/7").name("OilFilter").description("Champion CH48110")
				.workPerformed("replaced").datePerformed(new Date()).hoursPerformed(1000f).dateDue(new Date())
				.hoursDue(1500f).partUri("http://localhost:6001/parts/258").build();
		
		Part oldPart = Part.builder().id(25l).build();
		Part newPart = Part.builder().id(258l).name("part 258 name").build();
		Component oldComponent = Component.builder().id(7l).part(oldPart).name("old name").build();

		Optional<Component> optionalComponent = Optional.of(oldComponent);
		when(componentRepository.findById(7l)).thenReturn(optionalComponent);
		Optional<Part> optionalPart = Optional.of(newPart);
		when(partRepository.findById(258l)).thenReturn(optionalPart);
		
		componentPersistenceService.updateComponentAndHistory(componentRequest);
		
		ArgumentCaptor<Component> componentArg = ArgumentCaptor.forClass(Component.class);
		verify(componentRepository).save(componentArg.capture());
		assertThat(componentArg.getValue().getName(), equalTo(componentRequest.getName()));
		assertThat(componentArg.getValue().getDescription(), equalTo(componentRequest.getDescription()));
		assertThat(componentArg.getValue().getWorkPerformed(), equalTo(componentRequest.getWorkPerformed()));
		assertThat(componentArg.getValue().getHoursPerformed(), equalTo(componentRequest.getHoursPerformed()));
		assertThat(componentArg.getValue().getComponentHistorySet().size(), equalTo(0));
	}

	@Test
	public void testUpdateComponentAndHistory_ModifyComponentAndAddHistoryRecord_Success() throws ApplicationException {
		// Set up request component
		ComponentRequest componentRequest = ComponentRequest.builder()
				.componentUri("http://localhost:6001/components/7").name("OilFilter").description("Champion CH48110")
				.workPerformed("replaced").datePerformed(new Date()).hoursPerformed(1000f).dateDue(new Date())
				.hoursDue(1500f).partUri("http://localhost:6001/parts/258").build();
		// Set up request history record
		ComponentHistoryVo componentHistoryVo = ComponentHistoryVo.builder().name("OilFilter")
				.description("Tempest CH48110").workPerformed("replaced").datePerformed(new Date())
				.hoursPerformed(1000f).partUri("http://localhost:6001/parts/259").build();
		componentRequest.getHistoryRequestSet().add(componentHistoryVo);
		
		Part oldPart = Part.builder().id(25l).build();
		Part newPart = Part.builder().id(258l).name("part 258 name").build();
		Part newHistoryPart = Part.builder().id(259l).name("part 259 name").build();
		Component oldComponent = Component.builder().id(7l).part(oldPart).name("old name").build();

		Optional<Component> optionalComponent = Optional.of(oldComponent);
		when(componentRepository.findById(7l)).thenReturn(optionalComponent);
		Optional<Part> optionalPart = Optional.of(newPart);
		when(partRepository.findById(258l)).thenReturn(optionalPart);
		Optional<Part> optionalHistoryPart = Optional.of(newHistoryPart);
		when(partRepository.findById(259l)).thenReturn(optionalHistoryPart);
		
		componentPersistenceService.updateComponentAndHistory(componentRequest);
		
		ArgumentCaptor<Component> componentArg = ArgumentCaptor.forClass(Component.class);
		verify(componentRepository).save(componentArg.capture());
		assertThat(componentArg.getValue().getName(), equalTo(componentRequest.getName()));
		assertThat(componentArg.getValue().getDescription(), equalTo(componentRequest.getDescription()));
		assertThat(componentArg.getValue().getWorkPerformed(), equalTo(componentRequest.getWorkPerformed()));
		assertThat(componentArg.getValue().getHoursPerformed(), equalTo(componentRequest.getHoursPerformed()));
		assertThat(componentArg.getValue().getComponentHistorySet().size(), equalTo(1));
	}

	@Test
	public void testUpdateComponentAndHistory_ModifyComponentAndModifyHistoryRecord_Success() throws ApplicationException {
		// Set up request component
		ComponentRequest componentRequest = ComponentRequest.builder()
				.componentUri("http://localhost:6001/components/7").name("OilFilter").description("Champion CH48110")
				.workPerformed("replaced").datePerformed(new Date()).hoursPerformed(1000f).dateDue(new Date())
				.hoursDue(1500f).partUri("http://localhost:6001/parts/258").build();
		// Set up request history record
		ComponentHistoryVo componentHistoryVo = ComponentHistoryVo.builder()
				.historyUri("ttp://localhost:6001/componentHistories/24").name("OilFilter")
				.description("Tempest CH48110").workPerformed("replaced").datePerformed(new Date())
				.hoursPerformed(1000f).partUri("http://localhost:6001/parts/259").build();
		componentRequest.getHistoryRequestSet().add(componentHistoryVo);
		
		Part oldPart = Part.builder().id(25l).build();
		Part oldHistoryPart = Part.builder().id(259l).name("part 259 name").build();
		Part newPart = Part.builder().id(258l).name("part 258 name").build();
		Part newHistoryPart = Part.builder().id(259l).name("new part 259 name").build();
		// Create old component with one history record
		Component oldComponent = Component.builder().id(7l).part(oldPart).name("old name").build();
		ComponentHistory oldComponentHistory = ComponentHistory.builder().id(24l).name("OilFilter")
				.description("Tempest CH48110").workPerformed("replaced").datePerformed(new Date())
				.hoursPerformed(1000f).part(oldHistoryPart).build();
		oldComponent.getComponentHistorySet().add(oldComponentHistory);

		Optional<Component> optionalComponent = Optional.of(oldComponent);
		when(componentRepository.findById(7l)).thenReturn(optionalComponent);
		Optional<Part> optionalPart = Optional.of(newPart);
		when(partRepository.findById(258l)).thenReturn(optionalPart);
		Optional<Part> optionalNewHistoryPart = Optional.of(newHistoryPart);
		when(partRepository.findById(259l)).thenReturn(optionalNewHistoryPart);
		
		componentPersistenceService.updateComponentAndHistory(componentRequest);
		
		ArgumentCaptor<Component> componentArg = ArgumentCaptor.forClass(Component.class);
		verify(componentRepository).save(componentArg.capture());
		assertThat(componentArg.getValue().getName(), equalTo(componentRequest.getName()));
		assertThat(componentArg.getValue().getDescription(), equalTo(componentRequest.getDescription()));
		assertThat(componentArg.getValue().getWorkPerformed(), equalTo(componentRequest.getWorkPerformed()));
		assertThat(componentArg.getValue().getHoursPerformed(), equalTo(componentRequest.getHoursPerformed()));
		assertThat(componentArg.getValue().getComponentHistorySet().size(), equalTo(1));
		ComponentHistory newComponentHistory = componentArg.getValue().getComponentHistorySet().iterator().next();
		assertThat(newComponentHistory.getDescription(), equalTo(componentHistoryVo.getDescription()));
		assertThat(newComponentHistory.getPart().getId(), equalTo(newHistoryPart.getId()));
	}

	@Test
	public void testUpdateComponentAndHistory_ModifyComponentAndDeleteHistoryRecord_Success() throws ApplicationException {
		// Set up request component with no history record
		ComponentRequest componentRequest = ComponentRequest.builder()
				.componentUri("http://localhost:6001/components/7").name("OilFilter").description("Champion CH48110")
				.workPerformed("replaced").datePerformed(new Date()).hoursPerformed(1000f).dateDue(new Date())
				.hoursDue(1500f).partUri("http://localhost:6001/parts/258").build();
		
		Part oldPart = Part.builder().id(25l).build();
		Part oldHistoryPart = Part.builder().id(259l).name("part 259 name").build();
		Part newPart = Part.builder().id(258l).name("part 258 name").build();
		// Create old component with one history record
		Component oldComponent = Component.builder().id(7l).part(oldPart).name("old name").build();
		ComponentHistory oldComponentHistory = ComponentHistory.builder().id(24l).name("OilFilter")
				.description("Tempest CH48110").workPerformed("replaced").datePerformed(new Date())
				.hoursPerformed(1000f).part(oldHistoryPart).build();
		oldComponent.getComponentHistorySet().add(oldComponentHistory);

		Optional<Component> optionalComponent = Optional.of(oldComponent);
		when(componentRepository.findById(7l)).thenReturn(optionalComponent);
		Optional<Part> optionalPart = Optional.of(newPart);
		when(partRepository.findById(258l)).thenReturn(optionalPart);
		
		componentPersistenceService.updateComponentAndHistory(componentRequest);
		
		ArgumentCaptor<Component> componentArg = ArgumentCaptor.forClass(Component.class);
		verify(componentRepository).save(componentArg.capture());
		assertThat(componentArg.getValue().getName(), equalTo(componentRequest.getName()));
		assertThat(componentArg.getValue().getDescription(), equalTo(componentRequest.getDescription()));
		assertThat(componentArg.getValue().getWorkPerformed(), equalTo(componentRequest.getWorkPerformed()));
		assertThat(componentArg.getValue().getHoursPerformed(), equalTo(componentRequest.getHoursPerformed()));
		assertThat(componentArg.getValue().getComponentHistorySet().size(), equalTo(0));
	}

}
