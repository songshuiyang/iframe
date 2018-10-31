package com.songsy.iframe.core.persistence.provider.mapper;

import com.songsy.iframe.core.persistence.provider.MybatisProvider;
import com.songsy.iframe.core.persistence.provider.entity.BaseEntity;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.io.Serializable;
import java.util.List;

/**
 * 通用增删改查Mapper
 * @author songshuiyang
 * @date 2018/10/28 11:22
 */
public interface CurdMapper<T extends BaseEntity, ID extends Serializable> {
    /**
     * 查询所有数据
     * @return
     */
    @SelectProvider(type=MybatisProvider.class,method = MybatisProvider.FIND_ALL)
    List<T> findAll();

    /**
     * 根据id查询记录
     * @return
     */
    @SelectProvider(type=MybatisProvider.class, method = MybatisProvider.FIND_BY_ID)
    T findById(Object id);

    /**
     * 插入记录
     * @param entity
     * @return
     */
    @InsertProvider(type=MybatisProvider.class, method = MybatisProvider.INSERT)
    int insert(T entity);

    /**
     * 更新记录
     * @param entity
     * @return
     */
    @UpdateProvider(type=MybatisProvider.class, method = MybatisProvider.UPDATE)
    int update(T entity);

}
