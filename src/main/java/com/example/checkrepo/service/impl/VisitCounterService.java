package com.example.checkrepo.service.impl;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class VisitCounterService {

    private final Map<String, AtomicLong> urlCounterMap = new ConcurrentHashMap<>();

    public void incrementCount(String url) {
        urlCounterMap.computeIfAbsent(url, k -> new AtomicLong(0)).incrementAndGet();
    }

    public long getCount(String url) {
        AtomicLong counter = urlCounterMap.get(url);
        return counter != null ? counter.get() : 0;
    }
}
