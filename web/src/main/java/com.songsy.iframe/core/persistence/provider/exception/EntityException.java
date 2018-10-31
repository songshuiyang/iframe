package com.songsy.iframe.core.persistence.provider.exception;

/**
 * @author songsy
 * @Date 2018/10/31 15:44
 */
public class EntityException  extends RuntimeException{

    public EntityException() {
        super();
    }

    public EntityException(String message) {
        super("[" + message + "]获取当前实体类名失败");
    }

    public EntityException(Throwable cause) {
        super(cause);
    }

    public EntityException(String message, Throwable cause) {
        super(message, cause);
    }

}
