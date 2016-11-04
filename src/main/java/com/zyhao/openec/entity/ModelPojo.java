package com.zyhao.openec.entity;

import java.util.List;
/**
 * 模板输出代理
 * @author zheng.lu
 * @2016年10月20日
 */
public class ModelPojo {
	
	//模板名称
	private String modelName;
	
	//输出文件名 如 goodsInfo.json
	private String outFileName;
	//填充模板的数据、商品列表或者详情
	private List<InfoData> Data;
	
	public ModelPojo() {
		
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getOutFileName() {
		return outFileName;
	}
	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}
	public List<InfoData> getData() {
		return Data;
	}
	public void setData(List<InfoData> data) {
		Data = data;
	}
	
}
