/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.com.caucho.hessian.io.issue73;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ComplainDto implements Serializable {

    private static final long serialVersionUID = 9118106863827943078L;

    private String complainOrderId;

    private String complainDate;

    private Byte complainStatus;

    private String complainStatusName;

    private String customerName;

    private String class1Name;

    private String class2Name;

    private String class3Name;

    private String remark;

    private String appealReason;

    private List<String> userAccessory;

    private List<String> complainOi;

    private String surplusDate;

    private String complainResult;

    private Byte totalType;

    private BigDecimal total;

	private Integer appealState;

    private Byte appealType;

    private Boolean showType;


    public String getComplainOrderId() {
        return complainOrderId;
    }

    public void setComplainOrderId(String complainOrderId) {
        this.complainOrderId = complainOrderId;
    }

    public String getComplainDate() {
        return complainDate;
    }

    public void setComplainDate(String complainDate) {
        this.complainDate = complainDate;
    }

    public Byte getComplainStatus() {
        return complainStatus;
    }

    public void setComplainStatus(Byte complainStatus) {
        this.complainStatus = complainStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getClass1Name() {
        return class1Name;
    }

    public void setClass1Name(String class1Name) {
        this.class1Name = class1Name;
    }

    public String getClass2Name() {
        return class2Name;
    }

    public void setClass2Name(String class2Name) {
        this.class2Name = class2Name;
    }

    public String getClass3Name() {
        return class3Name;
    }

    public void setClass3Name(String class3Name) {
        this.class3Name = class3Name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAppealReason() {
        return appealReason;
    }

    public void setAppealReason(String appealReason) {
        this.appealReason = appealReason;
    }

    public List<String> getUserAccessory() {
        return userAccessory;
    }

    public void setUserAccessory(List<String> userAccessory) {
        this.userAccessory = userAccessory;
    }

    public List<String> getComplainOi() {
        return complainOi;
    }

    public void setComplainOi(List<String> complainOi) {
        this.complainOi = complainOi;
    }

    public String getSurplusDate() {
        return surplusDate;
    }

    public void setSurplusDate(String surplusDate) {
        this.surplusDate = surplusDate;
    }

    public String getComplainStatusName() {
        return complainStatusName;
    }

    public void setComplainStatusName(String complainStatusName) {
        this.complainStatusName = complainStatusName;
    }

	public String getComplainResult() {
		return complainResult;
	}

	public void setComplainResult(String complainResult) {
		this.complainResult = complainResult;
	}

    public Byte getTotalType() {
        return totalType;
    }

    public void setTotalType(Byte totalType) {
        this.totalType = totalType;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

	public Integer getAppealState() {
		return appealState;
	}

	public void setAppealState(Integer appealState) {
		this.appealState = appealState;
	}

    public Byte getAppealType() {
        return appealType;
    }

    public void setAppealType(Byte appealType) {
        this.appealType = appealType;
    }

    public Boolean getShowType() {
        return showType;
    }

    public void setShowType(Boolean showType) {
        this.showType = showType;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ComplainDto that = (ComplainDto) object;
        return Objects.equals(complainOrderId, that.complainOrderId) && Objects.equals(complainDate, that.complainDate) && Objects.equals(complainStatus, that.complainStatus) && Objects.equals(complainStatusName, that.complainStatusName) && Objects.equals(customerName, that.customerName) && Objects.equals(class1Name, that.class1Name) && Objects.equals(class2Name, that.class2Name) && Objects.equals(class3Name, that.class3Name) && Objects.equals(remark, that.remark) && Objects.equals(appealReason, that.appealReason) && Objects.equals(userAccessory, that.userAccessory) && Objects.equals(complainOi, that.complainOi) && Objects.equals(surplusDate, that.surplusDate) && Objects.equals(complainResult, that.complainResult) && Objects.equals(totalType, that.totalType) && Objects.equals(total, that.total) && Objects.equals(appealState, that.appealState) && Objects.equals(appealType, that.appealType) && Objects.equals(showType, that.showType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(complainOrderId, complainDate, complainStatus, complainStatusName, customerName, class1Name, class2Name, class3Name, remark, appealReason, userAccessory, complainOi, surplusDate, complainResult, totalType, total, appealState, appealType, showType);
    }
}
