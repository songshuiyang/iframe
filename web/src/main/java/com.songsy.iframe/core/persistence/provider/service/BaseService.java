package com.songsy.iframe.core.persistence.provider.service;

import com.songsy.iframe.core.persistence.provider.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author songsy
 * @Date 2018/10/31 18:06
 */
public interface BaseService <T extends BaseEntity, ID extends Serializable>{

    List<T> findAll();

    T findById(ID id);

    T saveSelective(T entity);

    T saveSelective(T entity, Boolean hasId);

    int updateNull(T entity);

    int deleteOne (ID id);

    int logicDeleteOne (ID id);
}
