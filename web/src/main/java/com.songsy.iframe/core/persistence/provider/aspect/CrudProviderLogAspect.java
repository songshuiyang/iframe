package com.songsy.iframe.core.persistence.provider.aspect;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.javassist.ClassClassPath;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;

/**
 * CrudProvider 日志输出方法
 *
 * @author songsy
 * @Date 2018/11/1 9:16
 */
@Aspect
@Component
public class CrudProviderLogAspect {


    private final static Logger logger = LoggerFactory.getLogger(BaseCurdMapperAspect.class);

    @Pointcut("@annotation(com.songsy.iframe.controller.LogPrint)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint point) {
        System.out.println(234234);
    }

    @Around("pointcut()")
    public Object before(ProceedingJoinPoint point) throws Throwable {
        System.out.println("123");
        String msgInfo = "@aop[" + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName() + "]"; // 所在的类.方法
        String requestStr = getRequestParam(point);
        requestStr = parameterHandle(requestStr, 10000);
        logger.info(msgInfo + "start.输入参数：" + requestStr);
        long startTime = System.currentTimeMillis();// 开始时间
        Object result = null;
        try {
            // 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行
            result = point.proceed();// result的值就是被拦截方法的返回值
        } catch (Exception e) {
            throw e;
        } finally {
            long handleTime = System.currentTimeMillis() - startTime;// 开始时间
            String responseStr = result == null ? "无" : JSON.toJSONString(result);
            responseStr = parameterHandle(responseStr, 10000);

            StringBuffer endString = new StringBuffer(100);
            endString.append(msgInfo).append("end.");
            endString.append("耗时（" + handleTime + "ms）");
            endString.append("输出参数：").append(responseStr);

            logger.info(endString.toString());
        }
        return result;
    }

    /**
     * @param paramStr
     * @param strlength
     * @return
     * @Description : 参数处理，超过指定长度字符的，只显示1000...
     * @author : 陈惟鲜 danger
     * @Date : 2018年8月10日 上午11:44:11
     */
    private String parameterHandle(String paramStr, int strlength) {
        if (paramStr.length() > strlength) {
            paramStr = paramStr.substring(0, 1000) + "...";
        }
        if (paramStr.length() > 10) {
            paramStr = "[" + paramStr + "]";
        }
        return paramStr;
    }

    /***
     * @Description : 获取请求参数
     * @author : 陈惟鲜 danger
     * @Date : 2018年8月9日 下午3:47:08
     * @param point
     * @return
     */
    private String getRequestParam(ProceedingJoinPoint point) {
        String class_name = point.getTarget().getClass().getName();
        String method_name = point.getSignature().getName();
        /**
         * 获取方法的参数值数组。
         */
        Object[] methodArgs = point.getArgs();

        String[] paramNames = null;
        // 结果
        String requestStr = "";
        /**
         * 获取方法参数名称
         */
        try {
            paramNames = getFieldsName(class_name, method_name);
            requestStr = logParam(paramNames, methodArgs);
        } catch (Exception e) {
            requestStr = "获取参数失败";
        }
        return requestStr;
    }

    /**
     * 使用javassist来获取方法参数名称
     *
     * @param class_name  类名
     * @param method_name 方法名
     * @return
     * @throws Exception
     */
    private String[] getFieldsName(String class_name, String method_name) throws Exception {
        Class<?> clazz = Class.forName(class_name);
        String clazz_name = clazz.getName();
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(clazz);
        pool.insertClassPath(classPath);

        CtClass ctClass = pool.get(clazz_name);
        CtMethod ctMethod = ctClass.getDeclaredMethod(method_name);
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            return null;
        }
        String[] paramsArgsName = new String[ctMethod.getParameterTypes().length];
        int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramsArgsName.length; i++) {
            paramsArgsName[i] = attr.variableName(i + pos);
        }
        return paramsArgsName;
    }

    /**
     * 判断是否为基本类型：包括String
     *
     * @param clazz clazz
     * @return true：是;     false：不是
     */
    private boolean isPrimite(Class<?> clazz) {
        if (clazz.isPrimitive() || clazz == String.class) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 打印方法参数值  基本类型直接打印，非基本类型需要重写toString方法
     *
     * @param paramsArgsName  方法参数名数组
     * @param paramsArgsValue 方法参数值数组
     */
    private String logParam(String[] paramsArgsName, Object[] paramsArgsValue) {
        if (ArrayUtils.isEmpty(paramsArgsName) || ArrayUtils.isEmpty(paramsArgsValue)) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < paramsArgsValue.length; i++) {
            //参数名
            String name = paramsArgsName[i];
            //参数值
            Object value = paramsArgsValue[i];
            buffer.append(name + " = ");
            if (isPrimite(value.getClass())) {
                buffer.append(value + "  ,");
            } else {
                buffer.append(value.toString() + "  ,");
            }
        }
        return buffer.toString();
    }


}
