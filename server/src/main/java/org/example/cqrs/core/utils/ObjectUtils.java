package org.example.cqrs.core.utils;

public final class ObjectUtils {
    private ObjectUtils(){}

    public static boolean equalsAny(Object base, Object... others){
        if(base == null) return false;

        for (Object other : others) {
            if(base.equals(other)) return true;
        }
        return false;
    }
}
