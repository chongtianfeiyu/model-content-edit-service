package com.zyhao.openec.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.zyhao.openec.entity.InfoTemplate;

@Repository
public interface InfoTempleteRepository extends PagingAndSortingRepository<InfoTemplate, Long>{
	
}