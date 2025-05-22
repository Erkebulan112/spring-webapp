package myrzakhan_taskflow.dtos.event;

public record CacheEvent(
        CacheEventType eventType,
        String cacheName,
        Object key,
        Object value
) {
    public static CacheEvent evictEvent(String cacheName, Object key) {
        return new CacheEvent(CacheEventType.EVICT, cacheName, key, null);
    }

    public static CacheEvent updateEvent(String cacheName, Object key, Object value) {
        return new CacheEvent(CacheEventType.UPDATE, cacheName, key, value);
    }

    public static CacheEvent createEvent(String cacheName, Object key, Object value) {
        return new CacheEvent(CacheEventType.CREATE, cacheName, key, value);
    }
}