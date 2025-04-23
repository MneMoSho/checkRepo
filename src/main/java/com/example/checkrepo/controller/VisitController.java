package com.example.checkrepo.controller;

import com.example.checkrepo.service.impl.VisitCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visits/")
public class VisitController {
    private final VisitCounterService visitCounterService;

    @Autowired
    public VisitController(VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    @GetMapping()
    public ResponseEntity<Long> getVisitCount(@RequestParam String url) {
        long count = visitCounterService.getCount(url);
        return ResponseEntity.ok(count);
    }
}
