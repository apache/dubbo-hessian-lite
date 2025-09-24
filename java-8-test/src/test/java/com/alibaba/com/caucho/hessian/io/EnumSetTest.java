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
import java.util.EnumSet;
import java.util.List;

enum TestEnumSet {
    ONE, TWO, THREE
}
enum TestLargeEnumSet {
    E1, E2, E3, E4, E5, E6, E7, E8, E9, E10,
    E11, E12, E13, E14, E15, E16, E17, E18, E19, E20,
    E21, E22, E23, E24, E25, E26, E27, E28, E29, E30,
    E31, E32, E33, E34, E35, E36, E37, E38, E39, E40,
    E41, E42, E43, E44, E45, E46, E47, E48, E49, E50,
    E51, E52, E53, E54, E55, E56, E57, E58, E59, E60,
    E61, E62, E63, E64, E65, E66, E67, E68, E69
}

public class EnumSetTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        EnumSet<TestEnumSet> originalEnumSet = EnumSet.of(TestEnumSet.ONE, TestEnumSet.TWO);

        EnumSet<TestEnumSet> result = baseHessian2Serialize(originalEnumSet);

        Assertions.assertEquals(originalEnumSet, result);

        originalEnumSet.add(TestEnumSet.THREE);
        result.add(TestEnumSet.THREE);
        Assertions.assertEquals(originalEnumSet, result);
    }

    @Test
    void testJumbo() throws IOException {
        EnumSet<TestLargeEnumSet> originalEnumSet = EnumSet.of(TestLargeEnumSet.E1, TestLargeEnumSet.E2);

        EnumSet<TestLargeEnumSet> result = baseHessian2Serialize(originalEnumSet);

        Assertions.assertEquals(originalEnumSet, result);

        originalEnumSet.add(TestLargeEnumSet.E3);
        result.add(TestLargeEnumSet.E3);
        Assertions.assertEquals(originalEnumSet, result);
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testCompactHessian3() throws IOException {
        EnumSet<TestEnumSet> enumSet = EnumSet.of(TestEnumSet.ONE, TestEnumSet.TWO);
        Assertions.assertEquals(enumSet, baseHessian2Serialize(enumSet));
        Assertions.assertEquals(enumSet, hessian3ToHessian3(enumSet));
        Assertions.assertEquals(enumSet, hessian3ToHessian4(enumSet));
        Assertions.assertEquals(enumSet, hessian4ToHessian3(enumSet));

        EnumSet<TestLargeEnumSet> largeEnumSet = EnumSet.of(TestLargeEnumSet.E1, TestLargeEnumSet.E2);
        Assertions.assertEquals(largeEnumSet, baseHessian2Serialize(largeEnumSet));
        Assertions.assertEquals(largeEnumSet, hessian3ToHessian3(largeEnumSet));
        Assertions.assertEquals(largeEnumSet, hessian3ToHessian4(largeEnumSet));
        Assertions.assertEquals(largeEnumSet, hessian4ToHessian3(largeEnumSet));
    }

    @Test
    void testCollection() throws IOException {
        List<Object> list = new ArrayList<>();

        EnumSet<TestEnumSet> originalEnumSet = EnumSet.of(TestEnumSet.ONE, TestEnumSet.TWO);
        testCollection(list, originalEnumSet);
    }
}
