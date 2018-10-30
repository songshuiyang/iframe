package com.songsy.iframe.core.persistence.provider.model;

import lombok.Data;

/**
 * @author songshuiyang
 * @date 2018/10/28 11:35
 */
@Data
public class MybatisColumn {
    /**
     *
     */
    private MybatisTable mybatisTable;
    /**
     *
     */
    private String name;
    /**
     *
     */
    private String fieldName;
    /**
     *
     */
    private boolean insertable;
    /**
     *
     */
    private boolean updatable;
    /**
     *
     */
    private  boolean nullable;
}
