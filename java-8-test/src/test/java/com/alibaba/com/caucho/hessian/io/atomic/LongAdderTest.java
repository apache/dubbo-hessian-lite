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
package com.alibaba.com.caucho.hessian.io.atomic;

import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

public class LongAdderTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        LongAdder longAdder = new LongAdder();
        longAdder.add(1);

        Assertions.assertEquals(longAdder.longValue(), baseHessian2Serialize(longAdder).longValue());
    }

    @Test
    void testCollection() throws IOException {
        LongAdder longAdder = new LongAdder();
        longAdder.add(1);

        List<LongAdder> list = new ArrayList<>();
        list.add(longAdder);
        list.add(longAdder);

        List<LongAdder> result = baseHessian2Serialize(list);

        Assertions.assertEquals(list.size(), result.size());
        Assertions.assertSame(result.get(0), result.get(1));
        Assertions.assertEquals(longAdder.longValue(), result.get(0).longValue());
    }
}
