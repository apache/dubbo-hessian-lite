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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * InputStreamDeserializer 测试
 */
public class HessianInputStreamDeserializerTest extends SerializeTestBase {

    protected <T> T baseHessian2Serialize(T data) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bout);

        out.writeObject(data);
        out.flush();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        Hessian2Input input = new Hessian2Input(bin);
        Object obj = input.readObject(InputStream.class);
        return (T) obj;
    }

    byte[] returnBytes(int size){
        byte[] bytes = new byte[size];
        Arrays.fill(bytes, (byte) 'A');
        bytes[0] = '&';
        bytes[bytes.length-3] = '$';
        bytes[bytes.length-2] = '%';
        bytes[bytes.length-1] = '#';
        return bytes;
    }

    /**
     * 测试1024字节，返回 ByteArrayInputStream
     */
    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    public void test1024Byte() throws IOException {
        int size = 1024;
        byte[] bytes = returnBytes(size);
        InputStream source = new ByteArrayInputStream(bytes);
        InputStream result = baseHessian2Serialize(source);

        byte[] resultByte = new byte[size];
        result.read(resultByte);

        assertInstanceOf(ByteArrayInputStream.class, result, "类型错误");
        assertEquals(bytes.length, resultByte.length);
        assertEquals(new String(bytes), new String(resultByte));
    }

    /**
     * 测试8192字节，返回 ByteArrayInputStream
     */
    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    public void test8192Byte() throws IOException {
        int size = 8192;
        byte[] bytes = returnBytes(size);
        InputStream source = new ByteArrayInputStream(bytes);
        InputStream result = baseHessian2Serialize(source);

        byte[] resultByte = new byte[size];
        result.read(resultByte);

        assertInstanceOf(ByteArrayInputStream.class, result, "类型错误");
        assertEquals(bytes.length, resultByte.length);
        assertEquals(new String(bytes), new String(resultByte));
    }

    /**
     * 测试8193字节，返回 ByteArrayInputStream
     * 比缓冲区多一个字节
     */
    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    public void test8193Byte() throws IOException {
        int size = 8193;
        byte[] bytes = returnBytes(size);
        InputStream source = new ByteArrayInputStream(bytes);
        InputStream result = baseHessian2Serialize(source);

        byte[] resultByte = new byte[size];
        result.read(resultByte);

        assertInstanceOf(FileInputStream.class, result, "类型错误");
        assertEquals(bytes.length, resultByte.length);
        assertEquals(new String(bytes), new String(resultByte));
    }

    /**
     * 测试81920字节，返回 ByteArrayInputStream
     * 比缓冲区多10倍字节
     */
    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    public void test81920Byte() throws IOException {
        int size = 81920;
        byte[] bytes = returnBytes(size);
        InputStream source = new ByteArrayInputStream(bytes);
        InputStream result = baseHessian2Serialize(source);

        byte[] resultByte = new byte[size];
        result.read(resultByte);

        assertInstanceOf(FileInputStream.class, result, "类型错误");
        assertEquals(bytes.length, resultByte.length);
        assertEquals(new String(bytes), new String(resultByte));
    }

}
