/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
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

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.collection.internal.PersistentSet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Public-API round-trip tests for Hibernate collection conversion and iterators.
 */
public class HibernateCollectionRegressionTest {
    private static SessionFactory sessions;

    @BeforeAll
    static void createDatabase() {
        sessions = new Configuration()
                .addAnnotatedClass(PurchaseOrder.class)
                .addAnnotatedClass(OrderItem.class)
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:hessian_hibernate_regression;DB_CLOSE_DELAY=-1")
                .setProperty("hibernate.connection.username", "sa")
                .setProperty("hibernate.connection.password", "")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                .setProperty("hibernate.show_sql", "false")
                .setProperty("hibernate.connection.pool_size", "1")
                .buildSessionFactory();
        try (Session session = sessions.openSession()) {
            Transaction transaction = session.beginTransaction();
            PurchaseOrder populated = new PurchaseOrder(1L);
            populated.orderItemSet.add(new OrderItem(11L, "first"));
            populated.orderItemSet.add(new OrderItem(12L, "second"));
            session.persist(populated);
            transaction.commit();
        }
    }

    @AfterAll
    static void closeDatabase() {
        if (sessions != null) {
            sessions.close();
        }
    }

    @Test
    public void eagerEntityCopiedToHashSetRoundTrips() throws IOException {
        PurchaseOrder order = detachedOrder(1L, 2);
        order.orderItemSet = new HashSet<OrderItem>(order.orderItemSet);
        List<PurchaseOrder> restored = roundTripOrders(order, true);
        assertEquals(new TreeSet<String>(Arrays.asList("first", "second")), names(restored.get(0).orderItemSet));
    }

    @Test
    public void eagerEntityWithCollectionTypeSuppressedRoundTrips() throws IOException {
        PurchaseOrder order = detachedOrder(1L, 2);
        List<PurchaseOrder> restored = roundTripOrders(order, false);
        assertEquals(new TreeSet<String>(Arrays.asList("first", "second")), names(restored.get(0).orderItemSet));
    }

    @Test
    public void initializedPersistentSetIteratorRoundTripsAsList() throws IOException {
        PurchaseOrder order = detachedOrder(1L, 2);
        byte[] bytes = encode(order.orderItemSet.iterator(), true);
        Hessian2Input in = new Hessian2Input(new ByteArrayInputStream(bytes));
        try {
            List<OrderItem> restored = (List<OrderItem>) in.readObject();
            assertEquals(new TreeSet<String>(Arrays.asList("first", "second")), names(restored));
        } finally {
            in.close();
        }
    }

    private static PurchaseOrder detachedOrder(long id, int itemCount) {
        PurchaseOrder order;
        try (Session session = sessions.openSession()) {
            order = session.get(PurchaseOrder.class, id);
            assertTrue(order.orderItemSet instanceof PersistentSet);
            assertTrue(Hibernate.isInitialized(order.orderItemSet));
        }
        // Check after the provider session closes: serialization starts with initialized data.
        assertEquals(itemCount, order.orderItemSet.size());
        System.out.println("provider: collection=" + order.orderItemSet.getClass().getName()
                + ", initialized=" + Hibernate.isInitialized(order.orderItemSet)
                + ", size=" + itemCount + ", sessionClosed=true");
        return order;
    }

    private static List<PurchaseOrder> roundTripOrders(PurchaseOrder order, boolean sendCollectionType)
            throws IOException {
        List<PurchaseOrder> result = new ArrayList<PurchaseOrder>();
        result.add(order);
        byte[] bytes = encode(result, sendCollectionType);
        Hessian2Input in = new Hessian2Input(new ByteArrayInputStream(bytes));
        try {
            return (List<PurchaseOrder>) in.readObject();
        } finally {
            in.close();
        }
    }

    private static byte[] encode(Object value, boolean sendCollectionType) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(buffer);
        if (!sendCollectionType) {
            SerializerFactory factory = new SerializerFactory();
            factory.setSendCollectionType(false);
            out.setSerializerFactory(factory);
        }
        out.writeObject(value);
        out.flush();
        byte[] bytes = buffer.toByteArray();
        System.out.println("encoded: bytes=" + bytes.length + ", containsPersistentSetType="
                + new String(bytes, StandardCharsets.ISO_8859_1).contains(PersistentSet.class.getName()));
        return bytes;
    }

    private static Set<String> names(Collection<OrderItem> items) {
        Set<String> names = new TreeSet<String>();
        for (OrderItem item : items) {
            names.add(item.name);
        }
        assertEquals(2, items.size());
        return names;
    }

    @Entity(name = "HessianRegressionOrder")
    @Table(name = "hessian_regression_order")
    public static class PurchaseOrder implements Serializable {
        @Id
        private Long id;

        @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinColumn(name = "order_id")
        private Set<OrderItem> orderItemSet = new HashSet<OrderItem>();

        public PurchaseOrder() {
        }

        public PurchaseOrder(Long id) {
            this.id = id;
        }
    }

    @Entity(name = "HessianRegressionOrderItem")
    @Table(name = "hessian_regression_order_item")
    public static class OrderItem implements Serializable {
        @Id
        private Long id;
        private String name;

        public OrderItem() {
        }

        public OrderItem(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
