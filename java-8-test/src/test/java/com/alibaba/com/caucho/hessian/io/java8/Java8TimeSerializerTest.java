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

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import com.alibaba.com.caucho.hessian.io.SerializerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
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
import java.util.Calendar;

/**
 * Test Java8TimeSerializer class
 */
public class Java8TimeSerializerTest {

    private static SerializerFactory factory;
    private static ByteArrayOutputStream os;

    @BeforeAll
    public static void setUp() {
        String javaVersion = System.getProperty("java.specification.version");
        factory = new SerializerFactory(Thread.currentThread().getContextClassLoader());
        os = new ByteArrayOutputStream();
    }

    @Test
    public void testNull() throws IOException {
        testJava8Time(null);
    }

    @Test
    public void testInstant() throws Exception {
        testJava8Time(Instant.now());
    }

    @Test
    public void testDuration() throws Exception {
        testJava8Time(Duration.ofDays(2));
    }

    @Test
    public void testLocalDate() throws Exception {
        testJava8Time(LocalDate.now());
    }

    @Test
    public void testLocalDateTime() throws Exception {
        testJava8Time(LocalDateTime.now());
    }

    @Test
    public void testLocalTime() throws Exception {
        testJava8Time(LocalTime.now());
    }

    @Test
    public void testYear() throws Exception {
        testJava8Time(Year.now());
    }

    @Test
    public void testYearMonth() throws Exception {
        testJava8Time(YearMonth.now());
    }

    @Test
    public void testMonthDay() throws Exception {
        testJava8Time(MonthDay.now());
    }

    @Test
    public void testPeriod() throws Exception {
        testJava8Time(Period.ofDays(3));
    }

    @Test
    public void testOffsetTime() throws Exception {
        testJava8Time(OffsetTime.now());
    }

    @Test
    public void testZoneOffset() throws Exception {
        testJava8Time(ZoneOffset.ofHours( 8));
    }

    @Test
    public void testOffsetDateTime() throws Throwable {
        testJava8Time(OffsetDateTime.now());
    }

    @Test
    public void testZonedDateTime() throws Exception {
        testJava8Time(ZonedDateTime.now());
    }

    @Test
    public void testZoneId() throws Exception {
        testJava8Time(ZoneId.of( "America/New_York"));
    }

    @Test
    public void testCalendar() throws IOException {
        Calendar calendar = Calendar.getInstance();
        testJava8Time(calendar);
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

    private void testJava8Time(Object expected) throws IOException {
        os.reset();

        Hessian2Output output = new Hessian2Output(os);
        output.setSerializerFactory(factory);
        output.writeObject(expected);
        output.flush();

        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        Hessian2Input input = new Hessian2Input(is);
        input.setSerializerFactory(factory);
        Object actual = input.readObject();

        Assertions.assertEquals(expected, actual);
    }
}
