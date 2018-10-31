package com.songsy.iframe.core.persistence.provider.entity;

import com.songsy.iframe.core.persistence.provider.annotation.GenerationType;
import lombok.Data;

/**
 * 主键描述类
 * @author songsy
 * @Date 2018/10/31 16:05
 */
@Data
public class IdColumnEntity extends ColumnEntity{
    /**
     * 主键生成类型
     */
    GenerationType generationType;
}
