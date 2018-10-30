package com.songsy.iframe.core.persistence.provider.exception;

/**
 * 实体类对象异常
 * @author songshuiyang
 * @date 2018/10/30 22:53
 */
public class EntityAnnotationException extends RuntimeException {

    public EntityAnnotationException() {
        super();
    }

    public EntityAnnotationException(String message) {
        super("[" + message + "]该实体类没有加Entity和Table注解");
    }

    public EntityAnnotationException(Throwable cause) {
        super(cause);
    }

    public EntityAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }
}
