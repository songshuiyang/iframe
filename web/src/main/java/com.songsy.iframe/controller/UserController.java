package com.songsy.iframe.controller;

import com.songsy.iframe.model.User;
import com.songsy.iframe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author songshuiyang
 * @date 2018/10/28 10:11
 */
@RestController
@RequestMapping("/pub/account")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("list")
    public List<User> findAll() {
        return userService.findAll();
    }
}
