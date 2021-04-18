package com.github.alesaudate.demostockexchange.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MapBuilder<K,V> {

    private final Map<K,V> backingMap;


    public static <K,V> MapBuilder<K,V> map() {
        return new MapBuilder<>(new HashMap<>());
    }

    public MapBuilder entry(K key, V value) {
        var newInstance = new MapBuilder<>(new HashMap<>(this.backingMap));
        newInstance.backingMap.put(key, value);
        return newInstance;
    }

    public MapBuilder entry(K key, Function<K, V> supplierFunction) {
        V value = supplierFunction.apply(key);
        return entry(key, value);
    }

    public Map<K, V> build() {
        return new HashMap<>(backingMap);
    }
}
