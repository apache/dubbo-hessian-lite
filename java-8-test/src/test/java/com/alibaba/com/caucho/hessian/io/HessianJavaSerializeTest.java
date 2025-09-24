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
import com.alibaba.com.caucho.hessian.io.beans.BaseUser;
import com.alibaba.com.caucho.hessian.io.beans.GrandsonUser;
import com.alibaba.com.caucho.hessian.io.beans.SubUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import java.util.ArrayList;
import java.util.List;

/**
 * fix hessian serialize bug:
 * the filed of parent class will cover the filed of sub class
 */
public class HessianJavaSerializeTest extends SerializeTestBase {

    @Test
    public void testGetBaseUserName() throws Exception {

        BaseUser baseUser = new BaseUser();
        baseUser.setUserId(1);
        baseUser.setUserName("tom");

        BaseUser serializedUser = baseHessian2Serialize(baseUser);
        Assertions.assertEquals("tom", serializedUser.getUserName());
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    public void testGetBaseUserNameCompact() throws Exception {

        BaseUser baseUser = new BaseUser();
        baseUser.setUserId(1);
        baseUser.setUserName("tom");

        BaseUser serializedUser = baseHessian2Serialize(baseUser);
        Assertions.assertEquals("tom", serializedUser.getUserName());
        Assertions.assertEquals("tom", hessian4ToHessian3(baseUser).getUserName());
        Assertions.assertEquals("tom", hessian3ToHessian3(baseUser).getUserName());
        Assertions.assertEquals("tom", hessian3ToHessian4(baseUser).getUserName());
    }

    @Test
    public void testGetSubUserName() throws Exception {
        SubUser subUser = new SubUser();
        subUser.setUserId(1);
        subUser.setUserName("tom");

        SubUser serializedUser = baseHessian2Serialize(subUser);
        Assertions.assertEquals("tom", serializedUser.getUserName());

    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    public void testGetSubUserNameCompact() throws Exception {
        SubUser subUser = new SubUser();
        subUser.setUserId(1);
        subUser.setUserName("tom");

        SubUser serializedUser = baseHessian2Serialize(subUser);
        Assertions.assertEquals("tom", serializedUser.getUserName());
        Assertions.assertEquals("tom", hessian4ToHessian3(subUser).getUserName());
        Assertions.assertEquals("tom", hessian3ToHessian3(subUser).getUserName());
        Assertions.assertEquals("tom", hessian3ToHessian4(subUser).getUserName());
    }


    @Test
    public void testSubUserWage() throws Exception {
        SubUser subUser = new SubUser();
        subUser.setUserId(1);
        subUser.setUserName("tom");
        List<Integer> list = new ArrayList<>();
        list.add(null);
        list.add(null);
        list.add(3);
        subUser.setAgeList(list);

        SubUser serializedUser = baseHessian2Serialize(subUser);
        Assertions.assertEquals(subUser.getAgeList(), serializedUser.getAgeList());

    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    public void testSubUserWageCompact() throws Exception {
        SubUser subUser = new SubUser();
        subUser.setUserId(1);
        subUser.setUserName("tom");
        List<Integer> list = new ArrayList<>();
        list.add(null);
        list.add(null);
        list.add(3);
        subUser.setAgeList(list);

        SubUser serializedUser = baseHessian2Serialize(subUser);
        Assertions.assertEquals(subUser.getAgeList(), serializedUser.getAgeList());
        Assertions.assertEquals(subUser.getAgeList(), hessian4ToHessian3(subUser).getAgeList());
        Assertions.assertEquals(subUser.getAgeList(), hessian3ToHessian3(subUser).getAgeList());
        Assertions.assertEquals(subUser.getAgeList(), hessian3ToHessian4(subUser).getAgeList());

    }

    @Test
    public void testGetGrandsonUserName() throws Exception {
        GrandsonUser grandsonUser = new GrandsonUser();
        grandsonUser.setUserId(1);
        grandsonUser.setUserName("tom");

        GrandsonUser serializedUser = baseHessian2Serialize(grandsonUser);
        Assertions.assertEquals("tom", serializedUser.getUserName());
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    public void testGetGrandsonUserNameCompact() throws Exception {
        GrandsonUser grandsonUser = new GrandsonUser();
        grandsonUser.setUserId(1);
        grandsonUser.setUserName("tom");

        GrandsonUser serializedUser = baseHessian2Serialize(grandsonUser);
        Assertions.assertEquals("tom", serializedUser.getUserName());
        Assertions.assertEquals("tom", hessian4ToHessian3(grandsonUser).getUserName());
        Assertions.assertEquals("tom", hessian3ToHessian3(grandsonUser).getUserName());
        Assertions.assertEquals("tom", hessian3ToHessian4(grandsonUser).getUserName());
    }

    @Test
    public void testFloat() throws Exception {
        Float fData = 99.8F;
        Double dData = 99.8D;
        Assertions.assertEquals(dData, baseHessian2Serialize(fData));
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    public void testFloatCompact() throws Exception {
        Float fData = 99.8F;
        Double dData = 99.8D;
        Assertions.assertEquals(dData, baseHessian2Serialize(fData));
        Assertions.assertEquals(dData, hessian4ToHessian3(fData));
        Assertions.assertEquals(dData, hessian3ToHessian3(fData));
        Assertions.assertEquals(dData, hessian3ToHessian4(fData));
    }

    @Test
    public void testCollection() throws Exception {
        BaseUser baseUser = new BaseUser();
        baseUser.setUserId(1);
        baseUser.setUserName("tom");
        List<BaseUser> baseUserList = new ArrayList<>();
        baseUserList.add(baseUser);
        baseUserList.add(baseUser);
        List<BaseUser> result = baseHessian2Serialize(baseUserList);
        Assertions.assertEquals(baseUserList.size(), result.size());
        Assertions.assertSame(result.get(0), result.get(1));
        Assertions.assertEquals("tom", result.get(0).getUserName());

        SubUser subUser = new SubUser();
        subUser.setUserId(1);
        subUser.setUserName("tom");
        List<SubUser> subUserList = new ArrayList<>();
        subUserList.add(subUser);
        subUserList.add(subUser);
        List<SubUser> subUserResult = baseHessian2Serialize(subUserList);
        Assertions.assertEquals(subUserList.size(), subUserResult.size());
        Assertions.assertSame(subUserResult.get(0), subUserResult.get(1));
        Assertions.assertEquals("tom", subUserResult.get(0).getUserName());

        List<Integer> list = new ArrayList<>();
        list.add(null);
        list.add(null);
        list.add(3);
        subUser.setAgeList(list);
        subUserResult = baseHessian2Serialize(subUserList);
        Assertions.assertEquals(subUserList.size(), subUserResult.size());
        Assertions.assertSame(subUserResult.get(0), subUserResult.get(1));
        Assertions.assertEquals("tom", subUserResult.get(0).getUserName());

        GrandsonUser grandsonUser = new GrandsonUser();
        grandsonUser.setUserId(1);
        grandsonUser.setUserName("tom");
        List<GrandsonUser> grandsonUserList = new ArrayList<>();
        grandsonUserList.add(grandsonUser);
        grandsonUserList.add(grandsonUser);
        List<GrandsonUser> grandsonUserResult = baseHessian2Serialize(grandsonUserList);
        Assertions.assertEquals(grandsonUserList.size(), grandsonUserResult.size());
        Assertions.assertSame(grandsonUserResult.get(0), grandsonUserResult.get(1));
        Assertions.assertEquals("tom", grandsonUserResult.get(0).getUserName());
    }

}
