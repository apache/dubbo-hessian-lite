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


import com.alibaba.com.caucho.hessian.io.AbstractDeserializer;
import com.alibaba.com.caucho.hessian.io.AbstractHessianInput;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author wuwen
 */
public class LocalDateTimeDeserializer extends AbstractDeserializer {

    @Override
    public Object readObject(AbstractHessianInput in,
                             Object[] fieldNames)
            throws IOException {

        LocalDate localDate = null;
        LocalTime localTime = null;
        for (Object fieldName : fieldNames) {
            if ("date".equals(fieldName)) {
                localDate = LocalDate.ofEpochDay(in.readLong());
            } else if ("time".equals(fieldName)) {
                localTime = LocalTime.ofNanoOfDay(in.readLong());
            } else {
                in.readObject();
            }
        }

        if (localDate == null || localTime == null) {
            throw new IOException("Missing required fields for LocalDateTime deserialization: "
                    + "date=" + localDate + ", time=" + localTime);
        }
        
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        in.addRef(localDateTime);
        return localDateTime;
    }
    
}
