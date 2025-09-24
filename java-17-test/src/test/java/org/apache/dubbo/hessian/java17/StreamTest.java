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
package org.apache.dubbo.hessian.java17;

import java.util.ArrayList;
import org.apache.dubbo.hessian.java17.base.SerializeTestBase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class StreamTest extends SerializeTestBase {
    @Test
    void testToList() throws IOException {
        List<Integer> list = Stream.of(1, 2, 3).toList();
        Assertions.assertEquals(list, baseHessian2Serialize(list));
    }

    @Test
    void testCollection() throws IOException {
        List<Object> list = new ArrayList<>();

        List<Integer> streamList = Stream.of(1, 2, 3).toList();
        testCollection(list, streamList);
    }
}
