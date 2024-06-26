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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * hessian base serialize utils
 *
 */
public class SerializeTestBase {

    /**
     * hessian2 serialize util
     *
     * @param data
     * @param <T>
     * @return
     * @throws IOException
     */
    protected <T> T baseHessian2Serialize(T data) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bout);

        out.writeObject(data);
        out.flush();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        Hessian2Input input = new Hessian2Input(bin);
        return (T) input.readObject();
    }

    protected <T> T hessian3ToHessian4(T data) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        com.alibaba.com.caucho.hessian3.io.Hessian2Output out = new com.alibaba.com.caucho.hessian3.io.Hessian2Output(bout);

        out.writeObject(data);
        out.flush();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        Hessian2Input input = new Hessian2Input(bin);
        return (T) input.readObject();
    }

    protected <T> T hessian3ToHessian3(T data) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        com.alibaba.com.caucho.hessian3.io.Hessian2Output out = new com.alibaba.com.caucho.hessian3.io.Hessian2Output(bout);

        out.writeObject(data);
        out.flush();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        com.alibaba.com.caucho.hessian3.io.Hessian2Input input = new com.alibaba.com.caucho.hessian3.io.Hessian2Input(bin);
        return (T) input.readObject();
    }

    protected <T> T hessian4ToHessian3(T data) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bout);

        out.writeObject(data);
        out.flush();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        com.alibaba.com.caucho.hessian3.io.Hessian2Input input = new com.alibaba.com.caucho.hessian3.io.Hessian2Input(bin);
        return (T) input.readObject();
    }


}
