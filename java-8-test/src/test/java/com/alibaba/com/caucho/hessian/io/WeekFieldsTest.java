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
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeekFieldsTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        WeekFields originalWeekFields = WeekFields.of(Locale.FRANCE);

        WeekFields result = baseHessian2Serialize(originalWeekFields);

        Assertions.assertEquals(originalWeekFields, result);
    }

    @Test
    void testCollection() throws IOException {
        WeekFields originalWeekFields = WeekFields.of(Locale.FRANCE);

        List<WeekFields> list = new ArrayList<>();
        list.add(originalWeekFields);
        list.add(originalWeekFields);

        List<WeekFields> result = baseHessian2Serialize(list);

        Assertions.assertEquals(list.size(), result.size());
        Assertions.assertSame(result.get(0), result.get(1));
        Assertions.assertEquals(originalWeekFields, result.get(0));
    }
}
