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
package org.apache.dubbo.hessian.java17.base;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Assertions;

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

    protected Object baseHessian2Serialize(Object data, Class cl) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bout);

        out.writeObject(data);
        out.flush();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        Hessian2Input input = new Hessian2Input(bin);
        return input.readObject(cl);
    }

    protected <T> void testCollection(List<Object> list, T t) throws IOException {
        try {
            list.clear();
            list.add(t);
            list.add(t);
            List<Object> result = baseHessian2Serialize(list);
            Assertions.assertEquals(list.size(), result.size());
            Assertions.assertSame(result.get(0), result.get(1));
            if (t instanceof Object[]) {
                Assertions.assertArrayEquals((Object[]) t, (Object[]) result.get(0));
            } else {
                Assertions.assertEquals(t, result.get(0));
            }
        } finally {
            list.clear();
        }
    }

}
