/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
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

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Hessian2BinaryChunkReadTest {
    @Test
    public void bufferedReadHandlesOneByteFinalChunk() throws IOException {
        // An 8192-byte output buffer produces 8189 bytes followed by a compact 1-byte chunk.
        assertBufferedRoundTrip(8190);
    }

    @Test
    public void bufferedReadHandlesShortFinalChunk() throws IOException {
        // The remaining 32 bytes use the two-byte compact binary header.
        assertBufferedRoundTrip(8221);
    }

    private static void assertBufferedRoundTrip(int length) throws IOException {
        byte[] expected = new byte[length];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = (byte) i;
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bytes);
        out.writeBytes(expected, 0, expected.length);
        out.flush();
        byte[] wire = bytes.toByteArray();

        // The ordinary reader must accept the same encoder-generated data.
        Hessian2Input ordinary = new Hessian2Input(new ByteArrayInputStream(wire));
        try {
            assertArrayEquals(expected, ordinary.readBytes());
        } finally {
            ordinary.close();
        }

        Hessian2Input in = new Hessian2Input(new ByteArrayInputStream(wire));
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            int count;
            while ((count = in.readBytes(buffer, 0, buffer.length)) >= 0) {
                assertTrue(count > 0, "The binary reader must make progress");
                actual.write(buffer, 0, count);
            }
            assertArrayEquals(expected, actual.toByteArray());
        } finally {
            in.close();
        }
    }
}
