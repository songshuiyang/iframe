package com.songsy.iframe.core.persistence.provider.mapper;

import com.songsy.iframe.core.persistence.provider.annotation.GeneratedValue;
import com.songsy.iframe.core.persistence.provider.annotation.GenerationType;
import com.songsy.iframe.core.persistence.provider.annotation.Id;
import com.songsy.iframe.core.persistence.provider.annotation.Version;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author songshuiyang
 * @date 2018/10/28 10:13
 */
@Getter
@Setter
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -3873745966284869947L;

    /**
     * 自定义主键生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.CUSTOM)
    private String id;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 时间
     */
    private Date createdDate;
    /**
     * 最后修改人
     */
    private String lastModifiedBy;
    /**
     * 最后修改时间
     */
    private Date lastModifiedDate;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 乐观锁字段
     */
    @Version
    private Integer version;
    /**
     * 逻辑删除标识
     */
    private boolean enable = true;

    @Override
    public int hashCode() {
        return this.id != null ? this.id.hashCode() : null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BaseEntity)) {
            return false;
        }
        BaseEntity i = (BaseEntity) obj;

        if (i.getId() == null || this.getId() == null) {
            return false;
        }
        if (this.getId().equals(i.getId())) {
            return true;
        }
        return false;
    }

}
