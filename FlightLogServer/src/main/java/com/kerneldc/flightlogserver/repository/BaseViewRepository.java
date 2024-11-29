package com.kerneldc.flightlogserver.repository;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;

import com.kerneldc.flightlogserver.domain.AbstractImmutableEntity;

@NoRepositoryBean
public interface BaseViewRepository<T extends AbstractImmutableEntity, ID extends Serializable> extends BaseEntityRepository<T, ID> {	

}
