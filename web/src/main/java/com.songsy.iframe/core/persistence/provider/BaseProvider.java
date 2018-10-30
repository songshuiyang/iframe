package com.songsy.iframe.core.persistence.provider;

import com.songsy.iframe.core.persistence.provider.model.MybatisTable;
import com.songsy.iframe.core.persistence.provider.utils.ProviderUtils;

/**
 * @author songshuiyang
 * @date 2018/10/28 11:34
 */
public class BaseProvider {

    protected MybatisTable getMybatisTable() {
        return ProviderUtils.getMybatisTable(getEntityClass());
    }

    protected Class getEntityClass() {
        return MyBatisProviderContext.get().getProviderContext().getEntityClass();
    }
}
