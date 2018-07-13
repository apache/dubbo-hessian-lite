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

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class CollectionSerializerTest extends SerializeTestBase {
    @Test
    public void testSetSerializer() throws Exception {

        Set set = new HashSet();
        set.add(1111);
        set.add(2222);

        Set deserialize = baseHessianSerialize(set);
        Assert.assertTrue(deserialize.equals(set));
    }

    @Test
    public void testListSerializer() throws Exception {

        List<Integer> list = new LinkedList<>();
        list.add(1111);
        list.add(2222);

        List deserialize = baseHessianSerialize(list);
        Assert.assertTrue(deserialize.equals(list));
    }


    @Test
    public void testVectorSerializer() throws Exception {

        Vector vector = new Vector();
        vector.add(1111);
        vector.add(2222);

        List deserialize = baseHessianSerialize(vector);
        Assert.assertTrue(deserialize.equals(vector));
    }
}
