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
package com.alibaba.com.caucho.hessian.io.socket;

import com.alibaba.com.caucho.hessian.HessianException;
import com.alibaba.com.caucho.hessian.io.AbstractHessianOutput;
import com.alibaba.com.caucho.hessian.io.AbstractSerializer;
import com.alibaba.com.caucho.hessian.io.HessianFieldException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.nio.file.Path;

public class UnixDomainSocketAddressSerializer extends AbstractSerializer {

    private static Method getHostAddressMethod = null;

    static {
        try {
            Class<?> cl = Class.forName("java.net.UnixDomainSocketAddress");
            getHostAddressMethod = cl.getMethod("getPath");
        } catch (Exception e) {
            // UnixDomainSocketAddress only available since Java 16
        }
    }

    @Override
    public void writeObject(Object obj, AbstractHessianOutput out)
            throws IOException {

        String replacedClName = "java.net.UnixDomainSocketAddress$Ser";

        int ref = out.writeObjectBegin(replacedClName);

        if (ref < -1) {
            out.writeString("pathname");
            writeUnixDomainSocketAddressPath(obj, out);

            out.writeMapEnd();
        } else {
            if (ref == -1) {
                out.writeClassFieldLength(1);
                out.writeString("pathname");

                out.writeObjectBegin(replacedClName);
            }

            writeUnixDomainSocketAddressPath(obj, out);
        }
    }

    private void writeUnixDomainSocketAddressPath(Object obj, AbstractHessianOutput out)
            throws IOException {
        if (getHostAddressMethod != null) {
            Path path;
            try {
                path = (Path) getHostAddressMethod.invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new HessianFieldException(e);
            }
            out.writeString(path.toString());
        } else {
            throw new HessianException("Unable to invoke UnixDomainSocketAddress.getPath() method.");
        }
    }
}
