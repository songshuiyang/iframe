package com.songsy.iframe.core.base;

import com.songsy.iframe.core.common.mo.ResponseMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author songshuiyang
 * @date 2019/03/29 10:11
 */
public abstract class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取当前用户的id
     *
     * @return
     */
    public String currentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = (String) request.getAttribute("userId");
        return userId == null ? "" : userId;
    }

    public <T> ResponseMO<T> success(T resMo, String msg) {
        ResponseMO<T> responseMO = new ResponseMO<T>();
        responseMO.setMsg(msg);
        responseMO.setData(resMo);
        return responseMO;
    }

    public <T> ResponseMO<T> success(T resMo) {
        ResponseMO<T> responseMO = new ResponseMO<T>();
        responseMO.setData(resMo);
        return responseMO;
    }

    public <T> ResponseMO<T> success() {
        return new ResponseMO<>();
    }

    public <T> ResponseMO<T> success(String msg) {
        ResponseMO<T> responseMO = new ResponseMO<T>();
        responseMO.setMsg(msg);
        return responseMO;
    }

    public <T> ResponseMO<T> error(T resMo, String msg) {
        ResponseMO<T> responseMO = new ResponseMO<T>();
        responseMO.setResponseCodeFailure();
        responseMO.setMsg(msg);
        responseMO.setData(resMo);
        return responseMO;
    }

    public <T> ResponseMO<T> error(String msg) {
        ResponseMO<T> responseMO = new ResponseMO<T>();
        responseMO.setResponseCodeFailure();
        responseMO.setMsg(msg);
        return responseMO;
    }

    public <T> ResponseMO<T> custom(int code, String msg) {
        ResponseMO<T> responseMO = new ResponseMO<T>();
        responseMO.setCode(code);
        responseMO.setMsg(msg);
        return responseMO;
    }

    public <T> ResponseMO<T> custom(T resMo, int code, String msg) {
        ResponseMO<T> responseMO = new ResponseMO<T>();
        responseMO.setCode(code);
        responseMO.setMsg(msg);
        responseMO.setData(resMo);
        return responseMO;
    }

    public <T> ResponseMO<T> errorCode(String errorCode, String msg) {
        ResponseMO<T> responseMO = new ResponseMO<T>();
        responseMO.setResponseCodeFailure();
        responseMO.setMsg(msg);
        responseMO.setDebugInfo(errorCode);
        return responseMO;
    }
}
