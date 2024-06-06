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

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DenyListTest {

    @Test
    public void testDeny() throws ClassNotFoundException {
        ClassFactory classFactory = new ClassFactory(this.getClass().getClassLoader());
        Assert.assertEquals(HashMap.class, classFactory.load("java.lang.Runtime"));
        Assert.assertEquals(HashMap.class, classFactory.load("java.beans.A"));
        Assert.assertEquals(HashMap.class, classFactory.load("java.beans.B"));
        Assert.assertEquals(HashMap.class, classFactory.load("java.beans.C"));
        Assert.assertEquals(HashMap.class, classFactory.load("java.beans.D"));
        Assert.assertEquals(HashMap.class, classFactory.load("java.beans.E"));
        Assert.assertEquals(HashMap.class, classFactory.load("sun.rmi.transport.StreamRemoteCall"));

        classFactory.deny(TestClass.class.getName());
        Assert.assertEquals(HashMap.class, classFactory.load(TestClass.class.getName()));
        Assert.assertEquals(HashMap.class, classFactory.load(TestClass1.class.getName()));

        classFactory.deny(TestInterface.class.getName());
        Assert.assertEquals(HashMap.class, classFactory.load(TestInterface.class.getName()));
        Assert.assertEquals(HashMap.class, classFactory.load(TestImpl.class.getName()));
    }

    @Test
    public void testAllow() throws ClassNotFoundException {
        ClassFactory classFactory = new ClassFactory(this.getClass().getClassLoader());
        Assert.assertEquals(Integer.class, classFactory.load(Integer.class.getName()));
        Assert.assertEquals(Long.class, classFactory.load(Long.class.getName()));
        Assert.assertEquals(TestClass.class, classFactory.load(TestClass.class.getName()));
        Assert.assertEquals(DenyListTest.class, classFactory.load(DenyListTest.class.getName()));
        Assert.assertEquals(List.class, classFactory.load(List.class.getName()));
        Assert.assertEquals(Array.class, classFactory.load(Array.class.getName()));
        Assert.assertEquals(LinkedList.class, classFactory.load(LinkedList.class.getName()));
        Assert.assertEquals(RuntimeException.class, classFactory.load(RuntimeException.class.getName()));
    }
}
