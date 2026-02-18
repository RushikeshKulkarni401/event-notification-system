package org.poc;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.concurrent.*;

@Slf4j
public abstract class AbstractEventProcessor implements EventProcessor {

    private final BlockingQueue<EventTask> queue = new LinkedBlockingQueue<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final CallbackService callbackService;

    @Getter
    private volatile boolean paused = false;

    protected AbstractEventProcessor(CallbackService callbackService) {
        this.callbackService = callbackService;
    }

    @PostConstruct
    public void start() {
        executor.submit(this::runTask);
    }

    @Override
    public void submit(EventTask task) {
        if (paused) {
            throw new RuntimeException("Event submission rejected because the system is temporarily paused.");
        }
        queue.offer(task);
        log.info("{} event queued: {}", getSupportedType(), task.getEventId());
    }

    private void runTask() {
        while (!queue.isEmpty()) {
            try {
                EventTask task = queue.poll(1, TimeUnit.SECONDS);
                if (task == null) continue;

                String processedAt;
                try {
                    process(task);

                    processedAt = Instant.now().toString();
                    CallbackRequest success = CallbackRequest.builder()
                            .eventId(task.getEventId())
                            .eventType(getSupportedType())
                            .status("COMPLETED")
                            .processedAt(processedAt)
                            .build();

                    callbackService.sendCallback(task.getRequest().getCallbackUrl(), success);

                    log.info("{} event completed: {}",
                            getSupportedType(),
                            task.getEventId());

                } catch (Exception e) {
                    processedAt = Instant.now().toString();

                    CallbackRequest failure = CallbackRequest.builder()
                            .eventId(task.getEventId())
                            .eventType(getSupportedType())
                            .status("FAILED")
                            .errorMessage(e.getMessage())
                            .processedAt(processedAt)
                            .build();

                    callbackService.sendCallback(task.getRequest().getCallbackUrl(), failure);

                    log.error("{} processing failed for {}: {}",
                            getSupportedType(),
                            task.getEventId(),
                            e.getMessage());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        log.info("{} worker stopped", getSupportedType());
    }

    protected abstract void process(EventTask task);

    protected void simulateDelay(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void simulateRandomFailure(double threshold) {
        if (Math.random() < threshold) {
            throw new RuntimeException("Simulated failure");
        }
    }


    public void toggle() {
        paused = !paused;
        if (paused) {
            log.info("{} processor PAUSED", getSupportedType());
        } else {
            log.info("{} processor RESUMED", getSupportedType());
        }
    }

    @PreDestroy
    @Override
    public void shutdown() {
        executor.shutdown();
    }
}
