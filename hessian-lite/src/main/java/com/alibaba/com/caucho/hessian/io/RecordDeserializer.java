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
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serializing an object for known object types.
 */
public class RecordDeserializer extends AbstractDeserializer {
    private RecordUtil.RecordComponent[] _components;
    private Map<String, RecordUtil.RecordComponent> _componentMap;
    private Constructor _constructor;
    private Class _cl;

    public RecordDeserializer(Class cl, FieldDeserializer2Factory fieldFactory) {
        _cl = cl;
        _components = RecordUtil.getRecordComponents(cl);
        _constructor = RecordUtil.getCanonicalConstructor(cl);
        _componentMap = new ConcurrentHashMap<>();
        for (RecordUtil.RecordComponent component : _components) {
            _componentMap.put(component.name(), component);
        }
    }

    public Class<?> getType() {
        return _cl;
    }

    @Override
    public Object readObject(AbstractHessianInput in,
                             Object[] fields)
            throws IOException {
        try {
            return readObject(in, (String[]) fields);
        } catch (IOException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IOExceptionWrapper(_cl.getName() + ":" + e, e);
        }
    }

    @Override
    public Object readObject(AbstractHessianInput in,
                             String[] fieldNames)
            throws IOException {
        try {
            Object[] args = new Object[_components.length];

            for (String fieldName : fieldNames) {
                RecordUtil.RecordComponent component = _componentMap.get(fieldName);
                Object target;
                target = in.readObject(component.type());
                if (component.type() == float.class || component.type() == Float.class) {
                    target = (float) ((double) target);
                }
                args[component.index()] = target;
            }
            Object obj = _constructor.newInstance(args);
            in.addRef(obj);

            return obj;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOExceptionWrapper(_cl.getName() + ":" + e, e);
        }
    }

}
