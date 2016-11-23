package com.zyhao.openec.api.v1;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.zyhao.openec.entity.ActionPojo;
import com.zyhao.openec.entity.InfoData;
import com.zyhao.openec.entity.InfoPlan;
import com.zyhao.openec.entity.InfoTemplete;
import com.zyhao.openec.entity.ModelPojo;
import com.zyhao.openec.entity.SellerUser;
import com.zyhao.openec.repository.InfoDataRepository;
import com.zyhao.openec.repository.InfoTempleteRepository;

/**
 * The {@link InfoDataServiceV1} implements business logic for aggregating the state of
 * a user's actions represented by a sequence of {@link CartEvent}. The generated aggregate
 * uses event sourcing to produce a {@link ShoppingCart} containing a collection of
 * {@link demo.cart.LineItem}.
 *
 * @author Ben Hale
 * @author Kenny Bastani
 */
@Service
public class InfoDataServiceV1 {

    private final Log log = LogFactory.getLog(InfoDataServiceV1.class);
    private OAuth2RestTemplate oAuth2RestTemplate;
    private RestTemplate restTemplate;
    @Resource
	private InfoDataRepository infoDataRepository;
    @Resource
    private InfoTempleteRepository infoTempleteRepository;
    
    @Autowired
    public InfoDataServiceV1(
                                 @LoadBalanced OAuth2RestTemplate oAuth2RestTemplate,
                                 @LoadBalanced RestTemplate normalRestTemplate) {
        this.oAuth2RestTemplate = oAuth2RestTemplate;
        this.restTemplate = normalRestTemplate;
    }

    /**
     * Get the authenticated user from the user service
     *
     * @return the currently authenticated user
     */
    public SellerUser getAuthenticatedUser() {
    	log.info("----------------------------------");
        return oAuth2RestTemplate.getForObject("http://seller-service/saa/v1/me",SellerUser.class);
    }
    
    /**
     * 调用静态化的公共服务方法,生成首页静态模板文件
     * @param activeData
     * @return
     * @throws Exception
     */
	public InfoPlan createStaticTemplateFile(InfoPlan activePlan) throws Exception{
		
		InfoTemplete findOne = infoTempleteRepository.findOne(Long.valueOf(activePlan.getInfoTempleteId()));
		ModelPojo model = new ModelPojo();
		model.setModelName(findOne.getFilePath());
		model.setOutFileName(activePlan.getOutFileName());
		
		try{
			SellerUser user = getAuthenticatedUser();
			
			if(user == null){
				log.error("createStaticTemplateFile method run failed ,no login");
				//throw new Exception("no login");
			}
			log.info("createStaticTemplateFile run user is "+user.getId());
			List<InfoData> findByStoreIdAndInfoPlanId = infoDataRepository.findByStoreIdAndInfoPlanId(Long.valueOf(user.getId()),String.valueOf(activePlan.getId()));
			model.setData(findByStoreIdAndInfoPlanId);
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		HttpEntity<ModelPojo> formEntity = new HttpEntity<ModelPojo>(model, headers);
		String result = restTemplate.postForObject("http://static-service/v1/makeIndexTemplate", formEntity,String.class);
		if(!"SUCCESS".equals(result)){
			log.error("createStaticTemplateFile method run failed,static-service run error");
			throw new Exception("static-service run error");
		}
		log.info("createStaticTemplateFile run result is "+result);
		
		return activePlan;
	}
	
	/**
     * 调用静态化的公共服务方法,生成首页静态模板文件
     * @param activeData
     * @return
     * @throws Exception
     */
	public InfoPlan createStaticTemplateFileByCMD(InfoPlan activePlan) throws Exception{
		
		try{
			SellerUser user = getAuthenticatedUser();
			
			if(user == null){
				log.error("createStaticTemplateFileByCMD method run failed ,no login");
				//throw new Exception("no login");
			}
			log.info("createStaticTemplateFileByCMD run user is "+user.getId());
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		ActionPojo action = new ActionPojo();
		action.setAction("modelContentEditService.createTemplate");
		action.setParam(activePlan.getId());
		
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		HttpEntity<ActionPojo > formEntity = new HttpEntity<ActionPojo>(action, headers);
		String result = restTemplate.postForObject("http://static-service/nologin/makeTemplateByCmd", formEntity,String.class);
		if(!"SUCCESS".equals(result)){
			log.error("createStaticTemplateFile method run failed,static-service run error");
			throw new Exception("static-service run error");
		}
		log.info("createStaticTemplateFile run result is "+result);
		
		return activePlan;
	}
	
	/**
	 * 展示和预览方案
	 * @param infoplan
	 * @return
	 * @throws Exception 
	 */
	public InfoPlan createTempStaticTemplateFileByCMD(InfoPlan infoplan) throws Exception {
		try{
			SellerUser user = getAuthenticatedUser();
			
			if(user == null){
				log.error("createTempStaticTemplateFileByCMD method run failed ,no login");
				//throw new Exception("no login");
			}
			log.info("createTempStaticTemplateFileByCMD run user is "+user.getId());
		}catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		ActionPojo action = new ActionPojo();
		action.setAction("modelContentEditService.showTemplate");
		action.setParam(infoplan.getId());
		
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		HttpEntity<ActionPojo > formEntity = new HttpEntity<ActionPojo>(action, headers);
		String result = restTemplate.postForObject("http://static-service/nologin/makeTemplateByCmd", formEntity,String.class);
		if(!"SUCCESS".equals(result)){
			log.error("createStaticTemplateFile method run failed,static-service run error");
			throw new Exception("static-service run error");
		}
		log.info("createStaticTemplateFile run result is "+result);
		
		return infoplan;
	}
}
