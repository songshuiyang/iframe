package com.songsy.iframe.core.persistence.provider.utils;

import java.util.UUID;

/**
 * 主键生成工具
 * @author songsy
 * @Date 2018/10/31 17:00
 */
public class IDGeneratorUtils {
    /**
     * 生成主键（32位）
     *
     * @return
     */
    public static String generateID() {
        return generateID(System.currentTimeMillis());
    }

    /**
     * 根据指定时间生成主键
     *
     * @param time
     * @return
     */
    public static String generateID(long time) {
        String rtnVal = Long.toHexString(time);
        rtnVal += UUID.randomUUID();
        rtnVal = rtnVal.replaceAll("-", "");
        return rtnVal.substring(0, 32);
    }

}
