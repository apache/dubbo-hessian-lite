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
import java.util.Properties;

public class PropertiesTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        Properties originalProperties = new Properties();
        originalProperties.setProperty("key1", "value1");
        originalProperties.setProperty("key2", "value2");

        Properties result = baseHessian2Serialize(originalProperties);

        Assertions.assertEquals(originalProperties, result);
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testCompact() throws IOException {
        Properties obj = new Properties();
        obj.setProperty("key1", "value1");
        obj.setProperty("key2", "value2");
        Assertions.assertEquals(obj, baseHessian2Serialize(obj));
        Assertions.assertEquals(obj, hessian3ToHessian3(obj));
        Assertions.assertEquals(obj, hessian4ToHessian3(obj));
        Assertions.assertEquals(obj, hessian3ToHessian4(obj));
    }
}
