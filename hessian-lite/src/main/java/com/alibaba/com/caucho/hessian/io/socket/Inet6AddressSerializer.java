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

import com.alibaba.com.caucho.hessian.io.AbstractHessianOutput;
import com.alibaba.com.caucho.hessian.io.AbstractSerializer;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;

public class Inet6AddressSerializer extends AbstractSerializer {

    @Override
    public void writeObject(Object obj, AbstractHessianOutput out)
            throws IOException {

        String replacedClName = "java.net.Inet6Address";

        int ref = out.writeObjectBegin(replacedClName);

        if (ref < -1) {
            out.writeString("holder");
            writeInet4AddressHolderObject(obj, out);
            out.writeString("holder6");
            writeInet6AddressHolderObject(obj, out);

            out.writeMapEnd();
        } else {
            if (ref == -1) {
                out.writeClassFieldLength(2);
                out.writeString("holder");
                out.writeString("holder6");

                out.writeObjectBegin(replacedClName);
            }

            writeInet4AddressHolderObject(obj, out);
            writeInet6AddressHolderObject(obj, out);
        }
    }

    private static void writeInet6AddressHolderObject(Object obj, AbstractHessianOutput out)
            throws IOException {

        String replacedClName = "java.net.Inet6Address$Inet6AddressHolder";

        int ref = out.writeObjectBegin(replacedClName);

        if (ref < -1) {
            Inet6Address inet6Address = (Inet6Address) obj;
            out.writeString("ipaddress");
            out.writeObject((inet6Address.getAddress()));

            out.writeString("scope_id");
            out.writeObject(inet6Address.getScopeId());

            out.writeString("scope_id_set");
            out.writeObject(inet6Address.getScopeId() > 0);

            out.writeString("scope_ifname_set");
            out.writeObject(inet6Address.getScopedInterface() != null);

            out.writeString("scope_ifname");
            out.writeObject(inet6Address.getScopedInterface() == null ? null : inet6Address.getScopedInterface().getName());

            out.writeMapEnd();
        } else {
            if (ref == -1) {
                out.writeClassFieldLength(5);

                out.writeString("ipaddress");
                out.writeString("scope_id");
                out.writeString("scope_id_set");
                out.writeString("scope_ifname_set");
                out.writeString("scope_ifname");

                out.writeObjectBegin(replacedClName);
            }

            Inet6Address inet6Address = (Inet6Address) obj;
            out.writeObject((inet6Address.getAddress()));
            out.writeObject(inet6Address.getScopeId());
            out.writeObject(inet6Address.getScopeId() > 0);
            out.writeObject(inet6Address.getScopedInterface() != null);
            out.writeObject(inet6Address.getScopedInterface() == null ? null : inet6Address.getScopedInterface().getName());
        }
    }

    private void writeInet4AddressHolderObject(Object obj, AbstractHessianOutput out)
            throws IOException {

        String replacedClName = "java.net.InetAddress$InetAddressHolder";

        int ref = out.writeObjectBegin(replacedClName);

        if (ref < -1) {
            out.writeString("hostName");
            out.writeObject(((InetAddress)obj).getHostName());
            out.writeString("address");
            out.writeObject(0);

            out.writeString("family");
            out.writeInt(2);

            out.writeMapEnd();
        } else {
            if (ref == -1) {
                out.writeClassFieldLength(3);

                out.writeString("hostName");
                out.writeString("address");
                out.writeString("family");

                out.writeObjectBegin(replacedClName);
            }

            out.writeObject(((InetAddress)obj).getHostName());
            out.writeObject(0);
            out.writeInt(2);
        }
    }
}
