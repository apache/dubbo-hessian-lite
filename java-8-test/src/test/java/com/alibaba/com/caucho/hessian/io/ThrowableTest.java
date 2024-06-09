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

public class ThrowableTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        Throwable throwable = new Throwable("test");
        Throwable result = baseHessian2Serialize(throwable);
        Assertions.assertArrayEquals(throwable.getStackTrace(), result.getStackTrace());
        Assertions.assertEquals(throwable.getMessage(), result.getMessage());
        Assertions.assertEquals(throwable.getLocalizedMessage(), result.getLocalizedMessage());
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testCompact() throws IOException {
        Throwable throwable = new Throwable("test");

        Assertions.assertArrayEquals(throwable.getStackTrace(), baseHessian2Serialize(throwable).getStackTrace());
        Assertions.assertEquals(throwable.getMessage(), baseHessian2Serialize(throwable).getMessage());
        Assertions.assertEquals(throwable.getLocalizedMessage(), baseHessian2Serialize(throwable).getLocalizedMessage());

        Assertions.assertArrayEquals(throwable.getStackTrace(), hessian4ToHessian3(throwable).getStackTrace());
        Assertions.assertEquals(throwable.getMessage(), hessian4ToHessian3(throwable).getMessage());
        Assertions.assertEquals(throwable.getLocalizedMessage(), hessian4ToHessian3(throwable).getLocalizedMessage());

        Assertions.assertArrayEquals(throwable.getStackTrace(), hessian3ToHessian3(throwable).getStackTrace());
        Assertions.assertEquals(throwable.getMessage(), hessian3ToHessian3(throwable).getMessage());
        Assertions.assertEquals(throwable.getLocalizedMessage(), hessian3ToHessian3(throwable).getLocalizedMessage());

        Assertions.assertArrayEquals(throwable.getStackTrace(), hessian3ToHessian4(throwable).getStackTrace());
        Assertions.assertEquals(throwable.getMessage(), hessian3ToHessian4(throwable).getMessage());
        Assertions.assertEquals(throwable.getLocalizedMessage(), hessian3ToHessian4(throwable).getLocalizedMessage());
    }
}
