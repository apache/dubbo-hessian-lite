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
import java.net.InetSocketAddress;

public class InetSocketAddressSerializer extends AbstractSerializer {

    @Override
    public void writeObject(Object obj, AbstractHessianOutput out)
            throws IOException {

        String replacedClName = "java.net.InetSocketAddress";

        int ref = out.writeObjectBegin(replacedClName);

        if (ref < -1) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress)obj;
            out.writeString("hostName");
            out.writeObject(inetSocketAddress.getHostName());
            out.writeString("addr");
            out.writeObject(inetSocketAddress.getAddress());

            out.writeString("port");
            out.writeInt(inetSocketAddress.getPort());

            out.writeMapEnd();
        } else {
            if (ref == -1) {
                out.writeClassFieldLength(3);

                out.writeString("hostName");
                out.writeString("addr");
                out.writeString("port");

                out.writeObjectBegin(replacedClName);
            }

            InetSocketAddress inetSocketAddress = (InetSocketAddress)obj;
            out.writeObject(inetSocketAddress.getHostName());
            out.writeObject(inetSocketAddress.getAddress());
            out.writeInt(inetSocketAddress.getPort());
        }
    }

}
