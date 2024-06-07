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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DenyListTest {

    @Test
    public void testDeny() throws ClassNotFoundException {
        ClassFactory classFactory = new ClassFactory(this.getClass().getClassLoader());
        Assertions.assertEquals(HashMap.class, classFactory.load("java.lang.Runtime"));
        Assertions.assertEquals(HashMap.class, classFactory.load("java.beans.A"));
        Assertions.assertEquals(HashMap.class, classFactory.load("java.beans.B"));
        Assertions.assertEquals(HashMap.class, classFactory.load("java.beans.C"));
        Assertions.assertEquals(HashMap.class, classFactory.load("java.beans.D"));
        Assertions.assertEquals(HashMap.class, classFactory.load("java.beans.E"));
        Assertions.assertEquals(HashMap.class, classFactory.load("sun.rmi.transport.StreamRemoteCall"));

        classFactory.deny(TestClass.class.getName());
        Assertions.assertEquals(HashMap.class, classFactory.load(TestClass.class.getName()));
        Assertions.assertEquals(HashMap.class, classFactory.load(TestClass1.class.getName()));

        classFactory.deny(TestInterface.class.getName());
        Assertions.assertEquals(HashMap.class, classFactory.load(TestInterface.class.getName()));
        Assertions.assertEquals(HashMap.class, classFactory.load(TestImpl.class.getName()));
    }

    @Test
    public void testAllow() throws ClassNotFoundException {
        ClassFactory classFactory = new ClassFactory(this.getClass().getClassLoader());
        Assertions.assertEquals(Integer.class, classFactory.load(Integer.class.getName()));
        Assertions.assertEquals(Long.class, classFactory.load(Long.class.getName()));
        Assertions.assertEquals(TestClass.class, classFactory.load(TestClass.class.getName()));
        Assertions.assertEquals(DenyListTest.class, classFactory.load(DenyListTest.class.getName()));
        Assertions.assertEquals(List.class, classFactory.load(List.class.getName()));
        Assertions.assertEquals(Array.class, classFactory.load(Array.class.getName()));
        Assertions.assertEquals(LinkedList.class, classFactory.load(LinkedList.class.getName()));
        Assertions.assertEquals(RuntimeException.class, classFactory.load(RuntimeException.class.getName()));
    }
}
