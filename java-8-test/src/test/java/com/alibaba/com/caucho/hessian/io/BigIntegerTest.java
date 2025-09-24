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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BigIntegerTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        BigInteger originalBigInteger = new BigInteger("1234567890");

        BigInteger result = baseHessian2Serialize(originalBigInteger);

        Assertions.assertEquals(originalBigInteger, result);
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testCompact() throws IOException {
        BigInteger obj = new BigInteger("1234567890");
        Assertions.assertEquals(obj, baseHessian2Serialize(obj));
        Assertions.assertEquals(obj, hessian3ToHessian3(obj));
        Assertions.assertEquals(obj, hessian4ToHessian3(obj));
        Assertions.assertEquals(obj, hessian3ToHessian4(obj));
    }

    @Test
    void testCollection() throws IOException {
        List<Object> list = new ArrayList<>();

        BigInteger originalBigInteger = new BigInteger("1234567890");
        testCollection(list, originalBigInteger);
    }
}
