package com.songsy.iframe.controller;

import com.songsy.iframe.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songshuiyang
 * @date 2018/10/28 10:11
 */
@RestController
@RequestMapping("/pub/account")
public class UserController extends BaseController {

//    private String username;
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/list")
//    public ResponseMO findAll() {
//        return success(userService.findAll());
//    }
//
//    @GetMapping("/view")
//    public ResponseMO view() {
//        return success(userService.findById(1));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseMO view(@PathVariable("id") Integer id) {
//        return success(userService.findById(id));
//    }
//
//    @PostMapping("")
//    @RedisLock(key = "#user.username")
//    public ResponseMO updateUser(@RequestBody User user) throws Exception{
//        User user1 = userService.findById(user.getId());
//        Thread.sleep(5000);
//        user1.setUsername("update");
//        userService.saveSelective(user1);
//        return success(user);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseMO deleteUser(@PathVariable("id") Integer id) {
//        userService.logicDeleteOne(id);
//        return success();
//    }
//
//    public String function1 () {
//        return "";
//    }
}
