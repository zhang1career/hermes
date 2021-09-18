package lab.zhang.hermes.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangrj
 */
public class StrUtil {

    static public boolean isNill(String str) {
        return str == null || str.isEmpty() || "".equals(str.trim());
    }


    @Nullable
    static public List<Long> explode(@NotNull String str) {
        String[] strArr = str.split(",");
        if (strArr.length <= 0) {
            return null;
        }

        List<Long> retList = new ArrayList<>(strArr.length);
        for (String s : strArr) {
            retList.add(Long.parseLong(s.trim()));
        }

        return retList;
    }
}
