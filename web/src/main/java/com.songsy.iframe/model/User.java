package com.songsy.iframe.model;


import com.songsy.iframe.core.persistence.provider.annotation.Entity;
import com.songsy.iframe.core.persistence.provider.annotation.Table;
import com.songsy.iframe.core.persistence.provider.mapper.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 用户
 * @author songshuiyang
 * @date 2017/11/28 21:36
 */
@Data
@Entity
@Table(name = "sys_user")
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity<Integer> {

    private String username;

    private String password;

    private String nickname;

    private Integer sex;

    private Integer age;

    private String phone;

    private String email;

    private String address;

    private String salt;

    private String headPortrait;
}