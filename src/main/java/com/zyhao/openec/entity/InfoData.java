package com.zyhao.openec.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Model class of 信息数据.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
@Entity(name="info_data")
public class InfoData implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** id. */
	@Id
	@GeneratedValue
	private Long id;

	/** 配图名称. */
	private String name;

	/** 配图路径. */
	private String path;

	/** 类型. */
	private String type;

	/** 店铺ID. */
	private Long storeId;

	/** 图片跳转地址. */
	private String href;

	/** 方案. */
	private String infoPlanId;

	/** 数据. */
	private String data;

	/** 排序. */
	private Integer sort;

	/**
	 * Constructor.
	 */
	public InfoData() {
	}

	/**
	 * Set the id.
	 * 
	 * @param id
	 *            id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the id.
	 * 
	 * @return id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the 配图名称.
	 * 
	 * @param name
	 *            配图名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the 配图名称.
	 * 
	 * @return 配图名称
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the 配图路径.
	 * 
	 * @param path
	 *            配图路径
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Get the 配图路径.
	 * 
	 * @return 配图路径
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Set the 类型.
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get the 类型.
	 * 
	 * @return 类型
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Set the 店铺ID.
	 * 
	 * @param storeId
	 *            店铺ID
	 */
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	/**
	 * Get the 店铺ID.
	 * 
	 * @return 店铺ID
	 */
	public Long getStoreId() {
		return this.storeId;
	}

	/**
	 * Set the 图片跳转地址.
	 * 
	 * @param href
	 *            图片跳转地址
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * Get the 图片跳转地址.
	 * 
	 * @return 图片跳转地址
	 */
	public String getHref() {
		return this.href;
	}

	/**
	 * Set the 方案.
	 * 
	 * @param infoPlanId
	 *            方案
	 */
	public void setInfoPlanId(String infoPlanId) {
		this.infoPlanId = infoPlanId;
	}

	/**
	 * Get the 方案.
	 * 
	 * @return 方案
	 */
	public String getInfoPlanId() {
		return this.infoPlanId;
	}

	/**
	 * Set the 数据.
	 * 
	 * @param data
	 *            数据
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * Get the 数据.
	 * 
	 * @return 数据
	 */
	public String getData() {
		return this.data;
	}

	/**
	 * Set the 排序.
	 * 
	 * @param sort
	 *            排序
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	/**
	 * Get the 排序.
	 * 
	 * @return 排序
	 */
	public Integer getSort() {
		return this.sort;
	}

	@Override
	public String toString() {
		return "InfoData [id=" + id + ", name=" + name + ", path=" + path + ", type=" + type + ", storeId=" + storeId
				+ ", href=" + href + ", infoPlanId=" + infoPlanId + ", data=" + data + ", sort=" + sort + "]";
	}
	
}
