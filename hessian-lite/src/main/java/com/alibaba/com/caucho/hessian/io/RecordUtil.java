/* Copyright (c) 2008-2023, Nathan Sweet
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of Esoteric Software nor the names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.alibaba.com.caucho.hessian.io;

import com.alibaba.com.caucho.hessian.HessianException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

public class RecordUtil {
    private static final Method IS_RECORD;
    private static final Method GET_RECORD_COMPONENTS;
    private static final Method GET_NAME;
    private static final Method GET_TYPE;

    private static final ClassValue<Constructor<?>> CONSTRUCTOR = new ClassValue<Constructor<?>>() {
        protected Constructor<?> computeValue(Class<?> clazz) {
            final RecordComponent[] components = recordComponents(clazz, Comparator.comparing(RecordComponent::index));
            return getCanonicalConstructor(clazz, components);
        }
    };
    private static final ClassValue<RecordComponent[]> RECORD_COMPONENTS = new ClassValue<RecordComponent[]>() {
        protected RecordComponent[] computeValue(Class<?> type) {
            return recordComponents(type, Comparator.comparing(RecordComponent::index));
        }
    };


    static {
        Method isRecord;
        Method getRecordComponents;
        Method getName;
        Method getType;

        try {
            Class<?> c = Class.forName("java.lang.reflect.RecordComponent");
            isRecord = Class.class.getDeclaredMethod("isRecord");
            getRecordComponents = Class.class.getMethod("getRecordComponents");
            getName = c.getMethod("getName");
            getType = c.getMethod("getType");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            isRecord = null;
            getRecordComponents = null;
            getName = null;
            getType = null;
        }

        IS_RECORD = isRecord;
        GET_RECORD_COMPONENTS = getRecordComponents;
        GET_NAME = getName;
        GET_TYPE = getType;
    }

    public static boolean isRecord(Class<?> type) {
        if (IS_RECORD == null) {
            return false;
        }
        try {
            return (boolean) IS_RECORD.invoke(type);
        } catch (Throwable t) {
            throw new HessianException("Could not determine type (" + type + ")");
        }
    }

    public static RecordComponent[] getRecordComponents(Class<?> type) {
        return RECORD_COMPONENTS.get(type);
    }

    private static <T> RecordComponent[] recordComponents (Class<T> type,
                                                           Comparator<RecordComponent> comparator) {
        try {
            Object[] rawComponents = (Object[])GET_RECORD_COMPONENTS.invoke(type);
            RecordComponent[] recordComponents = new RecordComponent[rawComponents.length];
            for (int i = 0; i < rawComponents.length; i++) {
                final Object comp = rawComponents[i];
                recordComponents[i] = new RecordComponent(
                        type,
                        (String)GET_NAME.invoke(comp),
                        (Class<?>)GET_TYPE.invoke(comp), i);
            }
            if (comparator != null) Arrays.sort(recordComponents, comparator);
            return recordComponents;
        } catch (Throwable t) {
            throw new HessianException("Could not retrieve record components (" + type.getName() + ")", t);
        }
    }


    private <T> T invokeCanonicalConstructor (Class<? extends T> recordType, Object[] args) {
        try {
            return (T) CONSTRUCTOR.get(recordType).newInstance(args);
        } catch (Throwable t) {
            throw new HessianException("Could not construct type (" + recordType.getName() + ")", t);
        }
    }

    public static Constructor<?> getCanonicalConstructor(Class recordType) {
        return CONSTRUCTOR.get(recordType);
    }

    private static <T> Constructor<T> getCanonicalConstructor(Class<T> recordType, RecordComponent[] recordComponents) {
        try {
            Class<?>[] paramTypes = Arrays.stream(recordComponents)
                    .map(RecordComponent::type)
                    .toArray(Class<?>[]::new);
            return getCanonicalConstructor(recordType, paramTypes);
        } catch (Throwable t) {
            throw new HessianException("Could not retrieve record canonical constructor (" + recordType.getName() + ")", t);
        }
    }

    private static <T> Constructor<T> getCanonicalConstructor(Class<T> recordType, Class<?>[] paramTypes)
            throws NoSuchMethodException {
        Constructor<T> canonicalConstructor;
        try {
            canonicalConstructor = recordType.getConstructor(paramTypes);
            if (!canonicalConstructor.isAccessible()) {
                canonicalConstructor.setAccessible(true);
            }
        } catch (Exception e) {
            canonicalConstructor = recordType.getDeclaredConstructor(paramTypes);
            canonicalConstructor.setAccessible(true);
        }
        return canonicalConstructor;
    }

    public static final class RecordComponent {
        private final Class<?> recordType;
        private final String name;
        private final Class<?> type;
        private final int index;
        private final Method getter;

        RecordComponent(Class<?> recordType, String name, Class<?> type, int index) {
            this.recordType = recordType;
            this.name = name;
            this.type = type;
            this.index = index;

            try {
                getter = recordType.getDeclaredMethod(name);
                if (!getter.isAccessible()) {
                    getter.setAccessible(true);
                }
            } catch (Exception t) {
                throw new HessianException("Could not retrieve record component getter (" + recordType.getName() + ")", t);
            }
        }

        public String name() {
            return name;
        }

        public  Class<?> type() {
            return type;
        }

        public  int index() {
            return index;
        }

        public  Object getValue(Object recordObject) {
            try {
                return getter.invoke(recordObject);
            } catch (Exception t) {
                throw new HessianException("Could not retrieve record component value (" + recordType.getName() + ")", t);
            }
        }
    }

}
