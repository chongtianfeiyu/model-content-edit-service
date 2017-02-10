package com.zyhao.openec.api.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zyhao.openec.api.v1.InfoDataServiceV1;
import com.zyhao.openec.entity.InfoData;
import com.zyhao.openec.entity.InfoPlan;
import com.zyhao.openec.entity.InfoTemplate;
import com.zyhao.openec.entity.SellerUser;
import com.zyhao.openec.repository.InfoDataRepository;
import com.zyhao.openec.repository.InfoPlanRepository;
import com.zyhao.openec.repository.InfoTempleteRepository;

/**
 * 
 * Title:InfoDataController 
 * Desc: 增加物业和用户一对多
 * @author Administrator
 * @date 2016年12月17日 上午11:04:52
 */
@RestController
@RequestMapping("/v1")
public class InfoDataController {
private Log logger = LogFactory.getLog(InfoDataController.class);
	
	@Resource
	private InfoDataRepository infoDataRepository;
	@Resource
	private InfoDataServiceV1 infoDataServiceV1;
	@Resource
	private InfoPlanRepository infoPlanRepository;
	@Resource
	private InfoTempleteRepository infoTempleteRepository;

	
	/**
	 * 查询所有物业下的内容
	 * @return
	 */
	@Transactional
	@RequestMapping(path="/infodata/all/{businessId}",method = RequestMethod.GET)
	public ResponseEntity<List<InfoData>> findAllInfoData(@PathVariable("businessId") String businessId) throws Exception {
		logger.info("come into method findAllInfoData");
		
		return Optional.ofNullable(infoDataRepository.findByUserId(businessId))
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find InfoData list"));
	}
	
	/**
	 * 查询物业下的方案
	 * @return
	 */
	@Transactional
	@RequestMapping(path="/infodata/byInfoPlanId/{planId}",method = RequestMethod.GET)
	public ResponseEntity<List<InfoData>> findByInfoPlanId(@PathVariable("planId") String planId) throws Exception {
		logger.info("come into method findAllInfoData");
		SellerUser authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		return Optional.ofNullable(infoDataRepository.findByUserIdAndInfoPlanId(authenticatedUser.getEstateId(),planId))
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find InfoData list"));
	}
	
	/**
	 * 根据ID查询该物业的内容
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path="/infodata/{id}",method = RequestMethod.GET)
	public ResponseEntity<InfoData> findByTempId(@PathVariable("id") Long id) throws Exception {
		logger.info("come into method findByTempId with params:id="+id);
		SellerUser authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		
		return Optional.ofNullable(infoDataRepository.findByIdAndUserId(id,authenticatedUser.getEstateId()))
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find InfoData list"));
	}
	
//	/**
//	 * 创建店铺内容
//	 * @param infoDate
//	 * @return
//	 * @throws Exception
//	 *TODO 版本号获取及规则是否向上兼容 || 认证
//	 */
//	@Transactional
//	@RequestMapping(path="/infodata/new",method=RequestMethod.POST)
//	public ResponseEntity<InfoData> saveInfoData(@Validated @RequestBody InfoData infoData) throws Exception {
//		logger.info("come into method saveInfoData with params: "+infoData.toString());
//		infoData.setType("2");//类型1-平台，2-商店
//		SellerUser authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
//		infoData.setEstateId(authenticatedUser.getEstateId());
//		infoData.setUserId(""+authenticatedUser.getId());
//		infoData.setStoreId(authenticatedUser.getStoreId());
//		return Optional.ofNullable(infoDataRepository.save(infoData))
//	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
//	                .orElseThrow(() -> new Exception("Could not find a InfoData"));
//	}
	
	/**
	 * 创建物业内容
	 * @param infoDate
	 * @return
	 * @throws Exception
	 *TODO 版本号获取及规则是否向上兼容 || 认证
	 */
	@Transactional
	@RequestMapping(path="/infodata/new",method=RequestMethod.POST)
	public ResponseEntity<InfoData> saveInfoData(@Validated @RequestBody InfoData infoData) throws Exception {
		logger.info("come into method saveInfoData with params: "+infoData.toString());
		SellerUser authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		
		infoData.setStoreId(authenticatedUser.getStoreId());
		return Optional.ofNullable(infoDataRepository.save(infoData))
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find a InfoData"));
	}
	
	/**
	 * 修改
	 * @param infoDate
	 * @return
	 * @throws Exception
	 *TODO 版本号获取及规则是否向上兼容 || 认证
	 */
	@Transactional
	@RequestMapping(path="/infodata/update",method=RequestMethod.POST)
	public ResponseEntity<InfoData> updateInfoData(@Validated @RequestBody InfoData infoDate) throws Exception {
		logger.info("come into method updateInfoData with params: "+infoDate.toString());
		
		InfoData findOne = infoDataRepository.findOne(infoDate.getId());
		if(findOne == null){
			logger.error("come into method updateInfoData update info data error,cannot find data"+infoDate.toString());
			throw new Exception("Could not find a InfoData");
		}
		return Optional.ofNullable(infoDataRepository.save(infoDate))
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find a InfoData"));
	}
	
	/**
	 * 删除
	 * TODO 认证
	 */
	@Transactional
	@RequestMapping(path="/infodata/del/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<InfoData> deleteModelContent(@PathVariable("id") Long id) throws Exception {
		infoDataRepository.delete(id);
		return new ResponseEntity<InfoData>(HttpStatus.OK);
	}
	
	/**
	 * 上移或者下移
	 * TODO 认证
	 */
	@Transactional
	@RequestMapping(path="/infodata/batchUpdate",method=RequestMethod.POST)
	public ResponseEntity updateBatchInfoData(@Validated @RequestBody List<InfoData> infoData) throws Exception {
		logger.info("come into method updateBatchInfoData with params: "+infoData.toString());
		for (InfoData data : infoData) {
			InfoData findOne = infoDataRepository.findOne(data.getId());
			if(findOne == null){
				logger.error("come into method updateBatchInfoData update info data error,cannot find data"+data.toString());
				throw new Exception("Could not find a InfoData");
			}
			if(data.getSort() != null && !"".equals(data.getSort())){
			    findOne.setSort(data.getSort());
			}
			if(data.getHref() != null && !"".equals(data.getHref())){
			    findOne.setHref(data.getHref());
			}
			if(data.getData() != null && !"".equals(data.getData())){
			    findOne.setData(data.getData());
			}
			if(data.getPath() != null && !"".equals(data.getPath())){
			    findOne.setPath(data.getPath());
			}
			if(data.getName() != null && !"".equals(data.getName())){
			    findOne.setName(data.getName());
			}
			infoDataRepository.save(findOne);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	/**
	 * 删除方案
	 * TODO 认证
	 */
	@Transactional
	@RequestMapping(path="/plan/del/{id}",method=RequestMethod.DELETE)
	public ResponseEntity deletePlanContent(@PathVariable("id") Long id) throws Exception {
		infoPlanRepository.delete(id);
		return new ResponseEntity(HttpStatus.OK);
	}
	/**
	 * 查询该物业下的方案
	 * TODO 认证
	 */
	@Transactional
	@RequestMapping(path="/plan/one/{id}",method=RequestMethod.GET)
	public ResponseEntity findInfoPlan(@PathVariable("id") Long id) throws Exception {
		logger.info("come into method findInfoPlan");
		SellerUser authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		InfoPlan findByIdAndStoreId = infoPlanRepository.findByIdAndUserId(id,authenticatedUser.getEstateId());
		InfoTemplate findOne = infoTempleteRepository.findOne(Long.valueOf(findByIdAndStoreId.getInfoTempleteId()));
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("InfoPlan", findByIdAndStoreId);
		map.put("InfoTemplat", findOne);
		return Optional.ofNullable(map)
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find InfoPlan list"));
	}
	
	/**
	 * 查询所有物业下的方案
	 * TODO 认证
	 */
	@Transactional
	@RequestMapping(path="/plan/all",method=RequestMethod.GET)
	public ResponseEntity findAllInfoPlan() throws Exception {
		logger.info("come into method findAllInfoPlan");
		SellerUser authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		List<InfoPlan> findByStoreId = infoPlanRepository.findByUserId(authenticatedUser.getEstateId());
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (InfoPlan infoPlan : findByStoreId) {
			InfoTemplate findOne = infoTempleteRepository.findOne(Long.valueOf(infoPlan.getInfoTempleteId()));
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("InfoPlan", infoPlan);
			map.put("InfoTemplat", findOne);
			list.add(map);
		}
		return Optional.ofNullable(list)
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find InfoPlan list"));
	}
	/**
	 * 保存方案
	 * TODO 认证
	 */
	@Transactional
	@RequestMapping(path="/plan/new",method=RequestMethod.POST)
	public ResponseEntity saveInfoPlan(@RequestBody InfoPlan infoPlan) throws Exception {
		logger.info("come into method saveInfoPlan");
		infoPlan.setOutFileName("index.html");
		infoPlan.setActive("0");
		SellerUser authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
	
		infoPlan.setStoreId(authenticatedUser.getStoreId());
		return Optional.ofNullable(infoPlanRepository.save(infoPlan))
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not save infoPlan "));
	}
	
	
	/**
	 * 修改方案
	 * TODO 认证
	 */
	@Transactional
	@RequestMapping(path="/plan/update",method=RequestMethod.POST)
	public ResponseEntity updateInfoPlan(@RequestBody InfoPlan infoPlan) throws Exception {
		logger.info("come into method updateInfoPlan");
		InfoPlan findOne = infoPlanRepository.findOne(infoPlan.getId());
		if(findOne == null){
			throw new Exception("Could not save infoPlan ");
		}
		findOne.setInfoTempleteId(infoPlan.getInfoTempleteId());
		findOne.setName(infoPlan.getName());
		return Optional.ofNullable(infoPlanRepository.save(infoPlan))
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not save infoPlan "));
	}
	
	/**
	 * 激活方案，静态化的处理，生成首页
	 */
	@Transactional
	@RequestMapping(path="/activePlan",method=RequestMethod.POST)
	public ResponseEntity<InfoPlan> activePlan(@RequestBody InfoPlan infoplan) throws Exception {
		logger.info("activePlan method run params infoplan is "+infoplan.toString());
		//1.找到激活的模板内容
		List<InfoPlan> findAll = null;
		if(infoplan.getStoreId() != null){
			findAll = infoPlanRepository.findByStoreId(infoplan.getStoreId());
		}else{
			findAll = infoPlanRepository.findByUserIdAndType(""+infoplan.getUserId(),infoplan.getType());
		}
		for (InfoPlan plan : findAll) {
			if(infoplan.getId() != null && !infoplan.getId().equals(plan.getId())){
				plan.setActive("0");//是否激活-1-禁用  0-不激活，1-激活（只能激活一个
			}else{
				plan.setActive("1");//是否激活-1-禁用  0-不激活，1-激活（只能激活一个
				infoplan = plan;
			}
		}
		infoPlanRepository.save(findAll);
		logger.info("activePlan method active activeData is "+infoplan.toString());
		//2.静态化处理 模板名称,模板数据,输出路径
		try{
			//infoplan = infoDataServiceV1.createStaticTemplateFile(infoplan);
			infoplan = infoDataServiceV1.createStaticTemplateFileByCMD(infoplan);
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		return new ResponseEntity<InfoPlan>(infoplan,HttpStatus.OK);
	}
	
	/**
	 * 预览方案，静态化的处理
	 */
	@Transactional
	@RequestMapping(path="/showPlan",method=RequestMethod.POST)
	public ResponseEntity<InfoPlan> showPlan(@RequestBody InfoPlan infoplan) throws Exception {
		logger.info("showPlan method run params infoplan is "+infoplan.toString());
		//1.找到激活的模板内容
		
		//2.静态化处理 模板名称,模板数据,输出路径
		List<InfoPlan> findAll = null;
		if(infoplan.getStoreId() != null){
			findAll = infoPlanRepository.findByStoreId(infoplan.getStoreId());
		}else{
			findAll = infoPlanRepository.findByUserIdAndType(""+infoplan.getUserId(),infoplan.getType());
		}
		logger.info("showPlan method active activeData is "+findAll.toString());
		if(infoplan.getId() == null && infoplan.getUserId() != null && findAll != null){//激活指定商户
			infoplan = findAll.get(0);
		}
		try{
			//infoplan = infoDataServiceV1.createStaticTemplateFile(infoplan);
			infoplan = infoDataServiceV1.createTempStaticTemplateFileByCMD(infoplan);
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		return new ResponseEntity<InfoPlan>(infoplan,HttpStatus.OK);
	}
	
	/**
	 * 查询所有模板
	 * TODO 认证
	 */
	@Transactional
	@RequestMapping(path="/template/all",method=RequestMethod.GET)
	public ResponseEntity findAllInfoTemplate(
			@RequestParam("page") int page,
			@RequestParam("size") int size) throws Exception {
		logger.info("come into method findAllInfoTemplate");
		Pageable pageable = new PageRequest(page, size);
		Page<InfoTemplate> findAll = infoTempleteRepository.findAll(pageable);
		return Optional.ofNullable(findAll)
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find InfoPlan list"));
	}
	
}
