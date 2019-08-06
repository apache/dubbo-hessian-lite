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

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Deserializing an enum valued object
 */
public class EnumDeserializer extends AbstractDeserializer {
    private Class _enumType;
    private Method _valueOf;

    private HashMap<String, Method> _methodMap;

    public EnumDeserializer(Class cl) {
        // hessian/33b[34], hessian/3bb[78]
        if (cl.isEnum())
            _enumType = cl;
        else if (cl.getSuperclass().isEnum())
            _enumType = cl.getSuperclass();
        else
            throw new RuntimeException("Class " + cl.getName() + " is not an enum");

        try {
            _valueOf = _enumType.getMethod("valueOf",
                    new Class[]{Class.class, String.class});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        _methodMap = getMethodMap(cl);

    }

    public Class getType() {
        return _enumType;
    }

    public Object readMap(AbstractHessianInput in)
            throws IOException {
        String name = null;

        while (!in.isEnd()) {
            String key = in.readString();

            if (key.equals("name"))
                name = in.readString();
            else
                in.readObject();
        }

        in.readMapEnd();

        Object obj = create(name);

        in.addRef(obj);

        return obj;
    }

    public Object readObject(AbstractHessianInput in, String[] fieldNames)
            throws IOException {
        String value = null;

        for (int i = 0; i < fieldNames.length; i++) {
            if ("enum".equals(fieldNames[i]))
                value = in.readString();
            else
                in.readObject();
        }

        JSONObject jo = JSONObject.parseObject(value);
        Object obj = create(jo.getString("_"));
        wlDecode(obj, jo, _methodMap);

        in.addRef(obj);

        return obj;
    }

    private void wlDecode(Object obj, JSONObject jo, HashMap<String, Method> methodMap) {
        for (Map.Entry<String, Method> me : methodMap.entrySet()) {
            if (!jo.containsKey(me.getKey())) continue;
            Class c = me.getValue().getParameterTypes()[0];

            if (Enum.class.isAssignableFrom(c)) {
                try {
                    JSONObject _jo = jo.getJSONObject(me.getKey());

                    Method method = c.getMethod("valueOf", new Class[]{Class.class, String.class});

                    Object _obj = method.invoke(null, c, _jo.getString("_"));

                    HashMap<String, Method> _methodMap = getMethodMap(c);

                    wlDecode(_obj, _jo, _methodMap);

                    me.getValue().invoke(obj, _obj);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    me.getValue().invoke(obj, new Object[]{jo.get(me.getKey())});
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object create(String name)
            throws IOException {
        if (name == null)
            throw new IOException(_enumType.getName() + " expects name.");

        try {
            return _valueOf.invoke(null, _enumType, name);
        } catch (Exception e) {
            throw new IOExceptionWrapper(e);
        }
    }

    /**
     * Creates a map of the classes fields.
     */
    protected HashMap<String, Method> getMethodMap(Class cl) {
        HashMap<String, Method> methodMap = new HashMap<String, Method>();

        for (; cl != null; cl = cl.getSuperclass()) {
            Method[] methods = cl.getDeclaredMethods();

            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];

                if (Modifier.isStatic(method.getModifiers()))
                    continue;

                String name = method.getName();

                if (!name.startsWith("set"))
                    continue;

                Class[] paramTypes = method.getParameterTypes();
                if (paramTypes.length != 1)
                    continue;

                //if (!method.getReturnType().equals(void.class))
                //    continue;

                if (findGetter(methods, name, paramTypes[0]) == null)
                    continue;

                // XXX: could parameterize the handler to only deal with public
                try {
                    method.setAccessible(true);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                name = name.substring(3);

                int j = 0;
                for (; j < name.length() && Character.isUpperCase(name.charAt(j)); j++) {
                }

                if (j == 1)
                    name = name.substring(0, j).toLowerCase() + name.substring(j);
                else if (j > 1)
                    name = name.substring(0, j - 1).toLowerCase() + name.substring(j - 1);


                methodMap.put(name, method);
            }
        }

        return methodMap;
    }

    /**
     * Finds any matching setter.
     */
    private Method findGetter(Method[] methods, String setterName, Class arg) {
        String getterName = "get" + setterName.substring(3);

        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];

            if (!method.getName().equals(getterName))
                continue;

            if (!method.getReturnType().equals(arg))
                continue;

            Class[] params = method.getParameterTypes();

            if (params.length == 0)
                return method;
        }

        return null;
    }
}
