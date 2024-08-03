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
package com.alibaba.com.caucho.hessian.io.issue73;

import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class SerializeTest extends SerializeTestBase {
    @Test
    void test() throws IOException {
        AppResult<List<ComplainDto>> result = new AppResult<>();
        ComplainDto complainDto = new ComplainDto();
        complainDto.setAppealReason("1");
        complainDto.setAppealState(2);
        complainDto.setAppealType((byte) 3);
        complainDto.setClass1Name("4");
        complainDto.setClass2Name("5");
        complainDto.setClass3Name("6");
        complainDto.setComplainDate("7");
        complainDto.setComplainOi(Arrays.asList("8"));
        complainDto.setComplainOrderId("9");
        complainDto.setComplainResult("10");
        complainDto.setComplainStatus((byte) 11);
        complainDto.setComplainStatusName("12");
        complainDto.setCustomerName("13");
        complainDto.setRemark("14");
        complainDto.setShowType(Boolean.TRUE);
        complainDto.setSurplusDate("15");
        complainDto.setTotal(BigDecimal.valueOf(16L));
        complainDto.setTotalType((byte) 17);
        complainDto.setUserAccessory(Arrays.asList("18"));
        result.setData(Arrays.asList(complainDto));

        Assertions.assertEquals(result, baseHessian2Serialize(result));
        Assertions.assertEquals(result, hessian3ToHessian3(result));
        Assertions.assertEquals(result, hessian4ToHessian3(result));
        Assertions.assertEquals(result, hessian3ToHessian4(result));

    }
}
