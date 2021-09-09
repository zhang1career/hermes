package lab.zhang.hermes.repo;

import lab.zhang.hermes.entity.Identical;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangrj
 */
public class BaseRepo {

    @NotNull
    static public <P, R> List<R> columnOf(@NotNull List<P> inputList, Convertible<P, R> convertor) {
        List<R> output = new ArrayList<>(inputList.size());
        for (P input : inputList) {
            output.add(convertor.covertFrom(input));
        }

        return output;
    }

    @NotNull
    static public <P, R> Map<Long, R> columnOf(@NotNull Map<Long, P> inputMap, Convertible<P, R> convertor) {
        Map<Long, R> output = new HashMap<>(inputMap.size());
        for (Map.Entry<Long, P> entry : inputMap.entrySet()) {
            output.put(entry.getKey(), convertor.covertFrom(entry.getValue()));
        }

        return output;
    }

    @NotNull
    static public <T extends Identical> Map<Long, T> indexById(@NotNull List<T> list) {
        Map<Long, T> map = new HashMap<>(list.size());
        for (T item : list) {
            map.put(item.getId(), item);
        }
        return map;
    }
}
