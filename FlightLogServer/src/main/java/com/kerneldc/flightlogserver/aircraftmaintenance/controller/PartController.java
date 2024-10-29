package com.kerneldc.flightlogserver.aircraftmaintenance.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.Part;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.PartModel;
import com.kerneldc.flightlogserver.aircraftmaintenance.domain.part.PartModelAssembler;
import com.kerneldc.flightlogserver.aircraftmaintenance.repository.PartRepository;
import com.kerneldc.flightlogserver.controller.ControllerHelper;
import com.kerneldc.flightlogserver.domain.EntitySpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.SearchCriteria;

@RestController
@RequestMapping("/protected/partController")
//@ExposesResourceFor(Part.class) // needed for unit test to create entity links
public class PartController {

    private PartRepository partRepository;
    
	private PartModelAssembler partModelAssembler;
	
    public PartController(PartRepository partRepository, PartModelAssembler partModelAssembler) {
        this.partRepository = partRepository;
        this.partModelAssembler = partModelAssembler;
    }

    @GetMapping("/findAll")
	public PagedModel<PartModel> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<Part> pagedResourcesAssembler) {
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<Part> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<Part> partPage = partRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);

        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PartController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		return pagedResourcesAssembler.toModel(partPage, partModelAssembler, link);
    }
}
