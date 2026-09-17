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
package com.alibaba.com.caucho.hessian.io.issue108;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import com.alibaba.com.caucho.hessian.io.HessianProtocolException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class ObjectDefinitionFieldCountTest {

    @Test
    void testHugeFieldCount() {
        byte[] bytes = {(byte) 0x43, 0x01, 0x78, 0x49,
                (byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));

        HessianProtocolException e = Assertions.assertThrows(
                HessianProtocolException.class, input::readObject);
        Assertions.assertTrue(e.getMessage().contains("truncated object definition"));
    }

    @Test
    void testNegativeFieldCount() {
        byte[] bytes = {0x43, 0x01, 0x78, 0x49,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));

        HessianProtocolException e = Assertions.assertThrows(
                HessianProtocolException.class, input::readObject);
        Assertions.assertTrue(e.getMessage().contains("bad field count -1"));
    }

    @Test
    void testFieldNamesArrivingInPieces() {
        // 'C' 'x' count=6, then six empty field names
        byte[] bytes = {0x43, 0x01, 0x78, 0x49, 0x00, 0x00, 0x00, 0x06,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        Hessian2Input input = new Hessian2Input(new DribbleInputStream(bytes));

        Assertions.assertThrows(EOFException.class, input::readObject);
    }

    @Test
    void testRoundTripThroughDribblingStream() throws IOException {
        SampleBean bean = new SampleBean();
        bean.setName("issue-108");
        bean.setCount(6);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bout);
        out.writeObject(bean);
        out.flush();

        Hessian2Input input = new Hessian2Input(
                new DribbleInputStream(bout.toByteArray()));
        SampleBean copy = (SampleBean) input.readObject();

        Assertions.assertEquals(bean.getName(), copy.getName());
        Assertions.assertEquals(bean.getCount(), copy.getCount());
    }

    public static class SampleBean implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private int count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    static class DribbleInputStream extends InputStream {
        private final byte[] bytes;
        private int offset;

        DribbleInputStream(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public int read() {
            if (offset >= bytes.length)
                return -1;

            return bytes[offset++] & 0xff;
        }

        @Override
        public int read(byte[] buffer, int off, int len) {
            if (offset >= bytes.length)
                return -1;

            buffer[off] = bytes[offset++];

            return 1;
        }
    }
}
