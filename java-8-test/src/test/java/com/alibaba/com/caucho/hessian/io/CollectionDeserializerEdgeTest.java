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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionDeserializerEdgeTest {
    @Test
    public void listValuesRoundTripWithoutConsumingNextValue() throws IOException {
        List<String> values = new ArrayList<String>(Arrays.asList("first", "second"));
        Hessian2Input in = input(encode(values, "tail"));
        assertEquals(values, in.readObject());
        assertEquals("tail", in.readString());
    }

    @Test
    public void iteratorValuesRoundTripAsList() throws IOException {
        List<String> values = new ArrayList<String>(Arrays.asList("first", "second"));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bytes);
        out.writeObject(values.iterator());
        out.flush();

        Hessian2Input in = new Hessian2Input(new ByteArrayInputStream(bytes.toByteArray()));
        assertEquals(values, in.readObject());
    }

    @Test
    public void shortListRoundTripPreservesElementType() throws IOException {
        List<Short> values = Arrays.asList((short) 7, (short) -8);
        Hessian2Input in = input(encode(values, "tail"));
        assertEquals(values, in.readObject(List.class, Short.class));
        assertEquals("tail", in.readString());
    }

    private static Hessian2Input input(byte[] bytes) {
        return new Hessian2Input(new ByteArrayInputStream(bytes));
    }

    private static byte[] encode(Object... values) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bytes);
        for (Object value : values) {
            out.writeObject(value);
        }
        out.flush();
        return bytes.toByteArray();
    }
}
