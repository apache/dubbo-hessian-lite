/*
 * Copyright (c) 2001-2004 Caucho Technology, Inc.  All rights reserved.
 *
 * The Apache Software License, Version 1.1
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Caucho Technology (http://www.caucho.com/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Burlap", "Resin", and "Caucho" must not be used to
 *    endorse or promote products derived from this software without prior
 *    written permission. For written permission, please contact
 *    info@caucho.com.
 *
 * 5. Products derived from this software may not be called "Resin"
 *    nor may "Resin" appear in their names without prior written
 *    permission of Caucho Technology.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL CAUCHO TECHNOLOGY OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Scott Ferguson
 */

package com.alibaba.com.caucho.hessian.io;

import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

/**
 * InputStream类型返序列化
 * Serializing a stream object.
 */
public class InputStreamDeserializer extends AbstractDeserializer {
    public static final InputStreamDeserializer DESER
            = new InputStreamDeserializer();

    // TODO 允许通过外部配置
    @SuppressWarnings("FieldCanBeLocal")
    private final int bufferSize = Hessian2Output.SIZE;
    // TODO 允许通过外部配置
    private final File tmpDir;

    public InputStreamDeserializer() {
        tmpDir = new File(System.getProperty("java.io.tmpdir"), "dubbo-"+ System.currentTimeMillis());
        if(!tmpDir.exists()){
            //noinspection ResultOfMethodCallIgnored
            tmpDir.mkdirs();
        }
    }

    public Object readObject(AbstractHessianInput in) throws IOException {

        FileOutputStream out = null;
        try {
            @SuppressWarnings("resource")
            InputStream input = in.readInputStream();
            // 读取配置的2倍字节（16k）
            byte[] bytes = new byte[bufferSize * 2];
            File file = null;
            while (true) { // 循环读取

                int len = input.read(bytes, 0, bytes.length);

                if (out == null) {

                    // 如果InputStream的长度小于缓存，则创建字节流返回
                    if (len <= bufferSize) {
                        byte[] buff = new byte[len];
                        System.arraycopy(bytes, 0, buff, 0, len);
                        return new ByteArrayInputStream(buff);
                    }

                    // 如果InputStream的长度大于缓存，则创建临时文件返回
                    String name = String.format("%d-%s.dubbo.tmp", System.currentTimeMillis(), UUID.randomUUID().toString().replace("-", ""));
                    file = new File(tmpDir, name);
                    // 在 finally中关闭流
                    //noinspection resource
                    out = new FileOutputStream(file);
                }

                // 读取到末尾时退出
                if (len == -1) {
                    break;
                }
                out.write(bytes, 0 ,len);
            }

            out.flush();
            return Files.newInputStream(file.toPath());
        } finally {
            if (out != null){
                out.close();
            }
        }
    }

}
