package myrzakhan_taskflow.cache.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.dtos.event.CacheEvent;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisEventHandler {

    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;

    public void handleEvent(String message) {
        try {
            var event = objectMapper.readValue(message, CacheEvent.class);
            var cache = cacheManager.getCache(event.cacheName());

            if (cache == null) {
                log.warn("Cache not found for entity: {}", event.cacheName());
                return;
            }

            switch (event.eventType()) {
                case CREATE, UPDATE:
                    if (event.value() != null) {
                        cache.put(event.key(), event.value());
                        break;
                    } else {
                        log.warn("Event with key {} is null", event.key());
                    }
                case EVICT:
                    cache.evict(event.key());
                    break;
                default:
                    log.warn("Unknown event type: {}", event.eventType());
            }
        } catch (Exception e) {
            log.error("Error processing event: {}", e.getMessage());
        }
    }
}