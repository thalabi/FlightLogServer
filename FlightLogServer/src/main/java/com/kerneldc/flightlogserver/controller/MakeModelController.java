package com.kerneldc.flightlogserver.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.domain.EntitySpecificationsBuilder;
import com.kerneldc.flightlogserver.domain.SearchCriteria;
import com.kerneldc.flightlogserver.domain.makeModel.MakeModel;
import com.kerneldc.flightlogserver.domain.makeModel.MakeModelResource;
import com.kerneldc.flightlogserver.domain.makeModel.MakeModelResourceAssembler;
import com.kerneldc.flightlogserver.repository.MakeModelRepository;

@RestController
@RequestMapping("makeModelController")
@ExposesResourceFor(MakeModel.class) // needed for unit test to create entity links
public class MakeModelController {

    private MakeModelRepository makeModelRepository;
    
	private MakeModelResourceAssembler makeModelResourceAssembler;
	
    public MakeModelController(MakeModelRepository makeModelRepository, MakeModelResourceAssembler makeModelResourceAssembler) {
        this.makeModelRepository = makeModelRepository;
        this.makeModelResourceAssembler = makeModelResourceAssembler;
    }

    //@PreAuthorize("hasAuthority('make_model read')")
    @GetMapping("/findAll")
	public PagedResources<MakeModelResource> findAll(
			@RequestParam(value = "search") String search, Pageable pageable, PagedResourcesAssembler<MakeModel> pagedResourcesAssembler) {
    	List<SearchCriteria> searchCriteriaList = ControllerHelper.searchStringToSearchCriteriaList(search);
    	EntitySpecificationsBuilder<MakeModel> entitySpecificationsBuilder = new EntitySpecificationsBuilder<>();
        Page<MakeModel> makeModelPage = makeModelRepository.findAll(entitySpecificationsBuilder.with(searchCriteriaList).build(), pageable);
		Link link = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(MakeModelController.class).findAll(search, pageable, pagedResourcesAssembler)).withSelfRel();
		return pagedResourcesAssembler.toResource(makeModelPage, makeModelResourceAssembler, link);
    }
}
