/* Copyright (c) 2008-2023, Nathan Sweet
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of Esoteric Software nor the names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */
package org.apache.dubbo.hessian.java17;

import org.apache.dubbo.hessian.java17.base.SerializeTestBase;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.IntStream;

public class RecordTest extends SerializeTestBase {

    @Test
    public void testObject() throws IOException {
        testEquals(new TestRecordObject(123, "abc", "123", 123));
    }

    /**
     * Test where the single object is a record.
     */
    public record RecordRectangle(String height, int width, long x, double y) implements Serializable {
    }

    @Test
    public void testBasicRecord() throws IOException {
        var r1 = new RecordRectangle("one", 2, 3L, 4.0);
        testEquals(r1);
    }

    /**
     * Test where the single object is an empty record.
     */
    public record EmptyRecord() implements Serializable {
    }

    @Test
    public void testEmptyRecord() throws IOException {
        var r1 = new EmptyRecord();
        testEquals(r1);
    }


    /**
     * Test deserialisation of a record where the number of input values exceeds
     * the number of record components. In this case values are read from the
     * input sequentially until the number of record components is met,
     * any additional input values are ignored.
     */
    public record RecordPoint(int x, int y) implements Serializable {
    }

    @Test
    public void testDeserializeWrongNumberOfValues() throws IOException {
        var r1 = new RecordPoint(1, 1);
        testEquals(r1);
    }

    /**
     * Test where the record has an explicit constructor.
     */
    public record RecordWithConstructor(String height, int width, long x, double y) implements Serializable {
        public RecordWithConstructor(String height) {
            this(height, 20, 30L, 40.0);
        }
    }

    @Test
    public void testRecordWithConstructor() throws IOException {
        var r1 = new RecordWithConstructor("ten");
        testEquals(r1);
    }

    /**
     * Test where the record component object is a record.
     */
    public record RecordOfRecord(RecordRectangle r) implements Serializable {
    }

    @Test
    public void testRecordOfRecord() throws IOException {
        var r1 = new RecordOfRecord(new RecordRectangle("one", 2, 3L, 4.0));
        testEquals(r1);
    }

    /**
     * Test where the single object is an array of records.
     */
    @Test
    public void testArrayOfRecords() throws IOException {
        final var arr = new RecordPoint[100];
        IntStream.range(0, 100).forEach(i -> arr[i] = new RecordPoint(i, i + 1));

        testEquals(arr);
    }

    /**
     * Test where the record component object is an array of records.
     */
    public record RecordWithArray(RecordRectangle[] recordArray) implements Serializable {
        @Override
        public boolean equals(Object obj) {
            return Arrays.equals(recordArray, ((RecordWithArray) obj).recordArray);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(recordArray);
        }
    }

    @Test
    public void testRecordWithArray() throws IOException {

        var r1 = new RecordWithArray(new RecordRectangle[]{new RecordRectangle("one", 2, 3L, 4.0)});

        testEquals(r1);
    }

    /**
     * Test where record components are non-primitives with their default
     * value (null).
     */
    public record RecordWithNull(Object o, Number n, String s) implements Serializable {
    }

    @Test
    public void testRecordWithNull() throws IOException {
        var r1 = new RecordWithNull(null, null, null);
        testEquals(r1);
    }

    /**
     * Test where record components are primitives with their default values.
     */
    public record RecordWithDefaultValues(byte b, short s, int i, long l, float f, double d, char c,
                                          boolean bool) implements Serializable {
    }

    @Test
    public void testRecordWithPrimitiveDefaultValues() throws IOException {
        var r1 = new RecordWithDefaultValues(
                (byte) 0, (short) 0, 0, 0l, 0.0f, 0.0d, '\u0000', false);
        testEquals(r1);
    }

    /**
     * Test where the an exception is thrown in the record constructor.
     */
    public record PositivePoint(int x, int y) implements Serializable {
        public PositivePoint { // compact syntax
            if (x < 0)
                throw new IllegalArgumentException("negative x:" + x);
            if (y < 0)
                throw new IllegalArgumentException("negative y:" + y);
        }
    }

    /**
     * Test where the record parameters are the same but in different order.
     * This is supported as record components are sorted by name during
     * de/serialization.
     */
    public record R(long l, int i, String s) implements Serializable {
    }

    public record R1(int i, long l, String s) implements Serializable {
    }

    public record R2(String s, int i, long l) implements Serializable {
    }

    @Test
    public void testRecordWithParametersReordered1() throws IOException {
        var r = new R(1L, 1, "foo");
        R1 r1 = (R1) baseHessian2Serialize(r, R1.class);
        Assert.assertEquals(new R1(1, 1L, "foo"), r1);
    }

    @Test
    public void testRecordWithParametersReordered2() throws IOException {
        var r = new R(1L, 1, "foo");
        R2 r2 = (R2) baseHessian2Serialize(r, R2.class);
        Assert.assertEquals(new R2("foo", 1, 1L), r2);
    }

    public static record RecordWithSuperType(Number n) implements Serializable {
    }

    @Test
    public void testRecordWithSuperType() throws IOException {
        var r = new RecordWithSuperType(1L);
        testEquals(r);
    }

    static record PackagePrivateRecord(int i, String s) implements Serializable {
    }

    private static record PrivateRecord(String s, int i) implements Serializable {
    }

    @Test
    public void testNonPublicRecords() throws IOException {
        testEquals(new PackagePrivateRecord(1, "s1"));
        testEquals(new PrivateRecord("s2", 2));
    }


    void testEquals(Object object) throws IOException {
        if (object instanceof Object[]) {
            Object[] src = (Object[]) object;
            Object[] dst = baseHessian2Serialize(src);
            TestCase.assertEquals(src.length, dst.length);
            for (int i = 0; i < src.length; i++) {
                TestCase.assertEquals(src[i], dst[i]);
            }
        } else {
            TestCase.assertEquals(object, baseHessian2Serialize(object));
            TestCase.assertEquals(object.hashCode(), baseHessian2Serialize(object).hashCode());
        }
    }
}
