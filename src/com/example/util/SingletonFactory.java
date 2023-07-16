package com.example.util;
import java.util.HashMap;
import java.util.Map;

public class SingletonFactory {
    private static Map<Class<?>, Object> instances = new HashMap<>();

    private SingletonFactory() {
    }

    public static synchronized <T> T getInstance(Class<T> clazz) {
        if (!instances.containsKey(clazz)) {
            try {
                T instance = clazz.getDeclaredConstructor().newInstance();
                instances.put(clazz, instance);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return clazz.cast(instances.get(clazz));
    }
}
