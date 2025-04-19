package com.example.checkrepo.service.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> {
    private final Map<K, V> cache;

    public LruCache(int maxSize) {
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> old) {
                return size() > maxSize;
            }
        };
    }

    public V get(K key) {
        return cache.get(key);
    }

    public void putIfAbsent(K key, V value) {
        cache.putIfAbsent(key, value);
    }

    public void remove(K key) {
        cache.remove(key, cache.get(key));
    }

    public void replace(K key, V newValue) {
        V currentValue = cache.get(key);
        if (currentValue == null || !currentValue.equals(newValue)) {
            cache.remove(key, cache.get(key));
            cache.putIfAbsent(key, newValue);
        }
    }

    public Collection<V> getAll() {
        return new ArrayList<>(cache.values());
    }
}
