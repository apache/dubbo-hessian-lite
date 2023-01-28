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
package com.alibaba.com.caucho.hessian.io.base;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import com.alibaba.com.caucho.hessian.io.HessianInput;
import com.alibaba.com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * hessian base serialize utils
 *
 */
public class SerializeTestBase {
    /**
     * hessian serialize util
     *
     * @param data
     * @return
     * @param <T>
     * @throws IOException
     */
    protected <T> T baseHessianSerialize(T data) throws IOException {
        return baseHessianSerialize(data, false);
    }

    /**
     * hessian serialize util
     *
     * @param data
     * @param isAllowNonSerializable
     * @return
     * @param <T>
     * @throws IOException
     */
    protected <T> T baseHessianSerialize(T data, boolean isAllowNonSerializable) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        HessianOutput out = new HessianOutput(bout);
        out.getSerializerFactory().setAllowNonSerializable(isAllowNonSerializable);

        out.writeObject(data);
        out.flush();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        HessianInput input = new HessianInput(bin);
        input.getSerializerFactory().setAllowNonSerializable(isAllowNonSerializable);
        return (T) input.readObject();
    }

    /**
     * hessian2 serialize util
     *
     * @param data
     * @return
     * @param <T>
     * @throws IOException
     */
    protected <T> T baseHessian2Serialize(T data) throws IOException {
        return baseHessian2Serialize(data, false);
    }

    /**
     * hessian2 serialize util
     *
     * @param data
     * @param isAllowNonSerializable
     * @return
     * @param <T>
     * @throws IOException
     */
    protected <T> T baseHessian2Serialize(T data, boolean isAllowNonSerializable) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bout);
        out.findSerializerFactory().setAllowNonSerializable(isAllowNonSerializable);

        out.writeObject(data);
        out.flush();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        Hessian2Input input = new Hessian2Input(bin);
        input.findSerializerFactory().setAllowNonSerializable(isAllowNonSerializable);
        return (T) input.readObject();
    }
}
