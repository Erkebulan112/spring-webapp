
package myrzakhan_taskflow.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisProperties {

    private TimeToLive timeToLive = new TimeToLive();

    @Getter
    @Setter
    public static class TimeToLive {
        private long tasks;
        private long projects;
        private long users;
    }
}