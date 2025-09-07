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
import java.nio.charset.Charset;

public class StringCharsetTest extends SerializeTestBase {
    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testUnicode() throws IOException {
        int startCodePoint = 0x0000;
        int endCodePoint = 0x10FFFF;
        int chunkSize = 1000;

        for (int i = startCodePoint; i <= endCodePoint; i += chunkSize) {
            StringBuilder chunkBuilder = new StringBuilder();
            int chunkEnd = Math.min(i + chunkSize - 1, endCodePoint);

            for (int codePoint = i; codePoint <= chunkEnd; codePoint++) {
                if (Character.isValidCodePoint(codePoint)) {
                    chunkBuilder.append(Character.toChars(codePoint));
                }
            }

            Assertions.assertEquals(chunkBuilder.toString(), baseHessian2Serialize(chunkBuilder.toString()),
                    "Failed to serialize string with code points from " + i + " to " + chunkEnd);
            Assertions.assertEquals(chunkBuilder.toString(), hessian3ToHessian3(chunkBuilder.toString()),
                    "Failed to serialize string with code points from " + i + " to " + chunkEnd);
            Assertions.assertEquals(chunkBuilder.toString(), hessian3ToHessian4(chunkBuilder.toString()),
                    "Failed to serialize string with code points from " + i + " to " + chunkEnd);
            Assertions.assertEquals(chunkBuilder.toString(), hessian4ToHessian3(chunkBuilder.toString()),
                    "Failed to serialize string with code points from " + i + " to " + chunkEnd);
        }
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testGBK() throws IOException {
        int chunkSize = 1000;

        for (int high = 0x81; high <= 0xFE; high++) {
            for (int lowStart = 0x40; lowStart <= 0xFE; lowStart += chunkSize) {
                StringBuilder chunkBuilder = new StringBuilder();
                int lowEnd = Math.min(lowStart + chunkSize - 1, 0xFE);

                for (int low = lowStart; low <= lowEnd; low++) {
                    if (low == 0x7F) continue;

                    byte[] doubleBytes = {(byte) high, (byte) low};
                    String character = new String(doubleBytes, Charset.forName("GBK"));

                    if (character.codePointCount(0, character.length()) == 1) {
                        chunkBuilder.append(character);
                    }
                }

                Assertions.assertEquals(chunkBuilder.toString(), baseHessian2Serialize(chunkBuilder.toString()),
                        "Failed to serialize GBK string with high byte " + high +
                                " and low bytes from " + lowStart + " to " + lowEnd);
                Assertions.assertEquals(chunkBuilder.toString(), hessian3ToHessian3(chunkBuilder.toString()),
                        "Failed to serialize GBK string with high byte " + high +
                                " and low bytes from " + lowStart + " to " + lowEnd);
                Assertions.assertEquals(chunkBuilder.toString(), hessian3ToHessian4(chunkBuilder.toString()),
                        "Failed to serialize GBK string with high byte " + high +
                                " and low bytes from " + lowStart + " to " + lowEnd);
                Assertions.assertEquals(chunkBuilder.toString(), hessian4ToHessian3(chunkBuilder.toString()),
                        "Failed to serialize GBK string with high byte " + high +
                                " and low bytes from " + lowStart + " to " + lowEnd);
            }
        }
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testBig5() throws IOException {
        int chunkSize = 1000;

        for (int high = 0x81; high <= 0xFE; high++) {
            for (int lowStart = 0x40; lowStart <= 0xFE; lowStart += chunkSize) {
                StringBuilder chunkBuilder = new StringBuilder();
                int lowEnd = Math.min(lowStart + chunkSize - 1, 0xFE);

                for (int low = lowStart; low <= lowEnd; low++) {
                    if (low == 0x7F) continue;

                    byte[] doubleBytes = {(byte) high, (byte) low};
                    String character = new String(doubleBytes, Charset.forName("Big5"));

                    if (character.codePointCount(0, character.length()) == 1) {
                        chunkBuilder.append(character);
                    }
                }

                Assertions.assertEquals(chunkBuilder.toString(), baseHessian2Serialize(chunkBuilder.toString()),
                        "Failed to serialize Big5 string with high byte " + high +
                                " and low bytes from " + lowStart + " to " + lowEnd);
                Assertions.assertEquals(chunkBuilder.toString(), hessian3ToHessian4(chunkBuilder.toString()),
                        "Failed to serialize Big5 string with high byte " + high +
                                " and low bytes from " + lowStart + " to " + lowEnd);
                Assertions.assertEquals(chunkBuilder.toString(), hessian3ToHessian3(chunkBuilder.toString()),
                        "Failed to serialize Big5 string with high byte " + high +
                                " and low bytes from " + lowStart + " to " + lowEnd);
                Assertions.assertEquals(chunkBuilder.toString(), hessian4ToHessian3(chunkBuilder.toString()),
                        "Failed to serialize Big5 string with high byte " + high +
                                " and low bytes from " + lowStart + " to " + lowEnd);
            }
        }
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    void testGB2312() throws IOException {
        int chunkSize = 1000;

        for (int high = 0xA1; high <= 0xF7; high++) {
            for (int lowStart = 0xA1; lowStart <= 0xFE; lowStart += chunkSize) {
                StringBuilder chunkBuilder = new StringBuilder();
                int lowEnd = Math.min(lowStart + chunkSize - 1, 0xFE);

                for (int low = lowStart; low <= lowEnd; low++) {
                    byte[] doubleBytes = {(byte) high, (byte) low};
                    String character = new String(doubleBytes, Charset.forName("GB2312"));

                    if (character.codePointCount(0, character.length()) == 1) {
                        chunkBuilder.append(character);
                    }
                }

                Assertions.assertEquals(chunkBuilder.toString(),
                        baseHessian2Serialize(chunkBuilder.toString()),
                        "Failed to serialize GB2312 string with high byte " + high +
                                " and low bytes from " + lowStart + " to " + lowEnd);
                Assertions.assertEquals(chunkBuilder.toString(),
                        hessian3ToHessian4(chunkBuilder.toString()),
                        "Failed to serialize GB2312 string with high byte " + high +
                                " and low bytes from " + lowStart + " to " + lowEnd);
                Assertions.assertEquals(chunkBuilder.toString(),
                        hessian3ToHessian3(chunkBuilder.toString()),
                        "Failed to serialize GB2312 string with high byte " + high +
                                " and low bytes from " + lowStart + " to " + lowEnd);
                Assertions.assertEquals(chunkBuilder.toString(),
                        hessian4ToHessian3(chunkBuilder.toString()),
                        "Failed to serialize GB2312 string with high byte " + high +
                                " and low bytes from " + lowStart + " to " + lowEnd);
            }
        }
    }

}
