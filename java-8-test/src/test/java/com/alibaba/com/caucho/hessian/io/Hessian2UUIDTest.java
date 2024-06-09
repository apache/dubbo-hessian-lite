package com.alibaba.com.caucho.hessian.io;

import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;
import org.junit.jupiter.api.Disabled;
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
	@Disabled("UUID should support com.alibaba.com.caucho.hessian.io.uuid.BadExampleDTO")
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
	@Disabled("UUID should support com.alibaba.com.caucho.hessian.io.uuid.BadExampleDTO")
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
	@Disabled("UUID should support com.alibaba.com.caucho.hessian.io.uuid.BadExampleDTO")
	public void testUUIDMapCompact() throws IOException {
		Map<UUID, Object> actual = new HashMap<>(8);
		actual.put(UUID.randomUUID(), UUID.randomUUID());
		actual.put(UUID.randomUUID(), null);
		assertEquals(actual, baseHessian2Serialize(actual));
		assertEquals(actual, hessian3ToHessian3(actual));
		assertEquals(actual, hessian4ToHessian3(actual));
		assertEquals(actual, hessian3ToHessian4(actual));
	}

}
