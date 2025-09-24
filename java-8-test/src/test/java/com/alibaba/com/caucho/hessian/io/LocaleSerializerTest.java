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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocaleSerializerTest extends SerializeTestBase {

    /** {@linkplain LocaleSerializer#writeObject(Object, AbstractHessianOutput)} */
    @Test
    public void locale() throws IOException {
        assertLocale(null);
        assertLocale(new Locale(""));
        assertLocale(new Locale("zh"));
        assertLocale(new Locale("zh", "CN"));
        assertLocale(new Locale("zh-hant", "CN"));
        assertLocale(new Locale("zh-hant", "CN", "GBK"));
    }

    @Test
    @EnabledForJreRange(max = JRE.JAVA_11)
    public void localeCompact() throws IOException {
        assertLocaleCompact(null);
        assertLocaleCompact(new Locale(""));
        assertLocaleCompact(new Locale("zh"));
        assertLocaleCompact(new Locale("zh", "CN"));
        assertLocaleCompact(new Locale("zh-hant", "CN"));
        assertLocaleCompact(new Locale("zh-hant", "CN", "GBK"));
    }

    @Test
    public void testCollection() throws IOException {
        List<Object> list = new ArrayList<>();

        Locale locale = new Locale("zh", "CN", "Hans");
        testCollection(list, locale);
    }

    private void assertLocale(Locale locale) throws IOException {
        Assertions.assertEquals(locale, baseHessian2Serialize(locale));
    }

    private void assertLocaleCompact(Locale locale) throws IOException {
        Assertions.assertEquals(locale, baseHessian2Serialize(locale));
        Assertions.assertEquals(locale, hessian3ToHessian3(locale));
        Assertions.assertEquals(locale, hessian4ToHessian3(locale));
        Assertions.assertEquals(locale, hessian3ToHessian4(locale));
    }
}
