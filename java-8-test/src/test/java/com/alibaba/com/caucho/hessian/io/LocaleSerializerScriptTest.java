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

import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for Locale script preservation during serialization/deserialization
 */
public class LocaleSerializerScriptTest extends SerializeTestBase {
    
    @Test
    void testLocaleWithScript() throws IOException {
        // Create a Locale with script
        Locale zh_CN_Hans = new Locale.Builder()
                .setLanguage("zh")
                .setRegion("CN")
                .setScript("Hans")
                .build();

        // Serialize and deserialize
        Locale result = baseHessian2Serialize(zh_CN_Hans);

        // The script should be preserved
        assertEquals(zh_CN_Hans.getLanguage(), result.getLanguage(), "Language should be preserved");
        assertEquals(zh_CN_Hans.getCountry(), result.getCountry(), "Country should be preserved");
        assertEquals(zh_CN_Hans.getScript(), result.getScript(), "Script should be preserved");
        assertEquals(zh_CN_Hans.toString(), result.toString(), "String representation should match");
        assertEquals(zh_CN_Hans, result, "Locales should be equal");
    }

    @Test
    void testLocaleWithScriptAndVariant() throws IOException {
        // Create a Locale with script and variant
        Locale complex = new Locale.Builder()
                .setLanguage("en")
                .setRegion("US")
                .setScript("Latn")
                .setVariant("POSIX")
                .build();

        // Serialize and deserialize
        Locale result = baseHessian2Serialize(complex);

        // All components should be preserved
        assertEquals(complex.getLanguage(), result.getLanguage(), "Language should be preserved");
        assertEquals(complex.getCountry(), result.getCountry(), "Country should be preserved");
        assertEquals(complex.getScript(), result.getScript(), "Script should be preserved");
        assertEquals(complex.getVariant(), result.getVariant(), "Variant should be preserved");
        assertEquals(complex.toString(), result.toString(), "String representation should match");
        assertEquals(complex, result, "Locales should be equal");
    }

    @Test
    void testVariousScriptLocales() throws IOException {
        // Test different script codes
        Locale[] testLocales = {
            new Locale.Builder()
                    .setLanguage("ar")
                    .setRegion("EG")
                    .setScript("Arab")
                    .build(),
            new Locale.Builder()
                    .setLanguage("ja")
                    .setRegion("JP")
                    .setScript("Jpan")
                    .build(),
            new Locale.Builder()
                    .setLanguage("ru")
                    .setRegion("RU")
                    .setScript("Cyrl")
                    .build(),
            new Locale.Builder()
                    .setLanguage("hi")
                    .setRegion("IN")
                    .setScript("Deva")
                    .build()
        };

        for (Locale original : testLocales) {
            Locale result = baseHessian2Serialize(original);
            assertEquals(original.getScript(), result.getScript(), "Script should be preserved for " + original);
            assertEquals(original, result, "Locales should be equal for " + original);
        }
    }
}
