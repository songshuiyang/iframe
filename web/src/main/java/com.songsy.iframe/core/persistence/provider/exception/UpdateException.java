package com.songsy.iframe.core.persistence.provider.exception;

/**
 * @author songsy
 * @Date 2018/10/31 17:44
 */
public class UpdateException extends RuntimeException {
	
    public UpdateException() {
        super("更新结果集异常");
    }
	
    public UpdateException(String message) {
        super(message);
    }

    public UpdateException(Throwable cause) {
        super(cause);
    }

    public UpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
