package com.kerneldc.flightlogserver.aircraftmaintenance.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentRequest;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.ComponentResourceAssembler;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.ComponentHistoryRepository;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.ComponentRepository;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.PartRepository;
import com.kerneldc.flightlogserver.aircraftmaintenance.service.ComponentPersistenceService;
import com.kerneldc.flightlogserver.exception.ApplicationException;
import com.kerneldc.flightlogserver.security.config.UnauthorizedHandler;
import com.kerneldc.flightlogserver.security.service.CustomUserDetailsService;
import com.kerneldc.flightlogserver.security.util.JwtTokenProvider;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ComponentController.class)
public class ComponentControllerTest {

	private static final String COMPONENT_WRITE = "component write";
	
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    
	@MockBean
    private ComponentRepository componentRepository;
	@MockBean
    private ComponentHistoryRepository componentHistoryRepository;
	@MockBean
    private PartRepository partRepository;
	@MockBean
	private ComponentPersistenceService componentPersistenceService;
	@MockBean
	private ComponentResourceAssembler componentResourceAssembler;
	@MockBean
	private CustomUserDetailsService customUserDetailsService;
	@MockBean
	private UnauthorizedHandler unauthorizedHandler;
	@MockBean
	private JwtTokenProvider jwtTokenProvider;
	
	@Test
	@WithMockUser(authorities = COMPONENT_WRITE)
	public void testAdd_success() throws Exception {
		ComponentRequest componentRequest = new ComponentRequest();
		componentRequest.setName("OilFilter");
		componentRequest.setDescription("Champion CH48110");
		componentRequest.setWorkPerformed("replaced");
		componentRequest.setDatePerformed(new Date());
		componentRequest.setHoursPerformed(1000f);
		componentRequest.setDateDue(new Date());
		componentRequest.setHoursDue(1500f);
		componentRequest.setPartUri("http://localhost:6001/parts/258");
		Part part = Part.builder().id(258l).build();
		Component component = new Component();
		BeanUtils.copyProperties(componentRequest, component);
		component.setPart(part);
		Component savedComponent = SerializationUtils.clone(component);
		savedComponent.setId(7l);
		
		when(componentPersistenceService.parseAndFindPart(componentRequest.getPartUri())).thenReturn(part);
		
		mockMvc.perform(post("/componentController/add")
				.content(objectMapper.writeValueAsBytes(componentRequest))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andDo(print());
		
		InOrder inOrder = inOrder(componentPersistenceService, componentRepository);
		inOrder.verify(componentPersistenceService).parseAndFindPart(componentRequest.getPartUri());
		ArgumentCaptor<Component> componentArg = ArgumentCaptor.forClass(Component.class);
		inOrder.verify(componentRepository).save(componentArg.capture());
		assertThat(componentArg.getValue().getName(), equalTo(componentRequest.getName()));
		assertThat(componentArg.getValue().getDescription(), equalTo(componentRequest.getDescription()));
		assertThat(componentArg.getValue().getWorkPerformed(), equalTo(componentRequest.getWorkPerformed()));
		assertThat(componentArg.getValue().getHoursPerformed(), equalTo(componentRequest.getHoursPerformed()));
		assertThat(componentArg.getValue().getDateDue(), equalTo(componentRequest.getDateDue()));
		assertThat(componentArg.getValue().getHoursDue(), equalTo(componentRequest.getHoursDue()));
	}

	@Test
	@WithMockUser(authorities = COMPONENT_WRITE)
	public void testAdd_partNotFound_failure() throws Exception {
		ComponentRequest componentRequest = new ComponentRequest();
		componentRequest.setName("OilFilter");
		componentRequest.setDescription("Champion CH48110");
		componentRequest.setWorkPerformed("replaced");
		componentRequest.setDatePerformed(new Date());
		componentRequest.setHoursPerformed(1000f);
		componentRequest.setDateDue(new Date());
		componentRequest.setHoursDue(1500f);
		componentRequest.setPartUri("http://localhost:6001/parts/666");
		long partId = 666;
		Component component = new Component();
		BeanUtils.copyProperties(componentRequest, component);
		component.setId(7l);
		
		when(componentPersistenceService.parseAndFindPart(componentRequest.getPartUri())).thenThrow(new ApplicationException(String.format("Part ID: %d not found", partId)));
		
		mockMvc.perform(post("/componentController/add")
				.content(objectMapper.writeValueAsBytes(componentRequest))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", equalTo(String.format("Part ID: %d not found", partId))))
				.andExpect(jsonPath("$.stackTrace", startsWith(ApplicationException.class.getName())))
				.andDo(print());
	}

	@Test
	@WithMockUser(authorities = COMPONENT_WRITE)
	public void testAdd_partIdNotParsable_failure() throws Exception {
		ComponentRequest componentRequest = new ComponentRequest();
		componentRequest.setName("OilFilter");
		componentRequest.setDescription("Champion CH48110");
		componentRequest.setWorkPerformed("replaced");
		componentRequest.setDatePerformed(new Date());
		componentRequest.setHoursPerformed(1000f);
		componentRequest.setDateDue(new Date());
		componentRequest.setHoursDue(1500f);
		componentRequest.setPartUri("http://localhost:6001/parts/abc");
		
		when(componentPersistenceService.parseAndFindPart(componentRequest.getPartUri())).thenThrow(new ApplicationException(String.format("Could not parse part ID from uri: %s", componentRequest.getPartUri())));
		
		mockMvc.perform(post("/componentController/add")
				.content(objectMapper.writeValueAsBytes(componentRequest))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", equalTo(String.format("Could not parse part ID from uri: %s", componentRequest.getPartUri()))))
				.andExpect(jsonPath("$.stackTrace", startsWith(ApplicationException.class.getName())))
				.andDo(print());
	}

	@Test
	@WithMockUser(authorities = COMPONENT_WRITE)
	public void testModify_success() throws Exception {
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
		componentRequest.setCreateHistoryRecord(false);
		Part oldPart = Part.builder().id(25l).build();
		Part newPart = Part.builder().id(258l).name("part 258 name").build();
		Component oldComponent = Component.builder().id(7l).part(oldPart).name("old name").build();
		Component newComponent = new Component();
		BeanUtils.copyProperties(componentRequest, newComponent);
		newComponent.setId(7l);
		newComponent.setPart(newPart);
		
		when(componentPersistenceService.parseAndFindPart(componentRequest.getPartUri())).thenReturn(newPart);
		when(componentPersistenceService.parseAndFindComponent(componentRequest.getComponentUri())).thenReturn(oldComponent);
		
		mockMvc.perform(put("/componentController/modify")
				.content(objectMapper.writeValueAsBytes(componentRequest))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andDo(print());
		
		InOrder inOrder = inOrder(componentPersistenceService, componentRepository);
		inOrder.verify(componentPersistenceService).parseAndFindComponent(componentRequest.getComponentUri());
		inOrder.verify(componentPersistenceService).parseAndFindPart(componentRequest.getPartUri());
		ArgumentCaptor<Component> newComponentArg = ArgumentCaptor.forClass(Component.class);
		inOrder.verify(componentRepository).save(newComponentArg.capture());
		assertThat(newComponentArg.getValue().getName(), equalTo(componentRequest.getName()));
		assertThat(newComponentArg.getValue().getDescription(), equalTo(componentRequest.getDescription()));
		assertThat(newComponentArg.getValue().getWorkPerformed(), equalTo(componentRequest.getWorkPerformed()));
		assertThat(newComponentArg.getValue().getHoursPerformed(), equalTo(componentRequest.getHoursPerformed()));
		assertThat(newComponentArg.getValue().getDateDue(), equalTo(componentRequest.getDateDue()));
		assertThat(newComponentArg.getValue().getHoursDue(), equalTo(componentRequest.getHoursDue()));
		assertThat(newComponentArg.getValue().getPart().getId(), equalTo(newPart.getId()));
		assertThat(newComponentArg.getValue().getPart(), equalTo(newPart));
	}

	@Test
	@WithMockUser(authorities = COMPONENT_WRITE)
	public void testModify_createHistoryRecord_success() throws Exception {
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
		componentRequest.setCreateHistoryRecord(true);
		Part oldPart = Part.builder().id(25l).build();
		Part newPart = Part.builder().id(258l).name("part 258 name").build();
		Component oldComponent = Component.builder().id(7l).part(oldPart).name("old name").workPerformed("old replaced").datePerformed(new Date()).hoursPerformed(999f).build();
		ComponentHistory newComponentHistory = ComponentHistory.builder().workPerformed(oldComponent.getWorkPerformed())
				.datePerformed(oldComponent.getDatePerformed()).hoursPerformed(oldComponent.getHoursPerformed())
				.build();
		ComponentHistory savedNewComponentHistory = SerializationUtils.clone(newComponentHistory);
		savedNewComponentHistory.setId(9l);
		Component newComponent = new Component();
		BeanUtils.copyProperties(componentRequest, newComponent);
		newComponent.setId(7l);
		newComponent.setPart(newPart);
		
		when(componentPersistenceService.parseAndFindPart(componentRequest.getPartUri())).thenReturn(newPart);
		when(componentPersistenceService.parseAndFindComponent(componentRequest.getComponentUri())).thenReturn(oldComponent);
		
		mockMvc.perform(put("/componentController/modify")
				.content(objectMapper.writeValueAsBytes(componentRequest))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andDo(print());
		
		InOrder inOrder = inOrder(componentPersistenceService, componentRepository);
		inOrder.verify(componentPersistenceService).parseAndFindComponent(componentRequest.getComponentUri());
		inOrder.verify(componentPersistenceService).parseAndFindPart(componentRequest.getPartUri());
		ArgumentCaptor<Component> newComponentArg = ArgumentCaptor.forClass(Component.class);
		inOrder.verify(componentRepository).save(newComponentArg.capture());
		assertThat(newComponentArg.getValue().getName(), equalTo(componentRequest.getName()));
		assertThat(newComponentArg.getValue().getDescription(), equalTo(componentRequest.getDescription()));
		assertThat(newComponentArg.getValue().getWorkPerformed(), equalTo(componentRequest.getWorkPerformed()));
		assertThat(newComponentArg.getValue().getHoursPerformed(), equalTo(componentRequest.getHoursPerformed()));
		assertThat(newComponentArg.getValue().getDateDue(), equalTo(componentRequest.getDateDue()));
		assertThat(newComponentArg.getValue().getHoursDue(), equalTo(componentRequest.getHoursDue()));
		assertThat(newComponentArg.getValue().getPart().getId(), equalTo(newPart.getId()));
		assertThat(newComponentArg.getValue().getPart(), equalTo(newPart));
	}
}
