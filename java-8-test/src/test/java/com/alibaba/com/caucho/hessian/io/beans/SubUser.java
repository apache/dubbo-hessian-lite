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
package com.alibaba.com.caucho.hessian.io.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class SubUser extends BaseUser implements Serializable {
    private static final long serialVersionUID = 4017613093053853415L;
    private String userName;
    private List<Integer> ageList;
    private List<Double> weightList;
    private List<Boolean> sexyList;

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Integer> getAgeList() {
        return ageList;
    }

    public void setAgeList(List<Integer> ageList) {
        this.ageList = ageList;
    }

    public List<Double> getWeightList() {
        return weightList;
    }

    public void setWeightList(List<Double> weightList) {
        this.weightList = weightList;
    }

    public List<Boolean> getSexyList() {
        return sexyList;
    }

    public void setSexyList(List<Boolean> sexyList) {
        this.sexyList = sexyList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubUser)) return false;
        if (!super.equals(o)) return false;
        SubUser subUser = (SubUser) o;
        return Objects.equals(userName, subUser.userName) &&
                Objects.equals(ageList, subUser.ageList) &&
                Objects.equals(weightList, subUser.weightList) &&
                Objects.equals(sexyList, subUser.sexyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userName, ageList, weightList, sexyList);
    }
}
