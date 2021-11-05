package aspects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Timer {
    public static final Map<String, List<Long>> map = new ConcurrentHashMap<>();

    public static void add(String clazz, long time) {
        if (!map.containsKey(clazz)) {
            map.put(clazz, new ArrayList<>());
        }
        map.get(clazz).add(time);
    }
}
