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

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

public class Inet4AddressTest extends SerializeTestBase {
    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testJdk8() throws Exception {
        Inet4Address inet4Address = (Inet4Address) Inet4Address.getByAddress("baidu.com", new byte[]{1, 2, 3, 4});
        Assertions.assertEquals(inet4Address, baseHessian2Serialize(inet4Address));
        Assertions.assertEquals(hessian3ToHessian3(inet4Address), hessian3ToHessian4(inet4Address));
        Assertions.assertEquals(hessian3ToHessian3(inet4Address), hessian4ToHessian3(inet4Address));

        InetAddress inetAddress = Inet6Address.getByAddress("baidu.com", new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
        Assertions.assertEquals(inetAddress, baseHessian2Serialize(inetAddress));
//        Assertions.assertEquals(hessian3ToHessian3(inetAddress), hessian3ToHessian4(inetAddress));
        Assertions.assertEquals(hessian3ToHessian3(inetAddress), hessian4ToHessian3(inetAddress));
    }

    @Test
    @EnabledForJreRange(min = JRE.JAVA_17)
    void testJdk17() throws Exception {
        Inet4Address inet4Address = (Inet4Address) Inet4Address.getByAddress("baidu.com", new byte[]{1, 2, 3, 4});
        Assertions.assertEquals(inet4Address, baseHessian2Serialize(inet4Address));

        InetAddress inetAddress = Inet6Address.getByAddress("baidu.com", new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
        Assertions.assertEquals(inetAddress, baseHessian2Serialize(inetAddress));
    }
}
