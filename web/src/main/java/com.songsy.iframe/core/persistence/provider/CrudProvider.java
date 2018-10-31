package com.songsy.iframe.core.persistence.provider;

import com.songsy.iframe.core.persistence.provider.entity.TableEntity;
import com.songsy.iframe.core.persistence.provider.threadlocal.EntityThreadLocal;
import com.songsy.iframe.core.persistence.provider.utils.MybatisTableUtils;
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
        TableEntity tableEntity = MybatisTableUtils.getCurrentTableEntity();
        String sql = "SELECT * FROM " +  tableEntity.getTableName();
        logger.info("findAll sql: {}", sql);
        return sql;
    }
}
