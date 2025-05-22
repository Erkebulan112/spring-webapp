package myrzakhan_taskflow.cache.sub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.cache.handler.RedisEventHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisEventSubscriber implements MessageListener {

    private final RedisEventHandler redisEventHandler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            redisEventHandler.handleEvent(body);
        } catch (Exception e) {
            log.error("Error processing redis message: {}", e.getMessage());
        }
    }
}
