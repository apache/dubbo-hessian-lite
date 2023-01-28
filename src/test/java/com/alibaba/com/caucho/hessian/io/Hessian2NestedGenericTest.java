package com.alibaba.com.caucho.hessian.io;

import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;
import com.alibaba.com.caucho.hessian.io.beans.Hessian2NestedGenericType;
import com.alibaba.com.caucho.hessian.io.type.TypeReference;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.*;

public class Hessian2NestedGenericTest extends SerializeTestBase {

    @Test
    public void nestedGenericSerializationAndDeserializationTest() throws Exception {
        Hessian2NestedGenericType hessian2NestedGenericType = new Hessian2NestedGenericType();
        List<List<Short>> shortListList = new ArrayList<>();
        List<Short> shortList = new ArrayList<>();
        shortList.add((short) 1);
        shortListList.add(shortList);
        hessian2NestedGenericType.setShortListList(shortListList);

        Hessian2NestedGenericType deserializedObject = baseHessian2Serialize(hessian2NestedGenericType);
        assertTrue(deserializedObject.getShortListList() != null);
        assertTrue(deserializedObject.getShortListList().size() == hessian2NestedGenericType.getShortListList().size());
        assertTrue(deserializedObject.getShortListList().get(0).get(0) instanceof Short);
        assertEquals((short) 1, (short) deserializedObject.getShortListList().get(0).get(0));
    }

    @Test
    public void twoLevelNestedListSerializationAndDeserializationTest() throws Exception {
        List<List<Short>> shortListList = new ArrayList<>();
        List<Short> shortList = new ArrayList<>();
        shortList.add((short) 1);
        shortList.add((short) 2);
        shortListList.add(shortList);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bout);

        out.writeObject(shortListList);
        out.flush();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        Hessian2Input input = new Hessian2Input(bin);
        List<List<Short>> deserializedObject = (List) input.readObject(List.class, new TypeReference<List<List<Short>>>() {
        }.getType());
        assertNotNull(deserializedObject);
        assertEquals(1, deserializedObject.size());
        assertTrue(deserializedObject.get(0) instanceof List);
        assertEquals(2, deserializedObject.get(0).size());
        assertTrue(deserializedObject.get(0).get(0) instanceof Short);
        assertTrue(deserializedObject.get(0).get(1) instanceof Short);
        assertEquals((short) 1, (short) deserializedObject.get(0).get(0));
        assertEquals((short) 2, (short) deserializedObject.get(0).get(1));
    }

    @Test
    public void multiLevelNestedListSerializationAndDeserializationTest() throws IOException {
        List<List<List<Short>>> shortListListList = new ArrayList<>();
        List<List<Short>> shortListList = new ArrayList<>();
        List<Short> shortList = new ArrayList<>();
        shortList.add((short) 1);
        shortList.add((short) 2);
        shortListList.add(shortList);
        shortListListList.add(shortListList);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bout);

        out.writeObject(shortListListList);
        out.flush();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        Hessian2Input input = new Hessian2Input(bin);
        List<List<List<Short>>> deserializedObject = (List) input.readObject(List.class, new TypeReference<List<List<List<Short>>>>() {
        }.getType());
        assertNotNull(deserializedObject);
        assertEquals(1, deserializedObject.size());
        assertTrue(deserializedObject.get(0) instanceof List);
        assertEquals(1, deserializedObject.get(0).size());
        assertTrue(deserializedObject.get(0).get(0) instanceof List);

        assertEquals(2, deserializedObject.get(0).get(0).size());
        assertTrue(deserializedObject.get(0).get(0).get(0) instanceof Short);
        assertTrue(deserializedObject.get(0).get(0).get(1) instanceof Short);
        assertEquals((short) 1, (short) deserializedObject.get(0).get(0).get(0));
        assertEquals((short) 2, (short) deserializedObject.get(0).get(0).get(1));
    }

    @Test
    public void multiLevelNestedMapSerializationAndDeserializationTest() throws IOException {
        Map<Map<Byte, Short>, Map<Short, Byte>> byteToShortMapToShortToByteMap = new HashMap<>();
        Map<Byte, Short> byteToShortMap = new HashMap<>();
        byteToShortMap.put((byte) 1, (short) 2);
        Map<Short, Byte> shortToByteMap = new HashMap<>();
        shortToByteMap.put((short) 3, (byte) 4);
        byteToShortMapToShortToByteMap.put(byteToShortMap, shortToByteMap);

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bout);

        out.writeObject(byteToShortMapToShortToByteMap);
        out.flush();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        Hessian2Input input = new Hessian2Input(bin);
        input.findSerializerFactory().setAllowNonSerializable(true);
        Map<Map, Map> deserializedObject = (Map) input.readObject(Map.class, new TypeReference<Map<Map<Byte, Short>, Map<Short, Byte>>>() {
        }.getType());
        assertNotNull(deserializedObject);
        assertEquals(1, deserializedObject.size());
        Object keyMap = deserializedObject.entrySet().iterator().next().getKey();
        Object valueMap = deserializedObject.entrySet().iterator().next().getValue();
        assertTrue(keyMap instanceof Map);
        assertTrue(valueMap instanceof Map);
        assertEquals(1, ((Map) keyMap).size());
        assertEquals(1, ((Map) valueMap).size());

        Object key1 = ((Map) keyMap).keySet().iterator().next();
        Object value1 = ((Map) keyMap).values().iterator().next();
        assertTrue(key1 instanceof Byte);
        assertTrue(value1 instanceof Short);

        Object key2 = ((Map) valueMap).keySet().iterator().next();
        Object value2 = ((Map) valueMap).values().iterator().next();
        assertTrue(key2 instanceof Short);
        assertTrue(value2 instanceof Byte);

        assertEquals((byte) 1, (byte) key1);
        assertEquals((short) 2, (short) value1);
        assertEquals((short) 3, (short) key2);
        assertEquals((byte) 4, (byte) value2);
    }

}
