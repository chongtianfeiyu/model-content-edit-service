package com.zyhao.openec.entity;



/**
 * 根据指定动作生成静态文件 
 * 接收参数代理
 * @author zheng.lu
 * @2016年10月18日
 */
public class ActionPojo {
    
	private Object param;
	private String action;

	public ActionPojo() {
		super();
	}

	public ActionPojo(Object param, String action) {
		super();
		this.param = param;
		this.action = action;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
