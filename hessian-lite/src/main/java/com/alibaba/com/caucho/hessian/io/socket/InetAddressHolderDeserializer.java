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
import java.net.InetAddress;

public class InetAddressHolderDeserializer extends AbstractDeserializer {
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
            throw new IOExceptionWrapper("java.net.InetAddress$InetAddressHolder:" + e, e);
        }
    }

    @Override
    public Object readObject(AbstractHessianInput in,
                             String[] fieldNames)
            throws IOException {
        try {
            String hostName = null;
            int address = 0;
            for (String fieldName : fieldNames) {
                if ("hostName".equals(fieldName)) {
                    hostName = in.readString();
                } else if ("address".equals(fieldName)) {
                    address = in.readInt();
                } else {
                    in.readObject();
                }
            }

            byte[] addr = new byte[4];

            addr[0] = (byte) ((address >>> 24) & 0xFF);
            addr[1] = (byte) ((address >>> 16) & 0xFF);
            addr[2] = (byte) ((address >>> 8) & 0xFF);
            addr[3] = (byte) (address & 0xFF);

            InetAddress obj = InetAddress.getByAddress(hostName, addr);

            in.addRef(obj);

            return obj;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOExceptionWrapper("java.net.InetAddress$InetAddressHolder:" + e, e);
        }
    }
}
