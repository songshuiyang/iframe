package com.songsy.iframe.core.persistence.provider.mapper;

import java.io.Serializable;

/**
 * Mapper 增强接口
 * T: 实体类类型
 * ID: 主键类型
 * @author songshuiyang
 * @date 2018/10/28 11:31
 */
public interface BaseCurdMapper <T, ID extends Serializable> extends CurdMapper<T, ID> {

}
