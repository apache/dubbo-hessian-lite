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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NumberFormatTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        NumberFormat originalNumberFormat = NumberFormat.getInstance(Locale.US);

        NumberFormat result = baseHessian2Serialize(originalNumberFormat);

        Assertions.assertEquals(originalNumberFormat.getMaximumFractionDigits(), result.getMaximumFractionDigits());
        Assertions.assertEquals(originalNumberFormat.getMaximumIntegerDigits(), result.getMaximumIntegerDigits());
        Assertions.assertEquals(originalNumberFormat.getMinimumFractionDigits(), result.getMinimumFractionDigits());
        Assertions.assertEquals(originalNumberFormat.getMinimumIntegerDigits(), result.getMinimumIntegerDigits());
        Assertions.assertEquals(originalNumberFormat.getRoundingMode(), result.getRoundingMode());
        // TODO Support currency
//        Assertions.assertEquals(originalNumberFormat.getCurrency(), result.getCurrency());
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testCompact() throws IOException {
        NumberFormat obj = NumberFormat.getInstance(Locale.US);
        Assertions.assertEquals(obj.getMaximumFractionDigits(), baseHessian2Serialize(obj).getMaximumFractionDigits());
        Assertions.assertEquals(obj.getMaximumIntegerDigits(), baseHessian2Serialize(obj).getMaximumIntegerDigits());
        Assertions.assertEquals(obj.getMinimumFractionDigits(), baseHessian2Serialize(obj).getMinimumFractionDigits());
        Assertions.assertEquals(obj.getMinimumIntegerDigits(), baseHessian2Serialize(obj).getMinimumIntegerDigits());
        Assertions.assertEquals(obj.getRoundingMode(), baseHessian2Serialize(obj).getRoundingMode());
        // TODO Support currency
//        Assertions.assertEquals(obj.getCurrency(), baseHessian2Serialize(obj).getCurrency());

        Assertions.assertEquals(obj.getMaximumFractionDigits(), hessian3ToHessian3(obj).getMaximumFractionDigits());
        Assertions.assertEquals(obj.getMaximumIntegerDigits(), hessian3ToHessian3(obj).getMaximumIntegerDigits());
        Assertions.assertEquals(obj.getMinimumFractionDigits(), hessian3ToHessian3(obj).getMinimumFractionDigits());
        Assertions.assertEquals(obj.getMinimumIntegerDigits(), hessian3ToHessian3(obj).getMinimumIntegerDigits());
        Assertions.assertEquals(obj.getRoundingMode(), hessian3ToHessian3(obj).getRoundingMode());
        // TODO Support currency
//        Assertions.assertEquals(obj.getCurrency(), hessian3ToHessian3(obj).getCurrency());

        Assertions.assertEquals(obj.getMaximumFractionDigits(), hessian4ToHessian3(obj).getMaximumFractionDigits());
        Assertions.assertEquals(obj.getMaximumIntegerDigits(), hessian4ToHessian3(obj).getMaximumIntegerDigits());
        Assertions.assertEquals(obj.getMinimumFractionDigits(), hessian4ToHessian3(obj).getMinimumFractionDigits());
        Assertions.assertEquals(obj.getMinimumIntegerDigits(), hessian4ToHessian3(obj).getMinimumIntegerDigits());
        Assertions.assertEquals(obj.getRoundingMode(), hessian4ToHessian3(obj).getRoundingMode());
        // TODO Support currency
//        Assertions.assertEquals(obj.getCurrency(), hessian4ToHessian3(obj).getCurrency());

        Assertions.assertEquals(obj.getMaximumFractionDigits(), hessian3ToHessian4(obj).getMaximumFractionDigits());
        Assertions.assertEquals(obj.getMaximumIntegerDigits(), hessian3ToHessian4(obj).getMaximumIntegerDigits());
        Assertions.assertEquals(obj.getMinimumFractionDigits(), hessian3ToHessian4(obj).getMinimumFractionDigits());
        Assertions.assertEquals(obj.getMinimumIntegerDigits(), hessian3ToHessian4(obj).getMinimumIntegerDigits());
        Assertions.assertEquals(obj.getRoundingMode(), hessian3ToHessian4(obj).getRoundingMode());
        // TODO Support currency
//        Assertions.assertEquals(obj.getCurrency(), hessian3ToHessian4(obj).getCurrency());
    }

    @Test
    void testCollection() throws IOException {
        NumberFormat originalNumberFormat = NumberFormat.getInstance(Locale.US);

        List<NumberFormat> list = new ArrayList<>();
        list.add(originalNumberFormat);
        list.add(originalNumberFormat);

        List<NumberFormat> result = baseHessian2Serialize(list);

        Assertions.assertEquals(list.size(), result.size());
        Assertions.assertSame(result.get(0), result.get(1));
        Assertions.assertEquals(originalNumberFormat.getMaximumFractionDigits(), result.get(0).getMaximumFractionDigits());
        Assertions.assertEquals(originalNumberFormat.getMaximumIntegerDigits(), result.get(0).getMaximumIntegerDigits());
        Assertions.assertEquals(originalNumberFormat.getMinimumFractionDigits(), result.get(0).getMinimumFractionDigits());
        Assertions.assertEquals(originalNumberFormat.getMinimumIntegerDigits(), result.get(0).getMinimumIntegerDigits());
        Assertions.assertEquals(originalNumberFormat.getRoundingMode(), result.get(0).getRoundingMode());
    }
}
