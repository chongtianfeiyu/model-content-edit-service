package com.zyhao.openec.api.v1;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zyhao.openec.entity.InfoData;
import com.zyhao.openec.entity.InfoPlan;
import com.zyhao.openec.repository.InfoDataRepository;
import com.zyhao.openec.repository.InfoPlanRepository;
import com.zyhao.openec.user.User;


/**
 * 商户 模板内容管理
 * @author zheng.lu
 * @2016年10月20日
 */
@RestController
@RequestMapping("/v1")
public class InfoDataController {

	private org.slf4j.Logger logger = LoggerFactory.getLogger(InfoDataController.class);
	
	@Resource
	private InfoDataRepository infoDataRepository;
	@Resource
	private InfoDataServiceV1 infoDataServiceV1;
	@Resource
	private InfoPlanRepository infoPlanRepository;
	
	/**
	 * 查询所有
	 * @return
	 */
	@Transactional
	@RequestMapping(path="/infodata/all",method = RequestMethod.GET)
	public ResponseEntity<List<InfoData>> findAllInfoData() throws Exception {
		logger.info("come into method findAllInfoData");
		User authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		
		return Optional.ofNullable(infoDataRepository.findByStoreId(Long.valueOf(authenticatedUser.getId())))
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find InfoData list"));
	}
	
	/**
	 * 查询所有
	 * @return
	 */
	@Transactional
	@RequestMapping(path="/infodata/byInfoPlanId/{planId}",method = RequestMethod.GET)
	public ResponseEntity<List<InfoData>> findByInfoPlanId(@PathVariable("planId") String planId) throws Exception {
		logger.info("come into method findAllInfoData");
		User authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		return Optional.ofNullable(infoDataRepository.findByStoreIdAndInfoPlanId(authenticatedUser.getId(),planId))
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find InfoData list"));
	}
	
	/**
	 * 根据ID查询
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@RequestMapping(path="/infodata/{id}",method = RequestMethod.GET)
	public ResponseEntity<InfoData> findByTempId(@PathVariable("id") Long id) throws Exception {
		logger.info("come into method findByTempId with params:id="+id);
		User authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		
		return Optional.ofNullable(infoDataRepository.findByIdAndStoreId(id,Long.valueOf(authenticatedUser.getId())))
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find InfoData list"));
	}
	
	/**
	 * 创建
	 * @param infoDate
	 * @return
	 * @throws Exception
	 *TODO 版本号获取及规则是否向上兼容 || 认证
	 */
	@Transactional
	@RequestMapping(path="/infodata/new",method=RequestMethod.POST)
	public ResponseEntity<InfoData> saveInfoData(@Validated @RequestBody InfoData infoDate) throws Exception {
		logger.info("come into method saveInfoData with params: "+infoDate.toString());
		 return Optional.ofNullable(infoDataRepository.save(infoDate))
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
	 * 查询方案
	 * TODO 认证
	 */
	@Transactional
	@RequestMapping(path="/plan/one/{id}",method=RequestMethod.GET)
	public ResponseEntity findInfoPlan(@PathVariable("id") Long id) throws Exception {
		logger.info("come into method findInfoPlan");
		User authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		return Optional.ofNullable(infoPlanRepository.findByIdAndStoreId(id,authenticatedUser.getId()))
	                .map(varname -> new ResponseEntity<>(varname, HttpStatus.OK))
	                .orElseThrow(() -> new Exception("Could not find InfoPlan list"));
	}
	
	/**
	 * 查询所有方案
	 * TODO 认证
	 */
	@Transactional
	@RequestMapping(path="/plan/all",method=RequestMethod.GET)
	public ResponseEntity findAllInfoPlan() throws Exception {
		logger.info("come into method findAllInfoPlan");
		User authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		return Optional.ofNullable(infoPlanRepository.findByStoreId(authenticatedUser.getId()))
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
		User authenticatedUser = infoDataServiceV1.getAuthenticatedUser();
		List<InfoPlan> findAll = infoPlanRepository.findByStoreId(authenticatedUser.getId());
		for (InfoPlan plan : findAll) {
			if(!infoplan.getId().equals(plan.getId())){
				plan.setActive("0");//是否激活-1-禁用  0-不激活，1-激活（只能激活一个
			}else{
				plan.setActive("1");//是否激活-1-禁用  0-不激活，1-激活（只能激活一个
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
	
	
}
