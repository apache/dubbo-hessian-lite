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
import java.time.LocalDate;

public class LocalDateSerializer<T> extends AbstractSerializer {

    private final boolean useBitEncoding = Boolean.getBoolean("com.caucho.hessian.io.java.time.serializer.useBitEncoding");
    
    @Override
    public void writeObject(Object obj, AbstractHessianOutput out) throws IOException {
        if (obj == null) {
            out.writeNull();
            return;
        }

        if (!useBitEncoding) {
            out.writeObject(new LocalDateHandle(obj));
        } else {
            
            if (out.addRef(obj)) {
                return;
            }

            Class<?> cl = obj.getClass();
            
            int ref = out.writeObjectBegin(cl.getName());

            LocalDate localDate = (LocalDate) obj;

            if (ref < -1) {
                out.writeString("value");
                out.writeInt(encodeDate(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth()));
                out.writeMapEnd();
            } else {
                if (ref == -1) {
                    out.writeInt(1);
                    out.writeString("value");
                    out.writeObjectBegin(cl.getName());
                }
                out.writeInt(encodeDate(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth()));
            }
        }
    }

    private static int encodeDate(int year, int month, int day) {
        return (year << 9) | (month << 5) | day;
    }

}
