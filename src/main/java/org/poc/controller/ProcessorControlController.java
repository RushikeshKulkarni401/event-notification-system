package org.poc.controller;

import org.poc.eventing.service.ProcessorManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(
        name = "Processor Management API",
        description = "APIs for managing and monitoring notification processors"
)
@RestController
@RequestMapping("/api/processors")
public class ProcessorControlController {

    private final ProcessorManager processorManager;

    public ProcessorControlController(ProcessorManager processorManager) {
        this.processorManager = processorManager;
    }

    @Operation(
            summary = "Toggle all processors",
            description = "Enables or disables all event processors in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Processors toggled successfully")
    })
    @PostMapping("/toggle")
    public ResponseEntity<String> toggleProcessors() {
        processorManager.toggleAll();
        return ResponseEntity.ok("Processors toggled successfully");
    }

    @Operation(
            summary = "Get processor status",
            description = "Returns the current status (enabled/disabled) of all processors."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Current processor status retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Object.class)
                    )
            )
    })
    @GetMapping("/status")
    public ResponseEntity<?> status() {
        return ResponseEntity.ok(processorManager.getStatus());
    }
}
