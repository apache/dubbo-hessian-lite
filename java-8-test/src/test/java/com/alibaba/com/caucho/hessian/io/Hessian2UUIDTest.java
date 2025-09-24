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
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * fix hessian serialize bug:
 * the uuid can not be deserialized properly
 **/
public class Hessian2UUIDTest extends SerializeTestBase {

	@Test
	public void testUUIDObject() throws IOException {
		UUID actual = UUID.randomUUID();
		UUID deserialize = baseHessian2Serialize(actual);
		assertEquals(actual, deserialize);
	}

	@Test
	@EnabledForJreRange(max = JRE.JAVA_11)
	public void testUUIDObjectCompact() throws IOException {
		UUID actual = UUID.randomUUID();
		UUID deserialize = baseHessian2Serialize(actual);
		assertEquals(actual, deserialize);
		assertEquals(actual, hessian3ToHessian3(actual));
		assertEquals(actual, hessian4ToHessian3(actual));
		assertEquals(actual, hessian3ToHessian4(actual));
	}

	@Test
	public void testUUIDList() throws IOException {
		List<UUID> actual = new ArrayList<>(2);
		actual.add(UUID.randomUUID());
		actual.add(UUID.randomUUID());

		assertEquals(actual, baseHessian2Serialize(actual));
	}

	@Test
	@EnabledForJreRange(max = JRE.JAVA_11)
	public void testUUIDListCompact() throws IOException {
		List<UUID> actual = new ArrayList<>(2);
		actual.add(UUID.randomUUID());
		actual.add(UUID.randomUUID());

		assertEquals(actual, baseHessian2Serialize(actual));
		assertEquals(actual, hessian3ToHessian3(actual));
		assertEquals(actual, hessian4ToHessian3(actual));
		assertEquals(actual, hessian3ToHessian4(actual));
	}

	@Test
	public void testUUIDMap() throws IOException {
		Map<UUID, Object> actual = new HashMap<>(8);
		actual.put(UUID.randomUUID(), UUID.randomUUID());
		actual.put(UUID.randomUUID(), null);
		assertEquals(actual, baseHessian2Serialize(actual));
	}

	@Test
	@EnabledForJreRange(max = JRE.JAVA_11)
	public void testUUIDMapCompact() throws IOException {
		Map<UUID, Object> actual = new HashMap<>(8);
		actual.put(UUID.randomUUID(), UUID.randomUUID());
		actual.put(UUID.randomUUID(), null);
		assertEquals(actual, baseHessian2Serialize(actual));
		assertEquals(actual, hessian3ToHessian3(actual));
		assertEquals(actual, hessian4ToHessian3(actual));
		assertEquals(actual, hessian3ToHessian4(actual));
	}

	@Test
	public void testCollection() throws IOException {
		UUID uuid = UUID.randomUUID();

		List<UUID> list = new ArrayList<>();
		list.add(uuid);
		list.add(uuid);

		List<UUID> result = baseHessian2Serialize(list);

		assertEquals(list.size(), result.size());
		assertEquals(uuid, result.get(0));
		assertEquals(uuid, result.get(1));
		assertEquals(result.get(0), result.get(1));
	}
}
