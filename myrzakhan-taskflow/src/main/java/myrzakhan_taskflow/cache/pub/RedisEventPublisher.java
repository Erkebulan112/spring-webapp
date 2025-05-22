package myrzakhan_taskflow.cache.pub;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.dtos.event.CacheEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisEventPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CACHE_EVENT_CHANNEL = "cache:events";

    public void publishCacheEvent(CacheEvent cacheEvent) {
        try {
            var event = objectMapper.writeValueAsString(cacheEvent);
            redisTemplate.convertAndSend(CACHE_EVENT_CHANNEL, event);
        } catch (Exception e) {
            log.error("Error while publishing cache event", e);
        }
    }
}