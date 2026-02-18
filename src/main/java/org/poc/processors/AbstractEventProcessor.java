package org.poc.processors;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.poc.callback.CallbackRequest;
import org.poc.callback.CallbackService;
import org.poc.eventing.model.EventTask;
import org.poc.exception.EventingException;

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
    public void submit(EventTask task) throws EventingException {
        if (paused) {
            throw new EventingException("Event submission rejected because the system is temporarily paused.");
        }
        queue.offer(task);
        log.info("{} event queued: {}", getSupportedType(), task.getEventId());
    }

    private void runTask() {
        while (true) {
            try {
                EventTask task = queue.poll(1, TimeUnit.SECONDS);
                if (task == null) continue;

                String processedAt;
                try {
                    process(task);
                    processedAt = Instant.now().toString();

                    handleSuccessCallback(task, processedAt);
                    log.info("{} event completed: {}",
                            getSupportedType(),
                            task.getEventId());

                } catch (Exception e) {
                    processedAt = Instant.now().toString();

                    handleFailureCallback(e, task, processedAt);
                    log.error("{} processing failed for {}: {}",
                            getSupportedType(),
                            task.getEventId(),
                            e.getMessage());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    protected abstract void process(EventTask task);

    public void simulateDelay(Long seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void simulateRandomFailure(double threshold) {
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

    private void handleFailureCallback(Exception e, EventTask task, String processedAt) {
        CallbackRequest failure = CallbackRequest.builder()
                .eventId(task.getEventId())
                .eventType(getSupportedType())
                .status("FAILED")
                .errorMessage(e.getMessage())
                .processedAt(processedAt)
                .build();

        callbackService.sendCallback(task.getRequest().getCallbackUrl(), failure);
    }

    private void handleSuccessCallback(EventTask task, String processedAt) {
        CallbackRequest success = CallbackRequest.builder()
                .eventId(task.getEventId())
                .eventType(getSupportedType())
                .status("COMPLETED")
                .processedAt(processedAt)
                .build();

        callbackService.sendCallback(task.getRequest().getCallbackUrl(), success);
    }

    @PreDestroy
    @Override
    public void shutdown() {
        log.info("Shutting down executors");
        executor.shutdown();
    }

}
