/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1_localhost
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : iframe

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 16/11/2018 18:35:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `role_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 1, 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `sex` int(11) NULL DEFAULT NULL COMMENT '性别(0:男 1:女)',
  `age` int(11) NULL DEFAULT NULL,
  `phone` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机',
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `salt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '加密盐值',
  `head_portrait` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `created_date` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `last_modified_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  `last_modified_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  `version` bigint(255) NULL DEFAULT NULL COMMENT '版本',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `enable` bit(1) NULL DEFAULT b'1' COMMENT '是否启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '测试乐观锁111', 'ff1dbcc73a4364a24c2918713789b1b31e70f877', '系统管理员', 1, 1, '13184582591', 'songshuiyang@foxmail.com', '深圳市南山区科技园南区R2-B三楼步步高duyidan', 'lBXBtwOLfSly1D/7coxwMw==', 'http://111.230.226.158/uploadfile/20180606/005040-未标题-1.png', '2018-03-01 11:10:39', NULL, '2018-11-07 19:27:37', '', 3, '备注1', b'1');
INSERT INTO `sys_user` VALUES (39, 'xiaoli', 'b9881834c370bb0c6929edd4c5f69066ae54b9db', '小李', 1, 12, '131845658952', '4984894@qq.com', '江西省赣州市', 'LWGAjYbsSXqSlYBSdIRZ6w==', 'http://111.230.226.158/uploadfile/20180606/005116-露露.jpg', '2018-04-01 16:15:30', NULL, '2018-11-01 15:16:25', NULL, 0, '普通用户角色', b'1');
INSERT INTO `sys_user` VALUES (40, 'lilaoshi', 'ae8915e791d4b3957f9423b09775e7c66dee8205', '李老师', 0, NULL, '131585259547', '234234@qq.com', '江西省南昌市', 'CvtWh23inapDR7tV4++rKw==', 'http://111.230.226.158/uploadfile/20180606/005131-daea249fe11bd56b6b75196893a24eeb.png', '2018-04-01 16:24:58', NULL, '2018-11-01 15:16:23', NULL, 0, 'vip用户', b'1');
INSERT INTO `sys_user` VALUES (41, 'songsy', '4b726f584ceadc1cce0a4b2d9c74f839e36b9fb9', '小小阳', 1, NULL, '13585846574', '234234@qq.com', '', 'qm1DHk5hcnvFuE68QN6Jvw==', 'http://111.230.226.158/uploadfile/20180606/005144-938142f6a571eb7da77fad939b1c1295.png', '2018-04-19 22:01:34', NULL, '2018-11-01 15:16:22', NULL, 0, '', b'1');
INSERT INTO `sys_user` VALUES (42, 'zhangshp', 'eafa061e4b562bd511f71be1316a17da03ea2add', 'Jackie', 1, NULL, '18608712365', '1061458899@qq.com', '', 'N9WrBWyZmHnWOw3CxLywbw==', 'http://111.230.226.158/uploadfile/20180606/005157-84fa91720e41abf9055b4481b0463d2e.png', '2018-06-04 22:50:12', NULL, '2018-11-01 15:16:21', NULL, 0, '', b'1');
INSERT INTO `sys_user` VALUES (43, 'ssy', '6990c64e7809cbf432aff6fdbbe2d13611e76aaf', 'ssy', 1, NULL, '13184582591', '1213@qq.com', '', '6p6fMzSuXAnEGnuJKQrFqg==', 'http://111.230.226.158/uploadfile/20180606/005214-2caa79abf3b641f3461fdcdd6211c517.jpg', '2018-06-05 14:08:03', NULL, '2018-11-01 15:16:21', NULL, 0, '', b'1');
INSERT INTO `sys_user` VALUES (44, 'dsag', 'f80f110b98be79545dc1749b95ecf69c7eab66d4', 'sdag', 1, NULL, '13750527999', '1147052555@qq.com', '', 'UADO6SoBcv0QHy83TzP0fQ==', 'http://111.230.226.158/uploadfile/20180606/005229-cfc8ece2ed0691d167902f220df3bb89.jpeg', '2018-06-05 14:52:12', NULL, '2018-11-01 15:16:20', NULL, 0, '', b'1');
INSERT INTO `sys_user` VALUES (45, 'zihan-admin', '3a5139fb05ff332dd39fca4c5342dc6224380139', 'zihan', 1, NULL, '16620808680', '2351314567@qq.com', '', 'KSujCnsyc9UqIWlKF9fHqw==', 'http://111.230.226.158/uploadfile/20180606/005243-dc3f0d34340e33715c63684e9d0a5204.jpg', '2018-06-05 16:42:23', NULL, '2018-11-01 15:16:19', NULL, 0, '', b'1');
INSERT INTO `sys_user` VALUES (46, '123456', 'c7e9d0c4031409acf7c8e7abae0861a32f0d9250', '123456', 1, NULL, '12345678901', '11@123.com', '', 'g4Bhlvg05vPr1uOe3KelxQ==', 'http://111.230.226.158/uploadfile/20180606/005256-2caa79abf3b641f3461fdcdd6211c517.jpg', '2018-06-05 16:53:03', NULL, '2018-11-01 15:16:19', NULL, 0, '', b'1');
INSERT INTO `sys_user` VALUES (47, 'JasonLi', '5e9ca42e7aa396f8b9749d6ec4b26f2773c9a4f2', 'JasonLi', 1, NULL, '18778522634', '2986334354@qq.com', '', 'Odo/sfwvTtvONVUGUsZdSg==', 'http://111.230.226.158/uploadfile/20180606/005307-4d9ac51353dccd66c17a4fd2a75a7b72.jpg', '2018-06-05 17:19:12', NULL, '2018-11-01 15:16:18', NULL, 0, '', b'1');
INSERT INTO `sys_user` VALUES (49, 'songsy', 'root', '宋某', 1, 88, NULL, '1459074711@qq.com', '广东深圳', NULL, '头像', '2018-11-01 08:40:30', '', '2018-11-01 17:31:26', '', 0, NULL, b'0');
INSERT INTO `sys_user` VALUES (50, '测试updateNull', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, NULL, b'1');
INSERT INTO `sys_user` VALUES (51, 'songsy', 'root', '宋某', 1, 88, NULL, '1459074711@qq.com', '广东深圳', NULL, '头像', '2018-11-01 14:44:19', '', '2018-11-01 15:16:16', '', 0, NULL, b'1');
INSERT INTO `sys_user` VALUES (52, '测试乐观锁', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2018-11-01 15:12:11', '', '2018-11-01 15:12:11', '', 0, NULL, b'1');
INSERT INTO `sys_user` VALUES (53, 'songsy', 'root', '宋某', 1, 88, NULL, '1459074711@qq.com', '广东深圳', NULL, '头像', '2018-11-07 19:17:27', NULL, '2018-11-07 19:17:27', NULL, 1, NULL, b'1');
INSERT INTO `sys_user` VALUES (54, 'songsy', 'root', '宋某', 1, 88, NULL, '1459074711@qq.com', '广东深圳', NULL, '头像', '2018-11-07 19:27:38', NULL, '2018-11-07 19:27:38', NULL, 1, NULL, b'1');

SET FOREIGN_KEY_CHECKS = 1;
