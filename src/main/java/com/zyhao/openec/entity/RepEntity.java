package com.zyhao.openec.entity;

/**
 * 响应实体(pojo)类
 * @author guangyuan.zhu@zymobi.com
 * @date 2016年12月15日 上午11:18:45
 */
public class RepEntity {
	
	private String status;
	
	private String msg;
	
	private Object data;
	
	public RepEntity() {
		super();
	}

	public RepEntity(String status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}
	
	public RepEntity(String status, String msg, Object data) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
