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
package org.apache.dubbo.hessian.java11;

import org.apache.dubbo.hessian.java11.base.SerializeTestBase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ImmutableTest extends SerializeTestBase {

    @Test
    public void testImmutableCollections() throws IOException {
        testEquals(new TestClass(null, null, null));
        testEquals(new TestClass(List.of(), Map.of(), Set.of()));
        testEquals(new TestClass(List.of(1), Map.of(1, 2), Set.of(1)));
        testEquals(new TestClass(List.of(1, 2, 3), Map.of(1, 2, 3, 4), Set.of(1, 2, 3)));
    }

    @Test
    public void testImmutableList() throws IOException {
        testEquals(List.of());
        testEquals(List.of(1));
        testEquals(List.of(1, 2));
        testEquals(List.of(1, 2, 3));
        testEquals(List.of(1, 2, 3, 4));

        testEquals(List.of(1, 2, 3, 4).subList(0, 0));
        testEquals(List.of(1, 2, 3, 4).subList(0, 1));
        testEquals(List.of(1, 2, 3, 4).subList(0, 2));
        testEquals(List.of(1, 2, 3, 4).subList(0, 3));
    }


    @Test
    public void setImmutableMap() throws IOException {
        testEquals(Map.of());
        testEquals(Map.of(1, 2));
        testEquals(Map.of(1, 2, 3, 4));
    }

    @Test
    public void testImmutableSet() throws IOException {
        testEquals(Set.of());
        testEquals(Set.of(1));
        testEquals(Set.of(1, 2));
        testEquals(Set.of(1, 2, 3));
    }

    void testEquals(Object object) throws IOException {
        Assertions.assertEquals(object.hashCode(), baseHessian2Serialize(object).hashCode());
        Assertions.assertEquals(object, baseHessian2Serialize(object));
    }


    public static class TestClass implements Serializable {
        List a;
        Map b;
        Set c;

        public TestClass(List a, Map b, Set n) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            TestClass testClass = (TestClass) object;
            return Objects.equals(a, testClass.a) && Objects.equals(b, testClass.b) && Objects.equals(c, testClass.c);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c);
        }
    }
}
