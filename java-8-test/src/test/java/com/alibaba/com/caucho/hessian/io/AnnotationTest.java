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
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class AnnotationTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        TestAnnotation annotation = AnnotatedClass.class.getAnnotation(TestAnnotation.class);

        TestAnnotation testAnnotation = baseHessian2Serialize(annotation);

        Assertions.assertEquals(annotation, testAnnotation);
    }

    @Test
    void testCollection() throws IOException {
        List<Object> list = new ArrayList<>();

        TestAnnotation annotation = AnnotatedClass.class.getAnnotation(TestAnnotation.class);
        testCollection(list, annotation);
    }

    @TestAnnotation(byteValue = 1, intValue = 2, shortValue = 3, floatValue = 4.0f, doubleValue = 5.0, value = "test")
    public static class AnnotatedClass {
    }
    
    @Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface TestAnnotation {
        String value() default "default";
        
        int intValue() default 0;
        
        byte byteValue() default 0;

        short shortValue() default 0;
        
        float floatValue() default 1.0001f;

        double doubleValue() default 2.0001;
    }
}
