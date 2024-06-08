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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Serializing an object for known object types.
 */
public class RecordSerializer extends AbstractSerializer {
    private RecordUtil.RecordComponent[] _components;

    public RecordSerializer(Class cl) {
        _components = RecordUtil.getRecordComponents(cl);
    }

    @Override
    protected void writeObject10(Object obj, AbstractHessianOutput out) throws IOException {
        for (RecordUtil.RecordComponent component : _components) {
            out.writeString(component.name());
            out.writeObject(component.getValue(obj));
        }

        out.writeMapEnd();
    }

    @Override
    protected void writeDefinition20(Class<?> cl, AbstractHessianOutput out) throws IOException {
        out.writeClassFieldLength(_components.length);

        for (RecordUtil.RecordComponent component : _components) {
            out.writeString(component.name());
        }
    }

    @Override
    protected void writeInstance(Object obj, AbstractHessianOutput out) throws IOException {
        for (RecordUtil.RecordComponent component : _components) {
            out.writeObject(component.getValue(obj));
        }
    }
}
