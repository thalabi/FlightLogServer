package com.kerneldc.flightlogserver.aircraftmaintenance.service;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentRequestNew;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.ComponentRepository;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.PartRepository;
import com.kerneldc.flightlogserver.exception.ApplicationException;

@Service
public class ComponentPersistenceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final UriTemplate PART_URI_TEMPLATE = new UriTemplate("{protocol}://{host}:{port}/parts/{id}");
	private static final UriTemplate COMPONENT_URI_TEMPLATE = new UriTemplate("{protocol}://{host}:{port}/components/{id}");

	private PartRepository partRepository;
	private ComponentRepository componentRepository;
	public ComponentPersistenceService(PartRepository partRepository, ComponentRepository componentRepository) {
		this.partRepository = partRepository;
		this.componentRepository = componentRepository;
	}

	public void updateComponent(ComponentRequestNew omponentReques) {
		
		
	}

    public Part parseAndFindPart(String partUri) throws ApplicationException {
    	Long partId;
    	try {
    		partId = parseId(partUri, PART_URI_TEMPLATE);	
    	} catch (NumberFormatException e) {
    		throw new ApplicationException(String.format("Could not parse part ID from uri: %s", partUri));
    	}
    	Optional<Part> optionalPart = partRepository.findById(partId);
    	if (!optionalPart.isPresent()) {
    		throw new ApplicationException(String.format("Part ID: %d not found", partId));
    	}
    	return optionalPart.get();
    }
    
    public Component parseAndFindComponent(String componentUri) throws ApplicationException {
    	Long componentId;
    	try {
    		componentId = parseId(componentUri, COMPONENT_URI_TEMPLATE);	
    	} catch (NumberFormatException e) {
    		throw new ApplicationException(String.format("Could not parse component ID from uri: %s", componentUri));
    	}
    	Optional<Component> optionalComponent = componentRepository.findById(componentId);
    	if (!optionalComponent.isPresent()) {
    		throw new ApplicationException(String.format("Component ID: %d not found", componentId));
    	}
    	return optionalComponent.get();
    }
    
    private Long parseId(String uri, UriTemplate uriTemplate) {
    	Map<String, String> parameterMap = uriTemplate.match(uri);
    	return Long.parseLong(parameterMap.get("id"));
    }

}
