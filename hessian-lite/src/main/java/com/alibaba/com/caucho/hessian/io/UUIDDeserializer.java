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

import java.io.IOException;
import java.util.UUID;

/**
 * Deserializing a uuid valued object
 **/
public class UUIDDeserializer extends AbstractDeserializer {

	@Override
	public Class getType() {
		return UUID.class;
	}

	@Override
	public Object readObject(AbstractHessianInput in, Object[] fields) throws IOException {
		String uuidString = in.readString();
		UUID uuid = UUID.fromString(uuidString);

		in.addRef(uuid);
		return uuid;
	}

	@Override
	public Object readObject(AbstractHessianInput in, String[] fieldNames) throws IOException {
		String uuidString = in.readString();
		UUID uuid = UUID.fromString(uuidString);

		in.addRef(uuid);
		return uuid;
	}

}
