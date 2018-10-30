package com.songsy.iframe.core.persistence.provider.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author songshuiyang
 * @date 2018/10/28 11:35
 */
@Data
public class MybatisTable {
    private String name;
    private String category;
    private String  schema;
    private List<MybatisColumn> mybatisColumnList = Lists.newArrayList();
    private MybatisColumn id;
    private MybatisColumn createdBy;
    private MybatisColumn createdDate;
    private MybatisColumn lastModifiedBy;
    private MybatisColumn LastModifiedDate;
    private MybatisColumn version;
    private MybatisColumn deleted;
    private String generationType;
}
