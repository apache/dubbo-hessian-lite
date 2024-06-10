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
package com.alibaba.com.caucho.hessian.io.socket;

import com.alibaba.com.caucho.hessian.io.AbstractDeserializer;
import com.alibaba.com.caucho.hessian.io.AbstractHessianInput;
import com.alibaba.com.caucho.hessian.io.IOExceptionWrapper;

import java.io.IOException;
import java.net.URI;

public class URIDeserializer extends AbstractDeserializer {
    @Override
    public Object readObject(AbstractHessianInput in,
                             Object[] fields)
            throws IOException {
        try {
            return readObject(in, (String[]) fields);
        } catch (IOException | RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IOExceptionWrapper(URI.class.getName() + ":" + e, e);
        }
    }

    @Override
    public Object readObject(AbstractHessianInput in,
                             String[] fieldNames)
            throws IOException {
        try {
            String string = null;
            for (String fieldName : fieldNames) {
                if ("string".equals(fieldName)) {
                    string = in.readString();
                } else {
                    in.readObject();
                }
            }

            Object obj = URI.create(string);

            in.addRef(obj);

            return obj;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOExceptionWrapper(URI.class.getName() + ":" + e, e);
        }
    }
}
