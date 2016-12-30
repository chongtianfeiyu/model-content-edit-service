package com.zyhao.openec.repository;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.zyhao.openec.entity.InfoPlan;

@Repository
public interface InfoPlanRepository extends PagingAndSortingRepository<InfoPlan, Long> {

	public List<InfoPlan> findByStoreId(Long storeId);

	public InfoPlan findByIdAndStoreId(Long id, Long storeId);

	public List<InfoPlan> findByEstateId(String estateId);

	public InfoPlan findByIdAndEstateId(Long id, String estateId);

	public List<InfoPlan> findByEstateIdAndType(String estateId, String type);

}
