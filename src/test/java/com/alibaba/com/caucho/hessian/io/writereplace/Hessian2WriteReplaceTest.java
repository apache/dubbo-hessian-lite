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

package com.alibaba.com.caucho.hessian.io.writereplace;

import static org.junit.Assert.assertEquals;

import com.alibaba.com.caucho.hessian.io.base.SerializeTestBase;
import java.io.Serializable;
import org.junit.Test;


public class Hessian2WriteReplaceTest extends SerializeTestBase {

  @Test
  public void testWriteReplaceReturningItself() throws Exception {
    String someName = "some name";
    WriteReplaceReturningItself object = new WriteReplaceReturningItself(someName);

    WriteReplaceReturningItself result = baseHessian2Serialize(object);

    assertEquals(someName, result.getName());
  }

  @Test
  public void testNormalWriteReplace() throws Exception {
    String someFirstName = "first";
    String someLastName = "last";
    String someAddress = "some address";

    NormalWriteReplace object = new NormalWriteReplace(someFirstName, someLastName, someAddress);

    NormalWriteReplace result = baseHessian2Serialize(object);

    assertEquals(someFirstName, result.getFirstName());
    assertEquals(someLastName, result.getLastName());
    assertEquals(someAddress, result.getAddress());
  }

  static class WriteReplaceReturningItself implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    WriteReplaceReturningItself(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    /**
     * Some object may return itself for wrapReplace, e.g.
     * https://github.com/FasterXML/jackson-databind/blob/master/src/main/java/com/fasterxml/jackson/databind/JsonMappingException.java#L173
     */
    Object writeReplace() {
      //do some extra things

      return this;
    }
  }

  static class NormalWriteReplace implements Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String address;

    public NormalWriteReplace(String firstName, String lastName, String address) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.address = address;
    }

    public String getFirstName() {
      return firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public String getAddress() {
      return address;
    }

    //return a proxy to save space for serialization
    Object writeReplace() {
      return new NormalWriteReplaceProxy(this);
    }
  }

  static class NormalWriteReplaceProxy implements Serializable {
    private static final long serialVersionUID = 1L;

    private String data;

    //empty constructor for deserialization
    public NormalWriteReplaceProxy() {
    }

    public NormalWriteReplaceProxy(NormalWriteReplace normalWriteReplace) {
      this.data = normalWriteReplace.getFirstName() + "," + normalWriteReplace.getLastName() + "," + normalWriteReplace
          .getAddress();
    }

    //construct the actual object
    Object readResolve() {
      String[] parts = data.split(",");

      return new NormalWriteReplace(parts[0], parts[1], parts[2]);
    }
  }
}
