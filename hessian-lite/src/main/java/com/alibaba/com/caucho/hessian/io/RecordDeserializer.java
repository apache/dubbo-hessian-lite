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
            boolean readedIndex[] = new boolean[_components.length];
            for (String fieldName : fieldNames) {
                RecordUtil.RecordComponent component = _componentMap.get(fieldName);
                if (component == null) {
                    // ignore this field
                    in.readObject();
                    continue;
                }
                Object target;
                target = in.readObject(component.type());
                args[component.index()] = target;
                readedIndex[component.index()] = true;
            }
            for (int i = 0; i < readedIndex.length; i++) {
                if (!readedIndex[i]) {
                    args[i] = getParamArg(_components[i].type());
                }
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

    protected static Object getParamArg(Class<?> cl) {
        if (!cl.isPrimitive())
            return null;
        else if (boolean.class.equals(cl))
            return Boolean.FALSE;
        else if (byte.class.equals(cl))
            return Byte.valueOf((byte) 0);
        else if (short.class.equals(cl))
            return Short.valueOf((short) 0);
        else if (char.class.equals(cl))
            return Character.valueOf((char) 0);
        else if (int.class.equals(cl))
            return Integer.valueOf(0);
        else if (long.class.equals(cl))
            return Long.valueOf(0);
        else if (float.class.equals(cl))
            return Float.valueOf(0);
        else if (double.class.equals(cl))
            return Double.valueOf(0);
        else
            throw new UnsupportedOperationException();
    }

}
