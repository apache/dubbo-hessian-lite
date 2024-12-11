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

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class GlobalCache {
    private static final CacheItem[] CACHE_ITEMS = new CacheItem[16];

    static {
        for (int i = 0; i < CACHE_ITEMS.length; i++) {
            CACHE_ITEMS[i] = new CacheItem();
        }
    }

    static final AtomicReferenceFieldUpdater<CacheItem, byte[]> BYTES_UPDATER
            = AtomicReferenceFieldUpdater.newUpdater(CacheItem.class, byte[].class, "bytes");


    static final class CacheItem {
        volatile byte[] bytes = new byte[8 * 1024];
    }

    public static byte[] getBytes() {
        CacheItem cacheItem = CACHE_ITEMS[System.identityHashCode(Thread.currentThread()) & (CACHE_ITEMS.length - 1)];
        return BYTES_UPDATER.getAndSet(cacheItem, null);
    }

    public static void putBytes(byte[] bytes) {
        CacheItem cacheItem = CACHE_ITEMS[System.identityHashCode(Thread.currentThread()) & (CACHE_ITEMS.length - 1)];
        BYTES_UPDATER.set(cacheItem, bytes);
    }
}
