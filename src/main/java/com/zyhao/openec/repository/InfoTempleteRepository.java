package com.zyhao.openec.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.zyhao.openec.entity.InfoTemplete;

@Repository
public interface InfoTempleteRepository extends PagingAndSortingRepository<InfoTemplete, Long>{
	
}