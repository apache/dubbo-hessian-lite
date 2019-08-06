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
 * 4. The names "Burlap", "Resin", and "Caucho" must not be used to
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
 * Serializing an object for known object types.
 */
public class EnumSerializer extends AbstractSerializer {
    private Method _name;

    private HashMap<String, Method> _methodMap;

    public EnumSerializer(Class cl) {
        // hessian/32b[12], hessian/3ab[23]
        if (!cl.isEnum() && cl.getSuperclass().isEnum())
            cl = cl.getSuperclass();

        try {
            _name = cl.getMethod("name", new Class[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        _methodMap = getMethodMap(cl);
    }

    public void writeObject(Object obj, AbstractHessianOutput out)
            throws IOException {
        if (out.addRef(obj))
            return;

        Class cl = obj.getClass();

        if (!cl.isEnum() && cl.getSuperclass().isEnum())
            cl = cl.getSuperclass();

        String name = null;
        try {
            name = (String) _name.invoke(obj, (Object[]) null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JSONObject jo = new JSONObject();
        wlDecode(obj, jo, _methodMap);
        jo.put("_", name);
        String value = jo.toJSONString();

        // System.out.println("EnumSerializer writeObject " + value);

        int ref = out.writeObjectBegin(cl.getName());

        if (ref < -1) {
            out.writeString("enum");
            out.writeString(value);
            out.writeMapEnd();
        } else {
            if (ref == -1) {
                out.writeClassFieldLength(1);
                out.writeString("enum");
                out.writeObjectBegin(cl.getName());
            }

            out.writeString(value);

        }
    }

    private void wlDecode(Object obj, JSONObject jo, HashMap<String, Method> methodMap) {
        for (Map.Entry<String, Method> me : methodMap.entrySet()) {
            Class c = me.getValue().getReturnType();
            if (Enum.class.isAssignableFrom(c)) {
                try {
                    Object _obj = me.getValue().invoke(obj);
                    if (_obj == null) continue;

                    JSONObject _jo = new JSONObject();

                    Method _method = c.getMethod("name", new Class[0]);
                    String _name = (String) _method.invoke(_obj, (Object[]) null);
                    _jo.put("_", _name);

                    HashMap<String, Method> _methodMap = getMethodMap(c);

                    wlDecode(_obj, _jo, _methodMap);

                    jo.put(me.getKey(), _jo);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    jo.put(me.getKey(), me.getValue().invoke(obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
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

                if (method.getParameterTypes().length != 0)
                    continue;

                String name = method.getName();

                if (!name.startsWith("get"))
                    continue;

                Class type = method.getReturnType();

                if (type.equals(void.class))
                    continue;

                if (findGetter(methods, name, type) == null)
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

        methodMap.remove("declaringClass");
        methodMap.remove("class");

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
