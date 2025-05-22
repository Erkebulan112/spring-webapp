package myrzakhan_taskflow.config;

import lombok.RequiredArgsConstructor;
import myrzakhan_taskflow.cache.sub.RedisEventSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class RedisEventConfig {

    private final RedisEventSubscriber subscriber;
    private static final String CACHE_EVENT_CHANNEL = "cache:events";

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        var container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(subscriber, new PatternTopic(CACHE_EVENT_CHANNEL));
        return container;
    }

    @Bean
    public ChannelTopic cacheEventChannel() {
        return new ChannelTopic(CACHE_EVENT_CHANNEL);
    }
}