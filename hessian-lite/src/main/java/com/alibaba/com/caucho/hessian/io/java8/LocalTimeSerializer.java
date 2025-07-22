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


import com.alibaba.com.caucho.hessian.io.AbstractHessianOutput;
import com.alibaba.com.caucho.hessian.io.AbstractSerializer;

import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeSerializer<T> extends AbstractSerializer {

    private final boolean useBitEncoding = Boolean.getBoolean("com.caucho.hessian.io.java.time.serializer.useBitEncoding");
    
    @Override
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (obj == null) {
            out.writeNull();
            return;
        }

        if (!useBitEncoding) {
            out.writeObject(new LocalTimeHandle(obj));
        } else {
            if (out.addRef(obj)) {
                return;
            }

            Class<?> cl = obj.getClass();

            int ref = out.writeObjectBegin(cl.getName());

            LocalTime localTime = (LocalTime) obj;
            
            if (ref < -1) {
                out.writeString("value");
                out.writeLong(encodeTime(localTime));
                out.writeMapEnd();
            } else {
                if (ref == -1) {
                    out.writeInt(1);
                    out.writeString("value");
                    out.writeObjectBegin(cl.getName());
                }
                out.writeLong(encodeTime(localTime));
            }
        }
    }

    private static long encodeTime(LocalTime time) {
        return ((long) time.getHour() << 47) |
                ((long) time.getMinute() << 41) |
                ((long) time.getSecond() << 35) |
                time.getNano();
    }
}
