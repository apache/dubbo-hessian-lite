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
import java.util.Map;
import java.util.TreeMap;

public class TreeMapTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        TreeMap<String, Integer> originalTreeMap = new TreeMap<>();
        originalTreeMap.put("one", 1);
        originalTreeMap.put("two", 2);
        originalTreeMap.put("three", 3);

        TreeMap<String, Integer> result = baseHessian2Serialize(originalTreeMap);

        Assertions.assertEquals(originalTreeMap, result);

        originalTreeMap.put("four", 4);
        result.put("four", 4);
        Assertions.assertEquals(originalTreeMap, result);
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testCompact() throws IOException {
        TreeMap<String, Integer> obj = new TreeMap<>();
        obj.put("one", 1);
        obj.put("two", 2);
        obj.put("three", 3);
        Assertions.assertEquals(obj, baseHessian2Serialize(obj));
        Assertions.assertEquals(obj, hessian3ToHessian3(obj));
        Assertions.assertEquals(obj, hessian4ToHessian3(obj));
        Assertions.assertEquals(obj, hessian3ToHessian4(obj));
    }

    @Test
    void testWithComparator() throws IOException {
        TreeMap<Integer, Integer> originalMap = new TreeMap<>(Integer::compareTo);
        originalMap.put(1, 1);
        originalMap.put(2, 2);
        originalMap.put(4, 4);
        originalMap.put(-1, -1);

        TreeMap<Integer, Integer> result = baseHessian2Serialize(originalMap);
        Assertions.assertEquals(originalMap, result);

        originalMap.put(3, 3);
        result.put(3, 3);

        Assertions.assertEquals(originalMap, result);
        originalMap.forEach((k, v) -> {
            Map.Entry<Integer, Integer> resultEntry = result.pollFirstEntry();
            Assertions.assertEquals(k, resultEntry.getKey());
            Assertions.assertEquals(v, resultEntry.getValue());
        });
    }
}
