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
import java.time.LocalDateTime;

public class LocalDateTimeSerializer<T> extends AbstractSerializer {

    @Override
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (SerializationConfig.isCompactMode()) {
            if (out.addRef(obj)) {
                return;
            }

            Class<?> cl = obj.getClass();

            int ref = out.writeObjectBegin(cl.getName());

            LocalDateTime localDateTime = (LocalDateTime) obj;

            if (ref < -1) {
                out.writeString("date");
                out.writeLong(localDateTime.toLocalDate().toEpochDay());
                out.writeString("time");
                out.writeLong(localDateTime.toLocalTime().toNanoOfDay());
                out.writeMapEnd();
            } else {
                if (ref == -1) {
                    out.writeInt(2);
                    out.writeString("date");
                    out.writeString("time");
                    out.writeObjectBegin(cl.getName());
                }
                out.writeLong(localDateTime.toLocalDate().toEpochDay());
                out.writeLong(localDateTime.toLocalTime().toNanoOfDay());
            }   
        } else {
            super.writeObject(obj, out);
        }
    }

    @Override
    protected Object writeReplace(Object obj) {
        return new LocalDateTimeHandle(obj);
    }

}
