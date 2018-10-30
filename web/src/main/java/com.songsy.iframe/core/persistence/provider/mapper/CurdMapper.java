package com.songsy.iframe.core.persistence.provider.mapper;

import com.songsy.iframe.core.persistence.provider.MybatisProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.List;

/**
 * 通用增删改查Mapper
 * @author songshuiyang
 * @date 2018/10/28 11:22
 */
public interface CurdMapper<T, ID extends Serializable> {
    /**
     * 查询所有数据
     * @return
     */
    @SelectProvider(type=MybatisProvider.class,method = MybatisProvider.FIND_ALL)
    List<T> findAll();
}
