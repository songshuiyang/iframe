package com.songsy.iframe.core.persistence.provider.mapper;

import com.songsy.iframe.core.persistence.provider.MybatisProvider;
import com.songsy.iframe.core.persistence.provider.Page;
import com.songsy.iframe.core.persistence.provider.entity.BaseEntity;
import org.apache.ibatis.annotations.DeleteProvider;
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

    /**
     * 更新记录(null值记录也更新)
     * @param entity
     * @return
     */
    @UpdateProvider(type=MybatisProvider.class, method = MybatisProvider.UPDATE_NULL)
    int updateNull(T entity);

    /**
     * 根据id物理删除记录
     * @param id
     * @return
     */
    @DeleteProvider(type=MybatisProvider.class, method = MybatisProvider.DELETE_ONE)
    int deleteOne (Object id);

    /**
     * 根据id逻辑删除记录
     * @param id
     * @return
     */
    @DeleteProvider(type=MybatisProvider.class, method = MybatisProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne (Object id);


    /**
     * 分页查询
     * @param page
     * @return
     */
    @SelectProvider(type=MybatisProvider.class,method = MybatisProvider.FIND_AUTO_BY_PAGE)
    List<T> findAutoByPage(Page<T> page);
}
