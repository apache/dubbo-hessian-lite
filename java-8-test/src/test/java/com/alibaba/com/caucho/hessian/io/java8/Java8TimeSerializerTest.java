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

import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Test Java8TimeSerializer class
 */
public class Java8TimeSerializerTest extends SerializeTestBase {

    @Test
    public void testNull() throws IOException {
        testJava8Time(null);
    }

    @Test
    public void testInstant() throws Exception {
        List<Object> list = new ArrayList<>();
        Instant instant = Instant.now();
        list.add(instant);
        list.add(instant);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(instant);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testDuration() throws Exception {
        List<Object> list = new ArrayList<>();
        Duration duration = Duration.ofDays(2);
        list.add(duration);
        list.add(duration);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(duration);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testLocalDate() throws Exception {
        List<Object> list = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        list.add(localDate);
        list.add(localDate);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(localDate);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testLocalDateTime() throws Exception {
        List<Object> list = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        list.add(localDateTime);
        list.add(localDateTime);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(localDateTime);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testLocalTime() throws Exception {
        List<Object> list = new ArrayList<>();
        LocalTime localTime = LocalTime.now();
        list.add(localTime);
        list.add(localTime);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(localTime);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testYear() throws Exception {
        List<Object> list = new ArrayList<>();
        Year year = Year.now();
        list.add(year);
        list.add(year);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(year);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testYearMonth() throws Exception {
        List<Object> list = new ArrayList<>();
        YearMonth yearMonth = YearMonth.now();
        list.add(yearMonth);
        list.add(yearMonth);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(yearMonth);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testMonthDay() throws Exception {
        List<Object> list = new ArrayList<>();
        MonthDay monthDay = MonthDay.now();
        list.add(monthDay);
        list.add(monthDay);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(monthDay);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testPeriod() throws Exception {
        List<Object> list = new ArrayList<>();
        Period period = Period.ofDays(3);
        list.add(period);
        list.add(period);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(period);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testOffsetTime() throws Exception {
        List<Object> list = new ArrayList<>();
        OffsetTime offsetTime = OffsetTime.now();
        list.add(offsetTime);
        list.add(offsetTime);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(offsetTime);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testZoneOffset() throws Exception {
        List<Object> list = new ArrayList<>();
        ZoneOffset zoneOffset = ZoneOffset.ofHours(8);
        list.add(zoneOffset);
        list.add(zoneOffset);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(zoneOffset);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testOffsetDateTime() throws Throwable {
        List<Object> list = new ArrayList<>();
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        list.add(offsetDateTime);
        list.add(offsetDateTime);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(offsetDateTime);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testZonedDateTime() throws Exception {
        List<Object> list = new ArrayList<>();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        list.add(zonedDateTime);
        list.add(zonedDateTime);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(zonedDateTime);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testZoneId() throws Exception {
        List<Object> list = new ArrayList<>();
        ZoneId zoneId = ZoneId.of("America/New_York");
        list.add(zoneId);
        list.add(zoneId);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(zoneId);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    public void testCalendar() throws IOException {
        List<Object> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        list.add(calendar);
        list.add(calendar);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(calendar);
        list.add(o);
        testJava8Time(list);
    }

    @Test
    void testChronology() throws IOException {
        testJava8Time(Chronology.of("islamic"));
        testJava8Time(Chronology.of("ThaiBuddhist"));
        testJava8Time(Chronology.of("ISO"));
        testJava8Time(Chronology.of("Hijrah"));
        testJava8Time(Chronology.of("Japanese"));
        testJava8Time(Chronology.of("Hijrah-umalqura"));
        testJava8Time(Chronology.of("Minguo"));

        testJava8Time(JapaneseDate.now());
        testJava8Time(HijrahDate.now());
        testJava8Time(MinguoDate.now());
        testJava8Time(ThaiBuddhistDate.now());

        testJava8Time(ChronoPeriod.between(LocalDate.now(), LocalDate.now()));
        testJava8Time(ChronoPeriod.between(JapaneseDate.now(), JapaneseDate.now()));
        testJava8Time(ChronoPeriod.between(HijrahDate.now(), HijrahDate.now()));
        testJava8Time(ChronoPeriod.between(MinguoDate.now(), MinguoDate.now()));
        testJava8Time(ChronoPeriod.between(ThaiBuddhistDate.now(), ThaiBuddhistDate.now()));
    }


    @Test
    void testChronologyList() throws IOException {
        List<Object> list = new ArrayList<>();
        Chronology chronology = Chronology.of("islamic");
        list.add(chronology);
        list.add(chronology);
        TestInner o = new TestInner();
        list.add(o);
        list.add(o);
        list.add(chronology);
        list.add(o);
        testJava8Time(list);
    }

    protected void testJava8Time(Object expected) throws IOException {
        Object result = baseHessian2Serialize(expected);
        Assertions.assertEquals(expected, result);
        if (expected instanceof List && !((List) expected).isEmpty() && ((List) expected).get(0) instanceof Chronology) {
            return;
        }
        if (expected instanceof Chronology || expected instanceof ChronoPeriod || expected instanceof JapaneseDate
                || expected instanceof HijrahDate || expected instanceof MinguoDate || expected instanceof ThaiBuddhistDate) {
            return;
        }
        Assertions.assertEquals(expected, hessian3ToHessian3(expected));
        Assertions.assertEquals(expected, hessian4ToHessian3(expected));
        Assertions.assertEquals(expected, hessian3ToHessian4(expected));
    }
    
    /**
     * Helper class used in tests to verify reference handling during serialization.
     * Instances of this class are added multiple times to collections to ensure
     * that object references are correctly preserved or duplicated as expected
     * when serializing and deserializing with Hessian.
     */
    static class TestInner implements Serializable {
        String value;
        
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof TestInner) {
                return Objects.equals(((TestInner) o).value, value);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }
    }
}
