package com.kerneldc.flightlogserver.controller;

import java.util.List;
import java.util.Objects;

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

import com.kerneldc.flightlogserver.domain.EntitySpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.domain.makeModel.MakeModel;
import com.kerneldc.flightlogserver.domain.makeModel.MakeModelModel;
import com.kerneldc.flightlogserver.domain.makeModel.MakeModelModelAssembler;
import com.kerneldc.flightlogserver.repository.MakeModelRepository;

@RestController
@RequestMapping("/protected/makeModelController")
//@ExposesResourceFor(MakeModel.class) // needed for unit test to create entity links
public class MakeModelController {

    private MakeModelRepository makeModelRepository;
    
	private MakeModelModelAssembler makeModelModelAssembler;
	
    public MakeModelController(MakeModelRepository makeModelRepository, MakeModelModelAssembler makeModelModelAssembler) {
        this.makeModelRepository = makeModelRepository;
        this.makeModelModelAssembler = makeModelModelAssembler;
    }

    //@PreAuthorize("hasAuthority('make_model read')")
    @GetMapping("/findAll")
	public PagedModel<MakeModelModel> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<MakeModel> pagedResourcesAssembler) {
    	Objects.requireNonNull(pagedResourcesAssembler, "pagedResourcesAssembler cannot be null for this controller to work");
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<MakeModel> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<MakeModel> makeModelPage = makeModelRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MakeModelController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		return pagedResourcesAssembler.toModel(makeModelPage, makeModelModelAssembler, link);
    }
}
