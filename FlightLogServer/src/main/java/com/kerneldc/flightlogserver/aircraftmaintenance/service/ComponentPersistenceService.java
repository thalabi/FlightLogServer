package com.kerneldc.flightlogserver.aircraftmaintenance.service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentHistoryVo;
import com.kerneldc.flightlogserver.aircraftmaintenance.bean.ComponentRequest;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.component.Component;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.componenthistory.ComponentHistory;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.ComponentRepository;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.PartRepository;
import com.kerneldc.flightlogserver.exception.ApplicationException;


@Service
public class ComponentPersistenceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final UriTemplate PART_URI_TEMPLATE = new UriTemplate("{protocol}://{host}:{port}/parts/{id}");
	private static final UriTemplate COMPONENT_URI_TEMPLATE = new UriTemplate("{protocol}://{host}:{port}/components/{id}");
	private static final UriTemplate COMPONENT_HISTORY_URI_TEMPLATE = new UriTemplate("{protocol}://{host}:{port}/componentHistories/{id}");

	private PartRepository partRepository;
	private ComponentRepository componentRepository;
	public ComponentPersistenceService(PartRepository partRepository, ComponentRepository componentRepository) {
		this.partRepository = partRepository;
		this.componentRepository = componentRepository;
	}

	public void updateComponentAndHistory(ComponentRequest componentRequest)
			throws ApplicationException {
//    	LOGGER.debug("componentRequest: {}", componentRequest);
//		String componentUri = componentRequest.getComponentUri();
//    	LOGGER.debug("component description: {}, componentUri(): {}, componentId: {}, partUri: {}, datePerformed: {}",
//    			componentRequest.getDescription(), componentRequest.getComponentUri(), parseId(componentUri, COMPONENT_URI_TEMPLATE), componentRequest.getPartUri(), componentRequest.getDatePerformed());
//    	componentRequest.getHistoryRequestSet().forEach(historyRequest -> {
//    		String historyUri = historyRequest.getHistoryUri();
//    		Long historyId = StringUtils.isNotEmpty(historyUri) ? parseId(historyUri, COMPONENT_HISTORY_URI_TEMPLATE) : null;
//    		LOGGER.debug("history description: {}, historyUri(): {}, historyId: {}, partUri: {}, datePerformed: {}",
//    				historyRequest.getDescription(), historyRequest.getHistoryUri(), historyId, historyRequest.getPartUri(), historyRequest.getDatePerformed());
//    	});
    	
    	List<Long> requestHistoryIdList = componentRequest.getHistoryRequestSet().stream()
    		.map(historyRequest -> {
    			String historyUri = historyRequest.getHistoryUri();
    			return StringUtils.isNotEmpty(historyUri) ? parseId(historyUri, COMPONENT_HISTORY_URI_TEMPLATE) : null;
    		}).collect(Collectors.toList());
    	LOGGER.debug("requestHistoryIdList: {}", requestHistoryIdList);
    	
		Component component = parseAndFindComponent(componentRequest.getComponentUri());
		// 1. Update component with request
		BeanUtils.copyProperties(componentRequest, component, "part");
		// Set the part as the copyProperties could not have done that
		component.setPart(parseAndFindPart(componentRequest.getPartUri()));
		
    	List<Long> existingHistoryIdList = component.getComponentHistorySet().stream()
        		.map(ComponentHistory::getId).collect(Collectors.toList());
    	LOGGER.debug("existingHistoryIdList: {}", existingHistoryIdList);
    	
    	// Get the list of componentHistory ids to be deleted
    	List<Long> historyIdToBeDeletedList = new ArrayList<>(existingHistoryIdList);
    	historyIdToBeDeletedList.removeAll(requestHistoryIdList);
    	LOGGER.debug("historyIdToBeDeletedList: {}", historyIdToBeDeletedList);
    	LOGGER.debug("componentHistorySet() size before delete: {}", component.getComponentHistorySet().size());
    	// 2. Delete componentHistory records
    	component.getComponentHistorySet().removeIf(componentHistory -> historyIdToBeDeletedList.contains(componentHistory.getId()));
    	LOGGER.debug("componentHistorySet() size after delete: {}", component.getComponentHistorySet().size());
		// 3. Update componentHistory records
    	for (ComponentHistory componentHistory : component.getComponentHistorySet()) {
    		// Find component history in the request that has a non blank uri and same id as in existing component history
    		ComponentHistoryVo componentHistoryVo = componentRequest.getHistoryRequestSet().stream()
					.filter(historyRequest -> StringUtils.isNotEmpty(historyRequest.getHistoryUri())
							&& parseId(historyRequest.getHistoryUri(), COMPONENT_HISTORY_URI_TEMPLATE)
									.equals(componentHistory.getId()))
					.findFirst().orElse(null);
			BeanUtils.copyProperties(componentHistoryVo, componentHistory, "part");
			// Set the part as the copyProperties could not have done that
			componentHistory.setPart(parseAndFindPart(componentHistoryVo.getPartUri()));
			LOGGER.debug("finished updating component history: {}", componentHistory);
    	}
    	// 4. Add new component history records
    	Collection<ComponentHistory> componentHistoryToAdd = new ArrayList<>();
    	for (ComponentHistoryVo componentHistoryVo : componentRequest.getHistoryRequestSet()) {
    		if (StringUtils.isEmpty(componentHistoryVo.getHistoryUri())) {
	    		ComponentHistory componentHistory = new ComponentHistory();
	    		BeanUtils.copyProperties(componentHistoryVo, componentHistory, "part");
	    		componentHistory.setPart(parseAndFindPart(componentHistoryVo.getPartUri()));
	    		componentHistoryToAdd.add(componentHistory);
	    		LOGGER.debug("added component history: {}, to set", componentHistory);
    		}
    	}
    	component.getComponentHistorySet().addAll(componentHistoryToAdd);
    	
		LOGGER.debug("modified component: {}", component);
		componentRepository.save(component);
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
