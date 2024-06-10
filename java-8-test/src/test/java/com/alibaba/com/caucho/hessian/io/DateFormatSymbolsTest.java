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
import java.text.DateFormatSymbols;
import java.util.Locale;

public class DateFormatSymbolsTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        DateFormatSymbols originalDateFormatSymbols = new DateFormatSymbols(Locale.US);

        DateFormatSymbols result = baseHessian2Serialize(originalDateFormatSymbols);

        Assertions.assertArrayEquals(originalDateFormatSymbols.getAmPmStrings(), result.getAmPmStrings());
        Assertions.assertArrayEquals(originalDateFormatSymbols.getEras(), result.getEras());
        Assertions.assertArrayEquals(originalDateFormatSymbols.getMonths(), result.getMonths());
        Assertions.assertArrayEquals(originalDateFormatSymbols.getShortMonths(), result.getShortMonths());
        Assertions.assertArrayEquals(originalDateFormatSymbols.getWeekdays(), result.getWeekdays());
        Assertions.assertArrayEquals(originalDateFormatSymbols.getShortWeekdays(), result.getShortWeekdays());
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testCompact() throws IOException {
        DateFormatSymbols obj = new DateFormatSymbols(Locale.US);
//        Assertions.assertArrayEquals(obj.getAmPmStrings(), baseHessian2Serialize(obj).getAmPmStrings());
//        Assertions.assertArrayEquals(obj.getEras(), baseHessian2Serialize(obj).getEras());
//        Assertions.assertArrayEquals(obj.getMonths(), baseHessian2Serialize(obj).getMonths());
//        Assertions.assertArrayEquals(obj.getShortMonths(), baseHessian2Serialize(obj).getShortMonths());
//        Assertions.assertArrayEquals(obj.getWeekdays(), baseHessian2Serialize(obj).getWeekdays());
//        Assertions.assertArrayEquals(obj.getShortWeekdays(), baseHessian2Serialize(obj).getShortWeekdays());
//
        Assertions.assertArrayEquals(obj.getAmPmStrings(), hessian3ToHessian3(obj).getAmPmStrings());
//        Assertions.assertArrayEquals(obj.getEras(), hessian3ToHessian3(obj).getEras());
//        Assertions.assertArrayEquals(obj.getMonths(), hessian3ToHessian3(obj).getMonths());
//        Assertions.assertArrayEquals(obj.getShortMonths(), hessian3ToHessian3(obj).getShortMonths());
//        Assertions.assertArrayEquals(obj.getWeekdays(), hessian3ToHessian3(obj).getWeekdays());
//        Assertions.assertArrayEquals(obj.getShortWeekdays(), hessian3ToHessian3(obj).getShortWeekdays());

        Assertions.assertArrayEquals(obj.getAmPmStrings(), hessian4ToHessian3(obj).getAmPmStrings());
        Assertions.assertArrayEquals(obj.getEras(), hessian4ToHessian3(obj).getEras());
        Assertions.assertArrayEquals(obj.getMonths(), hessian4ToHessian3(obj).getMonths());
        Assertions.assertArrayEquals(obj.getShortMonths(), hessian4ToHessian3(obj).getShortMonths());
        Assertions.assertArrayEquals(obj.getWeekdays(), hessian4ToHessian3(obj).getWeekdays());
        Assertions.assertArrayEquals(obj.getShortWeekdays(), hessian4ToHessian3(obj).getShortWeekdays());

        Assertions.assertArrayEquals(obj.getAmPmStrings(), hessian3ToHessian4(obj).getAmPmStrings());
        Assertions.assertArrayEquals(obj.getEras(), hessian3ToHessian4(obj).getEras());
        Assertions.assertArrayEquals(obj.getMonths(), hessian3ToHessian4(obj).getMonths());
        Assertions.assertArrayEquals(obj.getShortMonths(), hessian3ToHessian4(obj).getShortMonths());
        Assertions.assertArrayEquals(obj.getWeekdays(), hessian3ToHessian4(obj).getWeekdays());
        Assertions.assertArrayEquals(obj.getShortWeekdays(), hessian3ToHessian4(obj).getShortWeekdays());
    }
}
