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
package com.alibaba.com.caucho.hessian.io;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Hessian2ReuseTest {

    @Test
    public void test() throws IOException {
        Hessian2Input h2i = new Hessian2Input(null);
        Hessian2Output h2o = new Hessian2Output(null);
        for (int i = 0; i < 100; i++) {
            String str = "Hello, Hessian2, round:" + i;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            h2o.init(outputStream);
            h2o.writeObject(str);
            h2o.flush();

            byte[] bytes = outputStream.toByteArray();

            h2i.init(new ByteArrayInputStream(bytes));
            String newStr = (String) h2i.readObject(String.class);
            Assert.assertEquals(str, newStr);
        }
    }
}
