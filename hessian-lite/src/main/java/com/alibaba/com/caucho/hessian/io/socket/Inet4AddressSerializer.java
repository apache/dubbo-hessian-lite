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
import java.net.InetAddress;

public class Inet4AddressSerializer extends AbstractSerializer {

    @Override
    public void writeObject(Object obj, AbstractHessianOutput out)
            throws IOException {
        if (out.addRef(obj)) {
            return;
        }

        String replacedClName = "java.net.InetAddress";

        int ref = out.writeObjectBegin(replacedClName);

        if (ref < -1) {
            out.writeString("holder");
            writeInetAddressHolderObject(obj, out);

            out.writeMapEnd();
        } else {
            if (ref == -1) {
                out.writeClassFieldLength(1);
                out.writeString("holder");

                out.writeObjectBegin(replacedClName);
            }

            writeInetAddressHolderObject(obj, out);
        }
    }

    private void writeInetAddressHolderObject(Object obj, AbstractHessianOutput out)
            throws IOException {

        String replacedClName = "java.net.InetAddress$InetAddressHolder";

        int ref = out.writeObjectBegin(replacedClName);

        if (ref < -1) {
            out.writeString("hostName");
            out.writeObject(((InetAddress)obj).getHostName());
            out.writeString("address");

            byte[] addr = ((InetAddress) obj).getAddress();
            int address  = addr[3] & 0xFF;
            address |= ((addr[2] << 8) & 0xFF00);
            address |= ((addr[1] << 16) & 0xFF0000);
            address |= ((addr[0] << 24) & 0xFF000000);
            out.writeObject(address);

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
            byte[] addr = ((InetAddress) obj).getAddress();
            int address  = addr[3] & 0xFF;
            address |= ((addr[2] << 8) & 0xFF00);
            address |= ((addr[1] << 16) & 0xFF0000);
            address |= ((addr[0] << 24) & 0xFF000000);
            out.writeObject(address);
            out.writeInt(1);
        }
    }
}
