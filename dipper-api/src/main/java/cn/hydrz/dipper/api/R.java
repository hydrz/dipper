/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package cn.hydrz.dipper.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Optional;

/**
 * 统一API响应结果封装
 *
 * @author hydrz
 */
@Getter
@Setter
@ToString
@ApiModel(description = "返回信息")
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "承载数据")
    private T data;

    @ApiModelProperty(value = "状态码", required = true)
    private int code;

    @ApiModelProperty(value = "返回消息", required = true)
    private String msg;

    /**
     * 判断返回是否为成功
     *
     * @param result Result
     * @return 是否成功
     */
    public static boolean isSuccess(R<?> result) {
        return Optional.ofNullable(result)
                .map(x -> ObjectUtils.nullSafeEquals(ResultCode.SUCCESS.code, x.code))
                .orElse(Boolean.FALSE);
    }

    /**
     * 判断返回是否为成功
     *
     * @param result Result
     * @return 是否成功
     */
    public static boolean isNotSuccess(R<?> result) {
        return !R.isSuccess(result);
    }


    /**
     * 返回R
     *
     * @param <T> 任意Bean
     * @return 返回值
     */
    public static <T> R<T> ok() {
        return ok(ResultCode.SUCCESS);
    }

    /**
     * 返回R
     *
     * @param msg 消息
     * @param <T> 任意Bean
     * @return 返回值
     */
    public static <T> R<T> ok(String msg) {
        return ok(null, msg);
    }

    /**
     * 返回R
     *
     * @param data 载体
     * @param <T>  任意Bean
     * @return 返回值
     */
    public static <T> R<T> ok(T data) {
        return ok(data, ResultCode.SUCCESS);
    }

    /**
     * 返回R
     *
     * @param resultCode 返回类
     * @param <T>        任意Bean
     * @return 返回值
     */
    public static <T> R<T> ok(IResultCode resultCode) {
        return ok(null, resultCode);
    }

    /**
     * 返回R
     *
     * @param data 载体
     * @param msg  消息
     * @param <T>  任意Bean
     * @return 返回值
     */
    public static <T> R<T> ok(T data, String msg) {
        return ok(data, ResultCode.SUCCESS.getCode(), msg);
    }

    /**
     * 返回R
     *
     * @param data 载体
     * @param code 返回码
     * @param msg  消息
     * @param <T>  任意Bean
     * @return 返回值
     */
    public static <T> R<T> ok(T data, int code, String msg) {
        R<T> result = ok(data, ResultCode.SUCCESS);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 返回R
     *
     * @param data       载体
     * @param resultCode 结果
     * @param <T>        任意Bean
     * @return 返回值
     */
    @SneakyThrows
    public static <T> R<T> ok(T data, IResultCode resultCode) {
        R<T> result = new R<>(data, resultCode.getCode(), resultCode.getMessage());
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        response.setStatus(resultCode.getHttpStatus());
        return result;
    }

    /**
     * 返回R
     *
     * @param <T> 任意Bean
     * @return 返回值
     */
    public static <T> R<T> failed() {
        return failed(ResultCode.FAILURE);
    }

    /**
     * 返回R
     *
     * @param msg 消息
     * @param <T> 任意Bean
     * @return 返回值
     */
    public static <T> R<T> failed(String msg) {
        return failed(msg, null);
    }

    /**
     * 返回R
     *
     * @param data 载体
     * @param <T>  任意Bean
     * @return 返回值
     */
    public static <T> R<T> failed(T data) {
        return failed(ResultCode.FAILURE, data);
    }

    /**
     * 返回R
     *
     * @param resultCode 结果
     * @param <T>        任意Bean
     * @return 返回值
     */
    public static <T> R<T> failed(IResultCode resultCode) {
        return failed(ResultCode.FAILURE, null);
    }

    /**
     * 返回R
     *
     * @param msg  消息
     * @param data 载体
     * @param <T>  任意Bean
     * @return 返回值
     */
    public static <T> R<T> failed(String msg, T data) {
        return failed(ResultCode.FAILURE.getCode(), msg, data);
    }

    /**
     * 返回R
     *
     * @param code 返回码
     * @param msg  消息
     * @param data 载体
     * @param <T>  任意Bean
     * @return 返回值
     */
    public static <T> R<T> failed(int code, String msg, T data) {
        R<T> tr = new R<>(data, code, msg);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        response.setStatus(ResultCode.FAILURE.httpStatus);
        throw new ApiException(tr);
    }

    /**
     * 返回R
     *
     * @param resultCode 结果
     * @param data       载体
     * @param <T>        任意Bean
     * @return 返回值
     */
    public static <T> R<T> failed(IResultCode resultCode, T data) {
        R<T> tr = new R<>(data, resultCode.getCode(), resultCode.getMessage());
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        response.setStatus(resultCode.getHttpStatus());
        throw new ApiException(tr);
    }
}
