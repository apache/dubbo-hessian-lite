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

import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StringBufferTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        StringBuffer originalStringBuffer = new StringBuffer("Hello, World!");

        StringBuffer result = baseHessian2Serialize(originalStringBuffer);

        Assertions.assertEquals(originalStringBuffer.toString(), result.toString());
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testCompact() throws IOException {
        StringBuffer obj = new StringBuffer("Hello, World!");
        Assertions.assertEquals(obj.toString(), baseHessian2Serialize(obj).toString());
        Assertions.assertEquals(obj.toString(), hessian3ToHessian3(obj).toString());
        Assertions.assertEquals(obj.toString(), hessian4ToHessian3(obj).toString());
        Assertions.assertEquals(obj.toString(), hessian3ToHessian4(obj).toString());
    }

    @Test
    void testCollection() throws IOException {
        StringBuffer originalStringBuffer = new StringBuffer("Hello, World!");

        List<StringBuffer> list = new ArrayList<>();
        list.add(originalStringBuffer);
        list.add(originalStringBuffer);

        List<StringBuffer> result = baseHessian2Serialize(list);

        Assertions.assertEquals(list.size(), result.size());
        Assertions.assertSame(result.get(0), result.get(1));
        Assertions.assertEquals(originalStringBuffer.toString(), result.get(0).toString());
    }
}
