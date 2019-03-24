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
import com.alibaba.com.caucho.hessian.io.beans.SubUser;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Assert;
import org.junit.Test;

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
    public void testSubUser() throws IOException {
        int times = 100;
        final CountDownLatch latch = new CountDownLatch(times);
        final AtomicInteger error = new AtomicInteger();
        for (int i = 0; i < times; i++) {
           new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        try {
                            SubUser subUser = new SubUser();
                            subUser.setUserId(1);
                            subUser.setUserName("tony");
                            List<Integer> list1 = Arrays.asList(null, 3, 1);
                            List<Double> list2 = Arrays.asList(null, 1.1, 1.2);
                            List<Boolean> list3 = Arrays.asList(false, null, true);
                            subUser.setAgeList(list1);
                            subUser.setWeightList(list2);
                            subUser.setSexyList(list3);
                            SubUser serializeUser = baseHessian2Serialize(subUser);
                            Assert.assertEquals(subUser.getAgeList(), serializeUser.getAgeList());
                            Assert.assertEquals(subUser.getWeightList(), serializeUser.getWeightList());
                            Assert.assertEquals(subUser.getSexyList(), serializeUser.getSexyList());
                        } catch (Throwable e) {
                            error.incrementAndGet();
                        }
                    }
                    latch.countDown();
                }
            }).start();
        }
        try {
            latch.await();
            Assert.assertEquals(0, error.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
