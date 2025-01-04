package com.kerneldc.flightlogserver.controller;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kerneldc.flightlogserver.domain.AbstractEntity;
import com.kerneldc.flightlogserver.domain.EntityEnumUtilities;
import com.kerneldc.flightlogserver.domain.IEntityEnum;
import com.kerneldc.flightlogserver.repository.EntityRepositoryFactory;
import com.kerneldc.flightlogserver.search.EntitySpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/protected/genericEntityController")
@RequiredArgsConstructor
@Slf4j
public class GenericEntityController {

	private final EntityRepositoryFactory<AbstractEntity, Long> entityRepositoryFactory;
	private final EntityRepresentationModelAssembler entityRepresentationModelAssembler;
	private final EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@GetMapping("/findAll")
	public ResponseEntity<PagedModel<EntityModel<AbstractEntity>>> findAll(
			@RequestParam @NotBlank String tableName, @RequestParam String search,
			Pageable pageable, @NotNull PagedResourcesAssembler<AbstractEntity> pagedResourcesAssembler) {

		IEntityEnum entityEnum = EntityEnumUtilities.getEntityEnum(tableName);
    	LOGGER.info("search: {}", search);
    	LOGGER.info("pageable: {}", pageable);
    	
    	// Retrieve the repository with proper typing
    	var entityRepository = entityRepositoryFactory.getRepository(entityEnum);
    	
    	// Retrieve entity metamodel with proper typing
    	var entityMetamodel = entityManager.getMetamodel().entity(entityEnum.getEntity());
    	
    	// Create a typed specification
    	var entitySpecification = new EntitySpecification<AbstractEntity>(entityMetamodel, search);
    	
    	// Perform the query
		var page = entityRepository.findAll(entitySpecification, pageable);
		
		// Build the PagedModel
        PagedModel<EntityModel<AbstractEntity>> pagedModel;
        
        if (! /* not */ page.hasContent()) {
        	pagedModel = (PagedModel<EntityModel<AbstractEntity>>) pagedResourcesAssembler.toEmptyModel(page, entityEnum.getEntity());
        } else {
        	var link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GenericEntityController.class).findAll(tableName, search, pageable, pagedResourcesAssembler)).withSelfRel();
        	pagedModel = pagedResourcesAssembler.toModel(page, entityRepresentationModelAssembler, link);
        }

		LOGGER.debug("pagedModel: {}", pagedModel);
		
		var response = ResponseEntity.ok(pagedModel);
		
		LOGGER.debug("r: {}", response);
		return response;	
    }

	@GetMapping("/countAll")
	public ResponseEntity<Long> countAll(@RequestParam @NotBlank String tableName) {

		IEntityEnum entityEnum = EntityEnumUtilities.getEntityEnum(tableName);
		var entityRepository = entityRepositoryFactory.getRepository(entityEnum);

		var count = entityRepository.count();
		var response = ResponseEntity.ok(count);

		LOGGER.debug("r: {}", response);
		return response;
	}
}
