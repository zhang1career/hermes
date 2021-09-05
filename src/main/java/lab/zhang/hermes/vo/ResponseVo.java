package lab.zhang.hermes.vo;

import lombok.Data;

/**
 * @author zhangrj
 */
@Data
public class ResponseVo<T> {

    private static final int CODE_OK = 0;


    /**
     * 处理结果状态码
     */
    private Integer code;
    /**
     * 返回结果
     */
    private String msg;
    /**
     * 返回各种类型值
     */
    private T data;

    public ResponseVo(Integer code, String msg, T data) {
        this.code = code;
        this.msg  = msg;
        this.data = data;
    }

    public ResponseVo(T data) {
        this(CODE_OK, "", data);
    }
}
