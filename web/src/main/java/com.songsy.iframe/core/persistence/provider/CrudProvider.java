package com.songsy.iframe.core.persistence.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author songshuiyang
 * @date 2018/10/28 11:34
 */
public class CrudProvider extends BaseProvider{

    private static Logger logger = LoggerFactory.getLogger(CrudProvider.class);

    public static final String FIND_ALL = "findAll";

    public String findAll() {
        String sql = "SELECT * FROM " + getMybatisTable().getName();
        return sql;
    }
}
