package com.songsy.iframe.core.persistence.provider.exception;

/**
 * 实体类泛型错误异常
 * @author songshuiyang
 * @date 2018/10/30 22:53
 */
public class ParameterizedTypeException extends RuntimeException {

    public ParameterizedTypeException() {
        super();
    }

    public ParameterizedTypeException(String message) {
        super("[" + message + "]实体类泛型错误");
    }

    public ParameterizedTypeException(Throwable cause) {
        super(cause);
    }

    public ParameterizedTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
