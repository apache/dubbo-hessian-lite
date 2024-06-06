package org.apache.dubbo.hessian.java17;

import java.io.Serializable;

public record TestRecordObject(Integer id, String name, String email, Integer age) implements Serializable {
}
