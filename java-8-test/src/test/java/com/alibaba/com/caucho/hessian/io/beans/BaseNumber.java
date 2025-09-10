package com.alibaba.com.caucho.hessian.io.beans;

import java.io.Serializable;

public class BaseNumber implements Serializable {

    private float number;
    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }
}
