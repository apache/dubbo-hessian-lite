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
package com.alibaba.com.caucho.hessian.io;

import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class URITest extends SerializeTestBase {
    @Test
    void testStr() throws IOException, URISyntaxException {
        URI originalURI = new URI("http://username:password@www.example.com:8080/path/to/resource?param1=value1&param2=value2#fragment");

        URI result = baseHessian2Serialize(originalURI);

        Assertions.assertEquals(originalURI.toString(), result.toString());
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testCompact() throws IOException, URISyntaxException {
        URI originalURI = new URI("http://username:password@www.example.com:8080/path/to/resource?param1=value1&param2=value2#fragment");
        URI result = baseHessian2Serialize(originalURI);
        Assertions.assertEquals(originalURI.toString(), result.toString());
        Assertions.assertEquals(originalURI.toString(), hessian3ToHessian3(originalURI).toString());
        Assertions.assertEquals(originalURI.toString(), hessian4ToHessian3(originalURI).toString());
        Assertions.assertEquals(originalURI.toString(), hessian3ToHessian4(originalURI).toString());
    }


    @Test
    void testEmp() throws IOException, URISyntaxException {
        URI originalURI = new URI("http", "username:password", "www.example.com", 8080, "/path/to/resource", "param1=value1&param2=value2", "fragment");

        URI result = baseHessian2Serialize(originalURI);

        Assertions.assertEquals(originalURI.toString(), result.toString());
    }
}
