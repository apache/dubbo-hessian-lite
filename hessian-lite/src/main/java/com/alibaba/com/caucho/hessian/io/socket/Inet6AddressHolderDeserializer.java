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

import com.alibaba.com.caucho.hessian.io.AbstractDeserializer;
import com.alibaba.com.caucho.hessian.io.AbstractHessianInput;
import com.alibaba.com.caucho.hessian.io.IOExceptionWrapper;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

public class Inet6AddressHolderDeserializer extends AbstractDeserializer {
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
            throw new IOExceptionWrapper("java.net.Inet6Address$Inet6AddressHolder:" + e, e);
        }
    }

    @Override
    public Object readObject(AbstractHessianInput in,
                             String[] fieldNames)
            throws IOException {
        try {
            byte[] ipaddress = new byte[16];
            int scope_id = 0;
            String ifname = null;
            for (String fieldName : fieldNames) {
                if ("ipaddress".equals(fieldName)) {
                    ipaddress = (byte[]) in.readObject();
                } else if ("scope_id".equals(fieldName)) {
                    scope_id = in.readInt();
                } else if ("scope_ifname".equals(fieldName)) {
                    ifname = in.readString();
                } else {
                    in.readObject();
                }
            }

            InetAddress obj = null;
            if (ifname != null) {
                NetworkInterface scopeIfname = NetworkInterface.getByName(ifname);
                if (scopeIfname != null) {
                    obj = Inet6Address.getByAddress("", ipaddress, scopeIfname);
                }
            } 
            
            if (obj == null) {
                obj = Inet6Address.getByAddress("", ipaddress, scope_id <= 0 ? -1 : scope_id);
            }

            in.addRef(obj);

            return obj;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOExceptionWrapper("java.net.Inet6Address$Inet6AddressHolder:" + e, e);
        }
    }


}
