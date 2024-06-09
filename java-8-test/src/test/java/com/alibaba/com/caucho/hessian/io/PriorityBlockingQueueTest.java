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
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        PriorityBlockingQueue<Integer> originalPriorityBlockingQueue = new PriorityBlockingQueue<>(16, Integer::compareTo);
        originalPriorityBlockingQueue.add(1);
        originalPriorityBlockingQueue.add(2);
        originalPriorityBlockingQueue.add(4);
        originalPriorityBlockingQueue.add(-1);

        PriorityBlockingQueue<Integer> result = baseHessian2Serialize(originalPriorityBlockingQueue);

        Assertions.assertEquals(new ArrayList<>(originalPriorityBlockingQueue), new ArrayList<>(result));

        originalPriorityBlockingQueue.add(3);
        result.add(3);
        Assertions.assertEquals(new ArrayList<>(originalPriorityBlockingQueue), new ArrayList<>(result));
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testCompact() throws IOException {
        PriorityBlockingQueue<Integer> originalPriorityBlockingQueue = new PriorityBlockingQueue<>(16, Integer::compareTo);
        originalPriorityBlockingQueue.add(1);
        originalPriorityBlockingQueue.add(2);
        originalPriorityBlockingQueue.add(4);
        originalPriorityBlockingQueue.add(-1);

        Assertions.assertEquals(new ArrayList<>(originalPriorityBlockingQueue), new ArrayList<>(baseHessian2Serialize(originalPriorityBlockingQueue)));
        Assertions.assertEquals(new ArrayList<>(originalPriorityBlockingQueue), new ArrayList<>(hessian4ToHessian3(originalPriorityBlockingQueue)));
        Assertions.assertEquals(new ArrayList<>(originalPriorityBlockingQueue), new ArrayList<>(hessian3ToHessian3(originalPriorityBlockingQueue)));
        Assertions.assertEquals(new ArrayList<>(originalPriorityBlockingQueue), new ArrayList<>(hessian3ToHessian4(originalPriorityBlockingQueue)));
    }
}
