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


class MapUtil {

  private static final int MAX_POWER_OF_TWO = 1 << (Integer.SIZE - 2);

  /**
   * @see java.util.HashMap#DEFAULT_INITIAL_CAPACITY
   */
  private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

  protected static int capacity(int expectedSize) {
    if (expectedSize < 0) {
      return DEFAULT_INITIAL_CAPACITY;
    }

    if (expectedSize < 3) {
      return expectedSize + 1;
    }
    if (expectedSize < MAX_POWER_OF_TWO) {
      // This is the calculation used in JDK8 to resize when a putAll
      // happens; it seems to be the most conservative calculation we
      // can make.  0.75 is the default load factor.
      return (int) ((float) expectedSize / 0.75F + 1.0F);
    }
    return Integer.MAX_VALUE;
  }

}
