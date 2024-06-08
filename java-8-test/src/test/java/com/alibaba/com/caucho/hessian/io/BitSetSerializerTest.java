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

import java.io.IOException;
import java.util.BitSet;

public class BitSetSerializerTest extends SerializeTestBase {

    /** {@linkplain BitSetSerializer#writeObject(Object, AbstractHessianOutput)} */
    @Test
    public void bitSet() throws IOException {
        long[] words = new long[3];
        words[0] = 0x0102030405060708L;
        words[1] = 0x1112131415161718L;
        words[2] = 0x2122232425262728L;
        assertBitSet(null);
        assertBitSet(new BitSet());
        assertBitSet(new BitSet(1));
        assertBitSet(BitSet.valueOf(words));
    }

    private void assertBitSet(BitSet bitSet) throws IOException {
        Assertions.assertEquals(bitSet, baseHessian2Serialize(bitSet));
    }
}
