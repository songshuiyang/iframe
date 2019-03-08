package com.songsy.iframe.controller;

import com.songsy.iframe.model.User;
import com.songsy.iframe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author songshuiyang
 * @date 2018/10/28 10:11
 */
@RestController
@RequestMapping("/pub/account")
public class UserController {

    private String username;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/view")
    public User view() {
        return userService.findById(1);
    }

    @GetMapping("/{id}")
    public User view(@PathVariable("id") Integer id) {
        return userService.findById(id);
    }

    @PostMapping("")
    public User updateUser(@RequestBody User user) {
        User user1 = userService.findById(user.getId());
        user1.setUsername("update");
        userService.saveSelective(user1);
        return user;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Integer id) {
        userService.logicDeleteOne(id);
    }

    public String function1 () {
        return "";
    }
}
