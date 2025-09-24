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
import java.util.concurrent.LinkedTransferQueue;

public class LinkedTransferQueueTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        LinkedTransferQueue<Integer> originalLinkedTransferQueue = new LinkedTransferQueue<>();
        originalLinkedTransferQueue.offer(1);
        originalLinkedTransferQueue.offer(2);
        originalLinkedTransferQueue.offer(3);

        LinkedTransferQueue<Integer> result = baseHessian2Serialize(originalLinkedTransferQueue);

        Assertions.assertEquals(new ArrayList<>(originalLinkedTransferQueue), new ArrayList<>(result));

        originalLinkedTransferQueue.offer(4);
        result.offer(4);
        Assertions.assertEquals(new ArrayList<>(originalLinkedTransferQueue), new ArrayList<>(result));
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testCompact() throws IOException {
        LinkedTransferQueue<Integer> obj = new LinkedTransferQueue<>();
        obj.offer(1);
        obj.offer(2);
        obj.offer(3);
        Assertions.assertEquals(new ArrayList<>(obj), new ArrayList<>(baseHessian2Serialize(obj)));
        Assertions.assertEquals(new ArrayList<>(obj), new ArrayList<>(hessian3ToHessian3(obj)));
        Assertions.assertEquals(new ArrayList<>(obj), new ArrayList<>(hessian4ToHessian3(obj)));
        Assertions.assertEquals(new ArrayList<>(obj), new ArrayList<>(hessian3ToHessian4(obj)));
    }

    @Test
    void testCollection() throws IOException {
        LinkedTransferQueue<Integer> originalLinkedTransferQueue = new LinkedTransferQueue<>();
        originalLinkedTransferQueue.offer(1);
        originalLinkedTransferQueue.offer(2);
        originalLinkedTransferQueue.offer(3);

        List<LinkedTransferQueue<Integer>> list = new ArrayList<>();
        list.add(originalLinkedTransferQueue);
        list.add(originalLinkedTransferQueue);

        List<LinkedTransferQueue<Integer>> result = baseHessian2Serialize(list);

        Assertions.assertEquals(list.size(), result.size());
        Assertions.assertSame(result.get(0), result.get(1));
        Assertions.assertEquals(new ArrayList<>(originalLinkedTransferQueue), new ArrayList<>(result.get(0)));
    }
}
