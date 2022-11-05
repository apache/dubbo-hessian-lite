/*
 * Copyright (c) 2001-2004 Caucho Technology, Inc.  All rights reserved.
 *
 * The Apache Software License, Version 1.1
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Caucho Technology (http://www.caucho.com/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Hessian", "Resin", and "Caucho" must not be used to
 *    endorse or promote products derived from this software without prior
 *    written permission. For written permission, please contact
 *    info@caucho.com.
 *
 * 5. Products derived from this software may not be called "Resin"
 *    nor may "Resin" appear in their names without prior written
 *    permission of Caucho Technology.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL CAUCHO TECHNOLOGY OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Scott Ferguson
 */

package com.alibaba.com.caucho.hessian.io;

import com.alibaba.com.caucho.hessian.util.TypeUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Deserializing a JDK 1.2 Map.
 */
public class MapDeserializer extends AbstractMapDeserializer {
    private Class _type;
    private Constructor _ctor;

    public MapDeserializer(Class type) {
        if (type == null)
            type = HashMap.class;

        _type = type;

        Constructor[] ctors = type.getConstructors();
        for (int i = 0; i < ctors.length; i++) {
            if (ctors[i].getParameterTypes().length == 0)
                _ctor = ctors[i];
        }

        if (_ctor == null) {
            try {
                _ctor = HashMap.class.getConstructor(new Class[0]);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public Class getType() {
        if (_type != null)
            return _type;
        else
            return HashMap.class;
    }

    @Override
    public Object readMap(AbstractHessianInput in)
            throws IOException {
        return readMap(in, null, null);
    }

    /**
     *  support generic type of map, fix the type of byte/short serialization <p>
     *  eg: Map<String, Short> serialize & deserialize
     */
    @Override
    public Object readMap(AbstractHessianInput in, Type actualKeyType, Type actualValueType) throws IOException {
        Map map;

        if (_type == null)
            map = new HashMap();
        else if (_type.equals(Map.class))
            map = new HashMap();
        else if (_type.equals(SortedMap.class))
            map = new TreeMap();
        else {
            try {
                map = (Map) _ctor.newInstance();
            } catch (Exception e) {
                throw new IOExceptionWrapper(e);
            }
        }

        in.addRef(map);

        doReadMap(in, map, actualKeyType, actualValueType);

        in.readEnd();

        return map;
    }

    protected void doReadMap(AbstractHessianInput in, Map map, Type actualKeyType, Type actualValueType) throws IOException {
        Deserializer keyDeserializer = null, valueDeserializer = null;

        SerializerFactory factory = findSerializerFactory(in);
        if (TypeUtil.isByteOrShortClass(actualKeyType)) {
            keyDeserializer = factory.getDeserializer(((Class<?>) actualKeyType).getName());
        }
        if (TypeUtil.isByteOrShortClass(actualValueType)) {
            valueDeserializer = factory.getDeserializer(((Class<?>) actualValueType).getName());
        }

        while (!in.isEnd()) {
            map.put(keyDeserializer != null ? keyDeserializer.readObject(in) : in.readObject(actualKeyType),
                    valueDeserializer != null ? valueDeserializer.readObject(in) : in.readObject(actualValueType));
        }
    }

    @Override
    public Object readObject(AbstractHessianInput in,
                             String[] fieldNames)
            throws IOException {
        Map map = createMap();

        int ref = in.addRef(map);

        for (int i = 0; i < fieldNames.length; i++) {
            String name = fieldNames[i];

            map.put(name, in.readObject());
        }

        return map;
    }

    private Map createMap()
            throws IOException {

        if (_type == null)
            return new HashMap();
        else if (_type.equals(Map.class))
            return new HashMap();
        else if (_type.equals(SortedMap.class))
            return new TreeMap();
        else {
            try {
                return (Map) _ctor.newInstance();
            } catch (Exception e) {
                throw new IOExceptionWrapper(e);
            }
        }
    }
}
