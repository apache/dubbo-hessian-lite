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
import java.util.Objects;

public class AppResult<T> implements Serializable {

    private int ret;
    private T Data;
    private int errorCode;
    private String message;

    public AppResult() {
    }

    public AppResult(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        ret = 0;
    }

    public AppResult(T data) {
        this.message = "success";
        this.Data = data;
        ret = 1;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AppResult<?> appResult = (AppResult<?>) object;
        return ret == appResult.ret && errorCode == appResult.errorCode && Objects.equals(Data, appResult.Data) && Objects.equals(message, appResult.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ret, Data, errorCode, message);
    }
}
