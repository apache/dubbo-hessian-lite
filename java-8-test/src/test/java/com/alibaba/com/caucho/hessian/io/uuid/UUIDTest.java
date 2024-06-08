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
package com.alibaba.com.caucho.hessian.io.uuid;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class UUIDTest extends SerializeTestBase {
    @Test
    void test() throws Exception {
        List<BadExampleDTO> data = createBadData();
        Assertions.assertEquals(data, baseHessian2Serialize(data));
    }

    private List<BadExampleDTO> createBadData() {
        UUID uuid = UUID.randomUUID();

        BadExampleDTO badExampleDTO = new BadExampleDTO();
        badExampleDTO.setId(1L);
        badExampleDTO.setSign(uuid);

        BadExampleDTO badExampleDTO2 = new BadExampleDTO();
        badExampleDTO2.setId(2L);
        badExampleDTO2.setSign(uuid);

        return Arrays.asList(badExampleDTO, badExampleDTO2);
    }

}
