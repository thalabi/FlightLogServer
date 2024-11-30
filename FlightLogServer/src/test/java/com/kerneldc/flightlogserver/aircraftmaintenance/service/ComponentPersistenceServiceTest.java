package com.kerneldc.flightlogserver.aircraftmaintenance.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentHistoryVo;
import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentRequest;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.ComponentRepository;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.PartRepository;
import com.kerneldc.flightlogserver.exception.ApplicationException;

@ExtendWith(SpringExtension.class)
class ComponentPersistenceServiceTest {

	@InjectMocks
	private ComponentPersistenceService componentPersistenceService;

	@Mock
    private ComponentRepository componentRepository;
	@Mock
    private PartRepository partRepository;
	
	private final String BASE_URI = "http://localhost:6001/protected/data-rest";

	@Test
	void testParseAndFindComponent_ValidComponentUri_Success() throws ApplicationException {
		String componentUri = BASE_URI + "/components/7";
		Part part = Part.builder().name("part 25").build();
		part.setId(25l);
		Component component = Component.builder().part(part).name("component 7").build();
		component.setId(7l);
		Optional<Component> optionalComponent = Optional.of(component);
		when(componentRepository.findById(7l)).thenReturn(optionalComponent);
		
		Component resultComponent = componentPersistenceService.parseAndFindComponent(componentUri);
		
		assertThat(resultComponent.getId(), equalTo(component.getId()));
		assertThat(resultComponent.getName(), equalTo(component.getName()));
		assertThat(resultComponent.getPart().getId(), equalTo(component.getPart().getId()));
		assertThat(resultComponent.getPart().getName(), equalTo(component.getPart().getName()));
	}
	
	
	@Test
	void testParseAndFindComponent_InvalidComponentUri_Failure() throws ApplicationException {
		String componentUri = BASE_URI + "/componentsXXX/7";
		
		assertThrows(ApplicationException.class,
				() -> componentPersistenceService.parseAndFindComponent(componentUri));
	}
	
	@Test
	void testUpdateComponentAndHistory_ModifyComponentWithNoHistory_Success() throws ApplicationException {
		ComponentRequest componentRequest = ComponentRequest.builder()
				.componentUri(BASE_URI + "/components/7").name("OilFilter").description("Champion CH48110")
				.workPerformed("replaced").datePerformed(new Date()).hoursPerformed(1000f).dateDue(new Date())
				.hoursDue(1500f).partUri(BASE_URI + "/parts/258").build();
		
		Part oldPart = Part.builder().build();
		oldPart.setId(25l);
		Part newPart = Part.builder().name("part 258 name").build();
		newPart.setId(258l);
		Component oldComponent = Component.builder().part(oldPart).name("old name").build();
		oldComponent.setId(7l);

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
	void testUpdateComponentAndHistory_ModifyComponentAndAddHistoryRecord_Success() throws ApplicationException {
		// Set up request component
		ComponentRequest componentRequest = ComponentRequest.builder()
				.componentUri(BASE_URI + "/components/7").name("OilFilter").description("Champion CH48110")
				.workPerformed("replaced").datePerformed(new Date()).hoursPerformed(1000f).dateDue(new Date())
				.hoursDue(1500f).partUri(BASE_URI + "/parts/258").build();
		// Set up request history record
		ComponentHistoryVo componentHistoryVo = ComponentHistoryVo.builder().name("OilFilter")
				.description("Tempest CH48110").workPerformed("replaced").datePerformed(new Date())
				.hoursPerformed(1000f).partUri(BASE_URI + "/parts/259").build();
		componentRequest.getHistoryRequestSet().add(componentHistoryVo);
		
		Part oldPart = Part.builder().build();
		oldPart.setId(25l);
		Part newPart = Part.builder().name("part 258 name").build();
		newPart.setId(258l);
		Part newHistoryPart = Part.builder().name("part 259 name").build();
		newHistoryPart.setId(259l);
		Component oldComponent = Component.builder().part(oldPart).name("old name").build();
		oldComponent.setId(7l);

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
	void testUpdateComponentAndHistory_ModifyComponentAndModifyHistoryRecord_Success() throws ApplicationException {
		// Set up request component
		ComponentRequest componentRequest = ComponentRequest.builder()
				.componentUri(BASE_URI + "/components/7").name("OilFilter").description("Champion CH48110")
				.workPerformed("replaced").datePerformed(new Date()).hoursPerformed(1000f).dateDue(new Date())
				.hoursDue(1500f).partUri(BASE_URI + "/parts/258").build();
		// Set up request history record
		ComponentHistoryVo componentHistoryVo = ComponentHistoryVo.builder()
				.historyUri(BASE_URI + "/componentHistories/24").name("OilFilter")
				.description("Tempest CH48110").workPerformed("replaced").datePerformed(new Date())
				.hoursPerformed(1000f).partUri(BASE_URI + "/parts/259").build();
		componentRequest.getHistoryRequestSet().add(componentHistoryVo);
		
		Part oldPart = Part.builder().build();
		oldPart.setId(25l);
		Part oldHistoryPart = Part.builder().name("part 259 name").build();
		oldHistoryPart.setId(259l);
		Part newPart = Part.builder().name("part 258 name").build();
		newPart.setId(258l);
		Part newHistoryPart = Part.builder().name("new part 259 name").build();
		newHistoryPart.setId(259l);
		// Create old component with one history record
		Component oldComponent = Component.builder().part(oldPart).name("old name").build();
		oldComponent.setId(7l);
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
	void testUpdateComponentAndHistory_ModifyComponentAndDeleteHistoryRecord_Success() throws ApplicationException {
		// Set up request component with no history record
		ComponentRequest componentRequest = ComponentRequest.builder()
				.componentUri(BASE_URI + "/components/7").name("OilFilter").description("Champion CH48110")
				.workPerformed("replaced").datePerformed(new Date()).hoursPerformed(1000f).dateDue(new Date())
				.hoursDue(1500f).partUri(BASE_URI + "/parts/258").build();
		
		Part oldPart = Part.builder().build();
		oldPart.setId(25l);
		Part oldHistoryPart = Part.builder().name("part 259 name").build();
		oldHistoryPart.setId(259l);
		Part newPart = Part.builder().name("part 258 name").build();
		newPart.setId(258l);
		// Create old component with one history record
		Component oldComponent = Component.builder().part(oldPart).name("old name").build();
		oldComponent.setId(7l);
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
