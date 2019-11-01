package cn.hydrz.dipper.api;

import lombok.Getter;

/**
 * @author hydrz
 */
public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Getter
    private R result;

    /**
     * 构造
     *
     * @param code 错误码
     * @param msg  消息
     */
    public ApiException(int code, String msg) {
        result = new R<>(null, code, msg);
        new ApiException(result);
    }

    /**
     * 构造
     *
     * @param msg 消息
     */
    public ApiException(String msg) {
        result = new R<>(null, ResultCode.FAILURE.getCode(), msg);
        new ApiException(result);
    }

    /**
     * 构造
     *
     * @param result 返回结构
     */
    public ApiException(R result) {
        super(result.getMsg());
        this.result = result;
    }
}
