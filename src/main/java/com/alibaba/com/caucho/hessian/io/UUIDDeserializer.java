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
    public Object readObject(AbstractHessianInput in) throws IOException {
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
