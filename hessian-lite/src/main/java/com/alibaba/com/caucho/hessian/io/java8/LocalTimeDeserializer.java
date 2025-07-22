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
import java.time.LocalTime;

/**
 * @author wuwen
 */
public class LocalTimeDeserializer<T> extends AbstractDeserializer {

    @Override
    public Object readObject(AbstractHessianInput in,
                             Object[] fields)
            throws IOException {
        LocalTime localTime = decodeTime(in.readLong());
        in.addRef(localTime);
        return localTime;
    }

    private static LocalTime decodeTime(long encoded) {
        int hour = (int) (encoded >>> 47);
        int minute = (int) ((encoded >>> 41) & 0b111111);
        int second = (int) ((encoded >>> 35) & 0b111111);
        // 0x3FFFFFFF is used to mask the last 30 bits for nanoseconds
        int nano = (int) (encoded & 0x3FFFFFFF);
        return LocalTime.of(hour, minute, second, nano);
    }
    
}
