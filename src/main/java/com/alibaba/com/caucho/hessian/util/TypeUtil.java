package com.alibaba.com.caucho.hessian.util;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class TypeUtil {

    private TypeUtil() {
        throw new AssertionError();
    }

    public static boolean isByteOrShortClass(Type type) {
        if (type == null) {
            return false;
        }

        return type == byte.class || type == Byte.class || type == short.class || type == Short.class;
    }

    public static Type[] extractActualArgumentTypes(Type genericType) {
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return parameterizedType.getActualTypeArguments();
        }

        return null;
    }

    public static Type extractActualArgumentType(Type genericType) {
        Type[] types = extractActualArgumentTypes(genericType);
        if (types != null && types.length == 1) {
            return types[0];
        }

        return null;
    }

}
