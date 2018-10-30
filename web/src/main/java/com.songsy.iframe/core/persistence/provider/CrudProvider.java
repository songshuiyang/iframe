package com.songsy.iframe.core.persistence.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songshuiyang
 * @date 2018/10/28 11:34
 */
public class CrudProvider {

    private static Logger logger = LoggerFactory.getLogger(CrudProvider.class);

    public static final String FIND_ALL = "findAll";

    /**
     * 查询所有数据
     * @return
     */
    public String findAll() {
        String sql = "SELECT * FROM " + "";
        return sql;
    }
}
