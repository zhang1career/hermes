package lab.zhang.hermes.errmsg;

import lab.zhang.hermes.exception.InvalidParameterException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangrj
 */

public class ErrorCode {
    static public final Map<String, Integer> MAP = new HashMap<>();

    static {
        MAP.put(InvalidParameterException.class.getName(), 1001);
    }
}
