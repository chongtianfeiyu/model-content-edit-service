package com.zyhao.openec.api.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.zyhao.openec.entity.InfoData;
import com.zyhao.openec.entity.InfoPlan;
import com.zyhao.openec.entity.InfoTemplate;
import com.zyhao.openec.entity.RepEntity;
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
    private static String active_1 = "1";//0-不激活 1-激活
    private static String type_2 = "2";//1-平台2-商店
    private static String status_1 = "1";//1-有效 -1无效
	/**
	 * 查询所有物业下的内容
	 * @return
	 */
	@Transactional
	@RequestMapping(path="/infodata/all",method = RequestMethod.GET)
	public ResponseEntity<List<InfoData>> findAllInfoData() throws Exception {
		Sort sort = new Sort(Sort.Direction.ASC, "sort");
		
		Map<String,String[]> authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		if(authenticatedUser.get("Session_businessId") == null){
			logger.error(authenticatedUser+"物业ID不能为空");
			throw new Exception("物业ID不能为空");
		}
		logger.info("come into method findAllInfoData Session_businessId="+authenticatedUser.get("Session_businessId")[0]);
		return Optional.ofNullable(infoDataRepository.findByUserId(authenticatedUser.get("Session_businessId")[0],sort))
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
		Map<String,String[]> authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		if(authenticatedUser.get("Session_businessId") == null){
			logger.error(authenticatedUser+"物业ID不能为空");
			throw new Exception("物业ID不能为空");
		}
		return Optional.ofNullable(infoDataRepository.findByUserIdAndInfoPlanId(authenticatedUser.get("Session_businessId")[0],planId))
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
		Map<String,String[]> authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		String[] businessId = authenticatedUser.get("Session_businessId");
		if(businessId == null){
			logger.error(authenticatedUser+"物业ID不能为空");
			throw new Exception("物业ID不能为空");
		}
		return Optional.ofNullable(infoDataRepository.findByIdAndUserId(id,businessId[0]))
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find InfoData list"));
	}
	
	/**
	 * 创建物业内容
	 * @param infoDate
	 * @return
	 * @throws Exception
	 *TODO 版本号获取及规则是否向上兼容 || 认证
	 */
	@Transactional
	@RequestMapping(path="/infodata/new",method=RequestMethod.POST)
	public ResponseEntity<InfoData> saveInfoData(
			@Validated @RequestBody InfoData infoData,
			HttpServletRequest request) throws Exception {
		logger.info("come into method saveInfoData with params: "+infoData.toString());
		Map<String,String[]> authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		String[] Session_businessId = authenticatedUser.get("Session_businessId");
		if(Session_businessId == null){
			logger.error(authenticatedUser+"小区ID不能为空");
			throw new Exception("物业ID不能为空");
		}
		infoData.setType(type_2);
		infoData.setUserId(Session_businessId[0]);
		
		List<InfoPlan> findByUserId = infoPlanRepository.findByUserId(Session_businessId[0]);
		if(findByUserId != null && findByUserId.size() == 1){
			infoData.setInfoPlanId(""+findByUserId.get(0).getId());
		}else{
			for(InfoPlan pl : findByUserId){
				if(active_1.equals(pl.getActive())){//0-不激活 1-激活
					infoData.setInfoPlanId(""+pl.getId());
					break;
				}
			}
		}
		
		if(infoData.getId() != null){
			InfoData findOne = infoDataRepository.findOne(infoData.getId());
			findOne.setUserId(Session_businessId[0]);
			if(infoData.getData() != null){
				findOne.setData(infoData.getData());
			}
			if(infoData.getHref() != null){
				findOne.setHref(infoData.getHref());
			}
			if(infoData.getName() != null){
				findOne.setName(infoData.getName());
			}
			if(infoData.getPath() != null){
				findOne.setPath(infoData.getPath());
			}
			if(infoData.getSort() != null){
				findOne.setSort(infoData.getSort());
			}
			if(infoData.getStatus() != null&&infoData.getStatus() != ""){
				findOne.setStatus(infoData.getStatus());
			}
			if(findOne.getInfoPlanId() == null || findOne.getInfoPlanId() == ""){
				findOne.setInfoPlanId(infoData.getInfoPlanId());
			}
			
			findOne.setType(type_2);
			findOne.setUserId(Session_businessId[0]);
			
			
			
			
			infoData = findOne;
		}else{
			
			Long sort = 1L;
			sort = infoDataRepository.getMaxSortByUserIdAndType(Session_businessId[0],type_2);
			if(sort == null){
				sort = 1L;
			}
			infoData.setSort(sort.intValue()+1);
			if(infoData.getStatus() == null || infoData.getStatus()==""){
				infoData.setStatus(status_1);
			}
			
			infoData.setUserId(Session_businessId[0]);
		}
		
		InfoData save = infoDataRepository.save(infoData);
//		try{
//		    activePlan(infoPlanRepository.findOne(Long.valueOf(infoData.getInfoPlanId())),request);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
		return Optional.ofNullable(save)
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find a InfoData"));
	}
	
	
	/**
	 * 物业内容
	 * @param infoDate
	 * @return
	 * @throws Exception
	 *TODO 版本号获取及规则是否向上兼容 || 认证
	 */
	@Transactional
	@RequestMapping(path="/infodata/status",method=RequestMethod.POST)
	public ResponseEntity<InfoData> updateInfoData(
			@Validated @RequestBody InfoData infoData,
			HttpServletRequest request) throws Exception {
		logger.info("come into method saveInfoData with params: "+infoData.toString());
		Map<String,String[]> authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		String[] Session_businessId = authenticatedUser.get("Session_businessId");
		if(Session_businessId == null){
			logger.error(authenticatedUser+"小区ID不能为空");
			throw new Exception("物业ID不能为空");
		}
		
		InfoData findOne = infoDataRepository.findOne(infoData.getId());
		findOne.setType(type_2);
		findOne.setStatus(infoData.getStatus());
		findOne = infoDataRepository.save(findOne);

		return Optional.ofNullable(findOne)
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find a InfoData"));
	}
	
	/**
	 * 删除
	 * TODO 认证
	 */
	@Transactional
	@RequestMapping(path="/infodata/del/{id}",method=RequestMethod.GET)
	public ResponseEntity<RepEntity> deleteModelContent(@PathVariable("id") Long id) throws Exception {
		
		Map<String,String[]> authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		String[] Session_businessId = authenticatedUser.get("Session_businessId");
		String[] userId = authenticatedUser.get("Session_userId");
		
		if(Session_businessId == null){
			logger.error(authenticatedUser+"小区ID不能为空");
			throw new Exception("物业ID不能为空");
		}
		logger.info("deleteModelContent id="+id+"userId="+userId[0]+" Session_businessId = "+Session_businessId[0]);
		Long l = infoDataRepository.deleteByIdAndUserId(id,Session_businessId[0]);
		RepEntity rep = new RepEntity();
		if(l >=0){
			rep.setStatus("0");
			rep.setMsg("success");
			rep.setData(l);
		}else{
			rep.setStatus("-1");
			rep.setMsg("error");
			rep.setData(l);
		}
		return new ResponseEntity<RepEntity>(rep,HttpStatus.OK);
	}
	
	/**
	 * 上移或者下移
	 * TODO 认证
	 */
	@Transactional
	@RequestMapping(path="/infodata/batchUpdate",method=RequestMethod.POST)
	public ResponseEntity<RepEntity> updateBatchInfoData(@Validated @RequestBody List<InfoData> infoData) throws Exception {
		logger.info("come into method updateBatchInfoData with params: "+infoData.toString());
		
		RepEntity rep = new RepEntity();
		rep.setStatus("0");
		rep.setMsg("success");
		
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
		
		return new ResponseEntity<RepEntity>(rep,HttpStatus.OK);
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
		Map<String,String[]> authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		String[] businessId = authenticatedUser.get("Session_businessId");
		if(businessId == null){
			logger.error(authenticatedUser+"物业ID不能为空");
			throw new Exception("物业ID不能为空");
		}
		InfoPlan findByIdAndStoreId = infoPlanRepository.findByIdAndUserId(id,businessId[0]);
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

		Map<String,String[]> authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		String[] businessId = authenticatedUser.get("Session_businessId");
		if(businessId == null){
			logger.error(authenticatedUser+"物业ID不能为空");
			throw new Exception("物业ID不能为空");
		}
		List<InfoPlan> findByStoreId = infoPlanRepository.findByUserId(businessId[0]);
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

		Map<String,String[]> authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		String[] Session_businessId = authenticatedUser.get("Session_businessId");
		if(Session_businessId == null){
			logger.error(authenticatedUser+"物业ID不能为空");
			throw new Exception("物业ID不能为空");
		}
		
		infoPlan.setStoreId(Long.valueOf(Session_businessId[0]));
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
	public ResponseEntity<InfoPlan> activePlan(
			@RequestBody InfoPlan infoplan,
			HttpServletRequest request) throws Exception {
		logger.info("activePlan method run params infoplan is "+infoplan.toString());
		//1.找到激活的模板内容
		List<InfoPlan> findAll = null;
		if(infoplan.getStoreId() != null){
			findAll = infoPlanRepository.findByStoreId(infoplan.getStoreId());
		}else if(infoplan.getUserId() != null && infoplan.getType() != null){
			findAll = infoPlanRepository.findByUserIdAndType(""+infoplan.getUserId(),infoplan.getType());
		}else{
			String userId = infoDataServiceV1.getAuthenticatedUser().get("Session_businessId")[0];
			findAll = infoPlanRepository.findByUserIdAndType(userId,type_2);
		}
		if(findAll != null && findAll.size() == 1){
			findAll.get(0).setActive("0");
			infoplan = findAll.get(0);
		}else{
			for (InfoPlan plan : findAll) {
				if(infoplan.getId() != null && !infoplan.getId().equals(plan.getId())){
					plan.setActive("0");//是否激活-1-禁用  0-不激活，1-激活（只能激活一个
				}else{
					plan.setActive("1");//是否激活-1-禁用  0-不激活，1-激活（只能激活一个
					infoplan = plan;
				}
			}
		}
		infoPlanRepository.save(findAll);
		logger.info("activePlan method active activeData is "+infoplan.toString());
		//2.静态化处理 模板名称,模板数据,输出路径
		try{
			//infoplan = infoDataServiceV1.createStaticTemplateFile(infoplan);
			infoplan = infoDataServiceV1.createStaticTemplateFileByCMD(request,infoplan);
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
	public ResponseEntity<InfoPlan> showPlan(
			@RequestBody InfoPlan infoplan,
			HttpServletRequest request) throws Exception {
		logger.info("showPlan method run params infoplan is "+infoplan.toString());
		//1.找到激活的模板内容
		List<InfoPlan> findAll = null;
		if(infoplan.getStoreId() != null){
			findAll = infoPlanRepository.findByStoreId(infoplan.getStoreId());
		}else if(infoplan.getUserId() != null && infoplan.getType() != null){
			findAll = infoPlanRepository.findByUserIdAndType(""+infoplan.getUserId(),infoplan.getType());
		}else{
			String userId = infoDataServiceV1.getAuthenticatedUser().get("Session_businessId")[0];
			findAll = infoPlanRepository.findByUserIdAndType(userId,type_2);
		}
		if(findAll != null && findAll.size() == 1){
			infoplan = findAll.get(0);
		}
		//2.静态化处理 模板名称,模板数据,输出路径
		
		try{
			//infoplan = infoDataServiceV1.createStaticTemplateFile(infoplan);
			infoplan = infoDataServiceV1.createTempStaticTemplateFileByCMD(request,infoplan);
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
