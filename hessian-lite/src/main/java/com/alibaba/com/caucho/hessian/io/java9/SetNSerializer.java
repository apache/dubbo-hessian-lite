package com.alibaba.com.caucho.hessian.io.java9;

import com.alibaba.com.caucho.hessian.io.AbstractHessianOutput;
import com.alibaba.com.caucho.hessian.io.AbstractSerializer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SetNSerializer extends AbstractSerializer {

    @Override
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (obj == null) {
            out.writeNull();
            return;
        }

        out.writeObject(new HashSet<>((Set) obj));
    }
}
