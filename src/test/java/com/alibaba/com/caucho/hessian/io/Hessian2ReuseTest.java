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
import com.alibaba.com.caucho.hessian.io.beans.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Hessian2ReuseTest extends SerializeTestBase {

    private static final Hessian2Input h2i = new Hessian2Input(null);

    private static final Hessian2Output h2o = new Hessian2Output(null);

    @SuppressWarnings("unchecked")
    private static <T> T serializeAndDeserialize(T obj, Class<T> clazz) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        h2o.init(outputStream);
        h2o.writeObject(obj);
        h2o.flush();

        h2i.init(new ByteArrayInputStream(outputStream.toByteArray()));
        return (T) h2i.readObject(clazz);
    }

    @Test
    public void testString() throws IOException {
        for (int i = 0; i < 100; i++) {
            String obj = "Hello, Hessian2, round:" + i;

            String newObj = serializeAndDeserialize(obj, String.class);
            Assert.assertEquals(obj, newObj);

            String newObj2 = baseHessian2Serialize(obj);
            Assert.assertEquals(newObj, newObj2);
        }
    }

    @Test
    public void testDate() throws IOException {
        long mills = System.currentTimeMillis();
        serializeAndDeserialize(new Date(mills), Date.class);
        serializeAndDeserialize(new Date(mills + 1000_000), Date.class);
    }

    @Test
    public void testBaseUser() throws IOException {
        for (int i = 0; i < 100; i++) {
            BaseUser obj = new BaseUser();
            obj.setUserId(i * ThreadLocalRandom.current().nextInt(10000));
            obj.setUserName(String.valueOf(System.currentTimeMillis()));

            BaseUser newObj = serializeAndDeserialize(obj, BaseUser.class);
            Assert.assertEquals(obj, newObj);

            BaseUser newObj2 = baseHessian2Serialize(obj);
            Assert.assertEquals(newObj, newObj2);
        }
    }

    @Test
    public void testSubUser() throws IOException {
        for (int i = 0; i < 100; i++) {
            SubUser obj = new SubUser();
            obj.setUserId(i * ThreadLocalRandom.current().nextInt(10000));
            obj.setUserName(String.valueOf(System.currentTimeMillis()));
            obj.setAgeList(Arrays.asList(12, 13, 14, 15));
            obj.setSexyList(Arrays.asList(true, false));
            obj.setWeightList(Arrays.asList(120D, 130D, 140D));

            SubUser newObj = serializeAndDeserialize(obj, SubUser.class);
            Assert.assertEquals(obj, newObj);

            SubUser newObj2 = baseHessian2Serialize(obj);
            Assert.assertEquals(newObj, newObj2);
        }
    }

    @Test
    public void testGrandsonUser() throws IOException {
        for (int i = 0; i < 100; i++) {
            GrandsonUser obj = new GrandsonUser();
            obj.setUserId(i * ThreadLocalRandom.current().nextInt(10000));
            obj.setUserName(String.valueOf(System.currentTimeMillis()));
            obj.setAgeList(Arrays.asList(12, 13, 14, 15));
            obj.setSexyList(Arrays.asList(true, false));
            obj.setWeightList(Arrays.asList(120D, 130D, 140D));

            SubUser newObj = serializeAndDeserialize(obj, SubUser.class);
            Assert.assertEquals(obj, newObj);

            SubUser newObj2 = baseHessian2Serialize(obj);
            Assert.assertEquals(newObj, newObj2);
        }
    }

    @Test
    public void testType() throws IOException {
        serializeAndDeserialize(Type.Lower, Type.class);
        serializeAndDeserialize(Type.Normal, Type.class);
        serializeAndDeserialize(Type.High, Type.class);
    }

    @Test
    public void testHessian2StringShortType() throws IOException {
        for (int i = 0; i < 100; i++) {
            Hessian2StringShortType obj = new Hessian2StringShortType();
            obj.shortSet = new HashSet<>();
            obj.stringShortMap = new HashMap<>();
            obj.stringByteMap = new HashMap<>();
            obj.stringPersonTypeMap = new HashMap<>();

            obj.shortSet.add((short) i);
            obj.shortSet.add((short) (i * 2));

//            shortType.stringShortMap.put(String.valueOf(i), (short) i);
//            shortType.stringShortMap.put(String.valueOf(i * 100), (short) (i * 100));

//            shortType.stringByteMap.put(String.valueOf(i), (byte) 1);

            List<Short> shorts = Arrays.asList((short) 12, (short) 4);
            PersonType abc = new PersonType("ABC", 12, 128D, (short) 1, (byte) 2, shorts);
            obj.stringPersonTypeMap.put("P_" + i, abc);

            Hessian2StringShortType newObj = serializeAndDeserialize(obj, Hessian2StringShortType.class);
            Assert.assertEquals(obj, newObj);

            Hessian2StringShortType newObj2 = baseHessian2Serialize(obj);
            Assert.assertEquals(newObj, newObj2);
        }
    }

}
