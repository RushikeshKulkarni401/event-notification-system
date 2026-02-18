package org.poc.controller;

import org.poc.eventing.service.ProcessorManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/processors")
public class ProcessorControlController {

    private final ProcessorManager processorManager;

    public ProcessorControlController(ProcessorManager processorManager) {
        this.processorManager = processorManager;
    }

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleProcessors() {
        processorManager.toggleAll();
        return ResponseEntity.ok("Processors toggled successfully");
    }

    @GetMapping("/status")
    public ResponseEntity<?> status() {
        return ResponseEntity.ok(processorManager.getStatus());
    }
}
