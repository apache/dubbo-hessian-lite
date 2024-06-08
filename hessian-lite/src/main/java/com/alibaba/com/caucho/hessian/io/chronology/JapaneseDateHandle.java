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
package com.alibaba.com.caucho.hessian.io.chronology;

import com.alibaba.com.caucho.hessian.io.HessianHandle;

import java.io.Serializable;
import java.time.chrono.AbstractChronology;
import java.time.chrono.Chronology;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.temporal.ChronoField;

public class JapaneseDateHandle implements HessianHandle, Serializable {

    private final int year;
    private final int month;
    private final int dayOfMonth;

    public JapaneseDateHandle(JapaneseDate o) {
        this.year = o.get(ChronoField.YEAR);
        this.month = o.get(ChronoField.MONTH_OF_YEAR);
        this.dayOfMonth = o.get(ChronoField.DAY_OF_MONTH);
    }

    private Object readResolve() {
        return JapaneseChronology.INSTANCE.date(year, month, dayOfMonth);
    }
}

