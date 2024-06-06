package com.alibaba.com.caucho.hessian.io.java9;

import com.alibaba.com.caucho.hessian.io.AbstractHessianOutput;
import com.alibaba.com.caucho.hessian.io.AbstractSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubListSerializer extends AbstractSerializer {

    @Override
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (obj == null) {
            out.writeNull();
            return;
        }

        out.writeObject(new ArrayList<>((List) obj));
    }
}
