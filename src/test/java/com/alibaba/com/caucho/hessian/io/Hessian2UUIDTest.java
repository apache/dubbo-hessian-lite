package com.alibaba.com.caucho.hessian.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

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
        List<UUID> actual = new ArrayList<>(8);
        UUID fixedUuid = UUID.randomUUID();
        actual.add(fixedUuid);
        actual.add(UUID.randomUUID());
        actual.add(fixedUuid);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bout);
        out.writeObject(actual);
        out.flush();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        Hessian2Input input = new Hessian2Input(bin);
        List<?> deserialize = (List<?>) input.readObject();

        assertNotNull(deserialize);
        assertEquals(deserialize.size(), actual.size());
        assertEquals(actual, deserialize);
    }

    @Test
    public void testUUIDMap() throws IOException {
        Map<UUID, Object> actual = new HashMap<>(8);
        UUID fixedUuid = UUID.randomUUID();
        actual.put(UUID.randomUUID(), fixedUuid);
        actual.put(UUID.randomUUID(), null);
        actual.put(UUID.randomUUID(), fixedUuid);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bout);
        out.writeObject(actual);
        out.flush();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        Hessian2Input input = new Hessian2Input(bin);
        Map<?, ?> deserialize = (Map<?, ?>) input.readObject();

        assertNotNull(deserialize);
        assertEquals(deserialize.size(), actual.size());
        assertEquals(actual, deserialize);
    }

}
