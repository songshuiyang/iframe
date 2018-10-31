package com.songsy.iframe.core.persistence.provider.exception;

/**
 * @author songsy
 * @Date 2018/10/31 17:44
 */
public class VersionException extends RuntimeException {
	
    public VersionException() {
        super("更新对象的版本号是null");
    }
	
    public VersionException(String message) {
        super(message);
    }

    public VersionException(Throwable cause) {
        super(cause);
    }

    public VersionException(String message, Throwable cause) {
        super(message, cause);
    }
}
