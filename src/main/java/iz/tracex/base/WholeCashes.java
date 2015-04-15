package iz.tracex.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author izumi_j
 *
 */
public final class WholeCashes {
    private WholeCashes() {
    }

    private static final Map<Class<?>, Map<?, ?>> WHOLE_CACHE = new ConcurrentHashMap<>();

    public static <K, V> Map<K, V> getCache(Class<?> clazz) {
        @SuppressWarnings("unchecked")
        Map<K, V> cache = (Map<K, V>) WHOLE_CACHE.get(clazz);
        if (cache == null) {
            cache = new ConcurrentHashMap<K, V>();
            WHOLE_CACHE.put(clazz, cache);
        }
        return cache;
    }
}
