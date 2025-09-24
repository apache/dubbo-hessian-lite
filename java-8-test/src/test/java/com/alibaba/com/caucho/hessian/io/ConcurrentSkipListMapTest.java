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
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConcurrentSkipListMapTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        ConcurrentSkipListMap<String, Integer> originalMap = new ConcurrentSkipListMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);
        originalMap.put("three", 3);

        ConcurrentSkipListMap<String, Integer> result = baseHessian2Serialize(originalMap);

        Assertions.assertEquals(originalMap, result);
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testWithCompact() throws IOException {
        ConcurrentSkipListMap<String, Integer> originalMap = new ConcurrentSkipListMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);
        originalMap.put("three", 3);

        Assertions.assertEquals(originalMap, baseHessian2Serialize(originalMap));
        Assertions.assertEquals(originalMap, hessian3ToHessian3(originalMap));
        Assertions.assertEquals(originalMap, hessian4ToHessian3(originalMap));
        Assertions.assertEquals(originalMap, hessian3ToHessian4(originalMap));
    }

    @Test
    void testWithComparator() throws IOException {
        ConcurrentSkipListMap<Integer, Integer> originalMap = new ConcurrentSkipListMap<>(Integer::compareTo);
        originalMap.put(1, 1);
        originalMap.put(2, 2);
        originalMap.put(4, 4);
        originalMap.put(-1, -1);

        ConcurrentSkipListMap<Integer, Integer> result = baseHessian2Serialize(originalMap);
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

    @Test
    void testCollection() throws IOException {
        List<Object> list = new ArrayList<>();

        ConcurrentSkipListMap<String, Integer> originalMap = new ConcurrentSkipListMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);
        originalMap.put("three", 3);
        list.add(originalMap);
    }
}
