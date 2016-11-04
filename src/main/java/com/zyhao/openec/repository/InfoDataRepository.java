package com.zyhao.openec.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.zyhao.openec.entity.InfoData;

@Repository
public interface InfoDataRepository extends PagingAndSortingRepository<InfoData, Long> {

	public List<InfoData> findByStoreId(Long storedId);

	public InfoData findByIdAndStoreId(Long id,Long storedId);
	
	public List<InfoData> findByStoreIdAndInfoPlanId(Long storedId,String planId);
}
