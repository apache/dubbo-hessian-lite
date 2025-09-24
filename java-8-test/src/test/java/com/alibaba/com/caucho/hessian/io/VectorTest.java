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
import java.util.Vector;

public class VectorTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        Vector<Integer> treeSet = new Vector<>();
        treeSet.add(1);
        treeSet.add(2);
        treeSet.add(4);
        treeSet.add(-1);

        Vector<Integer> result = baseHessian2Serialize(treeSet);
        Assertions.assertEquals(treeSet, result);

        treeSet.add(3);
        result.add(3);

        Assertions.assertEquals(treeSet, result);
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testCompact() throws IOException {
        Vector<Integer> treeSet = new Vector<>();
        treeSet.add(1);
        treeSet.add(2);
        treeSet.add(4);
        treeSet.add(-1);

        Assertions.assertEquals(treeSet, baseHessian2Serialize(treeSet));
        Assertions.assertEquals(treeSet, hessian4ToHessian3(treeSet));
        Assertions.assertEquals(treeSet, hessian3ToHessian3(treeSet));
        Assertions.assertEquals(treeSet, hessian3ToHessian4(treeSet));
    }

    @Test
    void testCollection() throws IOException {
        List<Object> list = new ArrayList<>();

        Vector<Integer> vector = new Vector<>();
        vector.add(1);
        vector.add(2);
        vector.add(4);
        vector.add(-1);
        testCollection(list, vector);
    }
}
