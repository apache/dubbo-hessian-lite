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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CollectionsTest extends SerializeTestBase {
    @Test
    void testRandomAccessToUnmodifiableList() throws IOException {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> unmodifiableList = Collections.unmodifiableList(list);
        Assertions.assertEquals(unmodifiableList, baseHessian2Serialize(unmodifiableList));
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testRandomAccessToUnmodifiableListCompact() throws IOException {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> unmodifiableList = Collections.unmodifiableList(list);
        Assertions.assertEquals(unmodifiableList, baseHessian2Serialize(unmodifiableList));
        Assertions.assertEquals(unmodifiableList, hessian3ToHessian3(unmodifiableList));
        Assertions.assertEquals(unmodifiableList, hessian4ToHessian3(unmodifiableList));
        Assertions.assertEquals(unmodifiableList, hessian3ToHessian4(unmodifiableList));
    }

    @Test
    void testLinkedToUnmodifiableList() throws IOException {
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> unmodifiableList = Collections.unmodifiableList(list);
        Assertions.assertEquals(unmodifiableList, baseHessian2Serialize(unmodifiableList));
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testLinkedToUnmodifiableListCompact() throws IOException {
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> unmodifiableList = Collections.unmodifiableList(list);
        Assertions.assertEquals(unmodifiableList, baseHessian2Serialize(unmodifiableList));
        Assertions.assertEquals(unmodifiableList, hessian4ToHessian3(unmodifiableList));
        Assertions.assertEquals(unmodifiableList, hessian3ToHessian3(unmodifiableList));
        Assertions.assertEquals(unmodifiableList, hessian3ToHessian4(unmodifiableList));
    }

    @Test
    void testRandomAccessToSynchronizedList() throws IOException {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> unmodifiableList = Collections.synchronizedList(list);
        Assertions.assertEquals(unmodifiableList, baseHessian2Serialize(unmodifiableList));
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testRandomAccessToSynchronizedListCompact() throws IOException {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> unmodifiableList = Collections.synchronizedList(list);
        Assertions.assertEquals(unmodifiableList, baseHessian2Serialize(unmodifiableList));
        Assertions.assertEquals(unmodifiableList, hessian3ToHessian4(unmodifiableList));
        Assertions.assertEquals(unmodifiableList, hessian3ToHessian3(unmodifiableList));
        Assertions.assertEquals(unmodifiableList, hessian4ToHessian3(unmodifiableList));
    }

    @Test
    void testLinkedToSynchronizedList() throws IOException {
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> unmodifiableList = Collections.synchronizedList(list);
        Assertions.assertEquals(unmodifiableList, baseHessian2Serialize(unmodifiableList));
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testLinkedToSynchronizedListCompact() throws IOException {
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Integer> unmodifiableList = Collections.synchronizedList(list);
        Assertions.assertEquals(unmodifiableList, baseHessian2Serialize(unmodifiableList));
        Assertions.assertEquals(unmodifiableList, hessian3ToHessian4(unmodifiableList));
        Assertions.assertEquals(unmodifiableList, hessian4ToHessian3(unmodifiableList));
        Assertions.assertEquals(unmodifiableList, hessian3ToHessian3(unmodifiableList));
    }

    @Test
    void testCopiesList() throws IOException {
        List<Integer> copiesList = Collections.nCopies(3, 1);
        Assertions.assertEquals(copiesList, baseHessian2Serialize(copiesList));
        Assertions.assertEquals(copiesList.subList(1, 2), baseHessian2Serialize(copiesList.subList(1, 2)));
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testCopiesListCompact() throws IOException {
        List<Integer> copiesList = Collections.nCopies(3, 1);
        Assertions.assertEquals(copiesList, baseHessian2Serialize(copiesList));
        Assertions.assertEquals(copiesList, hessian4ToHessian3(copiesList));
        Assertions.assertEquals(copiesList, hessian3ToHessian4(copiesList));
        Assertions.assertEquals(copiesList, hessian3ToHessian3(copiesList));

        Assertions.assertEquals(copiesList.subList(1, 2), baseHessian2Serialize(copiesList.subList(1, 2)));
        Assertions.assertEquals(copiesList.subList(1, 2), hessian3ToHessian4(copiesList.subList(1, 2)));
        Assertions.assertEquals(copiesList.subList(1, 2), hessian3ToHessian3(copiesList.subList(1, 2)));
        Assertions.assertEquals(copiesList.subList(1, 2), hessian4ToHessian3(copiesList.subList(1, 2)));
    }

    @Test
    void testCollection() throws IOException {
        List<Object> list = new ArrayList<>();

        List<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        List<Integer> unmodifiableList = Collections.unmodifiableList(arrayList);
        testCollection(list, unmodifiableList);

        List<Integer> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        unmodifiableList = Collections.unmodifiableList(linkedList);
        testCollection(list, unmodifiableList);

        List<Integer> synchronizedList = Collections.synchronizedList(arrayList);
        testCollection(list, synchronizedList);
        synchronizedList = Collections.synchronizedList(linkedList);
        testCollection(list, synchronizedList);

        List<Integer> copiesList = Collections.nCopies(3, 1);
        testCollection(list, copiesList);
    }
}
