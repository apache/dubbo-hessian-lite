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

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ConcurrentLinkedDequeTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        ConcurrentLinkedDeque<String> originalDeque = new ConcurrentLinkedDeque<>();
        originalDeque.add("one");
        originalDeque.add("two");
        originalDeque.add("three");

        ConcurrentLinkedDeque<String> result = baseHessian2Serialize(originalDeque);

        Assertions.assertEquals(new ArrayList<>(originalDeque), new ArrayList<>(result));

        originalDeque.add("four");
        result.add("four");

        Assertions.assertEquals(new ArrayList<>(originalDeque), new ArrayList<>(result));
        originalDeque.iterator().forEachRemaining(e -> Assertions.assertEquals(e, result.poll()));
    }
}
