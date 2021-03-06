package com.zyhao.openec.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Model class of 登录用户信息.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class SellerUser implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** id. */
	private Long id;

	/** 新增时间. */
	private Date createTime;

	/** 邮箱. */
	private String email;

	/** 用户名. */
	private String loginnum;

	/** 密码. */
	private String password;

	/** 手机号. */
	private String mobile;

	/** 店铺编号. */
	private Long storeId;
	
	/** 物业ID. */
	private String estateId;

	/**
	 * Constructor.
	 */
	public SellerUser() {
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
	 * Set the 新增时间.
	 * 
	 * @param createTime
	 *            新增时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * Get the 新增时间.
	 * 
	 * @return 新增时间
	 */
	public Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * Set the 邮箱.
	 * 
	 * @param email
	 *            邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get the 邮箱.
	 * 
	 * @return 邮箱
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Set the 用户名.
	 * 
	 * @param loginnum
	 *            用户名
	 */
	public void setLoginnum(String loginnum) {
		this.loginnum = loginnum;
	}

	/**
	 * Get the 用户名.
	 * 
	 * @return 用户名
	 */
	public String getLoginnum() {
		return this.loginnum;
	}

	/**
	 * Set the 密码.
	 * 
	 * @param password
	 *            密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Get the 密码.
	 * 
	 * @return 密码
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Set the 手机号.
	 * 
	 * @param mobile
	 *            手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * Get the 手机号.
	 * 
	 * @return 手机号
	 */
	public String getMobile() {
		return this.mobile;
	}

	/**
	 * Set the 店铺编号.
	 * 
	 * @param storeId
	 *            店铺编号
	 */
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	/**
	 * Get the 店铺编号.
	 * 
	 * @return 店铺编号
	 */
	public Long getStoreId() {
		return this.storeId;
	}

	/**
	 * Set the 物业ID.
	 * 
	 * @param estateId
	 *            物业ID
	 */
	public void setEstateId(String estateId) {
		this.estateId = estateId;
	}

	/**
	 * Get the 物业ID.
	 * 
	 * @return 物业ID
	 */
	public String getEstateId() {
		return this.estateId;
	}

	@Override
	public String toString() {
		return "SellerUser [id=" + id + ", createTime=" + createTime + ", email=" + email + ", loginnum=" + loginnum
				+ ", password=" + password + ", mobile=" + mobile + ", storeId=" + storeId + ", estateId=" + estateId
				+ "]";
	}
	
}
