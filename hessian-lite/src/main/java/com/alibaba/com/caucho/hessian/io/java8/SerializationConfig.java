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

package com.alibaba.com.caucho.hessian.io.java8;

/**
 * @author wuwen
 */
class SerializationConfig {

    /**
     * Whether to use compact mode for serialization, default false.
     * use -Dcom.caucho.hessian.io.java.time.serializer.compactMode=true to enable compact mode.
     */
    private static final boolean COMPACT_MODE = Boolean.getBoolean("com.caucho.hessian.io.java.time.serializer.compactMode");
    
    private SerializationConfig() {
    }

    /**
     * Checks if compact mode is enabled for serialization.
     * @return true if compact mode is enabled, false otherwise.
     */
    public static boolean isCompactMode() {
        return COMPACT_MODE;
    }
}
