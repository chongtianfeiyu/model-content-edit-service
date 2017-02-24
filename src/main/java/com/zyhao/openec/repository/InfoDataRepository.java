package com.zyhao.openec.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.zyhao.openec.entity.InfoData;

@Repository
public interface InfoDataRepository extends PagingAndSortingRepository<InfoData, Long> {

	public List<InfoData> findByStoreId(Long storedId);

	public InfoData findByIdAndStoreId(Long id,Long storedId);
	
	public List<InfoData> findByStoreIdAndInfoPlanId(Long storedId,String planId);

	public List<InfoData> findByUserId(String estateId, Sort pageable);

	public InfoData findByIdAndUserId(Long id,String estateId);

	public List<InfoData> findByUserIdAndInfoPlanId(String estateId, String planId);

	public Long deleteByIdAndUserId(Long id, String session_businessId);

	@Query(value="select max(sort) from info_data where type=?2 and user_id = ?1",nativeQuery=true )
	public Long getMaxSortByUserIdAndType(String userId, String type);

}
