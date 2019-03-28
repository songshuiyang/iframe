package com.songsy.iframe.core.log;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 打印输入输出参数
 * @author songsy
 * @date 2018/12/24 18:42
 */
@Aspect
@Slf4j
@Configuration
public class LogAspect {

    /**
     * 匹配Controller
     * @within :使用 “@within(注解类型)” 匹配所以持有指定注解类型内的方法;注解类型也必须是全限定类型名;
     */
    @Pointcut("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        String url = request.getRequestURI();
        String className = pjp.getSignature().getDeclaringType().getSimpleName();
        String methodName = pjp.getSignature().getName();

        log.info("===>>> [request url : {}, method: {}, className: {}, methodName: {}]", url, request.getMethod(), className, methodName);
        // 文件上传不打印body
        if (!ServletFileUpload.isMultipartContent(request)) {
            try {
                log.info("===>>> [request body: {}]", JSONObject.toJSONString(pjp.getArgs()));
            } catch (Exception e) {
                log.error("===>>> [request body parsing error]");
            }
        }
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long end = System.currentTimeMillis();
        try {
            log.info("===<<< [response url : {}, consuming: {} ms , method: {}, className: {}, methodName: {}]", url, end - start, request.getMethod(), className, methodName);
            String resultJsonString = JSONObject.toJSONString(result);
            if (resultJsonString.length() > 5000) {
                log.info("===<<< [response result filtered ....]");
            } else {
                log.info("===<<< [response result: {} ]", resultJsonString);
            }
        } catch (Exception e) {
            log.error("===<<< [response body parsing error]");
        }
        return result;
    }
}
