package com.alibaba.com.caucho.hessian.io;

import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * fix hessian serialize bug:
 * the uuid can not be deserialized properly
 **/
public class Hessian2UUIDTest extends SerializeTestBase {

	@Test
	public void testUUIDObject() throws IOException {
		UUID actual = UUID.randomUUID();
		UUID deserialize = baseHessian2Serialize(actual);
		Assert.assertEquals(actual, deserialize);
	}

	@Test
	public void testUUIDList() throws IOException {
		List<UUID> actual = new ArrayList<>(2);
		actual.add(UUID.randomUUID());
		actual.add(UUID.randomUUID());

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		Hessian2Output out = new Hessian2Output(bout);

		out.writeObject(actual);
		out.flush();

		ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
		Hessian2Input input = new Hessian2Input(bin);

		List<UUID> deserialize = (List) input.readObject();
		assertTrue(deserialize != null);
		assertTrue(deserialize.size() == 2);
		assertEquals(actual, deserialize);
	}

	@Test
	public void testUUIDMap() throws IOException {
		Map<UUID, Object> actual = new HashMap<>(8);
		actual.put(UUID.randomUUID(), UUID.randomUUID());
		actual.put(UUID.randomUUID(), null);

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		Hessian2Output out = new Hessian2Output(bout);

		out.writeObject(actual);
		out.flush();

		ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
		Hessian2Input input = new Hessian2Input(bin);

		Map<UUID, Object> deserialize = (Map<UUID, Object>) input.readObject();
		assertTrue(deserialize != null);
		assertTrue(deserialize.size() == 2);
		assertEquals(actual, deserialize);
	}

}
