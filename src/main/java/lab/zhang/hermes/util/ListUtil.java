package lab.zhang.hermes.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhangrj
 */
public class ListUtil {

    /**
     * check if the list is [null]
     * @param list the input data
     * @return is empty
     */
    static public <T> boolean isNill(List<T> list) {
        return list == null || list.size() == 0 || (list.size() == 1 && list.get(0) == null);
    }

    static public <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<>();
        set.addAll(list1);
        set.addAll(list2);
        return new ArrayList<>(set);
    }

    static public <T> List<T> intersect(List<T> list1, List<T> list2) {
        List<T> interList = new ArrayList<>();
        for (T t : list1) {
            if (!list2.contains(t)) {
                continue;
            }
            interList.add(t);
        }
        return interList;
    }

    static public <T> List<T> diff(List<T> list1, List<T> list2) {
        List<T> diffList = new ArrayList<>();
        for (T t : list1) {
            if (list2.contains(t)) {
                continue;
            }
            diffList.add(t);
        }
        return diffList;
    }

    static public <T> boolean isEqual(List<T> list1, List<T> list2) {
        return diff(list1, list2).size() <= 0 && diff(list2, list1).size() <= 0;
    }
}
