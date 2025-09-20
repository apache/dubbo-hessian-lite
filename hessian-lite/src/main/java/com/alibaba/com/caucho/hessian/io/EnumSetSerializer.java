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

import com.alibaba.com.caucho.hessian.HessianException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.logging.Level;

public class EnumSetSerializer extends AbstractSerializer {
    private static final boolean _isEnabled;

    @SuppressWarnings("restriction")
    private static Unsafe _unsafe;

    private static long _elementTypeOffset;

    static {
        boolean isEnabled = false;

        try {
            Class<?> unsafe = Class.forName("sun.misc.Unsafe");
            Field theUnsafe = null;
            for (Field field : unsafe.getDeclaredFields()) {
                if (field.getName().equals("theUnsafe"))
                    theUnsafe = field;
            }

            if (theUnsafe != null) {
                theUnsafe.setAccessible(true);
                _unsafe = (Unsafe) theUnsafe.get(null);
            }

            isEnabled = _unsafe != null;

            String unsafeProp = System.getProperty("com.caucho.hessian.unsafe");

            if ("false".equals(unsafeProp))
                isEnabled = false;

            if (isEnabled) {
                Field elementType = EnumSet.class.getDeclaredField("elementType");
                _elementTypeOffset = _unsafe.objectFieldOffset(elementType);
            }
        } catch (Throwable e) {
            log.log(Level.FINER, e.toString(), e);
        }

        _isEnabled = isEnabled;
    }

    @Override
    public Object writeReplace(Object obj) {
        EnumSet enumSet = (EnumSet) obj;
        Class type = getElementClass(enumSet);
        Object[] objects = enumSet.toArray();
        return new EnumSetHandler(type, objects);
    }

    private Class<?> getElementClass(EnumSet enumSet) {
        if (!_isEnabled) {
            if (enumSet.isEmpty()) {
                throw new HessianException(new HessianFieldException("Unable to serialize empty EnumSet without unsafe access"));
            }
            return enumSet.iterator().next().getClass();
        }
        try {
            return (Class<?>) _unsafe.getObject(enumSet, _elementTypeOffset);
        } catch (Throwable e) {
            throw new HessianException(e);
        }
    }
}
