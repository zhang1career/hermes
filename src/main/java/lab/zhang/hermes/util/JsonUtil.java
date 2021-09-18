package lab.zhang.hermes.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangrj
 */
public class JsonUtil {

    @Nullable
    static public Object analyze(@NotNull Object object) {
        if (!(object instanceof JSONObject)) {
            return object;
        }

        Map<String, Object> result = new HashMap<>(0);

        JSONObject jsonObject = (JSONObject) object;
        Set<String> keys = jsonObject.keySet();
        keys.parallelStream().forEach(key -> {
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
                JSONObject subJsonObject = (JSONObject) value;
                result.put(key, analyze(subJsonObject));
                return;
            }

            if (value instanceof JSONArray) {
                JSONArray subJsonArray = (JSONArray) value;
                if (subJsonArray.isEmpty()) {
                    return;
                }
                Object[] subArray = new Object[subJsonArray.size()];
                for (int i = 0; i < subJsonArray.size(); i++) {
                    subArray[i] = analyze(subJsonArray.get(i));
                }
                result.put(key, subArray);
                return;
            }

            result.put(key, value);
        });
        return result;
    }
}
