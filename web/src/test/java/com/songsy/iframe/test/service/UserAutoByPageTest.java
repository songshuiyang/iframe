package com.songsy.iframe.test.service;

import com.songsy.iframe.test.BaseTest;

/**
 * 分页插件查询
 * @author songsy
 * @Date 2018/11/8 10:26
 */
public class UserAutoByPageTest extends BaseTest {

//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    UserService userService;
//    /**
//     * 分页测试
//     * 查询第二页，一页5条数据
//     * 查询字段加上 NOT_查询
//     * sql：Preparing: SELECT sys_user0.* FROM sys_user sys_user0 limit 5,5
//     */
//    @Test
//    public void findAutoByPage0 () {
//        Page<User> userPage = new Page<>(0);
//        userPage.setLimit(5);
//        userPage.setPage(2);
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//    /**
//     * 单字段查询
//     * sql: SELECT sys_user0.* FROM sys_user sys_user0 WHERE sys_user0.username= ?
//     */
//    @Test
//    public void findAutoByPage1 () {
//        Page<User> userPage = new Page<>(0);
//        userPage.setParams("username","ssy");
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//    /**
//     * 多字段查询
//     * sql: SELECT sys_user0.* FROM sys_user sys_user0 WHERE sys_user0.password= ? and sys_user0.username= ?
//     */
//    @Test
//    public void findAutoByPage2 () {
//        Page<User> userPage = new Page<>(0);
//        userPage.setParams("username","songsy");
//        userPage.setParams("password","root");
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//    /**
//     * 字段模糊查询
//     * 查询字段加上 LIKE_前缀
//     * sql： SELECT sys_user0.* FROM sys_user sys_user0 WHERE sys_user0.username like CONCAT(CONCAT('%', ?), '%')
//     */
//    @Test
//    public void findAutoByPage3 () {
//        Page<User> userPage = new Page<>(0);
//        userPage.setParams("LIKE_username","song");
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//    /**
//     * 多值查询
//     * 查询字段加上 IN_前缀
//     * sql： SELECT sys_user0.* FROM sys_user sys_user0 WHERE sys_user0.username IN (?,?)
//     */
//    @Test
//    public void findAutoByPage4 () {
//        Page<User> userPage = new Page<>(0);
//        String[] usernameList = {"songsy","ssy"};
//        userPage.setParams("IN_username", Arrays.asList(usernameList));
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//    /**
//     * 多值查询
//     * 查询字段加上 NOTIN_前缀
//     * sql： SELECT sys_user0.* FROM sys_user sys_user0 WHERE sys_user0.username NOT IN (?,?)
//     */
//    @Test
//    public void findAutoByPage5 () {
//        Page<User> userPage = new Page<>(0);
//        String[] usernameList = {"songsy","ssy"};
//        userPage.setParams("NOTIN_username", Arrays.asList(usernameList));
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//    /**
//     * 大于查询
//     * 查询字段加上 GT_前缀
//     * sql：SELECT sys_user0.* FROM sys_user sys_user0 WHERE sys_user0.age > ?
//     */
//    @Test
//    public void findAutoByPage6 () {
//        Page<User> userPage = new Page<>(0);
//        userPage.setParams("GT_age", 88);
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//
//    /**
//     * 小于查询
//     * 查询字段加上 LT_前缀
//     * sql：SELECT sys_user0.* FROM sys_user sys_user0 WHERE sys_user0.age < ?
//     */
//    @Test
//    public void findAutoByPage7 () {
//        Page<User> userPage = new Page<>(0);
//        userPage.setParams("LT_age", 12);
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//    /**
//     * Between查询
//     * 查询字段加上 BTW_前缀
//     * sql：SELECT sys_user0.* FROM sys_user sys_user0 WHERE sys_user0.created_date between ? and ?
//     */
//    @Test
//    public void findAutoByPage8 () {
//        Page<User> userPage = new Page<>(0);
//        String startTime = "2018-11-1";
//        String endTime = "2018-11-30";
//        userPage.setParams("BTW_createdDate", startTime + "~" + endTime);
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//    /**
//     * NULL值查询
//     * 查询字段加上 NULL_前缀
//     * sql：SELECT sys_user0.* FROM sys_user sys_user0 WHERE sys_user0.salt is null
//     */
//    @Test
//    public void findAutoByPage9 () {
//        Page<User> userPage = new Page<>(0);
//        // salt 字段为null
//        userPage.setParams("NULL_salt", true);
//        // salt 字段不为null
//        // userPage.setParams("NULL_salt", false);
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//    /**
//     * not 查询
//     * 查询字段加上 NOT_查询
//     * sql：SELECT sys_user0.* FROM sys_user sys_user0 WHERE sys_user0.age <> ?
//     */
//    @Test
//    public void findAutoByPage10 () {
//        Page<User> userPage = new Page<>(0);
//        userPage.setParams("NOT_age", 88);
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//    /**
//     * not 查询
//     * 查询字段加上 NOT_查询
//     * sql：SELECT sys_user0.* FROM sys_user sys_user0 WHERE sys_user0.age <> ?
//     */
//    @Test
//    public void findAutoByPage11 () {
//        Page<User> userPage = new Page<>(0);
//        userPage.setParams("NOT_age", 88);
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//    /**
//     * 倒序查询
//     * 查询字段加上 NOT_查询
//     * sql：SELECT sys_user0.* FROM sys_user sys_user0 order by age DESC
//     */
//    @Test
//    public void findAutoByPage12 () {
//        Page<User> userPage = new Page<>(0);
//        userPage.setSortName("age");
//        userPage.setSortOrder("DESC");
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//    /**
//     * 升序查询
//     * 查询字段加上 NOT_查询
//     * sql：SELECT sys_user0.* FROM sys_user sys_user0 order by age ASC
//     */
//    @Test
//    public void findAutoByPage13 () {
//        Page<User> userPage = new Page<>(0);
//        userPage.setSortName("age");
//        userPage.setSortOrder("ASC");
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }
//
//    /**
//     * 复杂sql
//     */
//    @Test
//    public void findAutoByPage14 () {
//        Page<User> userPage = new Page<>(0);
//        userPage.setLimit(5);
//        userPage.setPage(2);
//        userPage.setSortName("age");
//        userPage.setSortOrder("ASC");
//        userPage.setParams("NOT_age", 88);
//        logger.info(userService.findAutoByPage(userPage).toString());
//    }

}
