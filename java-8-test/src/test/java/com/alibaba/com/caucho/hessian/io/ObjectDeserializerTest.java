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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectDeserializerTest {

    @Test
    public void deserializeUntypedMapIntoGenericInterfaceField() throws Exception {
        HashMap<Long, String> module = new HashMap<Long, String>();
        module.put(1L, "value");
        ResultDTO<HashMap<Long, String>> original = new ResultDTO<HashMap<Long, String>>(module);

        ResultDTO<?> result = (ResultDTO<?>) roundTrip(original, ResultDTO.class);

        Assertions.assertEquals(module, result.module);
    }

    @Test
    public void deserializeEmptyUntypedMapIntoGenericInterfaceField() throws Exception {
        HashMap<Long, String> module = new HashMap<Long, String>();
        ResultDTO<HashMap<Long, String>> original = new ResultDTO<HashMap<Long, String>>(module);

        ResultDTO<?> result = (ResultDTO<?>) roundTrip(original, ResultDTO.class);

        Assertions.assertEquals(module, result.module);
    }

    @Test
    public void deserializeUntypedMapIntoCloneableField() throws Exception {
        HashMap<Long, String> module = new HashMap<Long, String>();
        module.put(1L, "value");
        CloneableDTO original = new CloneableDTO(module);

        CloneableDTO result = (CloneableDTO) roundTrip(original, CloneableDTO.class);

        Assertions.assertEquals(module, result.module);
    }

    @Test
    public void deserializeUntypedMapIntoInterfaceWithExpectedTypes() throws Exception {
        HashMap<String, Short> original = new HashMap<String, Short>();
        original.put("key", Short.valueOf((short) 1));

        byte[] serialized = serialize(original);
        Assertions.assertEquals((byte) 'H', serialized[0]);

        HashMap<?, ?> result = (HashMap<?, ?>) deserialize(
                serialized, Serializable.class, String.class, Short.class);

        Assertions.assertEquals(original, result);
        Assertions.assertEquals(Short.class, result.get("key").getClass());
    }

    @Test
    public void deserializeUntypedMapRejectsIncompatibleInterface() throws Exception {
        HashMap<Long, String> original = new HashMap<Long, String>();
        original.put(1L, "value");

        try {
            deserialize(serialize(original), Runnable.class);
            Assertions.fail("HashMap must not be returned as Runnable");
        } catch (HessianProtocolException e) {
            Assertions.assertTrue(e.getMessage().contains(HashMap.class.getName()));
            Assertions.assertTrue(e.getMessage().contains(Runnable.class.getName()));
        }
    }

    @Test
    public void deserializeUntypedMapIntoInterfaceUsesRegisteredMapDeserializer() throws Exception {
        HashMap<Long, String> original = new HashMap<Long, String>();
        original.put(1L, "value");
        SerializerFactory serializerFactory = serializerFactoryWithMapType(LinkedHashMap.class);

        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(serialize(original)));
        input.setSerializerFactory(serializerFactory);
        Object result = input.readObject(Serializable.class);

        Assertions.assertEquals(LinkedHashMap.class, result.getClass());
        Assertions.assertEquals(original, result);
    }

    @Test
    public void deserializeUntypedMapIntoInterfaceAllowsNullFromRegisteredMapDeserializer() throws Exception {
        HashMap<Long, String> original = new HashMap<Long, String>();
        original.put(1L, "value");

        SerializerFactory serializerFactory = serializerFactoryWithMapDeserializer(new MapDeserializer(Map.class) {
            @Override
            public Object readMap(AbstractHessianInput in, Class<?> expectKeyType, Class<?> expectValueType)
                    throws IOException {
                super.readMap(in, expectKeyType, expectValueType);
                return null;
            }
        });

        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(serialize(original)));
        input.setSerializerFactory(serializerFactory);

        Assertions.assertNull(input.readObject(Serializable.class));
    }

    @Test
    public void deserializeUntypedMapRejectsSelfDelegatingRegisteredMapDeserializer() throws Exception {
        HashMap<Long, String> original = new HashMap<Long, String>();
        original.put(1L, "value");

        SerializerFactory serializerFactory = serializerFactoryWithMapDeserializer(new ObjectDeserializer(Map.class));

        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(serialize(original)));
        input.setSerializerFactory(serializerFactory);

        try {
            input.readObject(Serializable.class);
            Assertions.fail("A Map deserializer must not delegate to itself");
        } catch (HessianProtocolException e) {
            Assertions.assertTrue(e.getMessage().contains("cannot delegate Map deserialization to itself"));
        }
    }

    @Test
    public void deserializeUntypedMapIntoCustomInterfaceUsesCompatibleRegisteredMapType() throws Exception {
        HashMap<Long, String> original = new HashMap<Long, String>();
        original.put(1L, "value");
        SerializerFactory serializerFactory = serializerFactoryWithMapType(InterfaceMap.class);

        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(serialize(original)));
        input.setSerializerFactory(serializerFactory);
        Object result = input.readObject(MapMarker.class);

        Assertions.assertEquals(InterfaceMap.class, result.getClass());
        Assertions.assertEquals(original, result);
    }

    @Test
    public void deserializeHessian2MapWithEmptyTypeIntoInterface() throws Exception {
        HashMap<Long, String> expected = new HashMap<Long, String>();
        expected.put(1L, "value");
        byte[] untypedMap = serialize(expected);
        byte[] emptyTypeMap = new byte[untypedMap.length + 1];
        emptyTypeMap[0] = (byte) 'M';
        emptyTypeMap[1] = 0;
        System.arraycopy(untypedMap, 1, emptyTypeMap, 2, untypedMap.length - 1);

        Assertions.assertEquals(expected, deserialize(emptyTypeMap, Serializable.class));
    }

    @Test
    public void deserializeUntypedMapIntoInterfacePreservesSelfReference() throws Exception {
        HashMap<String, Object> original = new HashMap<String, Object>();
        original.put("self", original);

        Map<?, ?> result = (Map<?, ?>) roundTrip(original, Serializable.class);

        Assertions.assertSame(result, result.get("self"));
    }

    @Test
    public void deserializeArrayListIntoSerializableInterfaceIsUnchanged() throws Exception {
        ArrayList<String> original = new ArrayList<String>();
        original.add("value");

        Object result = roundTrip(original, Serializable.class);

        Assertions.assertEquals(ArrayList.class, result.getClass());
        Assertions.assertEquals(original, result);
    }

    @Test
    public void deserializeObjectIntoOrdinaryInterfaceIsUnchanged() throws Exception {
        ModuleImpl original = new ModuleImpl("value");

        Module result = (Module) roundTrip(original, Module.class);

        Assertions.assertEquals(ModuleImpl.class, result.getClass());
        Assertions.assertEquals(original.value, ((ModuleImpl) result).value);
    }

    @Test
    public void deserializeUntypedMapIntoDefaultMapTypesIsUnchanged() throws Exception {
        HashMap<Long, String> original = new HashMap<Long, String>();
        original.put(1L, "value");

        for (Class<?> expectedType : new Class<?>[] {Object.class, Map.class, HashMap.class}) {
            Object result = roundTrip(original, expectedType);
            Assertions.assertEquals(HashMap.class, result.getClass());
            Assertions.assertEquals(original, result);
        }
    }

    @Test
    public void deserializeUntypedMapIntoInterfacesPreservesSharedReference() throws Exception {
        HashMap<Long, String> original = new HashMap<Long, String>();
        original.put(1L, "value");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(bytes);
        output.writeObject(original);
        output.writeObject(original);
        output.flush();

        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes.toByteArray()));
        Object first = input.readObject(Serializable.class);
        Object second = input.readObject(Cloneable.class);

        Assertions.assertEquals(original, first);
        Assertions.assertSame(first, second);
    }

    @Test
    public void deserializeNullIntoInterfaceIsUnchanged() throws Exception {
        Assertions.assertNull(roundTrip(null, Serializable.class));
    }

    private Object roundTrip(Object original, Class<?> expectedClass, Class<?>... expectedTypes) throws Exception {
        return deserialize(serialize(original), expectedClass, expectedTypes);
    }

    private byte[] serialize(Object original) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(outputStream);
        output.writeObject(original);
        output.flush();

        return outputStream.toByteArray();
    }

    private Object deserialize(byte[] serialized, Class<?> expectedClass, Class<?>... expectedTypes) throws Exception {
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(serialized));
        return input.readObject(expectedClass, expectedTypes);
    }

    private SerializerFactory serializerFactoryWithMapType(final Class<? extends Map> mapType) {
        return serializerFactoryWithMapDeserializer(new MapDeserializer(mapType));
    }

    private SerializerFactory serializerFactoryWithMapDeserializer(final Deserializer mapDeserializer) {
        SerializerFactory serializerFactory = new SerializerFactory();
        serializerFactory.addFactory(new AbstractSerializerFactory() {
            @Override
            public Serializer getSerializer(Class cl) {
                return null;
            }

            @Override
            public Deserializer getDeserializer(Class cl) {
                if (Map.class.equals(cl)) {
                    return mapDeserializer;
                }
                return null;
            }
        });
        return serializerFactory;
    }

    private static class ResultDTO<T extends Serializable> implements Serializable {
        private static final long serialVersionUID = 1L;

        private T module;

        private ResultDTO() {
        }

        private ResultDTO(T module) {
            this.module = module;
        }
    }

    private static class CloneableDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private Cloneable module;

        private CloneableDTO() {
        }

        private CloneableDTO(Cloneable module) {
            this.module = module;
        }
    }

    private interface Module extends Serializable {
    }

    public interface MapMarker extends Serializable {
    }

    public static class InterfaceMap extends HashMap<Long, String> implements MapMarker {
        private static final long serialVersionUID = 1L;
    }

    private static class ModuleImpl implements Module {
        private static final long serialVersionUID = 1L;

        private String value;

        private ModuleImpl() {
        }

        private ModuleImpl(String value) {
            this.value = value;
        }
    }
}
