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

import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class InetSocketAddressTest extends SerializeTestBase {
    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testJdk8() throws Exception {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
        Assertions.assertEquals(inetSocketAddress, baseHessian2Serialize(inetSocketAddress));
        Assertions.assertEquals(inetSocketAddress.getAddress(), baseHessian2Serialize(inetSocketAddress).getAddress());
        Assertions.assertEquals(inetSocketAddress.getHostName(), baseHessian2Serialize(inetSocketAddress).getHostName());
        Assertions.assertEquals(inetSocketAddress.getHostString(), baseHessian2Serialize(inetSocketAddress).getHostString());

        Assertions.assertEquals(inetSocketAddress.getPort(), baseHessian2Serialize(inetSocketAddress).getPort());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress), hessian3ToHessian4(inetSocketAddress));
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getAddress(), hessian3ToHessian4(inetSocketAddress).getAddress());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getHostName(), hessian3ToHessian4(inetSocketAddress).getHostName());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getHostString(), hessian3ToHessian4(inetSocketAddress).getHostString());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getPort(), hessian3ToHessian4(inetSocketAddress).getPort());

        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress), hessian4ToHessian3(inetSocketAddress));
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress), hessian4ToHessian3(inetSocketAddress));
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getAddress(), hessian4ToHessian3(inetSocketAddress).getAddress());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getHostName(), hessian4ToHessian3(inetSocketAddress).getHostName());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getHostString(), hessian4ToHessian3(inetSocketAddress).getHostString());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getPort(), hessian4ToHessian3(inetSocketAddress).getPort());

        inetSocketAddress = new InetSocketAddress("unknown.host", 8080);
        Assertions.assertEquals(inetSocketAddress, baseHessian2Serialize(inetSocketAddress));
        Assertions.assertEquals(inetSocketAddress.getAddress(), baseHessian2Serialize(inetSocketAddress).getAddress());
        Assertions.assertEquals(inetSocketAddress.getHostName(), baseHessian2Serialize(inetSocketAddress).getHostName());
        Assertions.assertEquals(inetSocketAddress.getHostString(), baseHessian2Serialize(inetSocketAddress).getHostString());

        Assertions.assertEquals(inetSocketAddress.getPort(), baseHessian2Serialize(inetSocketAddress).getPort());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress), hessian3ToHessian4(inetSocketAddress));
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getAddress(), hessian3ToHessian4(inetSocketAddress).getAddress());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getHostName(), hessian3ToHessian4(inetSocketAddress).getHostName());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getHostString(), hessian3ToHessian4(inetSocketAddress).getHostString());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getPort(), hessian3ToHessian4(inetSocketAddress).getPort());

        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress), hessian4ToHessian3(inetSocketAddress));
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress), hessian4ToHessian3(inetSocketAddress));
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getAddress(), hessian4ToHessian3(inetSocketAddress).getAddress());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getHostName(), hessian4ToHessian3(inetSocketAddress).getHostName());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getHostString(), hessian4ToHessian3(inetSocketAddress).getHostString());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress).getPort(), hessian4ToHessian3(inetSocketAddress).getPort());

        InetAddress inet6Address = Inet6Address.getByAddress("baidu.com", new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
        inetSocketAddress = new InetSocketAddress(inet6Address, 8080);
        Assertions.assertEquals(inetSocketAddress, baseHessian2Serialize(inetSocketAddress));
        Assertions.assertEquals(inetSocketAddress.getAddress(), baseHessian2Serialize(inetSocketAddress).getAddress());
        Assertions.assertEquals(inetSocketAddress.getHostName(), baseHessian2Serialize(inetSocketAddress).getHostName());
        Assertions.assertEquals(inetSocketAddress.getHostString(), baseHessian2Serialize(inetSocketAddress).getHostString());
        Assertions.assertEquals(inetSocketAddress.getPort(), baseHessian2Serialize(inetSocketAddress).getPort());
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress), hessian3ToHessian4(inetSocketAddress));
        Assertions.assertEquals(hessian3ToHessian3(inetSocketAddress), hessian4ToHessian3(inetSocketAddress));
    }

    @Test
    @EnabledForJreRange(min = JRE.JAVA_17)
    void testJdk17() throws Exception {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
        Assertions.assertEquals(inetSocketAddress, baseHessian2Serialize(inetSocketAddress));
    }
    
    @Test
    void testCollectionRef() throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
        List<Object> list = new ArrayList<>();
        testCollection(list, inetSocketAddress);
    }
}
