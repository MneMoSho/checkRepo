package com.example.checkrepo.service.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> {
    private final Map<K, V> cache;

    public LruCache(int max) {
        this.cache = new LinkedHashMap<>(max, 0.75f, true);

    }

    public V get(K key) {
        return cache.get(key);
    }

    public void putIfAbsent(K key, V value) {
        cache.putIfAbsent(key, value);
    }

    public void remove(K key) {
        cache.remove(key);
    }

    public void replace(K key, V newValue) {
        V currentValue = cache.get(key);
        if (currentValue == null || !currentValue.equals(newValue)) {
            cache.remove(key, cache.get(key));
            cache.putIfAbsent(key, newValue);
        }
    }

    public Collection<V> getAll() {
        System.out.println("from cache");
        return new ArrayList<>(cache.values());
    }
}
