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
package com.alibaba.com.caucho.hessian.io.java8;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.List;

/**
 * Test Java8TimeSerializer class use compact mode
 */
public class Java8TimeSerializerUseCompactModeTest extends Java8TimeSerializerTest {

    @BeforeAll
    public static void setUp() {
        SerializationConfig.setCompactMode(true);
    }

    @AfterAll
    public static void tearDown() {
        SerializationConfig.setCompactMode(false);
    }

    @Test
    public void testLocalDate() throws Exception {
        testJava8Time(LocalDate.now());
    }

    protected void testJava8Time(Object expected) throws IOException {
        Assertions.assertEquals(expected, baseHessian2Serialize(expected));
        if (expected instanceof List && !((List) expected).isEmpty() && ((List) expected).get(0) instanceof Chronology) {
            return;
        }
        if (expected instanceof Chronology || expected instanceof ChronoPeriod || expected instanceof JapaneseDate
                || expected instanceof HijrahDate || expected instanceof MinguoDate || expected instanceof ThaiBuddhistDate) {
            return;
        }
        Assertions.assertEquals(expected, hessian3ToHessian3(expected));
        Assertions.assertEquals(expected, hessian3ToHessian4(expected));
    }
}
