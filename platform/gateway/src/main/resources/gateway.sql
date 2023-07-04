/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MariaDB
 Source Server Version : 101102
 Source Host           : localhost:3306
 Source Schema         : gateway

 Target Server Type    : MariaDB
 Target Server Version : 101102
 File Encoding         : 65001

 Date: 04/07/2023 17:54:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for route
-- ----------------------------
DROP TABLE IF EXISTS `route`;
CREATE TABLE `route`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '路由id',
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由uri',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由前缀',
  `del_flag` tinyint(4) NULL DEFAULT NULL COMMENT '1正常 0删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '路由表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of route
-- ----------------------------
INSERT INTO `route` VALUES ('news', 'lb://news', '/news', 1, '2023-07-04 09:14:48', NULL, NULL, NULL);
INSERT INTO `route` VALUES ('user', 'lb://user', '/user', 1, '2023-07-04 09:14:50', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for wl
-- ----------------------------
DROP TABLE IF EXISTS `wl`;
CREATE TABLE `wl`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '白名单id',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '白名单url',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '说明',
  `del_flag` tinyint(4) NOT NULL COMMENT '1正常 0删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `wl_url_unique`(`url`) USING BTREE COMMENT 'url唯一'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '白名单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wl
-- ----------------------------
INSERT INTO `wl` VALUES ('01459ef1f4op84fcf92a41b9ffa4f7357', '/user/doc.html', 'swagger', 1, NULL, NULL, NULL, NULL);
INSERT INTO `wl` VALUES ('68867907f0a34367bc32eef24bbfe05e', '/user/login', '登录接口', 1, '2023-07-04 16:42:17', NULL, NULL, NULL);
INSERT INTO `wl` VALUES ('7259ef1f42624fcf92a41b9ffa4f7530', '/user/getCaptcha', '获取验证码', 1, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
