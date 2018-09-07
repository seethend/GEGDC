/**
 * 
 */
package com.gegdcrm.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author charan kandula
 *
 */
@Entity
public class ProjectBean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int projectId;
	//cancle or active or Inactive
	@Column
	private int projectStatus;
	@Column
	private String amount;
	@Column
	private String deliveryDays;
	// hours or fixed 
	@Column
	private String bidType;
	@Column
	private boolean notifyByMail;
	@Column
	private String projectMessage;

	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int bidderId) {
		this.projectId = bidderId;
	}
	public int getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(int bidderStatus) {
		this.projectStatus = bidderStatus;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDeliveryDays() {
		return deliveryDays;
	}
	public void setDeliveryDays(String deliveryDays) {
		this.deliveryDays = deliveryDays;
	}
	public boolean isNotifyByMail() {
		return notifyByMail;
	}
	public void setNotifyByMail(boolean notifyByMail) {
		this.notifyByMail = notifyByMail;
	}
	public String getBiddingMessage() {
		return projectMessage;
	}
	public void setBiddingMessage(String projectMessage) {
		this.projectMessage = projectMessage;
	}

}
