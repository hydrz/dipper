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

import cn.hutool.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务代码枚举
 *
 * @author hydrz
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {
    /**
     * 操作成功 200
     */
    SUCCESS(HttpStatus.HTTP_OK, 0, "操作成功"),

    /**
     * 操作成功(不需要提示) 202
     */
    ACCEPTED(HttpStatus.HTTP_ACCEPTED, 0, "操作成功"),

    /**
     * 操作异常 400
     */
    FAILURE(HttpStatus.HTTP_BAD_REQUEST, HttpStatus.HTTP_BAD_REQUEST, "操作异常"),

    /**
     * 请求未授权 401
     */
    UNAUTHORIZED(HttpStatus.HTTP_UNAUTHORIZED, HttpStatus.HTTP_UNAUTHORIZED, "请求未授权"),

    /**
     * 需要先付费 402
     */
    PAYMENT_REQUIRED(HttpStatus.HTTP_PAYMENT_REQUIRED, HttpStatus.HTTP_PAYMENT_REQUIRED, "需要先付费"),

    /**
     * 没有权限 403
     */
    CLIENT_UN_AUTHORIZED(HttpStatus.HTTP_FORBIDDEN, HttpStatus.HTTP_FORBIDDEN, "没有权限"),

    /**
     * 资源未找到 404
     */
    NOT_FOUND(HttpStatus.HTTP_NOT_FOUND, HttpStatus.HTTP_NOT_FOUND, "资源未找到"),

    /**
     * 请求方法错误 405
     */
    BAD_METHOD(HttpStatus.HTTP_BAD_METHOD, HttpStatus.HTTP_BAD_METHOD, "请求方法错误"),

    /**
     * 请求超时 408
     */
    CLIENT_TIMEOUT(HttpStatus.HTTP_CLIENT_TIMEOUT, HttpStatus.HTTP_CLIENT_TIMEOUT, "请求超时"),

    /**
     * 请求冲突 409
     */
    CONFLICT(HttpStatus.HTTP_CONFLICT, HttpStatus.HTTP_CONFLICT, "请求冲突"),


    /**
     * 资源已删除 410
     */
    GONE(HttpStatus.HTTP_GONE, HttpStatus.HTTP_GONE, "资源已删除"),


    /**
     * 内容超出限制大小 413
     */
    ENTITY_TOO_LARGE(HttpStatus.HTTP_ENTITY_TOO_LARGE, HttpStatus.HTTP_ENTITY_TOO_LARGE, "内容超出限制大小"),

    /**
     * 不支持当前媒体类型
     */
    UNSUPPORTED_TYPE(HttpStatus.HTTP_UNSUPPORTED_TYPE, HttpStatus.HTTP_UNSUPPORTED_TYPE, "不支持当前媒体类型"),

    /**
     * 服务器异常
     */
    INTERNAL_SERVER_ERROR(HttpStatus.HTTP_INTERNAL_ERROR, HttpStatus.HTTP_INTERNAL_ERROR, "服务器异常"),

    /**
     * 缺少必要的请求参数
     */
    PARAM_MISS(HttpStatus.HTTP_BAD_REQUEST, HttpStatus.HTTP_BAD_REQUEST, "缺少必要的请求参数"),

    /**
     * 请求参数类型错误
     */
    PARAM_TYPE_ERROR(HttpStatus.HTTP_BAD_REQUEST, HttpStatus.HTTP_BAD_REQUEST, "请求参数类型错误"),

    /**
     * 请求参数绑定错误
     */
    PARAM_BIND_ERROR(HttpStatus.HTTP_BAD_REQUEST, HttpStatus.HTTP_BAD_REQUEST, "请求参数绑定错误"),

    /**
     * 参数校验失败
     */
    PARAM_VALID_ERROR(HttpStatus.HTTP_BAD_REQUEST, HttpStatus.HTTP_BAD_REQUEST, "参数校验失败"),
    ;

    /**
     * http状态码
     */
    final int httpStatus;

    /**
     * code编码
     */
    final int code;
    /**
     * 中文信息描述
     */
    final String message;

}
