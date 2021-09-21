package lab.zhang.hermes.util;

import java.util.Map;

/**
 * @author zhangrj
 */
public class MapUtil {

    static public<K, V> boolean isNill(Map<K, V> map) {
        return map == null || map.size() <= 0;
    }
}
