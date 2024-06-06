package com.alibaba.com.caucho.hessian.io.java9;

import com.alibaba.com.caucho.hessian.io.AbstractHessianOutput;
import com.alibaba.com.caucho.hessian.io.AbstractSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapNSerializer extends AbstractSerializer {

    @Override
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (obj == null) {
            out.writeNull();
            return;
        }

        out.writeObject(new HashMap<>((Map) obj));
    }
}
