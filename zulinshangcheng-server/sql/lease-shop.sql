/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : lease-shop

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 11/11/2026 14:40:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `address_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `user_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户电话',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市',
  `district` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `address_detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户详细地址（用户手动输入的地址）',
  `lng` double(20, 10) NULL DEFAULT NULL COMMENT '经度',
  `lat` double(20, 10) NULL DEFAULT NULL COMMENT '维度',
  `delete_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（0：未删除；1：删除）',
  `address_default` int(11) NULL DEFAULT NULL COMMENT '默认地址 0：非默认   1：默认',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`address_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 309 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address
-- ----------------------------

-- ----------------------------
-- Table structure for admin_info
-- ----------------------------
DROP TABLE IF EXISTS `admin_info`;
CREATE TABLE `admin_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '管理员id',
  `address` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '地址',
  `create_at` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '创建时间',
  `image_url` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '电话',
  `pwd` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '密码',
  `state` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT '1' COMMENT '状态',
  `user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `grade` int(11) NULL DEFAULT 0 COMMENT '等级',
  `character_id` bigint(20) NULL DEFAULT NULL COMMENT '暂未使用',
  `character_names` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '暂未使用',
  `merchant_id` bigint(20) NULL DEFAULT NULL COMMENT '商户id',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_info
-- ----------------------------
INSERT INTO `admin_info` VALUES (83, NULL, '2024-11-26 10:12:40', NULL, 'admin', 'b0bfee8fe33bc6a0234ddf40ccffc004', NULL, 'admin', 0, 0, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for adverti
-- ----------------------------
DROP TABLE IF EXISTS `adverti`;
CREATE TABLE `adverti`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '轮播id',
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片地址',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '跳转地址',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型 1用户端 2pc',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adverti
-- ----------------------------
INSERT INTO `adverti` VALUES (6, '2025-06-20 13:33:59', 'https://leaseshopyh.xianmxkj.com/file/uploadPath/2025/06/20/ba2cb766ffade83407fa28072ca611ac.png', '', 1);
INSERT INTO `adverti` VALUES (10, '2021-10-20 09:22:49', 'https://api.shengqianxiong.com.cn/file/uploadPath/2021/10/25/c1457b6e6e4a2c221a8116833208c7d0.png', 'https://www.gomyorder.cn/custom.jpg', 2);
INSERT INTO `adverti` VALUES (11, '2021-10-20 09:22:49', 'https://api.shengqianxiong.com.cn/file/uploadPath/2021/10/25/c1457b6e6e4a2c221a8116833208c7d0.png', 'https://www.gomyorder.cn/custom.jpg', 2);
INSERT INTO `adverti` VALUES (12, '2024-10-14 15:29:23', 'https://api.shengqianxiong.com.cn/file/uploadPath/2024/10/14/cdc2cbeeba45c768bd6a10ae3874118e.jpg', 'www.baidu.com', 3);

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `android_wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `create_at` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `des` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `ios_version` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `ios_wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `method` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `version` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app
-- ----------------------------

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'bannerid',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `state` int(2) NULL DEFAULT NULL COMMENT '状态1正常2隐藏',
  `classify` int(2) NULL DEFAULT NULL COMMENT '分类1banner图2金刚区分类',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转地址 ',
  `sort` int(4) NULL DEFAULT NULL COMMENT '顺序',
  `describes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `type` int(1) NULL DEFAULT NULL COMMENT '分类 1首页 2好店',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------

-- ----------------------------
-- Table structure for brand_type
-- ----------------------------
DROP TABLE IF EXISTS `brand_type`;
CREATE TABLE `brand_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `section_id` int(11) NULL DEFAULT NULL COMMENT '商城专区id',
  `brand_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌类型名称',
  `img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '品牌类型图片',
  `sort` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `state` int(1) NULL DEFAULT NULL COMMENT '状态 1正常 2隐藏',
  `classify` int(1) NULL DEFAULT NULL COMMENT '分类 1banner图 2金刚区分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '品牌分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of brand_type
-- ----------------------------

-- ----------------------------
-- Table structure for business
-- ----------------------------
DROP TABLE IF EXISTS `business`;
CREATE TABLE `business`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `legal_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '法人名称',
  `legal_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '法人身份证号码',
  `legal_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '法人联系电话',
  `business_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `business_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营业执照注册号',
  `business_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `business_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '营业执照照片',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(1) NULL DEFAULT NULL COMMENT '商家审核状态：0待审核  1已通过 2已拒绝',
  `audit_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核原因',
  `lng` double(20, 7) NULL DEFAULT NULL COMMENT '商家纬度',
  `lat` double(20, 7) NULL DEFAULT NULL COMMENT '商家经度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商家认证表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of business
-- ----------------------------

-- ----------------------------
-- Table structure for cash_out
-- ----------------------------
DROP TABLE IF EXISTS `cash_out`;
CREATE TABLE `cash_out`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请提现id',
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请时间',
  `is_out` int(2) NULL DEFAULT NULL COMMENT '是否转账',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '提现金额',
  `out_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转账时间',
  `relation_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `zhifubao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝账号',
  `zhifubao_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝姓名',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `state` int(11) NULL DEFAULT NULL COMMENT '状态 0待转账 1成功 -1退款',
  `refund` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款原因',
  `classify` int(11) NULL DEFAULT NULL COMMENT '提现方式 1支付宝 2微信小程序  3微信公众号',
  `wx_img` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信图片',
  `rate` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '利息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 134 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cash_out
-- ----------------------------

-- ----------------------------
-- Table structure for character_info
-- ----------------------------
DROP TABLE IF EXISTS `character_info`;
CREATE TABLE `character_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `create_at` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '创建时间',
  `lable` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `menu_ids` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '菜单编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of character_info
-- ----------------------------

-- ----------------------------
-- Table structure for chat
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat`  (
  `chat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '聊天id',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间',
  `store_head` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户头像',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '商户id',
  `store_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户名称',
  `user_head` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `store_count` int(11) NULL DEFAULT 0 COMMENT '商户未读条数',
  `user_count` int(11) NULL DEFAULT 0 COMMENT '用户未读条数',
  `is_delete` int(11) NULL DEFAULT NULL COMMENT '是否删除  1是',
  PRIMARY KEY (`chat_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1799 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat
-- ----------------------------

-- ----------------------------
-- Table structure for chat_content
-- ----------------------------
DROP TABLE IF EXISTS `chat_content`;
CREATE TABLE `chat_content`  (
  `chat_content_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '聊天内容id',
  `chat_conversation_id` bigint(20) NULL DEFAULT NULL COMMENT '聊天id',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容id',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间',
  `status` int(11) NULL DEFAULT 1 COMMENT '是否已读(1未读 2已读)',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '商户id',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型（1文字 2图片 3订单 4商品）',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `send_type` int(11) NULL DEFAULT NULL COMMENT '消息来源（1用户消息 2商户消息）',
  `is_delete` int(11) NULL DEFAULT NULL COMMENT '是否删除  1是',
  PRIMARY KEY (`chat_content_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2375 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_content
-- ----------------------------

-- ----------------------------
-- Table structure for chat_conversation
-- ----------------------------
DROP TABLE IF EXISTS `chat_conversation`;
CREATE TABLE `chat_conversation`  (
  `chat_conversation_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '聊天会话id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '发送用户id',
  `focused_user_id` int(11) NULL DEFAULT NULL COMMENT '接受用户id',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_send_msg` int(11) NULL DEFAULT NULL,
  `is_wx_msg` int(11) NULL DEFAULT NULL,
  `is_delete` int(11) NULL DEFAULT NULL COMMENT '是否删除  1是',
  PRIMARY KEY (`chat_conversation_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 304 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_conversation
-- ----------------------------

-- ----------------------------
-- Table structure for chats
-- ----------------------------
DROP TABLE IF EXISTS `chats`;
CREATE TABLE `chats`  (
  `chat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话id',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `user_count` int(20) NULL DEFAULT 0 COMMENT '用户未读条数',
  `user_count2` int(20) NULL DEFAULT 0 COMMENT '好友未读条数',
  `user_head` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `user_head2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '好友头像',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `user_id2` bigint(20) NULL DEFAULT NULL COMMENT '好友id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `user_name2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '好友昵称',
  `store_count` int(11) NULL DEFAULT 0 COMMENT '后台未读条数',
  `store_head` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后台头像',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '总后台id(总后台传0',
  `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户昵称',
  PRIMARY KEY (`chat_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 208 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天会话' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chats
-- ----------------------------

-- ----------------------------
-- Table structure for chats_content
-- ----------------------------
DROP TABLE IF EXISTS `chats_content`;
CREATE TABLE `chats_content`  (
  `chat_content_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话内容id',
  `chat_id` bigint(20) NULL DEFAULT NULL COMMENT '会话Id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '聊天内容',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `send_type` bigint(20) NULL DEFAULT NULL COMMENT '消息来源（1用户消息 2好友消息）',
  `status` int(11) NULL DEFAULT 1 COMMENT '是否已读(1未读 2已读)',
  `type` int(1) NULL DEFAULT NULL COMMENT '类型（1文字 2图片 3链接）',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `user_id2` bigint(20) NULL DEFAULT NULL COMMENT '好友id',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '后台id（总后台传0）',
  `good_id` bigint(255) NULL DEFAULT NULL COMMENT '商品id',
  PRIMARY KEY (`chat_content_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1323 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天会话内容' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chats_content
-- ----------------------------

-- ----------------------------
-- Table structure for common_info
-- ----------------------------
DROP TABLE IF EXISTS `common_info`;
CREATE TABLE `common_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置文件id',
  `create_at` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `max` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `min` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置文件名称',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '值',
  `condition_from` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 529 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of common_info
-- ----------------------------
INSERT INTO `common_info` VALUES (1, '2023-07-18 09:40:54', NULL, '客服二维码', 1, NULL, 'image');
INSERT INTO `common_info` VALUES (2, '2020-02-21 21:17:17', NULL, '公众号二维码', 2, NULL, 'image');
INSERT INTO `common_info` VALUES (6, '2020-07-09 17:59:45', NULL, '微信公众号APPID	', 5, NULL, 'weixin');
INSERT INTO `common_info` VALUES (7, '2021-03-15 21:16:30', NULL, '短信签名', 6, '省钱兄', 'duanxin');
INSERT INTO `common_info` VALUES (16, '2020-02-25 20:43:38', NULL, '微信公众号秘钥	', 21, NULL, 'weixin');
INSERT INTO `common_info` VALUES (17, '2020-02-25 20:43:59', NULL, '公众号Token', 16, NULL, 'weixin');
INSERT INTO `common_info` VALUES (18, '2020-02-25 20:44:15', NULL, '公众号EncodingAESKey	', 17, NULL, 'weixin');
INSERT INTO `common_info` VALUES (20, '2023-07-17 15:26:13', NULL, '后台管理平台域名配置	', 20, NULL, 'xitong');
INSERT INTO `common_info` VALUES (21, '2023-07-07 13:32:18', NULL, 'h5服务域名配置	', 19, NULL, 'xitong');
INSERT INTO `common_info` VALUES (22, '2025-05-12 15:11:07', NULL, '短信服务商（1 腾讯云  2阿里云  3短信宝）', 79, '1', 'duanxin');
INSERT INTO `common_info` VALUES (23, '2025-06-20 14:14:40', NULL, '后台服务名称	', 12, NULL, 'xitong');
INSERT INTO `common_info` VALUES (27, '2023-07-18 11:04:22', NULL, '通用APP下载地址	', 25, NULL, 'xitong');
INSERT INTO `common_info` VALUES (33, '2023-11-08 19:29:38', NULL, '腾讯云短信clientId	', 31, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (34, '2023-11-08 19:29:43', NULL, '腾讯云短信clientSecret	', 32, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (44, '2022-03-10 20:38:21', NULL, '客服微信号', 44, NULL, 'xitong');
INSERT INTO `common_info` VALUES (47, '2022-01-22 20:52:42', NULL, '微信小程序APPID', 45, NULL, 'weixin');
INSERT INTO `common_info` VALUES (48, '2022-01-22 20:52:48', NULL, '微信小程序秘钥', 46, NULL, 'weixin');
INSERT INTO `common_info` VALUES (51, '2020-03-29 00:21:00', NULL, '分享安卓下载地址', 49, NULL, 'xitong');
INSERT INTO `common_info` VALUES (52, '2020-03-29 00:21:00', NULL, '分享苹果下载地址', 50, NULL, 'xitong');
INSERT INTO `common_info` VALUES (58, '2020-03-29 00:21:00', NULL, '开启微信登录', 53, '是', 'kaiguans');
INSERT INTO `common_info` VALUES (69, '2020-06-04 16:34:00', NULL, 'APP消息推送PushAppKey', 60, NULL, 'push');
INSERT INTO `common_info` VALUES (70, '2020-06-04 16:34:00', NULL, 'APP消息推送PushAppId', 61, NULL, 'push');
INSERT INTO `common_info` VALUES (71, '2020-06-04 16:34:00', NULL, 'APP消息推送PushMasterSecret', 62, NULL, 'push');
INSERT INTO `common_info` VALUES (72, '2020-06-04 16:34:00', NULL, '企业支付宝APPID', 63, NULL, 'zhifubao');
INSERT INTO `common_info` VALUES (73, '2020-06-04 16:34:00', NULL, '企业支付宝公钥', 64, NULL, 'zhifubaos');
INSERT INTO `common_info` VALUES (74, '2020-06-04 16:34:00', NULL, '企业支付宝商户秘钥', 65, NULL, 'zhifubao');
INSERT INTO `common_info` VALUES (77, '2021-03-15 21:22:30', NULL, '文件上传阿里云Endpoint', 68, NULL, 'oss');
INSERT INTO `common_info` VALUES (78, '2021-03-15 21:22:38', NULL, '文件上传阿里云账号accessKeyId', 69, NULL, 'oss');
INSERT INTO `common_info` VALUES (79, '2021-03-15 21:22:46', NULL, '文件上传阿里云账号accessKeySecret', 70, NULL, 'oss');
INSERT INTO `common_info` VALUES (80, '2021-03-15 21:22:53', NULL, '文件上传阿里云Bucket名称', 71, NULL, 'oss');
INSERT INTO `common_info` VALUES (81, '2021-03-15 21:23:00', NULL, '文件上传阿里云Bucket域名', 72, NULL, 'oss');
INSERT INTO `common_info` VALUES (83, '2020-07-27 15:17:00', NULL, '微信APPappId', 74, NULL, 'weixin');
INSERT INTO `common_info` VALUES (84, '2020-07-27 15:17:00', NULL, '微信商户key', 75, NULL, 'weixins');
INSERT INTO `common_info` VALUES (85, '2020-07-27 15:17:00', NULL, '微信商户号mchId', 76, NULL, 'weixin');
INSERT INTO `common_info` VALUES (96, '2020-07-27 15:17:00', NULL, '阿里云登陆或注册模板code（开启阿里云短信必须配置）', 80, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (97, '2020-07-27 15:17:00', NULL, '阿里云找回密码模板code（开启阿里云短信必须配置）', 81, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (98, '2020-07-27 15:17:00', NULL, '阿里云绑定手机号模板code（开启阿里云短信必须配置）', 82, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (99, '2020-07-27 15:17:00', NULL, '阿里云短信accessKeyId', 83, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (100, '2020-07-27 15:17:00', NULL, '阿里云短信accessSecret', 84, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (101, '2024-12-18 23:53:43', NULL, '转账方式 1支付宝证书  2支付宝秘钥 3手动', 98, '2', 'zhifubao');
INSERT INTO `common_info` VALUES (102, '2022-04-21 19:40:07', NULL, '支付宝支付方式 1秘钥 2证书', 97, '1', 'zhifubao');
INSERT INTO `common_info` VALUES (103, '2020-03-28 00:50:17', NULL, '订单取消成功通知', 86, NULL, 'xiaoxi');
INSERT INTO `common_info` VALUES (104, '2020-03-28 00:50:17', NULL, '订单发货通知', 87, NULL, 'xiaoxi');
INSERT INTO `common_info` VALUES (105, '2020-03-28 00:50:17', NULL, '退款成功通知', 88, NULL, 'xiaoxi');
INSERT INTO `common_info` VALUES (106, '2020-03-28 00:50:17', NULL, '客户新订单处理通知', 35, NULL, 'xiaoxi');
INSERT INTO `common_info` VALUES (108, '2023-07-14 16:40:21', NULL, '公众号是否自动登录', 108, '是', 'kaiguans');
INSERT INTO `common_info` VALUES (110, '2020-07-27 15:17:00', NULL, '阿里云快递查询AppCode', 94, NULL, 'xitong');
INSERT INTO `common_info` VALUES (118, '2020-07-27 15:17:00', NULL, '邀请推广内容', 101, NULL, 'xitongs');
INSERT INTO `common_info` VALUES (120, '2023-07-18 11:14:42', NULL, '邀请微信分享内容', 103, '邀请好友', 'weixin');
INSERT INTO `common_info` VALUES (128, '2023-07-20 17:19:05', NULL, '用户端腾讯地图key', 128, NULL, 'weixin');
INSERT INTO `common_info` VALUES (130, '2020-11-04 10:31:40', NULL, '提现最低额度', 112, '10', 'fuwufei');
INSERT INTO `common_info` VALUES (131, '2023-08-15 16:27:15', NULL, '一级佣金默认分成比例', 130, '0.1', 'fuwufei');
INSERT INTO `common_info` VALUES (133, '2023-08-15 16:27:15', NULL, '二级佣金默认分成比例', 131, '0.05', 'fuwufei');
INSERT INTO `common_info` VALUES (134, '2023-07-18 11:04:46', NULL, '海报邀请语', 116, '呼朋唤友来注册', 'xitong');
INSERT INTO `common_info` VALUES (154, '2023-07-18 11:15:11', NULL, '是否开启APP微信分享', 136, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (159, '2023-07-18 11:15:15', NULL, 'H5推广是否分享APP下载页面', 141, '否', 'kaiguan');
INSERT INTO `common_info` VALUES (170, '2022-03-17 18:03:01', NULL, '提现手续费（用户提现时候的手续费，可以作为平台服务费使用，设置0.3就是百分之30，举例提现100服务费30，到账70）', 152, '0.1', 'fuwufei');
INSERT INTO `common_info` VALUES (171, '2021-09-06 16:19:51', NULL, '最高提现金额', 153, '1000', 'fuwufei');
INSERT INTO `common_info` VALUES (182, '2022-03-20 15:06:28', NULL, '短信宝用户名', 164, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (183, '2026-04-03 17:56:09', NULL, '短信宝密码', 165, NULL, 'duanxin');
INSERT INTO `common_info` VALUES (184, '2020-11-04 10:31:40', NULL, '支付宝证书路径', 200, NULL, 'zhifubao');
INSERT INTO `common_info` VALUES (200, '2021-07-19 11:32:12', NULL, '帮助中心', 175, '<p>本应用尊重并保护所有使用服务用户的个人隐私权。为了给您提供更准确、更有个性化的服务，本应用会按照本隐私权政策的规定使用和披露您的个人信息。但本应用将以高度的勤勉、审慎义务对待这些信息。除本隐私权政策另有规定外，在未征得您事先许可的情况下，本应用不会将这些信息对外披露或向第三方提供。本应用会不时更新本隐私权政策。 您在同意本应用服务使用协议之时，即视为您已经同意本隐私权政策全部内容。本隐私权政策属于本应用服务使用协议不可分割的一部分。</p><p>1. 适用范围</p><p>(a) 在您注册本应用帐号时，您根据本应用要求提供的个人注册信息；</p><p>(b) 在您使用本应用网络服务，或访问本应用平台网页时，本应用自动接收并记录的您的浏览器和计算机上的信息，包括但不限于您的IP地址、浏览器的类型、使用的语言、访问日期和时间、软硬件特征信息及您需求的网页记录等数据；</p><p>(c) 本应用通过合法途径从商业伙伴处取得的用户个人数据。</p><p>您了解并同意，以下信息不适用本隐私权政策：</p><p>(a) 您在使用本应用平台提供的搜索服务时输入的关键字信息；</p><p>(b) 本应用收集到的您在本应用发布的有关信息数据，包括但不限于参与活动、成交信息及评价详情；</p><p>(c) 违反法律规定或违反本应用规则行为及本应用已对您采取的措施。</p><p>2. 信息使用</p><p>(a)本应用不会向任何无关第三方提供、出售、出租、分享或交易您的个人信息，除非事先得到您的许可，或该第三方和本应用（含本应用关联公司）单独或共同为您提供服务，且在该服务结束后，其将被禁止访问包括其以前能够访问的所有这些资料。</p><p>(b) 本应用亦不允许任何第三方以任何手段收集、编辑、出售或者无偿传播您的个人信息。任何本应用平台用户如从事上述活动，一经发现，本应用有权立即终止与该用户的服务协议。</p><p>(c) 为服务用户的目的，本应用可能通过使用您的个人信息，向您提供您感兴趣的信息，包括但不限于向您发出产品和服务信息，或者与本应用合作伙伴共享信息以便他们向您发送有关其产品和服务的信息（后者需要您的事先同意）。</p><p>3. 信息披露</p><p>在如下情况下，本应用将依据您的个人意愿或法律的规定全部或部分的披露您的个人信息：</p><p>(a) 经您事先同意，向第三方披露；</p><p>(b)为提供您所要求的产品和服务，而必须和第三方分享您的个人信息；</p><p>(c) 根据法律的有关规定，或者行政或司法机构的要求，向第三方或者行政、司法机构披露；</p><p>(d) 如您出现违反中国有关法律、法规或者本应用服务协议或相关规则的情况，需要向第三方披露；</p><p>(e) 如您是适格的知识产权投诉人并已提起投诉，应被投诉人要求，向被投诉人披露，以便双方处理可能的权利纠纷；</p><p>(f) 在本应用平台上创建的某一交易中，如交易任何一方履行或部分履行了交易义务并提出信息披露请求的，本应用有权决定向该用户提供其交易对方的联络方式等必要信息，以促成交易的完成或纠纷的解决。</p><p>(g) 其它本应用根据法律、法规或者网站政策认为合适的披露。</p><p>4. 信息存储和交换</p><p>本应用收集的有关您的信息和资料将保存在本应用及（或）其关联公司的服务器上，这些信息和资料可能传送至您所在国家、地区或本应用收集信息和资料所在地的境外并在境外被访问、存储和展示。</p><p>5. Cookie的使用</p><p>(a) 在您未拒绝接受cookies的情况下，本应用会在您的计算机上设定或取用cookies ，以便您能登录或使用依赖于cookies的本应用平台服务或功能。本应用使用cookies可为您提供更加周到的个性化服务，包括推广服务。</p><p>(b) 您有权选择接受或拒绝接受cookies。您可以通过修改浏览器设置的方式拒绝接受cookies。但如果您选择拒绝接受cookies，则您可能无法登录或使用依赖于cookies的本应用网络服务或功能。</p><p>(c) 通过本应用所设cookies所取得的有关信息，将适用本政策。</p><p>6. 信息安全</p><p>(a) 本应用帐号均有安全保护功能，请妥善保管您的用户名及密码信息。本应用将通过对用户密码进行加密等安全措施确保您的信息不丢失，不被滥用和变造。尽管有前述安全措施，但同时也请您注意在信息网络上不存在“完善的安全措施”。</p><p>(b) 在使用本应用网络服务进行网上交易时，您不可避免的要向交易对方或潜在的交易对方披露自己的个人信息，如联络方式或者邮政地址。请您妥善保护自己的个人信息，仅在必要的情形下向他人提供。如您发现自己的个人信息泄密，尤其是本应用用户名及密码发生泄露，请您立即联络本应用客服，以便本应用采取相应措施。</p><p>7.本隐私政策的更改</p><p>(a)如果决定更改隐私政策，我们会在本政策中、本公司网站中以及我们认为适当的位置发布这些更改，以便您了解我们如何收集、使用您的个人信息，哪些人可以访问这些信息，以及在什么情况下我们会透露这些信息。</p><p>(b)本公司保留随时修改本政策的权利，因此请经常查看。如对本政策作出重大更改，本公司会通过网站通知的形式告知。</p>', 'xieyi');
INSERT INTO `common_info` VALUES (201, '2022-04-18 14:05:03', NULL, '隐私政策', 176, '<p>个人信息保护政策</p><p>最近更新发布日期：2022年03月03日</p><p>版本生效日期：2022年03月03日</p><p>&nbsp;</p><p>温馨提示</p><p>省钱兄深知个人信息对您的重要性，并会尽全力保护您的个人信息安全可靠。我们致力于维持您对我们的信任，恪守以下原则，保护您的个人信息：权责一致原则、目的明确原则、选择同意原则、最小必要原则、确保安全原则、主体参与原则、公开透明原则等。同时，省钱兄承诺，我们将按业界成熟的安全标准，采取相应的安全保护措施来保护您的个人信息。</p><p>本政策由西安省钱兄网络科技有限公司及其关联公司提供，仅适用于我方平台提供的所有产品和服务（以下简称“我们的产品/或服务”）。西安省钱兄网络科技有限公司，是一家提供购物返利、促销信息、活动导购、广告等技术运营服务支持的互联网公司。您访问我方平台，使用我方平台提供的服务，及未设独立隐私权政策的我方平台网站、服务器、及/或登陆相关客户端，均适用本个人信息保护政策。如我们及关联公司（范围详见定义部分）的产品或服务中使用了省钱兄提供的产品或服务但未设个人信息保护政策的，则本政策同样适用于该部分产品或服务。我们及关联公司就其向您提供的产品或服务单独设立有个人信息保护政策的，则相应产品或服务适用相应个人信息保护政策。</p><p>在使用省钱兄各项产品或服务前，请您务必仔细阅读并透彻理解本政策，特别是以粗体/粗体下划线标识的条款，您应重点阅读，一旦您使用或继续使用我们的产品/服务，即意味着您同意本政策（含更新版本）内容，并且同意我们按照本政策收集、使用、保存和共享您的相关信息。</p><p>我们会遵循隐私政策收集、使用您的信息，但不会仅因您同意本隐私政策而采用强制捆绑的方式一揽子收集个人信息。当您使用或开启相关功能或使用服务时，为实现功能、服务所必需，我们会收集、使用相关信息。除非是为实现基本业务功能或根据法律法规要求所必需的必要信息，您均可以拒绝提供且不影响其他功能或服务。我们将在隐私政策中逐项说明哪些是必要信息。精确地理位置、摄像头、麦克风、相册权限，均不会默认开启，只有经过您的明示授权才会在为实现特定功能或服务时使用，您也可以撤回授权。特别需要指出的是，即使经过您的授权，我们获得了这些敏感权限，也不会在相关功能或服务不需要时而收集您的信息。</p><p>如果您未登录帐号，我们会通过设备对应的标识符信息来保障信息推送的基本功能。如果您登录了帐号，我们会根据帐号信息实现信息推送。</p><p>如对本政策内容有任何疑问、意见或建议，您可随时通过省钱兄APP/官网的在线客服系统与我们联系。</p><p>&nbsp;</p><p>第一部分&nbsp;定义</p><p>除您与省钱兄另有约定外，本协议及其补充协议和隐私政策当中的下列名词均采用如下解释：</p><p><strong>（1）本平台：</strong>指省钱兄享有全部著作权的、提供给您及其他用户登录和使用的省钱兄网站及APP客户端。</p><p><strong>（2）平台运营方：</strong>指省钱兄平台的网络及技术服务提供者。关于本政策相关事宜，您可以通过本协议第九条提供的方式与我们联系。<strong>省钱兄运营方可能根据平台业务调整而发生增加、变更，该等增加、变更不会影响您于本协议项下的权益。</strong></p><p><strong>（3）个人信息：</strong>指以电子或者其他方式记录的能够单独或者与其他信息结合识别特定自然人身份或者反映特定自然人活动情况的各种信息。</p><p><strong>（4）个人敏感信息：</strong>指包括身份证件号码、个人生物识别信息、银行账号、财产信息、行踪轨迹、交易信息、14岁以下（含）儿童信息这些个人信息（我们将在本隐私权政策中对具体个人敏感信息以粗体进行显著标识）。</p><p><strong>（5）个人信息删除：</strong>指在实现日常业务功能所涉及的系统中去除个人信息的行为，使其保持不可被检索、访问的状态。</p><p><strong>（6）儿童：</strong>指不满十四周岁的未成年人。</p><p>&nbsp;</p><p>&nbsp;</p><p>第二部分&nbsp;个人信息保护政策</p><p>本政策将帮助您了解以下内容：</p><p>1、我们如何收集和使用您的个人信息</p><p>2、我们如何使用 Cookie 和同类技术</p><p>3、我们如何共享、转让、公开披露您的个人信息</p><p>4、我们如何保护和保存您的个人信息</p><p>5、您的权利</p><p>6、未成年人信息的保护</p><p>7、您的个人信息如何在全球范围转移</p><p>8、关于本政策的更新</p><p>9、如何联系我们</p><p>附录：第三方合作SDK服务列表及其隐私政策链接明细表</p><p><strong>&nbsp;</strong></p><p>一、我们如何收集和使用您的个人信息</p><p><u>个人信息</u>是指以电子或者其他方式记录的能够单独或者与其他信息结合识别特定自然人身份或者反映特定自然人活动情况的各种信息。本个人信息保护政策中涉及的<u>个人信息</u>包括：<strong>基本信息（包括个人姓名、生日、性别、住址、个人电话号码、电子邮箱）；网络身份标识信息（包括系统账号、IP地址、邮箱地址及与前述有关的密码、口令、口令保护答案）；个人财产信息（交易和消费记录以及余额、支付宝账户信息）；个人上网记录（包括网站浏览记录、软件使用记录、点击记录）；个人常用设备信息（包括硬件型号、设备MAC地址、操作系统类型）、软件列表唯一设备识别码（不限于MEID/IMEI/android ID/IDFA/GUID、UUID、OAID、SIM卡IMSI信息在内的描述个人常用设备基本情况的信息；个人位置信息（包括行程信息、精准定位信息、经纬度）；</strong></p><p>个人敏感信息是指一旦泄露、非法提供或滥用可能危害人身和财产安全，极易导致个人名誉、身心健康受到损害或歧视性待遇等的个人信息，本个人信息保护政策中涉及的个人敏感信息包括：<strong>您的财产信息（包括交易记录及账户余额、支付宝账户信息）；网络身份识别信息（包括账户名、账户昵称、邮箱地址及与前述有关的密码和密码保护问题和答案）；其他信息（包括个人电话号码、手机号码、网页浏览记录、精准定位信息）。</strong></p><p>以上个人信息和个人敏感信息包含的内容出自于GB/T35273《个人信息安全规范》，分别与附录A附录A（资料性附录）-个人信息示例-表A.1 个人信息举例中的内容和附录B（资料性附录）-个人敏感信息判定-表B.1 个人敏感信息举例一致。</p><p>在您使用省钱兄产品或服务时，省钱兄可能自动接收并记录您的网站或应用程序上的服务器数值，包括但不限于省钱兄产品或服务的操作状态、网络环境、异常日志和其他一些明确且客观反映在省钱兄产品或服务服务器端的基本记录信息。您理解并同意，前述基本信息以及其他无法通过其单独或者与其他信息结合识别您的个人身份或活动情况的信息不属于您的个人信息。</p><p>我们仅会出于本政策所述的以下目的，收集和使用您的个人信息：</p><p><strong>（一）您须授权我们收集和使用您个人信息的情形</strong></p><p>我们的产品与/或服务包括一些核心功能，这些功能包含了实现网上购物所必须的功能、改进我们的产品与/或服务所必须的功能及保障交易安全所必须的功能<strong>。我们可能会收集、保存和使用下列与您有关的信息才能实现上述这些功能。如果您不提供相关信息，您将无法享受我们提供的产品与/或服务。这些功能包括：</strong></p><p>1、&nbsp;<strong>实现网上购物所必须的功能</strong></p><p><strong>（1）业务功能一：用户注册</strong></p><p>为完成创建账号，您需提供以下信息：您的<strong>姓名、手机号码、创建的用户名和密码</strong>。当您注册时向我们提供注册所用的<strong>手机号码</strong>（用于接收验证码），我们将通过发送短信验证码来验证您的身份是否有效。您的手机号码尾号四位为您的默认昵称，您可以修改补充您的昵称以及绑定您的<strong>支付宝账号/银行账户</strong>（用于账户提现）相关信息，这些信息均属于您的“账户信息”。您可以修改补充您的<strong>昵称、性别、生日、头像</strong>，这些信息均属于您的“账户信息”。当您使用第三方账号（微信账号、Apple账号）登录时，如果您决定绑定该第三方平台的账号，使用第三方账户登录，您将授权省钱兄读取您在第三方帐号注册的公开信息，包括<strong>用户名、手机号、头像、昵称、地区、性别</strong>（具体以第三方登录服务提供的信息为准）以及<strong>身份验证信息</strong>。绑定第三方帐号可以帮助您在遇到账号丢失、忘记密码问题时更便捷的找回账号和密码。我们收集上述信息是用于为您提供账号登录服务以及保障您的账户安全，防范安全风险。如果您拒绝授权上述信息，您将无法使用第三方平台的账号登录省钱兄平台，但不影响我们为您提供的其他产品和正常使用其他服务。</p><p>为了更好地向您提供导购服务以及个性化需求的信息展示，我们可在采取一定技术处理手段后对您包括但不限于第三方账号中的昵称、头像、优惠权益、粉丝昵称和数量信息（以下称“该等信息”）进行使用，并可向您本人或其他用户或好友展示该等信息。我们可能会视省钱兄产品具体情况为您提供相关权限，以便您可以对是否展示前述相关信息进行相应设置。</p><p>您补充的账户信息将有助于我们为您提供个性化的商品推荐和更优的购物体验，但如果您不提供这些补充信息，不会影响您网上购物的基本功能。另外我们会根据您的会员账户使用情况及平台设置的会员等级计算规则确定您当前的会员等级，并为您提供相应会员等级所对应的基本权益。</p><p>在注册过程中，如果您提供以下额外信息，将有助于我们给您提供更好的服务和体验：<strong>性别、出生日期、微信号、微信二维码、头像</strong>。但如果您不提供这些信息，将不会影响本服务的基本功能。</p><p><strong>（2）业务功能二：商品/服务信息展示和搜索</strong></p><p>为了让您快速地找到您所需要的商品或服务，我们可能会收集您使用我们的产品与/或服务的设备信息（包括<strong>设备名称、设备型号、设备识别码（android_id、oaid、imei、mac地址；其中imei、mac地址仅用于安全及风控管理）、操作系统和应用程序版本、语言设置、分辨率、服务提供商网络ID（PLMN））、浏览器类型</strong>来为您提供商品优惠券信息展示的最优方式。</p><p>您也可以通过搜索（包括但不限于关键词、标题及口令）来精准地找到您所需要的商品或服务。我们会保留您的搜索内容以方便您重复输入或为您展示与您搜索内容相关联的商品，搜索的内容将会存储在我们的服务器中，因为存储是实现服务所必需的，在读取分享口令时您的剪切板功能将会被调用。<strong>请您注意，您的搜索关键词信息无法单独识别您的身份，不属于您的个人信息，我们有权以任何的目的对其进行使用；只有当您的搜索关键词信息与您的其他信息相互结合使用并可以识别您的身份时，则在结合使用期间，我们会将您的搜索关键词信息作为您的个人信息，与您的搜索历史记录一同按照本个人信息保护政策对其进行处理与保护。</strong></p><p><strong>为向您提供更便捷、更符合您个性化需求的信息展示、搜索及推送服务，让您有更好的用户体验，我们会根据您的设备信息（设备标识符（IMEI、IDFA、Android ID、IMSI、MAC、OAID的相关信息）、应用信息（应用崩溃信息、通知开关状态、应用软件列表相关信息）、设备参数及系统信息（设备类型、设备型号、操作系统及硬件相关信息）、网络信息：IP地址，WiFi信息，基站信息相关信息）</strong>和服务日志信息，提取您的偏好特征，并基于特征标签产出间接人群画像，用于展示、推送信息和可能的商业广告。例如在省钱兄APP客户端可能设有专门的模块“猜你喜欢”“搜索发现”等功能。我们努力保障您的浏览体验。如果您不想接受我们给您发送的商业广告，您可通过短信提示回复退订或我们提供的其他方式进行<strong>退订或关闭（我的—设置—个性化商品推荐）</strong>；在您使用我们提供的站内搜索服务时，我们也同时提供了不针对您个人特征的通用排序选项，若您想关闭该定制化服务，您可以通过人工客服的方式与我们联系。</p><p>&nbsp;此外，我们也会为了不断改进和优化上述的功能来使用您的上述信息。</p><p><strong>（3）业务功能三：为您提供收藏、关注与分享功能</strong></p><p>&nbsp;&nbsp;&nbsp;&nbsp;在您浏览我们网站或客户端的过程中，您可以选择对感兴趣的商品及/或服务进行收藏、通过我们提供的功能组件向其他第三方分享信息。在您使用上述功能的过程中，我们会收集包括您的收藏及获取优惠券信息的记录、分享历史在内的服务日志信息用于实现上述功能及其他我们明确告知的目的。</p><p><strong>（4）业务功能四：客服与售后功能</strong></p><p>当您与我们联系或提出售中售后、争议纠纷处理申请时，为了保障您的账户及系统安全，<strong>我们需要您提供必要的个人信息以核验您的会员身份。此时</strong>我们的电话客服和售后功能可能会使用您的账号信息和订单信息。</p><p><strong>为便于与您联系、尽快帮助您解决问题或记录相关问题的处理方案及结果，我们可能会保存您与我们的通信/通话记录及相关内容（包括账号信息、订单信息、您为了证明相关事实提供的其他信息，或您留下的联系方式信息），如果您针对具体订单进行咨询、投诉或提供建议的，我们会使用您的账号信息和订单信息。</strong></p><p>（5）<strong>业务功能五：交易订单及服务</strong></p><p><strong>为了便于您了解查询部分订单信息并对部分订单信息进行管理，我们会收集您在使用我们服务过程中产品的部分订单信息（包括：订单来源（平台）、订单创建时间、订单金额、所购买的商品及/或服务信息、具体订单号、返利情况、订单状态）用于您对订单进行查询、共享、向您提供客服与售后服务及其他我们明确告知的目的。</strong></p><p>2、&nbsp;<strong>开启内部审计、数据分析和研究</strong></p><p><strong>我们可能会收集您的订单信息、浏览信息、您的兴趣爱好进行数据分析，用来将您感兴趣的商品或服务信息展示给您；为您提供个性化服务或内容；或在您搜索时向您展示您可能希望找到的商品。例如在省钱兄APP客户端可能设有专门的模块“猜你喜欢”等功能。如果您不希望接受个性化内容的影响，您可以在商品展示页选择按照分类进行浏览和查找商品服务；我们还可能会在app中向您提供了不针对个人喜好、个性化的商品和服务。</strong></p><p>3、&nbsp;<strong>安全管理</strong></p><p>为保障您使用我们的产品与/或服务时系统的安全性，更准确地预防钓鱼网站欺诈和保护账户安全，我们可能会通过了解<strong>您的浏览信息、订单信息、您常用的软件信息、设备信息（基站定位、sim卡、GPS地址、传感器、WiFi、蓝牙信息、设备硬件序列号）、您常用的软件信息、设备MAC地址、操作系统类型、软件列表唯一设备识别码（包括MEID/IMEI/android ID/IDFA/GUID、SIM卡IMSI信息）</strong>手段来判断您的账号风险，并可能会记录一些我们认为有风险的链接（“URL”））；我们也会收集您的设备信息（包括设备型号、操作系统版本、设备设置、MAC地址及IMEI、IDFA、OAID等设备标识符、设备环境、移动应用列表软硬件特征信息）对于省钱兄使用问题进行分析、统计流量并排查可能存在的风险、在您选择向我们发送异常信息时予以排查。如您不同意我们记录前述信息，可能无法完成风控验证。</p><p>4、&nbsp;<strong>日志信息</strong></p><p><strong>当您使用省钱兄的搜索和信息展示服务时，我们的服务器会自动记录您的日志信息，包括您输入的搜索关键词信息和点击链接以及您的IP地址、设备信息（IMEI/MAC/Android ID/IDFA/OpenUDID/GUID/IP地址/SIM 卡 IMSI 信息）、浏览器的类型和使用的语言、操作系统版本、网络运营商的信息、访问服务时间和收藏记录。</strong></p><p><strong>当您使用的客户端软件频繁向我们进行报错或显示错误日志信息时，为了检查软件错误，给您提供更好的服务，我们会在您的设备上记录、收集您的配置信息、异常数据等用于诊断并完善产品及服务的文件。</strong></p><p><strong>&nbsp;</strong></p><p><strong>（二）您可选择是否授权我们收集和使用您的个人信息的情形</strong></p><p>1、为使您购物更便捷或更有乐趣，从而提升您在省钱兄的购物体验，我们的以下附加功能中可能会收集和使用您的个人信息。<strong>如果您不提供这些个人信息，您依然可以进行网上购物，但您可能无法使用这些可以为您所带来购物乐趣的附加功能或在购买某些商品时需要重复填写一些信息。</strong>这些附加功能包括：</p><p><strong>（1）关于位置信息：</strong>经您授权，我们会收集您的<strong>位置信息</strong>（我们仅收集您当时所处的地理位置，但不会将您各时段的位置信息进行结合以判断您的行踪轨迹，也不会保存您的轨迹）来判断您所处的地点。地理位置信息属于敏感信息，拒绝提供该信息会影响您在第三方平台享受的部分服务（首页-热门-省钱外卖、首页-生活-携程/飞猪、首页-生活-加油优惠需要您提供具体位置信息来进行定位、导航服务），但不影响您正常使用省钱兄产品的其他功能，您也可以随时取消您的地理位置信息授权。</p><p><strong>（2）基于相机/摄像头的附加功能：</strong>您可以在开启相机/摄像头权限后使用该功能进行拍摄照片或视频用于商品的照片或视频素材下载、分享或设置账户头像及图搜功能。您还可以通过上传带有您账户信息的照片来实现客服对您的身份核验（我的-联系我们-专属客服）。</p><p><strong>（3）基于客服功能的附加功能：</strong>在您使用客服功能时，我们可能还会需要收集您的如下个人敏感信息：<strong>联系方式（您与我们联系时使用的电话号码/电子邮箱或您向我们主动提供的其他联系方式）、您与我们的沟通信息（包括文字/图片/音视频）、与您需求相关联的其他必要信息</strong>。在此过程中您需开启麦克风权限<strong>（仅限IOS申请权限）</strong>。我们收集这些信息是为了调查事实与帮助您解决问题，如您拒绝提供可能导致您无法使用我们的客服用户响应机制。</p><p>当您与我们的人工客服互动时，如使用到上传视频功能则需要您开启麦克风权限（我的-联系我们-专属客服-人工客服）。请您知晓，<strong>即使您已同意开启麦克风权限，我们也仅会在您主动点击客户端内麦克风图标时通过麦克风获取语音信息。</strong></p><p><strong>（4）基于日历的附加服务：</strong>在您开启我们可读取/写入您日历的权限后，我们将收集您的日历信息用于向您提供爆品推荐（首页-爆品推荐）、外卖点餐（首页-省钱外卖icon）这些服务的提醒。请您注意，我们仅会在以上情况下读取您购物相关的日历信息。</p><p><strong>（5）基于本地存储的附加功能：</strong>为了提供更好的服务，我们在系统运行时需要记录相关信息到您手机的外部存储器中，需要您开启可读取/写入您存储的权限。您也可以在系统设置中关闭此权限。</p><p><strong>（6）基于电话状态的附加服务</strong>：为保障您的账号与使用安全，您需要授权我们读取本机识别码。</p><p><strong>（7）基于快速识别商品搜索的附加功能（仅限安卓申请权限）</strong>。在您开启悬浮窗权限后，您可通过首页搜索栏处的相机图标拍摄商品照片在本APP内进行对应/相似商品的优惠信息搜索，或跳转第三方平台进行商品搜索。</p><p>2、上述附加功能可能需要您在您的设备中向我们开启您的<strong>地理位置（位置信息）、相册（图片库）、相机/摄像头、麦克风、日历、存储、悬浮窗的访问权限，</strong>以实现这些功能所涉及的信息的收集和使用。并可以决定将这些权限随时的开启或关闭。</p><p>3、系统权限的调用</p><p>省钱兄平台在提供服务过程中，<strong><u>会调用您的一些设备权限，您可以在您的设备设置或我们客户端“我-设置-隐私—系统权限”中逐项选择关闭部分或全部权限，从而拒绝省钱兄收集与使用相应的个人信息</u></strong>。在不同设备中，权限显示方式及关闭方式有所不同，请您参照设备或系统开发系统说明与指引。<strong><u>您的设备系统可能会在必要信息授权的同时开放其他权限，省钱兄自身无法通过手动或自动设置关闭授权。以下是省钱兄调用权限对应的业务功能、调用权限的目的以及调用权限前向您询问的情况：</u></strong></p><p>设备权限</p><p>业务功能</p><p>调用目的</p><p>可否手动关闭</p><p>定位</p><p>外卖、第三方平台携程、飞猪、淘油宝、团油</p><p>定基于（精准）位置信息的个性化推荐功能</p><p>可以</p><p>存储</p><p>数据存储</p><p>读取、修改或删除SD 卡的内容</p><p>可以</p><p>相机/摄像头</p><p>头像</p><p>实现拍照上传、反馈情况功能</p><p>可以</p><p>麦克风（仅IOS）</p><p>在线客服</p><p>使用客服功能的视频功能，进行音频录制、视频拍摄</p><p>可以</p><p>电话状态</p><p>问题诊断</p><p>获取电话、设备信息</p><p>可以</p><p>相册（图片库）</p><p>在线客服</p><p>上传/下载保存头像、照片、视频</p><p>可以</p><p>通知</p><p>全部功能</p><p>接收平台推送内容</p><p>可以</p><p>日历</p><p>外卖</p><p>读写日历事件，实现日历功能，实现预告提醒</p><p>可以</p><p>悬浮窗</p><p>搜索框图搜页面</p><p>通过照片进行商品识别，已实现加速购物搜索</p><p>可以</p><p>&nbsp;</p><p><strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请您注意：您开启这些权限即代表您授权我们可以收集和使用这些个人信息来实现上述的功能，您关闭权限即代表您取消了这些授权，则我们将不再继续收集和使用您的这些个人信息，也无法为您提供上述与这些权限所对应的功能。您关闭权限的决定不会影响此前基于您的授权所进行的个人信息的处理。</strong></p><p><strong>（三）您充分知晓，以下情形中，我们收集、使用个人信息无需征得您的授权同意：</strong></p><p><strong>1、与国家安全、国防安全有关的；</strong></p><p><strong>2、与公共安全、公共卫生、重大公共利益有关的；</strong></p><p><strong>3、与犯罪侦查、起诉、审判和判决执行等有关的；</strong></p><p><strong>4、出于维护个人信息主体或其他个人的生命、财产等重大合法权益但又很难得到本人同意的；</strong></p><p><strong>5、所收集的个人信息是个人信息主体自行向社会公众公开的；</strong></p><p><strong>6、从合法公开披露的信息中收集的您的个人信息的，如合法的新闻报道、政府信息公开等渠道；</strong></p><p><strong>7、根据您的要求签订合同所必需的；</strong></p><p><strong>8、用于维护所提供的产品与/或服务的安全稳定运行所必需的，例如发现、处置产品与/或服务的故障；</strong></p><p><strong>9、为合法的新闻报道所必需的；</strong></p><p><strong>10、学术研究机构基于公共利益开展统计或学术研究所必要，且对外提供学术研究或描述的结果时，对结果中所包含的个人信息进行去标识化处理的；</strong></p><p><strong>11、法律法规规定的其他情形。</strong></p><p><strong>（四）我们从第三方获得您个人信息的情形</strong></p><p><strong>我们可能从第三方获取您授权共享的账户信息（头像、昵称），并在您同意本个人信息保护政策后将您的第三方账户与您的省钱兄账户绑定，使您可以通过第三方账户直接登录并使用我们的产品与/或服务。我们会将依据与第三方的约定、对个人信息来源的合法性进行确认后，在符合相关法律和法规规定的前提下，使用您的这些个人信息。</strong></p><p><strong>（五）您个人信息使用的规则</strong></p><p>1、我们会根据本个人信息保护政策的约定并为实现我们的产品与/或服务功能对所收集的个人信息进行使用。</p><p>2、在收集您的个人信息后，我们将通过技术手段对数据进行去标识化处理，去标识化处理的信息将无法识别主体。</p><p><strong>3、请您注意，您在使用我们的产品与/或服务时所提供的所有个人信息，除非您删除或通过系统设置拒绝我们收集，否则将在您使用我们的产品与/或服务期间持续授权我们使用。在您注销账号时，我们将停止使用并删除您的个人信息。</strong></p><p>4、我们会对我们的产品与/或服务使用情况进行统计，并可能会与公众或第三方共享这些统计信息，以展示我们的产品与/或服务的整体使用趋势。但这些统计信息不包含您的任何身份识别信息。</p><p>5、当我们展示您的个人信息时，我们会采用包括内容替换、匿名处理方式对您的信息进行脱敏，以保护您的信息安全。</p><p>6、当我们要将您的个人信息用于本政策未载明的其它用途时，或基于特定目的收集而来的信息用于其他目的时，会通过您主动做出勾选的形式事先征求您的同意。</p><p>7、服务交互&nbsp;&nbsp;&nbsp;&nbsp;</p><p>省钱兄APP中含有多种多样的小程序/H5服务，您使用小程序或H5时，省钱兄需要收集的您现该功能所必要的日志信息，用以为您提供小程序或/H5相关的技术支持。</p><p>为保障我们的服务的稳定运行和功能服务，我们的应用中会嵌入授权合作伙伴的软件工具包或其他类似的应用程序。您可以查看附录：第三方合作SDK服务列表及其隐私政策链接明细表我们接入的软件工具包采集信息的详情。我们会对相关程序/H5接口和软件工具包进行严格的安全检测，并与授权合作伙伴约定严格的数据保护措施，令其按照本政策以及其他任何相关的保密和安全措施来处理个人信息。</p><p><strong>当省钱兄与第三方应用交互为您提供服务时，省钱兄可能会根据服务场景的必需和系统权限的设定向您的设备系统或者第三方提供您的位置信息、手机号码。</strong>如您拒绝此类信息收集使用，需要在设备系统中进行设置或关闭相关程序/H5服务。</p><p>二、我们如何使用&nbsp;Cookie 和同类技术</p><p>1、为实现您联机体验的个性化需求，使您获得更轻松的访问体验。我们可能会在您的移动设备上发送一个或多个名为Cookie的小数据文件，指定给您的Cookies是唯一的，它只能被将Cookie发布给您的域中的Web服务器读取。我们向您发送Cookie是为了简化您重复登录的步骤、存储您的购物偏好或您购物车中的商品等数据进而为您提供购物的偏好设置、帮助您优化对广告的选择与互动、帮助判断您的登录状态以及账户或数据安全。</p><p>2、我们不会将 Cookie用于本个人信息保护政策所述目的之外的任何用途。您可根据自己的偏好管理或删除 Cookie。您可以清除软件内保存的所有 Cookie，大部分网络浏览器会自动接受Cookie，但您通常可根据自己的需要来修改浏览器的设置以拒绝 Cookie；另外，您也可以清除软件内保存的所有Cookie。但如果您这么做，您可能需要在每一次访问省钱兄时亲自更改用户设置，而且您之前所记录的相应信息也均会被删除。</p><p>3、SDK的使用。<strong>为了使您能够接收信息推送、在第三方平台分享信息、播放视频必要的功能用途，省钱兄部分产品中可能涉及第三方提供的自动化工具（包括但不限于代码、接口、开发工具包（SDK））嵌入或接入。</strong>如基于终端厂商的要求嵌入通知类SDK，基于统计分析的需求嵌入数据处理服务商的SDK,第三方所调用的权限类型可能包括获取<strong>您的实时位置信息、读/写您的外部存储卡、读取您的手机状态和身份，查看WLAN连接、检索正在运行的应用/蓝牙（详见《第三方合作SDK服务列表及隐私政策链接明细表》）</strong>。我们会对合作伙伴获取有关信息的应用程序接口（API）、软件工具开发包（SDK）进行严格的安全监测。<strong>请您在同意开通相应权限前认真阅读对应第三方服务商的隐私政策。如您不同意该第三方收集相关个人信息，可能无法获得相应服务，但不影响您正常使用省钱兄App的其他功能或服务。</strong></p><p>三、我们如何共享、转让、公开披露您的个人信息</p><p>（一）共享</p><p>&nbsp;&nbsp;1、我们不会与省钱兄以外的任何公司、组织和个人共享您的个人信息，但以下情况除外：</p><p>（1）事先获得您明确的同意或授权；</p><p>（2）根据适用的法律法规、法律程序的要求、强制性的行政或司法要求所必须的情况下进行提供；</p><p>（3）在法律法规允许的范围内，为维护省钱兄、省钱兄的关联方或合作伙伴、您或其他省钱兄用户或社会公众利益、财产或安全免遭损害而有必要提供；</p><p>（4）只有共享您的信息，才能实现我们的产品与/或服务的核心功能或提供您需要的服务；</p><p>（5）应您需求为您处理您与他人的纠纷或争议；</p><p>（6）符合与您签署的相关协议（包括在线签署的电子协议以及相应的平台规则）或其他的法律文件约定所提供；</p><p>（7）基于学术研究而使用；</p><p>（8）基于符合法律法规的社会公共利益而使用。</p><p><strong>2、我们可能会将您的个人信息与我们的关联方共享，但我们只会共享必要的个人信息，且受本个人信息保护政策中所声明目的的约束。我们的关联方如要改变个人信息的处理目的，将再次征求您的授权同意。</strong></p><p><strong>3、我们可能会向合作伙伴等第三方共享您的订单信息、账户信息以及位置信息，以保障为您提供的服务顺利完成。但我们仅会出于合法、正当、必要、特定、明确的目的共享您的个人信息，并且只会共享提供服务所必要的个人信息。我们的合作伙伴无权将共享的个人信息用于任何其他用途。</strong></p><p>我们的合作伙伴可能包括以下类型：</p><p><strong>（1）软件服务提供商、智能设备提供商和系统服务提供商。</strong>省钱兄与第三方软件、设备系统结合为您提供基于位置的服务时，可能会基于您对<strong>系统定位的授权及设定，收取您的位置信息及设备信息并提供给系统</strong>，特殊情景下会包含您另行填写的个人信息。如果您拒绝此类信息收集及使用，您需要在设备系统中进行设置或关闭服务的软件，省钱兄无法通过自动或手动设置关闭相关服务。</p><p><strong>（2）商品或技术服务的供应商。我们可能会将您的个人信息共享给支持我们功能的第三方。</strong>这些支持包括为我们的供货或提供基础设施技术服务、物流配送服务、支付服务、数据处理、安全认证。我们共享这些信息的目的是可以实现我们产品与/或服务的核心购物功能，比如当您通过省钱兄去第三方平台购买商品或服务时，我们需将您的<strong>订单信息</strong>与交易有关的必要信息与第三方商家共享来实现您向其具体平台购买商品或服务的需求，并促使其可以完成后续的售后服务。我们使用神策来分析用户日活、统计数据。</p><p><strong>（3）委托我们进行推广的合作伙伴。</strong>有时我们会代表其他企业向使用我们产品与/或服务的用户群提供促销推广的服务。<strong>我们可能会使用您的个人信息以及您的非个人信息集合形成的间接用户画像与委托我们进行推广的合作伙伴（“委托方”）共享，但我们仅会向这些委托方提供推广的覆盖面和有效性的信息，而不会提供您的个人身份信息（手机号码），或者我们将这些信息进行汇总，以便它不会识别您个人。</strong>比如我们可以告知该委托方有多少人看了他们的推广信息或在看到这些信息后购买了委托方的商品，或者向他们提供不能识别个人身份的统计信息，帮助他们了解其受众或顾客。</p><p><strong>（4）打车、加油服务合作伙伴。</strong>当您选择使用打车或加油服务时，为了您能够顺利用车/加油服务并为您提供更安全、便利的服务，服务提供方将获得<strong>您的手机号信息、叫车时的时间/地点信息、目的地信息、紧急联系人的信息、加油时的地点信息</strong>。</p><p><strong>（5）基于合理商业习惯而共享。</strong>如我们与第三方共享联合营销时，您作为中奖/获胜者的，我们可能会向第三方共享您的个人信息，以便您能够顺利领取您的奖励。</p><p><strong>（6）我们可能会将所收集到的个人信息再进行匿名化处理后用于大数据分析，如形成不包括任何个人信息的热力图、洞察报告等。我们可能对外公开并与我们的合作伙伴分享经统计加工后不含身份识别内容的信息，用于了解用户如何使用我们服务或让公众了解我们服务的总体使用趋势。</strong></p><p><strong>（二）转让</strong></p><p><strong>我们不会将您的个人信息转让给任何公司、组织和个人，但以下情况除外：</strong></p><p>1、事先获得您明确的同意或授权；</p><p>2、根据适用的法律法规、法律程序的要求、强制性的行政或司法要求所必须的情况进行提供；</p><p>3、符合与您签署的相关协议（包括在线签署的电子协议以及相应的平台规则）或其他的法律文件约定所提供；</p><p>4、在涉及合并、收购、资产转让或类似的交易时，如涉及到个人信息转让，我们会要求新的持有您个人信息的公司、组织继续受本个人信息保护政策的约束，否则,我们将要求该公司、组织重新向您征求授权同意。</p><p><strong>（三）公开披露</strong></p><p>我们仅会在以下情况下，公开披露您的个人信息：</p><p>1、获得您明确同意后；</p><p>2、基于法律的披露：在法律、法律程序、诉讼或政府主管部门强制性要求的情况下，我们可能会公开披露您的个人信息。</p><p>四、我们如何保护和保存您的个人信息</p><p>（一）我们保护您个人信息的技术与措施</p><p><strong>我们非常重视个人信息安全，并采取一切合理可行的措施，保护您的个人信息：</strong></p><p><strong>1、数据安全技术措施</strong></p><p>我们会采用符合业界标准的安全防护措施，包括建立合理的制度规范、安全技术来防止您的个人信息遭到未经授权的访问使用、修改，避免数据的损坏或丢失。</p><p>我们的网络服务采取了传输层安全协议等加密技术，确保用户数据在传输过程中的安全。</p><p>我们采取加密技术对用户个人信息进行加密保存，并通过隔离技术进行隔离。</p><p>我们采用严格的数据访问权限控制和多重身份认证技术保护个人信息，避免数据被违规使用。</p><p>我们采用代码安全自动检查、数据访问日志分析技术进行个人信息安全审计。</p><p><strong>2、省钱兄为保护个人信息采取的其他安全措施</strong></p><p>²&nbsp;我们通过建立数据分类分级制度、数据安全管理规范、数据安全开发规范来管理规范个人信息的存储和使用。</p><p>²&nbsp;我们通过信息接触者保密协议、监控和审计机制来对数据进行全面安全控制。</p><p>²&nbsp;我们建立信息保护专职部门、数据安全应急响应组织来推进和保障个人信息安全。</p><p><strong>3、我们仅允许有必要知晓这些信息的省钱兄及省钱兄关联方的员工、合作伙伴访问个人信息，并为此设置了严格的访问权限控制和监控机制。我们同时要求可能接触到您个人信息的所有人员履行相应的保密义务。如果未能履行这些义务，可能会被追究法律责任或被中止与省钱兄的合作关系。</strong>我们同时要求可能接触到您个人信息的所有人员履行相应的保密义务。如果未能履行这些义务，可能会被追究法律责任或被终止与省钱兄的合作关系。</p><p><strong>4、我们会采取一切合理可行的措施，确保未收集无关的个人信息。</strong>我们只会在达成本政策所述目的所需的期限内保留您的个人信息，除非需要延长保留期或受到法律的允许。</p><p>5、互联网并非绝对安全的环境，而且电子邮件、即时通讯、社交软件等与其他用户的交流方式无法确定是否完全加密，我们建议您使用此类工具时请使用复杂密码，并注意保护您的个人信息安全。</p><p>6、互联网环境并非百分之百安全，我们将尽力确保或担保您发送给我们的任何信息的安全性。如果我们的物理、技术、或管理防护设施遭到破坏，导致信息被非授权访问、公开披露、篡改、或毁坏，导致您的合法权益受损，我们将承担相应的法律责任。</p><p><strong>7、安全事件处置</strong></p><p>在通过省钱兄与第三方进行网上商品或服务的交易时，您不可避免地要向交易对方或潜在的交易对方披露自己的个人信息，如联络方式或者邮政地址等。请您妥善保护自己的个人信息，仅在必要的情形下向他人提供。</p><p>为应对个人信息泄露、损毁和丢失等可能出现的风险，我们制定了多项制度，明确安全事件、安全漏洞的分类分级标准及相应的处理流程。我们也为安全事件建立了专门的应急响应团队，按照安全事件处置规范要求，针对不同安全事件启动安全预案，进行止损、分析、定位、制定补救措施、联合相关部门进行溯源和打击。在不幸发生个人信息安全事件后，我们将按照法律法规的要求，及时向您告知：安全事件的基本情况和可能的影响、我们已采取或将要采取的处置措施、您可自主防范和降低风险的建议、对您的补救措施等。我们同时将及时将事件相关情况可能以信函、电话、推送通知等方式告知您，难以逐一告知个人信息主体时，我们会采取合理、有效的方式发布公告。同时，我们还将按照监管部门要求，主动上报个人信息安全事件的处置情况。</p><p>8、如果您对我们的个人信息保护有任何疑问，可通过本政策最下方约定的联系方式联系我们。如您发现自己的个人信息泄密，尤其是您的账户及密码发生泄露，请您立即通过本政策中约定的联系方式联络我们，以便我们采取相应措施。</p><p><strong>（二）您个人信息的保存</strong></p><p>1、您的个人信息将全被存储于中华人民共和国境内。<strong>如您使用跨境交易服务，且需要向境外传输您的个人信息完成交易的，我们会单独征得您的授权同意并要求接收方按照我们的说明、本个人信息保护政策以及其他任何相关的保密和安全措施来处理这些个人信息。</strong></p><p>2、除非法律法规另有规定，省钱兄将按如下期间保存您的信息：</p><p>（1）您在使用省钱兄产品或服务期间，我们将持续为您保存，除非您自主删除这些信息或注销账户；</p><p>（2）当您删除个人信息或注销账户后，我们将及时采取措施将您的个人信息从业务功能系统删除，使其保持不可被检索、访问的状态。</p><p>（3）为了维护您和省钱兄的合法权益，我们有权在您删除或注销账户之日起36个月内继续在单独的系统中保存您的信息用于证据保存。证据保存期限到期后，我们将及时对该等保存的个人信息进行匿名化处理。如您在注销帐号或删除信息时，愿意放弃对省钱兄与删除信息相关的全部法律救济权利省钱兄将依据网络安全法和其他法律法规规定在您删除信息后6个月将相关数据删除。</p><p>（4）如果我们终止服务或运营，我们会至少提前十五个工作日向您通知，并在终止服务或运营后对您的个人信息进行删除或匿名化处理。</p><p>五、您的权利</p><p>省钱兄非常重视您对个人信息的关注，并尽全力保护您对于您个人信息访问、更正、删除以及撤回同意的权利，以使您拥有充分的能力保障您的隐私和安全。您的权利包括：</p><p>（一）查询访问和更正您的个人信息</p><p>除法律法规规定外，您有权随时访问和更正您的个人信息。如果您想行使数据访问权，可以通过以下方式自行访问：</p><p>账户信息——如果您希望访问或编辑您的账户中的个人资料信息和支付信息、更改您的密码、添加安全信息或关闭您的账户，您可以通过访问“省钱兄APP——我的——设置”执行此类操作。</p><p>搜索信息——您可以在省钱兄APP 首页界面的搜索栏中访问或清除您的搜索历史记录。</p><p>对于您在使用我们产品或服务过程中产生的其他个人信息，只要我们不需要过多投入，我们会向您提供。如果您想行使数据访问权，请联系客服详细说明您的需求。</p><p>另外，如您发现我们处理的关于您的个人信息有错误时，您有权要求我们作出更正。您可以通过“（一）查询访问和更正您的个人信息”中罗列的方式提出更正申请。</p><p>如果您无法通过上述途径访问或更正这些个人信息，您可以随时通过省钱兄APP的客服联系我们。我们将在15个工作日内回复您的请求。</p><p>（二）删除您的个人信息</p><p>您在我们的产品与/或服务页面访问“省钱兄APP—我的—设置”或“省钱兄APP 首页界面的搜索栏”中可以直接清除或删除的信息，包括搜索历史信息、浏览信息；当您删除个人信息或注销账户删除信息后，我们将及时采取措施将您个人信息从业务功能系统删除，使其保持不可被检索、访问的状态。在以下情形中，您可以向我们提出删除个人信息的请求：</p><p>（1）如果我们处理个人信息的行为违反法律法规；</p><p>（2）如果我们收集、使用您的个人信息，却未征得您的同意；</p><p>（3）如果我们处理个人信息的行为违反了与您的约定；</p><p>（4）如果您注销了省钱兄账户；</p><p>（5）如果我们终止服务及运营。</p><p>若我们决定响应您的删除请求，我们还将同时通知从我们获得您的个人信息的实体，要求其及时删除，除非法律法规另有规定，或这些实体获得您的独立授权。当您从我们的服务中删除信息后，我们可能不会立即备份系统中删除相应的信息，但会在备份更新时删除这些信息。</p><p>（三）改变您授权同意的范围或撤回您的授权</p><p>您可以通过删除信息、关闭设备功能、在省钱兄软件中进行隐私设置这些方式改变您授权我们继续收集个人信息的范围或撤回您的授权。如通过“我的—设置—系统权限”进行操作；您也可以通过注销账户的方式，撤回我们继续收集您个人信息的全部授权。</p><p><strong>请您理解，每个业务功能需要一些基本的个人信息才能得以完成，当您撤回同意或授权后，我们无法继续为您提供撤回同意或授权所对应的服务，也不再处理您相应的个人信息。但您撤回同意或授权的决定，不会影响此前基于您的授权而开展的个人信息处理。</strong></p><p>（四）注销账户</p><p>您可以在我们的产品中通过“我的—设置—关于—帐号注销”的访问路径直接申请注销账户。<strong>关于您注销账户的方式，请详见《省钱兄账户注销规则》。我们将在收到您注销请求并验证您的身份后的7个工作日内答复。</strong>您注销账户后，我们将停止为您提供产品与/或服务，并依据您的要求，除法律法规另有规定外，我们将删除您的个人信息。</p><p><strong>（五）商品推荐、发送促销营销信息</strong></p><p>如果您不想接受我们给您发送的促销信息，您随时可通过以下方式取消：</p><p>（1）您可以通过移动端APP设置是否接受我们推送给您的商品和促销信息。</p><p>（2）我们会与第三方的平台或媒体（“平台”）合作基于您的个人信息向您推荐个性化的商品。这些个人信息包括诸如在该网站的购物情况、访问该网站关联公司网站的情况及您在您的账户设置中填写的兴趣爱好。平台仅向我们提供了展示商品的窗口，窗口内容的链接是省钱兄站内的个性化商品展示信息，由省钱兄进行管理，因此我们不会向广告商提供您的任何的个人信息。而且我们在推荐窗内设置了屏蔽功能，您可选择屏蔽您不喜欢的广告。如您不愿意接受省钱兄在单个平台上的推荐服务，请联系平台进行关闭。</p><p>（六）响应您的请求</p><p>如果您无法通过上述方式访问、更正或删除您的个人信息，或您需要访问、更正或删除您在使用我们产品与/或服务时所产生的其他个人信息，或您认为省钱兄存在任何违反法律法规或与您关于个人信息的收集或使用的约定，或您需要注销账户时，您均可以通过本协议指定的方式与我们联系。<strong>为了保障安全，我们可能需要您提供书面请求，或以其他方式证明您的身份，我们将在收到您反馈并验证您的身份后的7个工作日内答复您的请求。</strong>对于那些无端重复、需要过多技术手段（例如，需要开发新系统或从根本上改变现行惯例）、给他人合法权益带来风险或者非常不切实际（例如，涉及备份磁带上存放的信息）的请求，我们可能会予以拒绝。</p><p><strong>在以下情形中，按照法律法规要求，我们将无法响应您的请求：</strong></p><p><strong>（1）与国家安全、国防安全有关的；</strong></p><p><strong>（2）与公共安全、公共卫生、重大公共利益有关的；</strong></p><p><strong>（3）与犯罪侦查、起诉和审判等有关的；</strong></p><p><strong>（4）有充分证据表明您存在主观恶意或滥用权利的；</strong></p><p><strong>（5）响应您的请求将导致您或其他个人、组织的合法权益受到严重损害的。</strong></p><p>（七）<strong>获取个人信息副本</strong></p><p><strong>如您想获取我们所收集的关于您的个人基本资料订单信息的副本，您可以通过客服渠道与我们取得联系，我们将在15个工作日内回复您的获取请求。</strong></p><p>（八）<strong>约束信息系统自动决策</strong></p><p><strong>在某些业务功能中，我们可能仅依据信息系统、算法等在内的非人工自动决策机制做出决定。如果这些决定显著影响您的合法权益，您有权要求我们作出解释，我们也将提供适当的救济方式。</strong></p><p>六、未成年人的个人信息保护</p><p>我们的产品和服务主要面向成人。<strong>若您是18周岁以下的未成年人，在使用我们的产品与/或服务前，应事先取得您家长或法定监护人的书面同意。</strong></p><p>1、我们非常重视对未成年人个人信息的保护。省钱兄会根据国家相关法律法规的规定保护未成年人的个人信息。</p><p>2、对于经父母或法定监护人同意而收集未成年人个人信息的情况，我们只会在受到法律允许、父母或监护人明确同意或者保护未成年人所必要的情况下使用或公开披露此信息。</p><p>3、尽管当地法律和习俗对儿童的定义不同，但<strong>我们将不满14周岁的任何人均视为儿童</strong>。</p><p>4、如果我们发现自己在未事先获得可证实的父母或法定监护人同意的情况下收集了未成年人的个人信息，则会设法尽快删除相关数据。</p><p>七、您的个人信息如何在全球范围转移</p><p>原则上，我们在中华人民共和国境内收集和产生的个人信息，将存储在中华人民共和国境内。如未来因业务发展需要，我们通过遍布全球的资源和服务器提供产品或服务，在获得您的授权同意后，您的个人信息可能会被转移到您使用产品或服务所在国家/地区的境外管辖区，或者受到来自这些管辖区的访问。此类管辖区可能设有不同的数据保护法，甚至未设立相关法律。</p><p>在此类情况下，我们会确保您的个人信息得到在中华人民共和国境内足够同等的保护。例如，我们会请求您对跨境转移个人信息的同意，或者在跨境数据转移之前实施数据去标识化等安全举措。</p><p>八、关于本政策的更新</p><p>1、为给您提供更好的服务以及随着我们业务的发展，本个人信息保护政策也会随之更新。但未经您明确同意，我们不会削减您依据本个人信息保护政策所应享有的权利。我们会通过省钱兄移动端上发出更新版本并通过公告或以其他适当方式提醒您相关内容的更新，也请您访问省钱兄移动端以便及时了解最新的个人信息保护政策。</p><p>2、对于重大变更，我们还会提供更为显著的通知（我们会通过包括但不限于电话、短信或在浏览页面做特别提示等方式，说明个人信息保护政策的具体变更内容）。</p><p><strong>本政策所指的重大变更包括但不限于：</strong></p><p><strong>（1）我们的服务模式发生重大变化。如处理个人信息的目的、处理的个人信息类型、个人信息的使用方式等；</strong></p><p><strong>（2）我们在所有权结构、组织架构等方面发生重大变化。如业务调整、破产并购等引起的所有者变更等；</strong></p><p><strong>（3）个人信息共享、转让或公开披露的主要对象发生变化；</strong></p><p><strong>（4）您参与个人信息处理方面的权利及其行使方式发生重大变化；</strong></p><p><strong>（5）我们负责处理个人信息安全的责任部门、联络方式及投诉渠道发生变化时；</strong></p><p><strong>（6）个人信息安全影响评估报告表明存在高风险时。</strong></p><p>九、如何联系我们</p><p>1、如您对本个人信息保护政策或您在使用省钱兄产品或服务时，就您的个人信息的收集、使用、共享、查询、删除、更正等相关事宜有任何问题、意见或建议，或您在使用产品或服务时遇到哪些问题，您都可以通过访问省钱兄APP在线客服系统与我们联系。</p><p>2、我们还设立了个人信息保护专职部门，您可以通过在线客服的方式与我们联系。</p><p>3、特别提醒您，如您反馈的问题涉及您的个人信息，尤其是您的个人敏感信息，请您不要直接在电话或邮件中进行透露，我们将会在确保您个人信息安全的情况下与您进一步沟通。</p><p>4、我们将在收到您的反馈并验证您身份后15个工作日内答复您的相关请求。</p><p><strong>如果您对我们的服务回复不满意，特别是您认为我们的个人信息处理行为损害了您的合法权益，您还可以通过向被告住所地有管辖权的法院提起诉讼来寻求解决方案。</strong></p><p><strong>&nbsp;</strong></p><p><strong>附录：第三方合作SDK服务列表及其隐私政策链接明细表</strong></p><p>为保障省钱兄App 的稳定运行或实现相关功能，我们可能会接入由第三方提供的软件开发包（SDK）实现前述目的。我们会进到审慎义务，对合作方获取信息的软件工具开发包（SDK）进行严格的安全检测，以保护数据安全。</p><p>我们接入的第三方SDK主要服务于您以及其他用户的需求，因此在满足新的服务需求及业务功能变更时我们可能会调整我们接入的第三方SDK。我们会及时在本说明中向您公开说明接入第三方SDK 的最新情况。目前省钱兄App接入的第三方SDK列明如下：</p><p>SDK名称</p><p>场景描述</p><p>个人信息类型</p><p>第三方机构名称</p><p>支付宝SDK</p><p>使用支付宝账号授权在省钱兄APP进行提现；第三方账户在APP登录</p><p>网络身份标识信息（支付宝ID）</p><p>支付宝（中国）网络技术有限公司</p><p><a href=\"https://render.alipay.com/p/c/k2cx0tg8\" rel=\"noopener noreferrer\" target=\"_blank\">https://render.alipay.com/p/c/k2cx0tg8</a></p><p>微信</p><p>使用微信授权登录省钱兄APP、分享</p><p>网络身份标识信息（微信ID、头像、昵称）</p><p>深圳市腾讯计算机系统有限公司</p><p><a href=\"https://weixin.qq.com/cgi-bin/readtemplate?lang=zh_CN&amp;t=weixin_agreement&amp;s=privacy\" rel=\"noopener noreferrer\" target=\"_blank\">https://weixin.qq.com/cgi-bin/readtemplate?lang=zh_CN&amp;t=weixin_agreement&amp;s=privacy</a></p><p>苹果SDK</p><p>使用苹果账户授权登录</p><p>网络身份标识信息（苹果账号）</p><p>苹果公司</p><p>https://www.apple.com.cn/legal/privacy/</p><p>极光推送</p><p>用于实现消息推送（或其他推送）功能</p><p>个人常用设备信息、个人位置信息、网络身份标识信息</p><p>深圳市和讯华谷信息技术有限公司</p><p>https://www.jiguang.cn/license/privacy</p><p>bugly</p><p>查找对应崩溃信息</p><p>个人基本资料、系统日志信息</p><p>深圳市腾讯计算机系统有限公司</p><p>https://bugly.qq.com/v2/contract</p><p>MobTech ShareSDK</p><p>应用于APP微信登录，分享、QQ分享、微博分享</p><p>系统运行信息、网络状态信息、匿名设备标识符(OAID)、应用列表信息、基站信息、地理位置</p><p>上海游昆信息技术有限公司http://www.mob.com/about/policy</p><p>SensorsAnalytics SDK</p><p>用户浏览页面和点击行为采集</p><p>个人常用设备信息、个人位置信息、</p><p>神策网络科技 (北京) 有限公司</p><p>https://www.sensorsdata.cn/terms/privacy.html</p><p>阿里百川</p><p>淘宝授权，直播业务</p><p>个人常用设备信息、个人位置信息、网络身份标识信息、个人上网记录、麦克风权限</p><p>北京阿里巴巴云计算技术有限公司</p><p><a href=\"https://terms.alicdn.com/legal-agreement/terms/suit_bu1_b2b/suit_bu1_b2b201703271337_94551.html?spm=a260k.dacugeneral.home2019footer.31.6633436cKdjxBP\" rel=\"noopener noreferrer\" target=\"_blank\">https://terms.alicdn.com/legal-agreement/terms/suit_bu1_b2b/suit_bu1_b2b201703271337_94551.html?spm=a260k.dacugeneral.home2019footer.31.6633436cKdjxBP</a></p><p>定位功能</p><p>设备功能，具体定位信息</p><p>使用加油优惠、飞猪、口碑、美团业务，</p><p>车主邦（北京）科技有限公司：https://v3hydev.czb365.com/userprotocol</p><p>浙江飞猪网络技术有限公司：https://terms.alicdn.com/legal-agreement/terms/Flyingpig/Flyingpig201802061043_85261.html?spm=181.11358650.a1z6c.52.6263223eQ6iN2h&amp;ttid=sem.000001061</p><p>上海拉扎斯信息科技有限公司：https://rule.koubei.com/#/rule?type=suggest&amp;no=ysbh0302</p><p>京东sdk</p><p>监测app账号跳转京东app异常登录</p><p>个人常用设备信息</p><p>北京京东叁佰陆拾度电子商务有限公司</p><p>https://about.jd.com/privacy/</p><p>拼多多</p><p>跳转拼多多app</p><p>个人基本资料</p><p>上海寻梦信息技术有限公司https://mobile.yangkeduo.com/private_policy.html</p><p>友盟移动统计SDK</p><p>对用户浏览页面和点击行为进行采集统计分析服务</p><p>个人常用设备信息、个人位置信息</p><p>友盟同欣（北京）科技有限公司</p><p><u>https://www.umeng</u>.com/page/policy</p><p>flutter sdk</p><p>APP 内功能栏目界面设计构建</p><p>个人上网记录</p><p>Google公司</p><p>https://docs.github.com/cn/github/site-policy/github-privacy-statement</p><p>团油</p><p>加油服务</p><p>个人基本资料、个人位置信息</p><p>车主邦（北京）科技有限公司</p><p>https://v3hydev.czb365.com/userprotocol</p><p>实名认证SDK</p><p>实名校验</p><p>个人常用设备信息、个人基本资料</p><p>中国移动通信集团有限公司</p><p>http://dev.10086.cn/docInside?contentId=10000009833270</p><p>本机号码校验</p><p>使用移动手机号码授权登录</p><p>个人常用设备信息、个人基本资料</p><p>中国移动通信集团有限公司</p><p><a href=\"http://dev.10086.cn/docInside?contentId=10000009826661\" rel=\"noopener noreferrer\" target=\"_blank\">http://dev.10086.cn/docInside?contentId=10000009826661</a></p><p>天翼账号 SDK</p><p>使用电信手机号码授权登录</p><p>个人常用设备信息、个人基本资料</p><p>中国电信集团有限公司</p><p>https://e.189.cn/sdk/agreement/content.do?type=main&amp;appKey=&amp;hidetop=true&amp;returnUrl=</p><p>中国联通</p><p>使用联通手机号码授权登录</p><p>个人常用设备信息、个人基本资料</p><p>中国联合网络通信集团有限公司</p><p><a href=\"https://saas.wostore.cn/solution.html?param=1&amp;num=3\" rel=\"noopener noreferrer\" target=\"_blank\">https://saas.wostore.cn/solution.html?param=1&amp;num=3</a></p><p>魅族集成推送SDK</p><p>提供魅族设备消息推送服务</p><p>个人常用设备信息、个人位置信息、个人上网记录</p><p>珠海市魅族通讯设备有限公司</p><p>http://open-wiki.flyme.cn/doc-wiki/index#id?10</p><p>Zxing 二维码扫描</p><p>二维码扫描</p><p>个人基本资料、个人财产信息、个人常用设备信息、个人上网记录</p><p>Google公司</p><p>https://docs.github.com/cn/github/site-policy/github-privacy-statement</p><p>百川云旺·即时通讯</p><p>淘宝直播</p><p>个人常用设备信息</p><p>北京阿里巴巴云计算技术有限公司</p><p>https://baichuan.taobao.com/portal/newDocIndex.htm?spm</p><p>极光认证</p><p>一键登录号码认证</p><p>个人常用设备信息、个人基本资料、网络信息</p><p>设备信息：设备标识符（IMEI、IDFA、Android ID、MAC、OAID、MEID等相关信息）、应用信息（应用崩溃信息、通知开关状态、软件列表等相关信息）、设备参数及系统信息（设备类型、设备型号、操作系统及硬件相关信息）</p><p>深圳市和讯华谷信息技术有限公司</p><p>https://www.jiguang.cn/identify</p><p>阿里云 OSS 对象存储 SDK</p><p>修改头像，意见反馈等图片上传</p><p>个人常用设备信息</p><p>北京阿里巴巴云计算技术有限公司</p><p>https://help.aliyun.com/document_detail/32043.html?spm=&nbsp;</p><p>a2c4g.11186623.6.1345.254d37bboim1mo</p><p>华为云云视频点播 SDK</p><p>淘宝直播</p><p>个人常用设备信息、个人基本资料、网络信息、网络身份标识信息、个人上网记录</p><p>华为软件技术有限公司</p><p>https://developer.huaweicloud.com/sdk?all#ANDROID</p><p>网易七鱼云客服SDK</p><p>提供在线人工客服系统、智能机器人客服系统</p><p>设备MAC地址、设备信息、设备登录的用户昵称、手机号</p><p>杭州网易质云科技有限公司</p><p>https://reg.163.com/agreement_mobile_ysbh_wap.shtml?v=20171127</p><p><strong>&nbsp;</strong></p><p>个人信息字段定义以及穷举</p><p>设备MAC地址：设备的mac地址，确认移动设备位置的地址</p><p>唯一设备识别码：iOS设备为IDFA、UUID，Android设备为IMEI、oaid、AndroidId、UUID（Android以上识别码可能会收集多个）</p><p>设备IP地址：移动互联网链接协议，确认网络连接的运营商服务</p><p>设备信息：设备品牌、设备型号、操作系统、操作系统版本</p><p>网络信息：当前网络环境（如WIFI、5G、4G、3G、2G）</p><p>运营商信息：移动、联通、电信</p><p>应用列表：判断是否安装了合作的第三方APP（如微信、QQ、新浪微博、淘宝、支付宝、天猫、美团、饿了么）</p><p>&nbsp;</p><p>附件:《省钱兄账号注销规则》</p><p>您在申请注销省钱兄账号前，应当认真阅读《省钱兄账号注销规则》（以下称“《注销规则》”）。</p><p>请您务必审慎阅读、充分理解协议中相关条款内容，其中包括：</p><p>1、与您约定免除或限制责任的条款；</p><p>2、其他以粗体下划线标识的重要条款。</p><p>如您对本《注销规则》有任何疑问，可通过省钱兄APP联系专属客服，您也可通过省钱兄APP的“意见反馈”提出申请需求。</p><p>【特别提示】当您按照注销页面提示阅读并同意本《注销规则》及相关条款与条件且完成全部注销程序后，即表示您已充分阅读、理解并接受本《注销规则》的全部内容。阅读本《注销规则》的过程中，如果您不同意相关任何条款和条件约定，请您立即停止账号注销程序。</p><p>&nbsp;</p><p>亲爱的省钱兄个人消费者用户：</p><p>我们在此善意地提醒您，您注销账号的行为会给您的售后维权带来诸多不便，且注销省钱兄账号后，您的个人信息我们只会在省钱兄的前台系统中去除，使其保持不可被检索、访问的状态，或对其进行匿名化处理。您知晓并理解，根据相关法律规定相关交易记录须在省钱兄后台保存5年甚至更长的时间。</p><p>1.&nbsp;如果您仍执意注销账号，您的账号需同时满足以下条件：</p><p>（1）在最近一个月内，账号没有进行更改密码、更改绑定信息等敏感操作，账户没有被盗、被封等风险；</p><p>（2）账户在省钱兄系统中无资产和虚拟权益（包括但不限于账户收益、用户权益、优惠券等）；</p><p>（3）账号无任何纠纷，包括投诉举报或被投诉举报；</p><p>（4）账号为正常使用中的账号且无任何账户被限制的记录；</p><p>1.&nbsp;省钱兄账号一旦被注销将不可恢复，请您在操作之前自行备份省钱兄账号相关的所有信息和数据。请您保存好订单商品和服务的交易凭证、票据等资料，否则您有可能须支付额外的账户和订单查询费用，或无法享受售后服务。</p><p>2.&nbsp;在省钱兄账户注销期间，如果您的省钱兄账户涉及争议纠纷，包括但不限于投诉、举报、诉讼、仲裁、国家有权机关调查等，省钱兄有权自行终止本省钱兄账号的注销而无需另行得到您的同意。</p><p>3.&nbsp;注销省钱兄账号，您将无法再使用本省钱兄账号，也将无法找回您省钱兄账号中及与账户相关的任何内容或信息，包括但不限于：</p><p>（1）您将无法登录、使用本省钱兄账户；</p><p>（2）本省钱兄账号的个人资料和历史信息（包括但不限于用户名、头像、购物记录、粉丝数量和昵称等）都将无法找回；</p><p>（3）您通过省钱兄账号使用、授权登录或绑定省钱兄账号后使用的省钱兄相关或第三方的其他服务的所有记录将无法找回。您将无法再登录、使用前述服务，您曾获得或即将获得的返利、推广奖励、优惠券、订单、用户权益、及其他卡券、未完成订单权益等视为您自行放弃，将无法继续使用。您理解并同意，省钱兄无法协助您重新恢复前述服务。</p><p>5.如您确认放弃账号中所有已享有或即将享有的任何权益，并要求省钱兄为您强制注销的，以上所有权益将视为您主动放弃并不再享有，同时可能引起的任何法律责任，与省钱兄无关。</p><p>1.&nbsp;注销本省钱兄账号并不代表本省钱兄账户注销前的账号行为和相关责任得到豁免或减轻。</p><p>&nbsp;</p>', 'xieyi');
INSERT INTO `common_info` VALUES (202, '2021-08-14 19:09:54', NULL, '注册协议', 177, '<p>尊敬的用户您好：在您使用本服务之前，请您认真阅读本用户协议，更好的了解我们所提供的服务以及您享有的权利义务。您开始使用时，即表示您已经了解并确认接受了本文件中的全部条款，包括我们对本服务条款随时做的任何修改。</p><p>一、协议的效力</p><p>本协议内容包括协议正文及所有已经发布或将来可能发布的各类规则。所有规则为本协议不可分割的组成部分，与协议正文具有同等法律效力。您承诺接受并遵守本协议的约定。如果您不同意本协议的约定，您应立即停止使用本平台服务。</p><p>二、用户行为规范</p><p>用户同意将不会利用本服务进行任何违法或不正当的活动，包括但不限于下列行为∶</p><p>发布或以其它方式传送含有下列内容之一的信息：</p><p>反对宪法所确定的基本原则的；</p><p>危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；</p><p>损害国家荣誉和利益的；</p><p>煽动民族仇恨、民族歧视、破坏民族团结的；</p><p>破坏国家宗教政策，宣扬邪教和封建迷信的；</p><p>散布谣言，扰乱社会秩序，破坏社会稳定的；</p><p>散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的；</p><p>侮辱或者诽谤他人，侵害他人合法权利的；</p><p>含有虚假、诈骗、有害、胁迫、侵害他人隐私、骚扰、侵害、中伤、粗俗、猥亵、或其它道德上令人反感的内容；</p><p>含有当地法律、法规、规章、条例以及任何具有法律效力之规范所限制或禁止的其它内容的；</p><p>含有不适合在本平台展示的内容；</p><p>以任何方式危害他人的合法权益；</p><p>冒充其他任何人或机构，或以虚伪不实的方式陈述或谎称与任何人或机构有关；</p><p>将依据任何法律或合约或法定关系（例如由于雇佣关系和依据保密合约所得知或揭露之内部资料、专属及机密资料）知悉但无权传送之任何内容加以发布、发送电子邮件或以其它方式传送；</p><p>将侵害他人著作权、专利权、商标权、商业秘密、或其它专属权利（以下简称“专属权利”）之内容加以发布或以其它方式传送；</p><p>将任何广告信函、促销资料、“垃圾邮件”、““滥发信件”、“连锁信件”、“直销”或其它任何形式的劝诱资料加以发布、发送或以其它方式传送；</p><p>将设计目的在于干扰、破坏或限制任何计算机软件、硬件或通讯设备功能之计算机病毒（包括但不限于木马程序（trojan horses）、蠕虫（worms）、定时炸弹、删除蝇（cancelbots）（以下简称“病毒”）或其它计算机代码、档案和程序之任何资料，加以发布、发送或以其它方式传送；</p><p>干扰或破坏本服务或与本服务相连线之服务器和网络，或违反任何关于本服务连线网络之规定、程序、政策或规范；</p><p>跟踪、人肉搜索或以其它方式骚扰他人；</p><p>故意或非故意地违反任何适用的当地、国家法律，以及任何具有法律效力的规则；</p><p>未经合法授权而截获、篡改、收集、储存或删除他人个人信息、站内邮件或其它数据资料，或将获知的此类资料用于任何非法或不正当目的。</p><p>三、知识产权</p><p>本平台所有设计图样以及其他图样、产品及服务名称。任何人不得使用、复制或用作其他用途。未经我们许可，任何单位和个人不得私自复制、传播、展示、镜像、上载、下载、使用，或者从事任何其他侵犯我们知识产权的行为。否则，我们将追究相关法律责任。</p><p>我们鼓励用户充分利用平台自由地张贴和共享自己的信息，但这些内容必须位于公共领域内，或者用户拥有这些内容的使用权。同时，用户对于其创作并在本平台上发布的合法内容依法享有著作权及其相关权利。</p><p>四、免责声明</p><p>互联网是一个开放平台，用户将照片等个人资料上传到互联网上，有可能会被其他组织或个人复制、转载、擅改或做其它非法用途，用户必须充分意识此类风险的存在。用户明确同意其使用本服务所存在的风险将完全由其自己承担；因其使用本服务而产生的一切后果也由其自己承担，我们对用户不承担任何责任。</p><p>对于用户上传的照片、资料、证件等，已采用相关措施并已尽合理努力进行审核，但不保证其内容的正确性、合法性或可靠性，相关责任由上传上述内容的会员负责。</p><p>尽管已采取相应的技术保障措施 ，但用户仍有可能收到各类的广告信或其他不以招聘/应聘为目的邮件或其它方式传送的任何内容，本平台不承担责任。</p><p>对于各种广告信息、链接、资讯等，不保证其内容的正确性、合法性或可靠性，相关责任由广告商承担；用户通过本服务与广告商进行任何形式的通讯或商业往来，或参与促销活动，包含相关商品或服务之付款及交付，以及达成的其它任何相关条款、条件、保证或声明，完全为用户与广告商之间之行为，与本平台无关。用户因前述任何交易或前述广告商而遭受的任何性质的损失或损害，本平台不承担任何责任。</p><p>本平台不保证其提供的服务一定能满足用户的要求和期望，也不保证服务不会中断，对服务的及时性、安全性、准确性也都不作保证。对于因不可抗力或无法控制的原因造成的网络服务中断或其他缺陷，不承担任何责任。我们不对用户所发布信息的删除或储存失败承担责任。我们有权判断用户的行为是否符合本网站使用协议条款之规定，如果我们认为用户违背了协议条款的规定，我们有终止向其提供服务的权利。</p><p>本平台保留变更、中断或终止部分网络服务的权利。保留根据实际情况随时调整平台提供的服务种类、形式的权利。本平台不承担因业务调整给用户造成的损失。本平台仅提供相关服务，除此之外与本服务有关的设备（如电脑、调制解调器及其他与接入互联网有关的装置）及所需的费用（如为接入互联网而支付的电话费及上网费）均应由用户自行负担。</p>', 'xieyi');
INSERT INTO `common_info` VALUES (203, '2021-08-14 19:09:54', NULL, '关于我们', 187, '关于我们，有问题可以在线联系客服哦。', 'xieyi');
INSERT INTO `common_info` VALUES (204, '2023-07-18 09:36:04', NULL, '登录是否获取手机号', 188, '否', 'kaiguan');
INSERT INTO `common_info` VALUES (217, '2020-07-27 15:17:00', NULL, '腾讯地图key', 217, NULL, 'xitong');
INSERT INTO `common_info` VALUES (234, '2020-11-04 10:31:40', NULL, '上传方式 1阿里云oss  2本地', 234, '2', 'oss');
INSERT INTO `common_info` VALUES (236, '2020-11-04 10:31:40', NULL, '退款微信通知', 236, NULL, 'weixin');
INSERT INTO `common_info` VALUES (240, '2020-11-04 10:31:40', NULL, '微信消息通知模版', 240, NULL, 'weixin');
INSERT INTO `common_info` VALUES (241, '2020-11-04 10:31:40', NULL, '超时几分钟短信通知', 241, '5', 'xitong');
INSERT INTO `common_info` VALUES (242, '2020-11-04 10:31:40', NULL, '几条未读短信通知', 242, '1', 'xitong');
INSERT INTO `common_info` VALUES (243, '2020-11-04 10:31:40', NULL, '是否开启微信提现', 243, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (244, '2021-12-22 10:11:36', NULL, '微信提现方式 1自动 2手动', 244, '2', 'xitong');
INSERT INTO `common_info` VALUES (245, '2020-11-04 10:31:40', NULL, '是否开启支付宝提现', 245, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (246, '2020-10-28 16:57:07', NULL, '微信证书地址', 201, NULL, 'weixin');
INSERT INTO `common_info` VALUES (257, '2020-11-04 10:31:40', NULL, '身份验证SecretID', 257, NULL, 'xitong');
INSERT INTO `common_info` VALUES (258, '2020-11-04 10:31:40', NULL, '身份验证SecretKey', 258, NULL, 'xitongs');
INSERT INTO `common_info` VALUES (259, '2020-11-04 10:31:40', NULL, '身份验证实例ID', 259, NULL, 'xitong');
INSERT INTO `common_info` VALUES (262, '2022-05-26 21:31:52', NULL, '小程序是否上架', 262, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (276, '2023-10-20 16:46:12', NULL, '推广文案', 276, '邀请好友注册', 'xitong');
INSERT INTO `common_info` VALUES (289, '2023-07-13 16:29:40', NULL, '推广图片', 289, NULL, 'image');
INSERT INTO `common_info` VALUES (290, '2023-07-10 10:32:30', NULL, '关键词过滤', 290, '滚 111', 'xitong');
INSERT INTO `common_info` VALUES (291, '2025-06-20 14:17:04', NULL, '聊天警告文字提醒', 291, '聊天過程中，請不要透露自己的個人信息', 'xitong');
INSERT INTO `common_info` VALUES (292, '2023-07-10 17:31:30', NULL, '审核状态通知', 292, NULL, 'weixin');
INSERT INTO `common_info` VALUES (293, '2023-07-10 17:31:30', NULL, '用户注销协议', 293, '1、除非您已充分阅读、完全理解并接受本协议所有条款，否则，**请您不要发起账号注销流程、操作。**您单击“同意”或“下一步”，或您开始进行任何账号注销的流程、操作，或者以其他任何明示或者默示方式表示接受本协议的，均视为您已阅读并同意签署本协议。本协议产生法律效力，成为对双方均具有约束力的法律文件。 请您务必审慎阅读、充分理解各条款内容，特别是免除或者限制责任的条款、对您权利进行限制的条款、约定争议解决方式和司法管辖的条款（如第四条相关约定）等。限制、免责条款或者其他涉及您重大权益的条款可能以加粗、加下划线等形式提示您重点注意。', 'xieyi');
INSERT INTO `common_info` VALUES (294, '2023-03-18 11:05:50', NULL, '是否开启评论自动过滤', 294, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (296, '2023-07-18 10:09:35', NULL, '客服类型(1:电话，2:企业微信，3:客服二维码)', 296, '1', 'xitong');
INSERT INTO `common_info` VALUES (297, '2023-07-11 16:07:32', NULL, '企业微信客服APPID\r\n', 297, NULL, 'xitong');
INSERT INTO `common_info` VALUES (298, '2023-07-11 16:04:28', NULL, '客服电话', 298, NULL, 'xitong');
INSERT INTO `common_info` VALUES (300, '2023-07-11 16:07:27', NULL, '企业客户链接', 300, NULL, 'xitong');
INSERT INTO `common_info` VALUES (301, '2023-10-20 17:31:46', NULL, '小程序上线开关1', 301, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (302, '2023-10-20 16:10:15', NULL, '小程序上线开关2', 302, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (303, '2024-07-05 13:58:53', NULL, '实名认证审核方式(1人工审核 2腾讯云自动审核)', 303, '1', 'xitong');
INSERT INTO `common_info` VALUES (309, '2025-06-20 14:14:55', NULL, '用户小程序名称配置', 309, NULL, 'xitong');
INSERT INTO `common_info` VALUES (332, '2024-05-07 23:15:36', NULL, '动态和用户资料审核方式(1人工审核 2百度敏感词自动审核)', 332, '1', 'xitong');
INSERT INTO `common_info` VALUES (414, '2023-07-26 16:27:15', NULL, '地图方式 1腾讯 2天地图', 414, '2', 'xitong');
INSERT INTO `common_info` VALUES (415, '2023-07-26 16:27:15', NULL, '天地图key', 415, NULL, 'xitong');
INSERT INTO `common_info` VALUES (418, '2024-04-17 14:37:56', NULL, '待支付订单多少分钟后自动取消', 417, '1', 'xitong');
INSERT INTO `common_info` VALUES (419, '2024-04-24 11:16:21', NULL, '已支付订单预约时间前多少分钟可以取消', 418, '60', 'xitongs');
INSERT INTO `common_info` VALUES (420, '2024-04-24 11:16:22', NULL, '订单预约时间到期后是否自动完成订单', 419, '是', 'kaiguans');
INSERT INTO `common_info` VALUES (421, '2023-03-18 11:05:50', NULL, '百度云apiKey', 420, NULL, 'xitong');
INSERT INTO `common_info` VALUES (507, '2023-03-18 11:05:50', NULL, '百度云secretKey', 507, NULL, 'xitong');
INSERT INTO `common_info` VALUES (508, '2024-09-19 17:49:55', NULL, '实名认证自动审核', 508, '否', 'kaiguan');
INSERT INTO `common_info` VALUES (509, '2024-09-19 17:49:55', NULL, '平台收取佣金比例', 509, '0.3', 'fuwufei');
INSERT INTO `common_info` VALUES (510, '2020-11-04 10:30:40', NULL, '小程序消息：退款成功通知', 510, NULL, 'xiaoxi');
INSERT INTO `common_info` VALUES (511, '2020-11-04 10:30:40', NULL, '小程序消息：订单发货通知', 511, NULL, 'xiaoxi');
INSERT INTO `common_info` VALUES (512, '2020-11-04 10:30:40', NULL, '小程序消息：提现成功通知', 512, NULL, 'xiaoxi');
INSERT INTO `common_info` VALUES (513, '2020-11-04 10:30:40', NULL, '小程序消息：提现申请失败通知', 513, NULL, 'xiaoxi');
INSERT INTO `common_info` VALUES (514, '2020-07-27 15:17:00', NULL, '商品送积分（1，开，2，关）', 514, '2', 'xitongs');
INSERT INTO `common_info` VALUES (515, '2020-07-27 15:17:00', NULL, '商品购买赠送积分比例（1元）', 515, '2', 'xitongs');
INSERT INTO `common_info` VALUES (516, '2024-11-07 10:39:50', NULL, '用户协议配置', 516, '用户协议配置', 'xieyi');
INSERT INTO `common_info` VALUES (517, '2024-11-07 10:39:50', NULL, '官方邀请码', 517, '666666', 'xitong');
INSERT INTO `common_info` VALUES (518, '2024-11-07 10:39:50', NULL, '折旧/赔付规则须知', 518, '租赁设备正常使用所产生的折旧费用由商家承担，人为损坏，碰撞，划痕等问题，租户需承担维修费用。租赁产品到期归还时不符合还机标准条件的，乙方应当按照下述标准向甲方支付相应的赔偿金。', 'xieyi');
INSERT INTO `common_info` VALUES (519, '2024-11-07 10:39:50', NULL, '租期计算', 519, '比如您选择的租期是2号到8号（计费时间为此7天），来回物流 不算租期，寄回时间以快递员揽收时间为准;若到店自取计费时间 从您提货次日起算，到归还日收费停止。超时时长固定----例如3小时，租期时长超过三小时按一天计费', 'xieyi');
INSERT INTO `common_info` VALUES (520, '2024-11-07 10:39:50', NULL, '租赁服务相关协议', 520, '这里是租赁服务协议', 'xieyi');
INSERT INTO `common_info` VALUES (521, '2026-04-16 10:05:55', NULL, '寄回地址', 521, NULL, 'xitong');
INSERT INTO `common_info` VALUES (522, '2024-11-07 10:39:50', NULL, '微信订单通知模板', 522, NULL, 'weixin');
INSERT INTO `common_info` VALUES (523, '2024-11-07 10:39:50', NULL, '微信充值订单通知模版', 523, NULL, 'weixin');
INSERT INTO `common_info` VALUES (524, '2025-02-20 13:20:56', NULL, '确认归还提示', 524, '<p>温馨提示：您将货物寄出以后，请及时填写物流订单编号，商家查到物流后会退还您的押金；若您到店归还，商家在收货时会退还押金。</p>', 'xieyi');
INSERT INTO `common_info` VALUES (525, '2025-02-05 20:07:21', NULL, '新旧程度', 525, '全新,99新,95新,9成新,8成新,5成新', 'xitong');
INSERT INTO `common_info` VALUES (526, '2025-02-05 20:07:21', NULL, '保障说明', 526, '假一赔十，极速退款', 'xitong');
INSERT INTO `common_info` VALUES (527, '2026-04-16 10:05:44', NULL, '自提地址', 527, NULL, 'xitong');
INSERT INTO `common_info` VALUES (528, '2025-02-20 14:46:50', NULL, '继续租赁超出未归还提示', 528, '若上次订单超出归还日期尚未归还，则当前选择归还日期需减去超出天数；若上次订单有剩余天数，则当前日期叠加租赁天数。', 'xitong');

-- ----------------------------
-- Table structure for continuing_order
-- ----------------------------
DROP TABLE IF EXISTS `continuing_order`;
CREATE TABLE `continuing_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '继续租赁表id',
  `order_id` int(11) NULL DEFAULT NULL COMMENT '订单id',
  `start_date` date NULL DEFAULT NULL COMMENT '起租日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束租期',
  `lease_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '租金',
  `lease_day` int(11) NULL DEFAULT NULL COMMENT '租赁天数',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租赁描述',
  `discount_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `sort` int(10) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of continuing_order
-- ----------------------------

-- ----------------------------
-- Table structure for express_delivery
-- ----------------------------
DROP TABLE IF EXISTS `express_delivery`;
CREATE TABLE `express_delivery`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '快递id',
  `express_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快递公司名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `parcel_services` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快递名称的缩写',
  `parcel_describe` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮寄情况描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '快递公司表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of express_delivery
-- ----------------------------

-- ----------------------------
-- Table structure for faqs
-- ----------------------------
DROP TABLE IF EXISTS `faqs`;
CREATE TABLE `faqs`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '常见问题',
  `answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '回复',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '常见问题表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of faqs
-- ----------------------------

-- ----------------------------
-- Table structure for gold_records
-- ----------------------------
DROP TABLE IF EXISTS `gold_records`;
CREATE TABLE `gold_records`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `gold_change` int(11) NULL DEFAULT NULL COMMENT '金币：1增加  -1减少',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '金币变化原因',
  `related_id` int(11) NULL DEFAULT NULL COMMENT '关联id，比如邀请好友，买卖商品，发布商品等id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `gold_coins` int(11) NULL DEFAULT NULL COMMENT '金币数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '金币记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gold_records
-- ----------------------------

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `descrition` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '商品描述',
  `img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '商品图片',
  `price` decimal(18, 2) NULL DEFAULT NULL COMMENT '价格',
  `sales` int(11) NULL DEFAULT NULL COMMENT '商品销量',
  `status` int(11) NULL DEFAULT 0 COMMENT '商品状态(默认1正常 2下架)',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '商品类型id',
  `member_price` decimal(18, 2) NULL DEFAULT NULL COMMENT '会员价格',
  `original_price` decimal(18, 2) NULL DEFAULT NULL COMMENT '商品原价',
  `commission_price` decimal(18, 2) NULL DEFAULT NULL COMMENT '商品佣金',
  `home_goods` int(11) NULL DEFAULT NULL COMMENT '首页商品(0默认 1是首页商品)',
  `is_select` int(11) NULL DEFAULT NULL COMMENT '精选推荐（0默认 1精选推荐）',
  `buy_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '必买理由',
  `cover_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '商品封面图片',
  `is_express` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否需要发货(1包邮 2邮寄 3自取）',
  `is_recommend` int(11) NULL DEFAULT NULL COMMENT '热爆租品（0默认 1热爆租品）',
  `postage_price` decimal(18, 2) NULL DEFAULT NULL COMMENT '邮费',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '一级商品类型id',
  `two_type_id` int(11) NULL DEFAULT NULL COMMENT '二级商品类型id',
  `three_type_id` int(11) NULL DEFAULT NULL COMMENT '三级商品类型id',
  `merchant_id` bigint(20) NULL DEFAULT NULL COMMENT '商户id',
  `merchants` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '商户',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT '品牌id',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺id',
  `store_type_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺分类id',
  `browse` int(11) NULL DEFAULT 0 COMMENT '浏览次数',
  `is_ji_fen_goods` int(11) NULL DEFAULT NULL COMMENT '是否是积分商品 1是',
  `activity_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '活动id',
  `antecedent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `label_id` int(11) NULL DEFAULT NULL COMMENT '标签id',
  `lng` double(10, 6) NULL DEFAULT NULL COMMENT '经度',
  `lat` double(10, 6) NULL DEFAULT NULL COMMENT '纬度',
  `province` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市',
  `district` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `support_buyout` int(1) NULL DEFAULT NULL COMMENT '是否支持买断：0不支持买断 1支持买断',
  `accessory_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '配件：',
  `gift_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '赠品：',
  `guarantee` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '保障说明：',
  `accessory_status` int(1) NULL DEFAULT NULL COMMENT '是否有配件：0无 1有',
  `gift_status` int(1) NULL DEFAULT NULL COMMENT '是否包含赠品：0无1有',
  `lease_start` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '起租时长',
  `deposit_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '押金',
  `daily_rent` double(20, 2) NULL DEFAULT NULL COMMENT '日租金',
  `member_rent` double(20, 2) NULL DEFAULT NULL COMMENT '会员日租金',
  `old_degree` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '新旧程度',
  `overdue_rent` decimal(10, 2) NULL DEFAULT NULL COMMENT '逾期日租金',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `type_id`(`type_id`) USING BTREE,
  INDEX `price`(`price`, `title`) USING BTREE,
  INDEX `type`(`type`, `type_id`) USING BTREE,
  INDEX `title`(`title`, `type`, `home_goods`) USING BTREE,
  INDEX `is_recommend`(`is_recommend`, `postage_price`, `type_id`, `merchant_id`, `merchants`, `brand_id`, `store_id`, `store_type_id`, `browse`) USING BTREE,
  INDEX `type_2`(`type`) USING BTREE,
  INDEX `create_at`(`create_at`, `price`, `sales`, `title`, `member_price`, `original_price`) USING BTREE,
  INDEX `brand_id_2`(`brand_id`) USING BTREE,
  INDEX `idx_goods_title`(`title`) USING BTREE,
  INDEX `idx_goods_type_id`(`type_id`) USING BTREE,
  INDEX `idx_goods_two_type_id`(`two_type_id`) USING BTREE,
  INDEX `idx_goods_three_type_id`(`three_type_id`) USING BTREE,
  INDEX `idx_goods_status`(`status`) USING BTREE,
  INDEX `idx_goods_is_ji_fen_goods`(`is_ji_fen_goods`) USING BTREE,
  INDEX `idx_goods_create_at`(`create_at`) USING BTREE,
  INDEX `idx_goods_type_status`(`type_id`, `status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 160 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------

-- ----------------------------
-- Table structure for goods_accessory
-- ----------------------------
DROP TABLE IF EXISTS `goods_accessory`;
CREATE TABLE `goods_accessory`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配件id',
  `accessory_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配件名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `accessory_num` int(10) NULL DEFAULT NULL COMMENT '配件数量',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '一级商品分类id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品配件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_accessory
-- ----------------------------

-- ----------------------------
-- Table structure for goods_gift
-- ----------------------------
DROP TABLE IF EXISTS `goods_gift`;
CREATE TABLE `goods_gift`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '赠品id',
  `gift_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '赠品名称',
  `gift_num` int(10) NULL DEFAULT NULL COMMENT '赠品数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '一级商品分类id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '赠品数量' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_gift
-- ----------------------------

-- ----------------------------
-- Table structure for goods_recovery
-- ----------------------------
DROP TABLE IF EXISTS `goods_recovery`;
CREATE TABLE `goods_recovery`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `good_id` int(11) NULL DEFAULT NULL COMMENT '回收的商品名称id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回收的商品名称',
  `brand_id` int(11) NULL DEFAULT NULL COMMENT '品牌id',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '是否删除：0未删除  1已删除',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `specs_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '规格id(列表)',
  `specs_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '规格列表的值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '二手物品回收估价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_recovery
-- ----------------------------

-- ----------------------------
-- Table structure for goods_specifications
-- ----------------------------
DROP TABLE IF EXISTS `goods_specifications`;
CREATE TABLE `goods_specifications`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `good_id` int(11) NULL DEFAULT NULL COMMENT '商品id',
  `degree` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新旧程度（如7成新 99新等）',
  `color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色',
  `size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '尺寸（或手机容量大小）',
  `weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '重量',
  `network_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网络类型（全网通等）',
  `version_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本类型（中国大陆等）',
  `stock_quantity` int(11) NULL DEFAULT NULL COMMENT '库存数量',
  `rental_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '租赁价格（单位：元）',
  `purchase_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '买断价格',
  `delete_flag` int(11) NULL DEFAULT 0 COMMENT '是否删除：0未删除 1已删除',
  `create_by` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` int(11) NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `antecedent_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '押金',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租赁物品的规格表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_specifications
-- ----------------------------

-- ----------------------------
-- Table structure for goods_type
-- ----------------------------
DROP TABLE IF EXISTS `goods_type`;
CREATE TABLE `goods_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品类型id',
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '时间',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级id',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_id`(`parent_id`) USING BTREE,
  INDEX `id`(`id`, `parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 115 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_type
-- ----------------------------

-- ----------------------------
-- Table structure for help_classify
-- ----------------------------
DROP TABLE IF EXISTS `help_classify`;
CREATE TABLE `help_classify`  (
  `help_classify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帮助中心分类',
  `help_classify_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级id',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`help_classify_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_classify
-- ----------------------------
INSERT INTO `help_classify` VALUES (1, '常見問題', 1, 0, '2025-06-20 14:29:59');
INSERT INTO `help_classify` VALUES (4, '订单问题', 1, 1, '2025-01-06 10:12:12');
INSERT INTO `help_classify` VALUES (5, '支付问题', 2, 1, '2025-01-06 10:12:12');
INSERT INTO `help_classify` VALUES (6, '发货问题', 3, 1, '2025-01-06 10:12:12');
INSERT INTO `help_classify` VALUES (7, '自提问题', 4, 1, '2025-01-06 10:12:12');

-- ----------------------------
-- Table structure for help_word
-- ----------------------------
DROP TABLE IF EXISTS `help_word`;
CREATE TABLE `help_word`  (
  `help_word_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帮助文档id',
  `help_word_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '帮助标题',
  `help_word_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '帮助文档内容',
  `help_classify_id` int(11) NULL DEFAULT NULL COMMENT '帮助分类id',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`help_word_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_word
-- ----------------------------
INSERT INTO `help_word` VALUES (4, '如何取消订单？', '<p>如未发货前，需取消订单，请联系人工客服取消订单后资金将会在1-3个工作日原路退回 (周末及法定节假日顺延);\n\n 如已发货，则无法取消订单，您可以选择拒收，收到退回的商品并检测无损后资金将会在1-3个工作日内退款(收到退回的商品并检测无损后资金将会在1-3个工作日内退款(注: 拒收将收取相应来回物流和保价费用:手机等小件物品100元，电脑等大件物品150元)\n \n非产品硬件故障,不支持7天无理由退货，硬件故障可以免费更换。非产品硬件故障,不支持7天无理由退货， 硬件故障可以免费更换。</p>', 4, 1, '2025-01-06 10:12:12');
INSERT INTO `help_word` VALUES (5, '商品可以提前归还吗？', '如未发货前，需取消订单，请联系人工客服取消订单后资金将会在1-3个工作日原路退回 (周末及法定节假日顺延);\n\n 如已发货，则无法取消订单，您可以选择拒收，收到退回的商品并检测无损后资金将会在1-3个工作日内退款(收到退回的商品并检测无损后资金将会在1-3个工作日内退款(注: 拒收将收取相应来回物流和保价费用:手机等小件物品100元，电脑等大件物品150元)\n \n非产品硬件故障,不支持7天无理由退货，硬件故障可以免费更换。非产品硬件故障,不支持7天无理由退货， 硬件故障可以免费更换。', 4, 2, '2025-01-06 10:12:12');

-- ----------------------------
-- Table structure for invite
-- ----------------------------
DROP TABLE IF EXISTS `invite`;
CREATE TABLE `invite`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '邀请者id',
  `invitee_user_id` int(11) NULL DEFAULT NULL COMMENT '被邀请者id',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '收益',
  `create_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `money_type` int(1) NULL DEFAULT NULL COMMENT '1会员2陪玩',
  `state` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '邀请信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invite
-- ----------------------------

-- ----------------------------
-- Table structure for invite_money
-- ----------------------------
DROP TABLE IF EXISTS `invite_money`;
CREATE TABLE `invite_money`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收益钱包id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `money_sum` decimal(10, 2) NULL DEFAULT NULL COMMENT '总获取收益',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '当前金额',
  `cash_out` decimal(10, 2) NULL DEFAULT NULL COMMENT '累计提现',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invite_money
-- ----------------------------

-- ----------------------------
-- Table structure for ji_fen
-- ----------------------------
DROP TABLE IF EXISTS `ji_fen`;
CREATE TABLE `ji_fen`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '积分明细id',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `create_at` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '时间',
  `des` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `number` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '数量',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6313 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ji_fen
-- ----------------------------

-- ----------------------------
-- Table structure for label_type
-- ----------------------------
DROP TABLE IF EXISTS `label_type`;
CREATE TABLE `label_type`  (
  `label_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `label` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签名称',
  `is_enable` int(1) NULL DEFAULT NULL COMMENT '是否已启用0否 1是',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `is_recovery` int(1) NULL DEFAULT NULL COMMENT '是否是回收类型 0否 1是',
  `is_type` int(1) NULL DEFAULT NULL COMMENT '是否是商品分类 0否 1是',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`label_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分类标签' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of label_type
-- ----------------------------

-- ----------------------------
-- Table structure for lease_days
-- ----------------------------
DROP TABLE IF EXISTS `lease_days`;
CREATE TABLE `lease_days`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `day_name` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '天数',
  `sort` int(1) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租赁天数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lease_days
-- ----------------------------
INSERT INTO `lease_days` VALUES (2, '7', 1, '2024-09-26 16:43:33');
INSERT INTO `lease_days` VALUES (3, '30', 2, '2024-09-26 16:43:37');
INSERT INTO `lease_days` VALUES (4, '60', 3, '2025-01-02 11:18:22');
INSERT INTO `lease_days` VALUES (5, '90', 4, '2025-01-02 11:18:22');
INSERT INTO `lease_days` VALUES (6, '180', 5, '2025-01-02 11:18:22');
INSERT INTO `lease_days` VALUES (7, '365', 6, '2025-01-02 11:18:22');

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `member_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '会员特权id',
  `member_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '特权图标',
  `member_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '特权名称',
  `sort` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`member_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (3, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/2ef451b3e0f7f98464b38430c13051b0.png', '团队特权', '2');
INSERT INTO `member` VALUES (4, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/37c225b26b3f011319de1cb4f1c18baf.png', '九折专享', '1');
INSERT INTO `member` VALUES (6, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/d03a8a0f55240c438209777b896ebd94.png', '身份标识', '4');
INSERT INTO `member` VALUES (9, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/071892b060308ae7f0c7275d26d92f80.png', '免广告', '5');
INSERT INTO `member` VALUES (10, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/b9e72742a7bba9a20cf2d13906af441c.png', '专属客服', '6');
INSERT INTO `member` VALUES (11, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/423a302253532b6a993788efab1ce8f7.png', '生日特权', '7');
INSERT INTO `member` VALUES (12, 'https://pw.xianmxkj.com/file/uploadPath/2022/01/19/dfb149846d4cc92a9f00d261d4c3cca1.png', '专属奖励', '8');

-- ----------------------------
-- Table structure for menu_info
-- ----------------------------
DROP TABLE IF EXISTS `menu_info`;
CREATE TABLE `menu_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `create_at` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '创建时间',
  `menu_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单集合',
  `menu_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu_info
-- ----------------------------

-- ----------------------------
-- Table structure for message_info
-- ----------------------------
DROP TABLE IF EXISTS `message_info`;
CREATE TABLE `message_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容',
  `create_at` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '创建时间',
  `image` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '图片',
  `is_see` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `send_state` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `send_time` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `state` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '1为公告2位用户反馈 3为投诉服务 4为投诉店铺 5为用户消息 6客服消息',
  `title` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `url` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '地址',
  `type` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `platform` int(11) NULL DEFAULT NULL COMMENT '来源',
  `user_id` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `audit_content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `by_user_id` int(11) NULL DEFAULT NULL,
  `platform_id` int(11) NULL DEFAULT NULL,
  `shop_id` int(11) NULL DEFAULT NULL COMMENT '店铺id',
  `data_id` int(11) NULL DEFAULT NULL COMMENT '服务id',
  `phone` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '联系方式',
  `to_user_name` varchar(64) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '暂未使用',
  `from_user_name` varchar(64) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '暂未使用',
  `open_id` varchar(64) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '暂未使用',
  `wx_biz_msg_crypt` varchar(64) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '暂未使用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 412 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_info
-- ----------------------------

-- ----------------------------
-- Table structure for money_details
-- ----------------------------
DROP TABLE IF EXISTS `money_details`;
CREATE TABLE `money_details`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '钱包明细id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型 1增加 2减少',
  `classify` int(11) NULL DEFAULT NULL COMMENT '分类 1结算 2积分 3任务 4提现',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '金额',
  `create_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间',
  `merchant_id` bigint(20) NULL DEFAULT NULL COMMENT '商户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1638 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of money_details
-- ----------------------------

-- ----------------------------
-- Table structure for msg
-- ----------------------------
DROP TABLE IF EXISTS `msg`;
CREATE TABLE `msg`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '短信验证码',
  `phone` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_name`(`code`, `phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of msg
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `consignee` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '收货人',
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `descrition` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '订单备注',
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '详细地址',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '商品图片',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '电话',
  `number` int(11) NULL DEFAULT NULL COMMENT '商品个数',
  `order_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '订单号',
  `pay_money` decimal(18, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `price` decimal(18, 2) NULL DEFAULT NULL COMMENT '价格',
  `status` int(11) NULL DEFAULT NULL COMMENT '订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消 10待评价',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '类型',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `finish_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '完成时间',
  `pay_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付时间',
  `pay_way` int(11) NULL DEFAULT NULL COMMENT '支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）',
  `coupon_user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户优惠券id',
  `commission_price` decimal(18, 2) NULL DEFAULT NULL COMMENT '订单佣金',
  `relation_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '会员邀请码id',
  `provinces` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '省市区',
  `express_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '快递名称',
  `express_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '快递单号',
  `refund` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '退款理由',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `is_express` int(11) NULL DEFAULT NULL COMMENT '是否需要发货(1包邮 2邮寄 3自取）',
  `is_refund` int(11) NULL DEFAULT NULL COMMENT '是否可以退款（2不可退款）',
  `refused_refund` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '拒绝退款理由',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'skuid',
  `pay_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '支付单号',
  `detail_json` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '商品sku信息',
  `group_sku_id` bigint(20) NULL DEFAULT NULL COMMENT '拼团商品skuId',
  `pink_id` bigint(20) NULL DEFAULT NULL COMMENT '拼团id',
  `seckill_id` bigint(20) NULL DEFAULT NULL COMMENT '秒杀id',
  `pink_status` int(11) NULL DEFAULT NULL COMMENT '拼团状态',
  `postage_price` decimal(18, 2) NULL DEFAULT NULL COMMENT '邮费',
  `group_pink_id` bigint(20) NULL DEFAULT NULL COMMENT '加入拼团团体id',
  `order_type` int(11) NULL DEFAULT NULL COMMENT '订单类型（1普通订单 2拼团 3秒杀 4积分订单 5租赁 6买断',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '拼团商品Id',
  `sec_kill_id` bigint(20) NULL DEFAULT NULL COMMENT '秒杀商品id',
  `merchant_id` bigint(20) NULL DEFAULT NULL COMMENT '商户id',
  `express_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发货时间',
  `coupon_money` decimal(18, 2) NULL DEFAULT NULL COMMENT '优惠券金额',
  `virtual_id` bigint(20) NULL DEFAULT NULL COMMENT '虚拟商品id',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺id',
  `store_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '店铺名称',
  `is_repeat` int(11) NULL DEFAULT NULL COMMENT '是否复购（1普通购买 2复购）',
  `is_cart` int(1) NULL DEFAULT NULL COMMENT '购物车订单 1',
  `day_id` int(11) NULL DEFAULT NULL COMMENT '租赁天数id',
  `one_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '一级佣金',
  `one_user_id` int(11) NULL DEFAULT NULL COMMENT '一级用户',
  `two_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '二级佣金',
  `two_user_id` int(11) NULL DEFAULT NULL COMMENT '二级用户',
  `lease_term` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租赁天数',
  `deposit` decimal(10, 2) NULL DEFAULT NULL COMMENT '押金',
  `gross_rent` decimal(10, 2) NULL DEFAULT NULL COMMENT '总租金',
  `continue_date` datetime(0) NULL DEFAULT NULL COMMENT '续租日期',
  `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT '取消时间',
  `lease_start_date` date NULL DEFAULT NULL COMMENT '预计起租日',
  `return_date` date NULL DEFAULT NULL COMMENT '预计归还日',
  `estimate_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '预估价格',
  `daily_rent` decimal(10, 2) NULL DEFAULT NULL COMMENT '日租金',
  `purchase_time` datetime(0) NULL DEFAULT NULL COMMENT '买断时间',
  `address_id` int(11) NULL DEFAULT NULL COMMENT '地址id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `return_time` datetime(0) NULL DEFAULT NULL COMMENT '归还日期',
  `supplement_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '补缴的租金',
  `supplement_status` int(1) NULL DEFAULT 2 COMMENT '补缴是否支付成功：1是  2否',
  `supplement_time` datetime(0) NULL DEFAULT NULL COMMENT '补缴时间',
  `supplement_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '补缴单号',
  `is_evaluate` int(1) NULL DEFAULT 0 COMMENT '是否已评论 0否 1是',
  `total_continue_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '继续租赁总金额',
  `seller_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '商家发货的快递单号',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '用户收货时间',
  `send_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发货人手机号',
  `evaluation_time` datetime(0) NULL DEFAULT NULL COMMENT '评价时间',
  `overdue_status` int(1) NULL DEFAULT 2 COMMENT '是否逾期：1已逾期  2未逾期',
  `buyout_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '当时买断价格',
  `re_order_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '续租订单号',
  `buy_order_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '买断订单号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `user_coupons_id`(`coupon_user_id`) USING BTREE,
  INDEX `seckill_id`(`seckill_id`) USING BTREE,
  INDEX `group_id`(`group_id`) USING BTREE,
  INDEX `virtual_id`(`virtual_id`) USING BTREE,
  INDEX `store_id`(`store_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for orders_history
-- ----------------------------
DROP TABLE IF EXISTS `orders_history`;
CREATE TABLE `orders_history`  (
  `order_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `id` int(11) NULL DEFAULT NULL COMMENT '历史订单id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '下单人id',
  `consignee` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `user_coupons_id` int(11) NULL DEFAULT NULL COMMENT '用户优惠券id',
  `coupon_money` decimal(10, 2) NOT NULL COMMENT '优惠金额',
  `address_id` int(11) NULL DEFAULT NULL COMMENT '地址id',
  `address_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `goods_id` int(11) NULL DEFAULT NULL COMMENT '商品id',
  `express_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '快递名称',
  `express_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '快递单号',
  `day_id` int(11) NULL DEFAULT NULL COMMENT '租赁天数id',
  `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `finish_time` datetime(0) NULL DEFAULT NULL COMMENT '完成时间',
  `pay_way` int(11) NULL DEFAULT NULL COMMENT '支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）',
  `pay_status` int(1) NULL DEFAULT NULL COMMENT '订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消',
  `one_money` decimal(10, 2) NOT NULL COMMENT '一级佣金',
  `one_user_id` int(11) NULL DEFAULT NULL COMMENT '一级用户',
  `two_money` decimal(10, 2) NOT NULL COMMENT '二级佣金',
  `two_user_id` int(11) NULL DEFAULT NULL COMMENT '二级用户',
  `spec_id` int(11) NULL DEFAULT NULL COMMENT '规格id',
  `selected_spec` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '已选规格的json数据',
  `pay_num` int(11) NULL DEFAULT NULL COMMENT '购买数量',
  `lease_term` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租赁天数',
  `freight` decimal(10, 2) NOT NULL COMMENT '运费',
  `deposit` decimal(10, 2) NOT NULL COMMENT '押金',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `gross_rent` decimal(10, 2) NOT NULL COMMENT '总租金',
  `pay_money` decimal(10, 2) NOT NULL COMMENT '支付金额（合计）',
  `lease_start_date` date NULL DEFAULT NULL COMMENT '预计起租日',
  `return_date` date NULL DEFAULT NULL COMMENT '预计归还日',
  `take_state` int(1) NULL DEFAULT NULL COMMENT '提货方式： 1商家邮寄 2到店自提',
  `estimate_money` decimal(10, 2) NOT NULL COMMENT '预估价格',
  `buyout_money` decimal(10, 2) NOT NULL COMMENT '买断价格（补差）',
  `cancellation_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到店自提的核销时间',
  `daily_rent` decimal(10, 2) NOT NULL COMMENT '日租金',
  `pickup_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自提地址',
  `delivery_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发货时间',
  `platform_money` decimal(10, 2) NOT NULL COMMENT '平台本单收入',
  `merchant_income` decimal(10, 2) NOT NULL COMMENT '商家本单收入',
  `business_id` int(11) NULL DEFAULT NULL COMMENT '商家id',
  `continue_date` date NULL DEFAULT NULL COMMENT '续租日期',
  `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT '取消时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders_history
-- ----------------------------

-- ----------------------------
-- Table structure for pay_classify
-- ----------------------------
DROP TABLE IF EXISTS `pay_classify`;
CREATE TABLE `pay_classify`  (
  `pay_classify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '充值分类id',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '售价',
  `give_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '赠送数量',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '水晶数量',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`pay_classify_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_classify
-- ----------------------------

-- ----------------------------
-- Table structure for pay_details
-- ----------------------------
DROP TABLE IF EXISTS `pay_details`;
CREATE TABLE `pay_details`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '充值id',
  `classify` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ' 1app  2微信二维码支付  3微信小程序 公众号支付 4支付宝APP支付 5支付宝H5支付',
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付单号',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '充值金额',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `state` int(4) NULL DEFAULT NULL COMMENT '0待支付 1支付成功 2失败',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `pay_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付时间',
  `type` int(4) NULL DEFAULT NULL COMMENT '购买类型 1充值余额 2支付订单',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '关联id(如果是购买会员则是会员类型id)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 320 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_details
-- ----------------------------

-- ----------------------------
-- Table structure for rental_guidelines
-- ----------------------------
DROP TABLE IF EXISTS `rental_guidelines`;
CREATE TABLE `rental_guidelines`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '服务内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租赁须知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rental_guidelines
-- ----------------------------

-- ----------------------------
-- Table structure for sc_browse
-- ----------------------------
DROP TABLE IF EXISTS `sc_browse`;
CREATE TABLE `sc_browse`  (
  `browse_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '浏览记录id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `data_id` int(11) NULL DEFAULT NULL COMMENT '信息id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `type` int(1) NULL DEFAULT NULL COMMENT '1服务 2店铺',
  `shop_id` int(11) NULL DEFAULT NULL COMMENT '店铺id',
  PRIMARY KEY (`browse_id`) USING BTREE,
  INDEX `data_browse`(`data_id`) USING BTREE,
  INDEX `shop_borwse`(`shop_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_browse
-- ----------------------------

-- ----------------------------
-- Table structure for sc_follow
-- ----------------------------
DROP TABLE IF EXISTS `sc_follow`;
CREATE TABLE `sc_follow`  (
  `follow_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关注id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `by_id` int(11) NULL DEFAULT NULL COMMENT '被关注的id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `classify` int(1) NULL DEFAULT NULL COMMENT '类型 1商品收藏 2商品想要 3关注用户',
  PRIMARY KEY (`follow_id`) USING BTREE,
  INDEX `delete_shop`(`by_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_follow
-- ----------------------------

-- ----------------------------
-- Table structure for sc_notice
-- ----------------------------
DROP TABLE IF EXISTS `sc_notice`;
CREATE TABLE `sc_notice`  (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告id',
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '公告内容',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '发布人id',
  `status` int(1) NULL DEFAULT NULL COMMENT '0未发布 1已发布',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `release_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sc_shop
-- ----------------------------
DROP TABLE IF EXISTS `sc_shop`;
CREATE TABLE `sc_shop`  (
  `shop_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '店铺id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `shop_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺名',
  `shop_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺详细地址',
  `business_data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '周一周二周三等',
  `stat_time` time(0) NULL DEFAULT NULL COMMENT '营业开始时间',
  `ent_time` time(0) NULL DEFAULT NULL COMMENT '营业结束时间',
  `introduce` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '简介',
  `shop_label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺标签',
  `shop_lng` double(20, 6) NULL DEFAULT NULL COMMENT '商铺经度',
  `shop_lat` double(20, 6) NULL DEFAULT NULL COMMENT '商铺维度',
  `province` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
  `district` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区/县',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `shop_title_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '主图',
  `shop_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '轮播图',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '法人姓名',
  `identity_card_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '法人身份证',
  `business_license` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营业执照',
  `browse_count` int(11) NULL DEFAULT NULL COMMENT '浏览次数',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态  1待审核 2审核通过 3审核未通过 4已封禁',
  `ban_remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '下架原因',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `is_enable` int(1) NULL DEFAULT 0 COMMENT '是否上架 0否 1是',
  `is_recommend` int(1) NULL DEFAULT 0 COMMENT '是否是推荐 0否 1是',
  `state` int(1) NULL DEFAULT 1,
  PRIMARY KEY (`shop_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_shop
-- ----------------------------

-- ----------------------------
-- Table structure for sc_table_class
-- ----------------------------
DROP TABLE IF EXISTS `sc_table_class`;
CREATE TABLE `sc_table_class`  (
  `class_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `class_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图标',
  `is_enable` int(1) NULL DEFAULT NULL COMMENT '是否启用 0不启用 1启用',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`class_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '业务分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_table_class
-- ----------------------------

-- ----------------------------
-- Table structure for sc_table_column
-- ----------------------------
DROP TABLE IF EXISTS `sc_table_column`;
CREATE TABLE `sc_table_column`  (
  `column_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `type_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类id',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '列描述',
  `column_length` int(10) NULL DEFAULT NULL COMMENT '列长度',
  `is_required` int(1) NOT NULL COMMENT '是否必填（1是）',
  `html_type` int(1) NULL DEFAULT NULL COMMENT '显示类型（1文本框、2文本域、3下拉框、4复选框、5单选框、6日期控件、7数字类型）',
  `option_json` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项值',
  `show_type` int(1) NULL DEFAULT NULL COMMENT '展示类型 1仅用户端展示 2仅管理端展示 3都展示',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 334 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '业务字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for sc_table_data
-- ----------------------------
DROP TABLE IF EXISTS `sc_table_data`;
CREATE TABLE `sc_table_data`  (
  `data_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `class_id` int(11) NULL DEFAULT NULL COMMENT '分类id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `status` int(1) NULL DEFAULT NULL COMMENT '1待审核 2审核通过 3审核未通过 4已封禁',
  `refuse_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '拒绝原因',
  `title_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '首页图',
  `data_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `rotation_img` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '轮播图',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `browse_count` int(10) NULL DEFAULT NULL COMMENT '浏览次数',
  `data_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'json数据',
  `is_recommend` int(1) NULL DEFAULT NULL COMMENT '是否是推荐(1是 0不是)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
  `county` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '县/区',
  `price_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '价格信息,这里不是仅填写货币,而是包括货币及单位',
  `details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '详细信息',
  `lng` double(10, 6) NULL DEFAULT NULL COMMENT '经度',
  `lat` double(10, 6) NULL DEFAULT NULL COMMENT '纬度',
  `state` int(1) NULL DEFAULT 1 COMMENT '1正常 2vip过期超出次数的服务已下架',
  `is_enable` int(1) NULL DEFAULT 1 COMMENT '是否上架 0否 1是',
  PRIMARY KEY (`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发布服务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sc_table_data
-- ----------------------------

-- ----------------------------
-- Table structure for search
-- ----------------------------
DROP TABLE IF EXISTS `search`;
CREATE TABLE `search`  (
  `search_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '搜索id',
  `search_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '搜索名称',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `type` int(1) NULL DEFAULT NULL COMMENT '1服务 2店铺',
  PRIMARY KEY (`search_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of search
-- ----------------------------

-- ----------------------------
-- Table structure for section
-- ----------------------------
DROP TABLE IF EXISTS `section`;
CREATE TABLE `section`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `section_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '专区名称',
  `img_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '专区图片',
  `sort` int(2) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商城专区表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of section
-- ----------------------------

-- ----------------------------
-- Table structure for self_activity
-- ----------------------------
DROP TABLE IF EXISTS `self_activity`;
CREATE TABLE `self_activity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自营菜单id',
  `create_at` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '时间',
  `image_url` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '图片',
  `state` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '分类',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '地址',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_activity
-- ----------------------------
INSERT INTO `self_activity` VALUES (1, '2025-08-21 18:24:55', 'https://leaseshopyh.xianmxkj.com/file/uploadPath/2025/06/20/27f0c0dc64e5b55c87a30a9333519cd9.png', '1', '/pages/index/categoryList?typeName=手机专区&typeId=31&pageType=0', '手机专区', '手机专区');
INSERT INTO `self_activity` VALUES (2, '2025-08-21 18:27:36', 'https://leaseshopyh.xianmxkj.com/file/uploadPath/2025/06/20/d2e13dfe1ab6bad8392620807740d020.png', '1', '/pages/index/categoryList?typeName=电脑专区&typeId=32&pageType=0', '电脑专区', '电脑专区');
INSERT INTO `self_activity` VALUES (3, '2025-08-21 18:27:45', 'https://leaseshopyh.xianmxkj.com/file/uploadPath/2025/06/20/cb2df092ca0516592dad4781bb884420.png', '1', '/pages/index/categoryList?typeName=平板专区&typeId=57&pageType=0', '平板专区', '平板专区');
INSERT INTO `self_activity` VALUES (5, '2025-08-21 18:27:59', 'https://leaseshopyh.xianmxkj.com/file/uploadPath/2025/06/20/8d65cc32855d2091cedb287bf6f8f412.png', '1', '/pages/index/categoryList?typeName=耳机音箱&typeId=63&pageType=0', '耳机音箱', '耳机音箱');
INSERT INTO `self_activity` VALUES (6, '2025-08-21 18:28:08', 'https://leaseshopyh.xianmxkj.com/file/uploadPath/2025/06/20/429bc06c9af080ec07fdbec1ab4d2c13.png', '1', '/pages/index/categoryList?typeName=电动车&typeId=73&pageType=0', '电动车', '电动车');
INSERT INTO `self_activity` VALUES (7, '2025-08-21 18:28:18', 'https://leaseshopyh.xianmxkj.com/file/uploadPath/2025/06/20/3b62c74dbfe0a23f4da64bce65455cc3.png', '1', '/pages/index/categoryList?typeName=数码科技&typeId=94&pageType=0', '数码科技', '数码科技');
INSERT INTO `self_activity` VALUES (8, '2025-08-21 18:28:26', 'https://leaseshopyh.xianmxkj.com/file/uploadPath/2025/06/20/5c42ee0d5132a57e2461dbae5bc0a84c.png', '1', '/pages/index/categoryList?typeName=家用电器&typeId=96&pageType=0', '家用电器', '家用电器');
INSERT INTO `self_activity` VALUES (9, '2025-08-21 18:28:35', 'https://leaseshopyh.xianmxkj.com/file/uploadPath/2025/06/20/b72f3451afc02d78202294ad7f34ff7e.png', '1', '/pages/index/categoryList?typeName=办公家具', '办公家具', '办公家具');
INSERT INTO `self_activity` VALUES (10, '2025-08-21 18:28:43', 'https://leaseshopyh.xianmxkj.com/file/uploadPath/2025/06/20/e6800301dbe63591cc930a1eae55b27d.png', '1', '/pages/index/categoryList?typeName=户外设备', '户外设备', '户外设备');

-- ----------------------------
-- Table structure for self_banner
-- ----------------------------
DROP TABLE IF EXISTS `self_banner`;
CREATE TABLE `self_banner`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自营轮播图',
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '时间',
  `img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '地址',
  `sort` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '排序',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_banner
-- ----------------------------

-- ----------------------------
-- Table structure for self_cart
-- ----------------------------
DROP TABLE IF EXISTS `self_cart`;
CREATE TABLE `self_cart`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '购物车',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `number` int(11) NULL DEFAULT NULL COMMENT '数量',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'skuId',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `merchant_id` bigint(20) NULL DEFAULT NULL COMMENT '商家id',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺id',
  `store_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `goods_id`(`goods_id`) USING BTREE,
  INDEX `sku_id`(`sku_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `store_id`(`store_id`) USING BTREE,
  INDEX `merchant_id`(`merchant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1245 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_cart
-- ----------------------------

-- ----------------------------
-- Table structure for self_coupon
-- ----------------------------
DROP TABLE IF EXISTS `self_coupon`;
CREATE TABLE `self_coupon`  (
  `coupon_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '优惠券id',
  `coupon_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `goods_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品id多个',
  `goods_images` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品图片',
  `less_money` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠券面值',
  `min_money` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠券最低消费',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态（1开启 2关闭）',
  `type` int(11) NULL DEFAULT NULL COMMENT '优惠券类型（1通用券 2商品券）',
  `valid_day` int(11) NULL DEFAULT NULL COMMENT '优惠券有效期限(天)',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺id',
  `merchant_id` bigint(20) NULL DEFAULT NULL COMMENT '商家id',
  `store_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
  PRIMARY KEY (`coupon_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for self_coupon_issue
-- ----------------------------
DROP TABLE IF EXISTS `self_coupon_issue`;
CREATE TABLE `self_coupon_issue`  (
  `coupon_issue_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '优惠券发布id',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '优惠券id',
  `coupon_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `end_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束时间',
  `is_limit` int(11) NULL DEFAULT NULL COMMENT '是否限量（1限量 2不限量）',
  `issue_number` int(11) NULL DEFAULT NULL COMMENT '发布数量',
  `remain_number` int(11) NULL DEFAULT NULL COMMENT '剩余数量',
  `start_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始时间',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态（1开启 2关闭）',
  `type` int(11) NULL DEFAULT NULL COMMENT '优惠券类型（1通用券 2商品券）',
  `goods_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品id 多个',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺id',
  `merchant_id` bigint(20) NULL DEFAULT NULL COMMENT '商家id',
  `store_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
  `goods_images` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `less_money` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最低金额',
  `min_money` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠金额',
  PRIMARY KEY (`coupon_issue_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 115 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_coupon_issue
-- ----------------------------

-- ----------------------------
-- Table structure for self_coupon_user
-- ----------------------------
DROP TABLE IF EXISTS `self_coupon_user`;
CREATE TABLE `self_coupon_user`  (
  `coupon_user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户优惠券id',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '优惠券id',
  `coupon_issue_id` bigint(20) NULL DEFAULT NULL COMMENT '领取优惠券id',
  `coupon_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `failure_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '失效时间',
  `goods_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品id 多个',
  `less_money` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '面值',
  `min_money` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最低使用金额',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态（1未使用 2已使用 3已过期）',
  `type` int(11) NULL DEFAULT NULL COMMENT '优惠券类型（1通用券 2商品券）',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺id',
  `merchant_id` bigint(20) NULL DEFAULT NULL COMMENT '商家id',
  `store_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
  `goods_images` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  PRIMARY KEY (`coupon_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 192 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_coupon_user
-- ----------------------------

-- ----------------------------
-- Table structure for self_goods_attr
-- ----------------------------
DROP TABLE IF EXISTS `self_goods_attr`;
CREATE TABLE `self_goods_attr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规格id',
  `attr_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '规格名称',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `rule_id` bigint(20) NULL DEFAULT NULL COMMENT '规格模板id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `goods_id`(`goods_id`) USING BTREE,
  INDEX `rule_id`(`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 130 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_goods_attr
-- ----------------------------

-- ----------------------------
-- Table structure for self_goods_attr_value
-- ----------------------------
DROP TABLE IF EXISTS `self_goods_attr_value`;
CREATE TABLE `self_goods_attr_value`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规格值',
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '属性值组合:{尺寸: \"7寸\", 颜色: \"红底\"}',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `attr_id` bigint(20) NULL DEFAULT NULL COMMENT '规格id',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '规格属性名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`, `goods_id`, `attr_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 589 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_goods_attr_value
-- ----------------------------

-- ----------------------------
-- Table structure for self_goods_brand
-- ----------------------------
DROP TABLE IF EXISTS `self_goods_brand`;
CREATE TABLE `self_goods_brand`  (
  `brand_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '品牌id',
  `brand_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `names` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品牌别名',
  PRIMARY KEY (`brand_id`) USING BTREE,
  UNIQUE INDEX `brand_id`(`brand_id`) USING BTREE,
  INDEX `brand_id_2`(`brand_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_goods_brand
-- ----------------------------

-- ----------------------------
-- Table structure for self_goods_comment
-- ----------------------------
DROP TABLE IF EXISTS `self_goods_comment`;
CREATE TABLE `self_goods_comment`  (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `img` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片',
  `sku` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品规格名称',
  `user_header` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `sku_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'skuId',
  `reply` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回复',
  `score` int(11) NULL DEFAULT NULL COMMENT '评分',
  `score_type` int(11) NULL DEFAULT NULL COMMENT '分数类型（1好评 2中评 3差评）',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `comment_id`(`comment_id`, `goods_id`, `user_id`, `order_id`, `sku_id`, `score_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_goods_comment
-- ----------------------------

-- ----------------------------
-- Table structure for self_goods_rule
-- ----------------------------
DROP TABLE IF EXISTS `self_goods_rule`;
CREATE TABLE `self_goods_rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模板id',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '时间',
  `rule_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模板名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`, `rule_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_goods_rule
-- ----------------------------

-- ----------------------------
-- Table structure for self_goods_rule_value
-- ----------------------------
DROP TABLE IF EXISTS `self_goods_rule_value`;
CREATE TABLE `self_goods_rule_value`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模板规格id',
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模板规格',
  `rule_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模板规格名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`, `rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 143 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_goods_rule_value
-- ----------------------------

-- ----------------------------
-- Table structure for self_goods_sku
-- ----------------------------
DROP TABLE IF EXISTS `self_goods_sku`;
CREATE TABLE `self_goods_sku`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品skuId',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `sku_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片',
  `sku_original_price` double NULL DEFAULT NULL COMMENT '原价',
  `sku_price` double NULL DEFAULT NULL COMMENT '现价',
  `member_price` double NULL DEFAULT NULL COMMENT '会员价',
  `detail_json` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'sku信息，json封装',
  `sales` int(11) NULL DEFAULT NULL COMMENT '销量',
  `stock` int(11) NULL DEFAULT NULL COMMENT '库存',
  `daily_rent` double NULL DEFAULT NULL COMMENT '日租金',
  `member_rent` double NULL DEFAULT NULL COMMENT '会员日租金',
  `old_degree` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '新旧程度',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `goods_id`(`goods_id`) USING BTREE,
  INDEX `goods_id_2`(`goods_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2270 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_goods_sku
-- ----------------------------

-- ----------------------------
-- Table structure for self_goods_virtual
-- ----------------------------
DROP TABLE IF EXISTS `self_goods_virtual`;
CREATE TABLE `self_goods_virtual`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '虚拟商品',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态（1正常 2关闭 3已使用）',
  `cover_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面图',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `link_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_goods_virtual
-- ----------------------------

-- ----------------------------
-- Table structure for self_group
-- ----------------------------
DROP TABLE IF EXISTS `self_group`;
CREATE TABLE `self_group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '拼团id',
  `commission_price` double NULL DEFAULT NULL COMMENT '佣金',
  `cover_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面图',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间',
  `descrition` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述',
  `end_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束时间',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `group_number` int(11) NULL DEFAULT NULL COMMENT '成团数量',
  `hours` int(11) NULL DEFAULT NULL COMMENT '时效  小时',
  `img` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片',
  `member_price` double NULL DEFAULT NULL COMMENT '会员价',
  `number` int(11) NULL DEFAULT NULL COMMENT '参团人数',
  `original_price` double NULL DEFAULT NULL COMMENT '原价',
  `postage_price` double NULL DEFAULT NULL COMMENT '邮费',
  `price` double NULL DEFAULT NULL COMMENT '现价',
  `start_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始时间',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态（1上架 2下架）',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`, `goods_id`, `status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_group
-- ----------------------------

-- ----------------------------
-- Table structure for self_group_pink
-- ----------------------------
DROP TABLE IF EXISTS `self_group_pink`;
CREATE TABLE `self_group_pink`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '拼团团体id',
  `count_number` int(11) NULL DEFAULT NULL COMMENT '拼团人数',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间',
  `end_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束时间',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '拼团id',
  `need_number` int(11) NULL DEFAULT NULL COMMENT '需要人数',
  `start_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始时间',
  `status` int(11) NULL DEFAULT NULL COMMENT '拼团状态（1拼团中 2已完成 3未完成）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `create_time`(`create_time`, `end_time`, `group_id`, `start_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 92 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_group_pink
-- ----------------------------

-- ----------------------------
-- Table structure for self_group_pink_user
-- ----------------------------
DROP TABLE IF EXISTS `self_group_pink_user`;
CREATE TABLE `self_group_pink_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '拼团用户',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `group_pink_id` bigint(20) NULL DEFAULT NULL COMMENT '拼团团体id',
  `header_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `order_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单id',
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `group_pink_id`(`group_pink_id`, `order_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 132 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_group_pink_user
-- ----------------------------

-- ----------------------------
-- Table structure for self_group_sku
-- ----------------------------
DROP TABLE IF EXISTS `self_group_sku`;
CREATE TABLE `self_group_sku`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '拼团skuId',
  `detail_json` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'skuJson封装',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `member_price` double NULL DEFAULT NULL COMMENT '会员价',
  `pink_price` double NULL DEFAULT NULL COMMENT '拼团价格',
  `pink_stock` int(11) NULL DEFAULT NULL COMMENT '拼团库存id',
  `sales` int(11) NULL DEFAULT NULL COMMENT '销量',
  `sku_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  `sku_original_price` double NULL DEFAULT NULL COMMENT '原价',
  `sku_price` double NULL DEFAULT NULL COMMENT '现价',
  `stock` int(11) NULL DEFAULT NULL COMMENT '库存',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '拼团id',
  `sec_kill_price` double NULL DEFAULT NULL COMMENT '秒杀现价',
  `sec_kill_stock` int(11) NULL DEFAULT NULL COMMENT '秒杀库存',
  `sec_kill_id` bigint(20) NULL DEFAULT NULL COMMENT '秒杀id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `goods_id`(`goods_id`, `group_id`, `sec_kill_id`) USING BTREE,
  INDEX `id`(`id`, `goods_id`, `group_id`, `sec_kill_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 500 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_group_sku
-- ----------------------------

-- ----------------------------
-- Table structure for self_order_relation
-- ----------------------------
DROP TABLE IF EXISTS `self_order_relation`;
CREATE TABLE `self_order_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单分销id',
  `commission_price` decimal(18, 2) NULL DEFAULT NULL COMMENT '佣金',
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '时间',
  `order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '订单id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分销明细',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '手机号',
  `status` int(11) NULL DEFAULT NULL COMMENT '1未到账 2已到账',
  `finish_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '收货时间',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `goods_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '商品图片',
  `goods_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '商品图片',
  `lower_user_id` bigint(20) NULL DEFAULT NULL COMMENT '下级用户id',
  `lower_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '下级用户昵称',
  `money_from` int(11) NULL DEFAULT NULL COMMENT '佣金来源（1直属 2自己 3非直属）',
  `pay_money` decimal(18, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `commission_money` decimal(18, 2) NULL DEFAULT NULL COMMENT '代理佣金/团长佣金',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户昵称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_id`(`order_id`, `user_id`, `goods_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 988 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_order_relation
-- ----------------------------

-- ----------------------------
-- Table structure for self_order_remind
-- ----------------------------
DROP TABLE IF EXISTS `self_order_remind`;
CREATE TABLE `self_order_remind`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '发货提醒 ',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '时间',
  `orders_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态（1提醒 2收到）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `orders_id`(`orders_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 293 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_order_remind
-- ----------------------------

-- ----------------------------
-- Table structure for self_prize
-- ----------------------------
DROP TABLE IF EXISTS `self_prize`;
CREATE TABLE `self_prize`  (
  `prize_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '奖品id',
  `add_ji_fen` int(11) NULL DEFAULT NULL COMMENT '中奖积分',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '奖品展示名称',
  `prize_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '奖品图片',
  `prize_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '抽奖规则id',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型',
  `weight` int(11) NULL DEFAULT NULL COMMENT '权重（1-100）',
  PRIMARY KEY (`prize_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_prize
-- ----------------------------

-- ----------------------------
-- Table structure for self_prize_rule
-- ----------------------------
DROP TABLE IF EXISTS `self_prize_rule`;
CREATE TABLE `self_prize_rule`  (
  `prize_rule_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '奖池id',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '活动规则',
  `ji_fen` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '消耗积分',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '抽奖名称',
  `number` int(11) NULL DEFAULT NULL COMMENT '每日抽取次数',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态（1开启 2关闭）',
  PRIMARY KEY (`prize_rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_prize_rule
-- ----------------------------

-- ----------------------------
-- Table structure for self_prize_user
-- ----------------------------
DROP TABLE IF EXISTS `self_prize_user`;
CREATE TABLE `self_prize_user`  (
  `prize_user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '抽奖记录id',
  `add_ji_fen` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '增加的积分',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `less_ji_fen` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '减少的积分',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '昵称',
  `prize_id` bigint(20) NULL DEFAULT NULL COMMENT '奖品id',
  `prize_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '奖池id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`prize_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 105 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_prize_user
-- ----------------------------

-- ----------------------------
-- Table structure for self_sec_kill
-- ----------------------------
DROP TABLE IF EXISTS `self_sec_kill`;
CREATE TABLE `self_sec_kill`  (
  `sec_kill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀id',
  `commission_price` double NULL DEFAULT NULL COMMENT '佣金',
  `cover_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面图',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间',
  `descrition` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述',
  `end_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束时间',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `img` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片',
  `member_price` double NULL DEFAULT NULL COMMENT '会员价',
  `number` int(11) NULL DEFAULT NULL COMMENT '数量',
  `original_price` double NULL DEFAULT NULL COMMENT '原价',
  `postage_price` double NULL DEFAULT NULL COMMENT '邮费',
  `price` double NULL DEFAULT NULL COMMENT '现价',
  `sec_kill_time_id` bigint(20) NULL DEFAULT NULL COMMENT '秒杀时间段id',
  `start_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始时间',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态（1上架 2下架）',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  PRIMARY KEY (`sec_kill_id`) USING BTREE,
  INDEX `goods_id`(`goods_id`) USING BTREE,
  INDEX `sec_kill_id`(`sec_kill_id`, `goods_id`, `sec_kill_time_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_sec_kill
-- ----------------------------

-- ----------------------------
-- Table structure for self_sec_kill_time
-- ----------------------------
DROP TABLE IF EXISTS `self_sec_kill_time`;
CREATE TABLE `self_sec_kill_time`  (
  `sec_kill_time_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀时间段id',
  `hold_hour` int(11) NULL DEFAULT NULL COMMENT '持续时间（整小时数）',
  `start_hour` int(11) NULL DEFAULT NULL COMMENT '开始时间（整小时数）',
  PRIMARY KEY (`sec_kill_time_id`) USING BTREE,
  INDEX `sec_kill_time_id`(`sec_kill_time_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_sec_kill_time
-- ----------------------------

-- ----------------------------
-- Table structure for self_user_browse
-- ----------------------------
DROP TABLE IF EXISTS `self_user_browse`;
CREATE TABLE `self_user_browse`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '浏览记录',
  `cover_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面图',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `price` double NULL DEFAULT NULL COMMENT '价格',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `goods_id`(`goods_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3487 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_user_browse
-- ----------------------------

-- ----------------------------
-- Table structure for self_user_collect
-- ----------------------------
DROP TABLE IF EXISTS `self_user_collect`;
CREATE TABLE `self_user_collect`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收藏id',
  `cover_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面图',
  `create_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间',
  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `price` double NULL DEFAULT NULL COMMENT '价格',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `goods_id`(`goods_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 416 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of self_user_collect
-- ----------------------------

-- ----------------------------
-- Table structure for sys_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha`  (
  `uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'uuid',
  `code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '验证码',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统验证码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_captcha
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'value',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `param_key`(`param_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典码',
  `value` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典值',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `parent_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------

-- ----------------------------
-- Table structure for sys_evaluate
-- ----------------------------
DROP TABLE IF EXISTS `sys_evaluate`;
CREATE TABLE `sys_evaluate`  (
  `evaluate_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id(评分人)',
  `indent_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `evaluate_message` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评价内容',
  `satisfaction_flag` int(1) NULL DEFAULT NULL COMMENT '评级 1:非常差 2:差 3:一般 4:满意 5:非常满意',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '评价时间',
  `evaluate_img` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '评价图片',
  `is_delete` int(1) NULL DEFAULT 0 COMMENT '是否已删除0否 -1是',
  `by_user_id` int(11) NULL DEFAULT NULL COMMENT '被评价人',
  PRIMARY KEY (`evaluate_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3237 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '评价表 用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_evaluate
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`) USING BTREE,
  INDEX `menu_id`(`menu_id`, `parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 263 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', NULL, NULL, 0, 'system', 20);
INSERT INTO `sys_menu` VALUES (2, 1, '管理员列表', 'sys/user', NULL, 1, 'admin', 1);
INSERT INTO `sys_menu` VALUES (3, 1, '角色管理', 'sys/role', NULL, 1, 'role', 2);
INSERT INTO `sys_menu` VALUES (4, 1, '菜单管理', 'sys/menu', NULL, 1, 'menu', 3);
INSERT INTO `sys_menu` VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, 2, '添加', NULL, 'sys:user:save,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, 2, '修改', NULL, 'sys:user:update,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (20, 3, '添加', NULL, 'sys:role:save,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, 4, '添加', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (32, 0, '用户中心', 'userList', NULL, 1, 'yonghul', 2);
INSERT INTO `sys_menu` VALUES (34, 0, '财务中心', 'financeList', '', 1, 'caiwu', 3);
INSERT INTO `sys_menu` VALUES (35, 0, '消息中心', 'message', '', 1, 'pinglun', 4);
INSERT INTO `sys_menu` VALUES (38, 0, '商城配置', 'storeList', '', 1, 'shangpin', 6);
INSERT INTO `sys_menu` VALUES (39, 0, '系统配置', 'allocationList', '', 1, 'zhedie', 19);
INSERT INTO `sys_menu` VALUES (44, 0, '订单中心', 'order', '', 1, 'ordercenter', 1);
INSERT INTO `sys_menu` VALUES (45, 0, '数据中心', 'home', '', 1, 'shuju', 0);
INSERT INTO `sys_menu` VALUES (48, 0, '微信配置', 'wechatList', '', 1, 'winxin', 5);
INSERT INTO `sys_menu` VALUES (59, 0, '商品管理', '', '', 0, 'shangcheng', 7);
INSERT INTO `sys_menu` VALUES (60, 59, '商品分类', 'classifyAdmin', '', 1, 'zyfl', 0);
INSERT INTO `sys_menu` VALUES (61, 59, '商品列表', 'shopAdmin', '', 1, 'shangpin', 1);
INSERT INTO `sys_menu` VALUES (67, 32, '查看', NULL, 'userList:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (68, 32, '删除', '', 'userList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (69, 34, '查看', '', 'financeList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (70, 34, '转账', '', 'financeList:accounts', 2, '', 0);
INSERT INTO `sys_menu` VALUES (71, 34, '退款', '', 'financeList:refund', 2, '', 0);
INSERT INTO `sys_menu` VALUES (72, 34, '结算', '', 'financeList:statement', 2, '', 0);
INSERT INTO `sys_menu` VALUES (73, 35, '查看', '', 'message:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (74, 35, '添加', '', 'message:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (75, 35, '修改', '', 'message:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (76, 35, '删除', '', 'message:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (77, 35, '消息推送', '', 'message:push', 2, '', 0);
INSERT INTO `sys_menu` VALUES (78, 48, '查看', '', 'wechatList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (79, 48, '添加', '', 'wechatList:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (80, 48, '修改', '', 'wechatList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (81, 48, '删除', '', 'wechatList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (82, 38, '查看', '', 'storeList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (83, 38, '添加', '', 'storeList:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (84, 38, '修改', '', 'storeList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (85, 38, '删除', '', 'storeList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (98, 39, '查看', '', 'allocationList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (99, 39, '修改', '', 'allocationList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (100, 60, '查看', '', 'classifyAdmin:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (101, 60, '添加', '', 'classifyAdmin:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (102, 60, '修改', '', 'classifyAdmin:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (103, 60, '删除', '', 'classifyAdmin:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (104, 61, '查看', '', 'shopAdmin:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (105, 61, '添加', '', 'shopAdmin:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (106, 61, '修改', '', 'shopAdmin:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (107, 61, '删除', '', 'shopAdmin:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (113, 44, '查看', '', 'orderAdmin:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (128, 48, '保存', '', 'wechatList:save', 2, '', 0);
INSERT INTO `sys_menu` VALUES (130, 59, '商品规格', 'specification', '', 1, 'tianjia', 2);
INSERT INTO `sys_menu` VALUES (131, 130, '查看', '', 'specification:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (132, 130, '添加', '', 'specification:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (133, 130, '修改', '', 'specification:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (134, 130, '删除', '', 'specification:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (135, 44, '发货', '', 'orderAdmin:deliver', 2, '', 0);
INSERT INTO `sys_menu` VALUES (136, 44, '退款', '', 'orderAdmin:refund', 2, '', 0);
INSERT INTO `sys_menu` VALUES (137, 44, '拒绝退款', '', 'orderAdmin:refuse', 2, '', 0);
INSERT INTO `sys_menu` VALUES (138, 0, '市场推广管理', '', '', 0, 'yingxiao', 9);
INSERT INTO `sys_menu` VALUES (139, 138, '拼团商品', 'marketing', '', 1, 'pdd', 0);
INSERT INTO `sys_menu` VALUES (140, 138, '拼团列表', 'marketList', '', 1, 'order', 1);
INSERT INTO `sys_menu` VALUES (141, 138, '秒杀商品', 'seckill', '', 1, 'miaosha', 2);
INSERT INTO `sys_menu` VALUES (142, 138, '秒杀配置', 'seckillConfig', '', 1, 'system', 3);
INSERT INTO `sys_menu` VALUES (143, 139, '查看', '', 'marketing:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (144, 139, '添加', '', 'marketing:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (145, 139, '修改', '', 'marketing:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (146, 139, '删除', '', 'marketing:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (147, 141, '查看', '', 'seckill:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (148, 141, '添加', '', 'seckill:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (149, 141, '修改', '', 'seckill:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (150, 141, '删除', '', 'seckill:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (152, 142, '查看', '', 'seckillConfig:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (153, 142, '添加', '', 'seckillConfig:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (154, 142, '修改', '', 'seckillConfig:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (155, 142, '删除', '', 'seckillConfig:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (161, 0, '人工客服', '', '', 0, 'pingtai', 8);
INSERT INTO `sys_menu` VALUES (164, 59, '商品品牌', 'brandShop', '', 1, 'editor', 3);
INSERT INTO `sys_menu` VALUES (165, 164, '查看', '', 'brandShop:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (166, 164, '添加', '', 'brandShop:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (167, 164, '修改', '', 'brandShop:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (168, 164, '删除', '', 'brandShop:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (170, 0, '优惠券管理', '', '', 0, 'dizhi', 7);
INSERT INTO `sys_menu` VALUES (171, 170, '优惠券制作', 'coupon', '', 1, 'caiwu', 0);
INSERT INTO `sys_menu` VALUES (172, 170, '已发布优惠券', 'couponissue', '', 1, 'renwu', 0);
INSERT INTO `sys_menu` VALUES (173, 170, '优惠券领取记录', 'couponuser', '', 1, 'bianji', 0);
INSERT INTO `sys_menu` VALUES (174, 171, '查看', '', 'coupon:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (175, 171, '添加', '', 'coupon:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (176, 171, '修改', '', 'coupon:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (177, 171, '删除', '', 'coupon:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (178, 171, '发布', '', 'coupon:issue', 2, '', 0);
INSERT INTO `sys_menu` VALUES (179, 172, '查看', '', 'couponissue:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (180, 172, '修改', '', 'couponissue:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (181, 172, '删除', '', 'couponissue:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (182, 59, '虚拟商品', 'virtual', '', 1, 'fenleilist', 4);
INSERT INTO `sys_menu` VALUES (183, 182, '查看', '', 'virtual:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (184, 182, '补货', '', 'virtual:replen', 2, '', 0);
INSERT INTO `sys_menu` VALUES (185, 138, '抽奖规则', 'lottery', '', 1, 'shuju', 5);
INSERT INTO `sys_menu` VALUES (186, 138, '抽奖记录', 'lotteryRecord', '', 1, 'ordercenter', 6);
INSERT INTO `sys_menu` VALUES (187, 185, '查看', '', 'lottery:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (188, 185, '添加', '', 'lottery:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (189, 185, '修改', '', 'lottery:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (190, 185, '删除', '', 'lottery:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (197, 161, '聊天室', 'vueMchat', '', 1, 'pinglun', 3);
INSERT INTO `sys_menu` VALUES (198, 59, '积分商品', 'integralist', '', 1, 'jifen', 5);
INSERT INTO `sys_menu` VALUES (199, 198, '查看', '', 'integralist:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (200, 198, '添加', '', 'integralist:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (201, 198, '修改', '', 'integralist:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (202, 197, '查看', '', 'vueMchat:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (203, 197, '删除', '', 'vueMchat:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (204, 44, '核销', '', 'orderAdmin:cancel', 2, '', 0);
INSERT INTO `sys_menu` VALUES (205, 32, '修改金额', '', 'userList:updatejf', 2, '', 0);
INSERT INTO `sys_menu` VALUES (206, 32, '修改状态', '', 'userList:updateStatus', 2, '', 0);
INSERT INTO `sys_menu` VALUES (212, 0, '评价管理', 'comment', '', 1, 'xiaoxi', 6);
INSERT INTO `sys_menu` VALUES (213, 212, '查看', '', 'comment:ist', 2, '', 0);
INSERT INTO `sys_menu` VALUES (214, 212, '回复', '', 'comment:huifu', 2, '', 0);
INSERT INTO `sys_menu` VALUES (215, 212, '删除', '', 'comment:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (221, 0, '帮助中心', 'materialsList', '', 1, 'pingtai', 10);
INSERT INTO `sys_menu` VALUES (222, 221, '查看', '', 'materialsList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (223, 221, '添加', '', 'materialsList:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (224, 221, '修改', '', 'materialsList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (225, 221, '删除', '', 'materialsList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (226, 236, '租赁须知配置', 'rentalNotice', '', 1, 'renwu', 1);
INSERT INTO `sys_menu` VALUES (227, 226, '查看', '', 'rentalNotice:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (228, 226, '添加', '', 'rentalNotice:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (229, 226, '修改', '', 'rentalNotice:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (230, 226, '删除', '', 'rentalNotice:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (231, 236, '租赁天数配置', 'commonProblem', '', 1, 'shangc', 2);
INSERT INTO `sys_menu` VALUES (232, 231, '查看', '', 'commonProblem:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (233, 231, '添加', '', 'commonProblem:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (234, 231, '修改', '', 'commonProblem:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (235, 231, '删除', '', 'commonProblem:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (236, 0, '商品配置', '', '', 0, 'peizhi', 11);
INSERT INTO `sys_menu` VALUES (237, 236, '商品配件配置', 'accessory', '', 1, 'order', 3);
INSERT INTO `sys_menu` VALUES (238, 237, '查看', '', 'accessory:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (239, 237, '添加', '', 'accessory:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (240, 237, '修改', '', 'accessory:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (241, 237, '删除', '', 'accessory:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (242, 236, '赠品配件配置', 'productGifts', '', 1, 'renwu', 4);
INSERT INTO `sys_menu` VALUES (243, 242, '查看', '', 'productGifts:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (244, 242, '添加', '', 'productGifts:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (245, 242, '修改', '', 'productGifts:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (246, 242, '删除', '', 'productGifts:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (247, 0, '实名认证', 'autonym', '', 1, 'geren', 6);
INSERT INTO `sys_menu` VALUES (248, 247, '查看', '', 'autonym:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (249, 247, '修改', '', 'autonym:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (250, 247, '删除', '', 'autonym:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (251, 247, '审核', '', 'autonym:shenhe', 2, '', 0);
INSERT INTO `sys_menu` VALUES (252, 32, '修改会员', '', 'userList:updateVip', 2, '', 0);
INSERT INTO `sys_menu` VALUES (253, 59, '标签管理', 'labelShop', '', 1, 'peizhi', 6);
INSERT INTO `sys_menu` VALUES (254, 253, '查看', '', 'labelShop:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (255, 253, '添加', '', 'labelShop:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (256, 253, '修改', '', 'labelShop:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (257, 253, '删除', '', 'labelShop:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (258, 0, '会员配置', 'memberDetails', '', 1, 'config', 10);
INSERT INTO `sys_menu` VALUES (259, 258, '查看', '', 'memberDetails:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (260, 258, '添加', '', 'memberDetails:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (261, 258, '修改', '', 'memberDetails:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (262, 258, '删除', '', 'memberDetails:delete', 2, '', 0);

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件上传' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  INDEX `role_id`(`role_id`, `create_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (3, '超级管理员', '超级管理员', NULL, '2025-08-21 17:49:03');
INSERT INTO `sys_role` VALUES (4, '普通管理员', '普通管理员', NULL, '2022-03-30 15:57:07');
INSERT INTO `sys_role` VALUES (5, '运营人员', '运营人员', NULL, '2021-02-23 16:33:32');
INSERT INTO `sys_role` VALUES (6, '财务', '财务', NULL, '2020-10-16 14:51:15');
INSERT INTO `sys_role` VALUES (16, '体验超级账号', '体验超级账号', NULL, '2022-03-15 10:03:55');
INSERT INTO `sys_role` VALUES (17, '测试', '测试', NULL, '2022-04-14 11:10:11');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12747 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色与菜单对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (12746, 3, -666666);
INSERT INTO `sys_role_menu` VALUES (12730, 3, 1);
INSERT INTO `sys_role_menu` VALUES (12731, 3, 2);
INSERT INTO `sys_role_menu` VALUES (12736, 3, 3);
INSERT INTO `sys_role_menu` VALUES (12741, 3, 4);
INSERT INTO `sys_role_menu` VALUES (12732, 3, 15);
INSERT INTO `sys_role_menu` VALUES (12733, 3, 16);
INSERT INTO `sys_role_menu` VALUES (12734, 3, 17);
INSERT INTO `sys_role_menu` VALUES (12735, 3, 18);
INSERT INTO `sys_role_menu` VALUES (12737, 3, 19);
INSERT INTO `sys_role_menu` VALUES (12738, 3, 20);
INSERT INTO `sys_role_menu` VALUES (12739, 3, 21);
INSERT INTO `sys_role_menu` VALUES (12740, 3, 22);
INSERT INTO `sys_role_menu` VALUES (12742, 3, 23);
INSERT INTO `sys_role_menu` VALUES (12743, 3, 24);
INSERT INTO `sys_role_menu` VALUES (12744, 3, 25);
INSERT INTO `sys_role_menu` VALUES (12745, 3, 26);
INSERT INTO `sys_role_menu` VALUES (12587, 3, 32);
INSERT INTO `sys_role_menu` VALUES (12593, 3, 34);
INSERT INTO `sys_role_menu` VALUES (12598, 3, 35);
INSERT INTO `sys_role_menu` VALUES (12610, 3, 38);
INSERT INTO `sys_role_menu` VALUES (12727, 3, 39);
INSERT INTO `sys_role_menu` VALUES (12581, 3, 44);
INSERT INTO `sys_role_menu` VALUES (12580, 3, 45);
INSERT INTO `sys_role_menu` VALUES (12604, 3, 48);
INSERT INTO `sys_role_menu` VALUES (12624, 3, 59);
INSERT INTO `sys_role_menu` VALUES (12625, 3, 60);
INSERT INTO `sys_role_menu` VALUES (12630, 3, 61);
INSERT INTO `sys_role_menu` VALUES (12588, 3, 67);
INSERT INTO `sys_role_menu` VALUES (12589, 3, 68);
INSERT INTO `sys_role_menu` VALUES (12594, 3, 69);
INSERT INTO `sys_role_menu` VALUES (12595, 3, 70);
INSERT INTO `sys_role_menu` VALUES (12596, 3, 71);
INSERT INTO `sys_role_menu` VALUES (12597, 3, 72);
INSERT INTO `sys_role_menu` VALUES (12599, 3, 73);
INSERT INTO `sys_role_menu` VALUES (12600, 3, 74);
INSERT INTO `sys_role_menu` VALUES (12601, 3, 75);
INSERT INTO `sys_role_menu` VALUES (12602, 3, 76);
INSERT INTO `sys_role_menu` VALUES (12603, 3, 77);
INSERT INTO `sys_role_menu` VALUES (12605, 3, 78);
INSERT INTO `sys_role_menu` VALUES (12606, 3, 79);
INSERT INTO `sys_role_menu` VALUES (12607, 3, 80);
INSERT INTO `sys_role_menu` VALUES (12608, 3, 81);
INSERT INTO `sys_role_menu` VALUES (12611, 3, 82);
INSERT INTO `sys_role_menu` VALUES (12612, 3, 83);
INSERT INTO `sys_role_menu` VALUES (12613, 3, 84);
INSERT INTO `sys_role_menu` VALUES (12614, 3, 85);
INSERT INTO `sys_role_menu` VALUES (12728, 3, 98);
INSERT INTO `sys_role_menu` VALUES (12729, 3, 99);
INSERT INTO `sys_role_menu` VALUES (12626, 3, 100);
INSERT INTO `sys_role_menu` VALUES (12627, 3, 101);
INSERT INTO `sys_role_menu` VALUES (12628, 3, 102);
INSERT INTO `sys_role_menu` VALUES (12629, 3, 103);
INSERT INTO `sys_role_menu` VALUES (12631, 3, 104);
INSERT INTO `sys_role_menu` VALUES (12632, 3, 105);
INSERT INTO `sys_role_menu` VALUES (12633, 3, 106);
INSERT INTO `sys_role_menu` VALUES (12634, 3, 107);
INSERT INTO `sys_role_menu` VALUES (12582, 3, 113);
INSERT INTO `sys_role_menu` VALUES (12609, 3, 128);
INSERT INTO `sys_role_menu` VALUES (12635, 3, 130);
INSERT INTO `sys_role_menu` VALUES (12636, 3, 131);
INSERT INTO `sys_role_menu` VALUES (12637, 3, 132);
INSERT INTO `sys_role_menu` VALUES (12638, 3, 133);
INSERT INTO `sys_role_menu` VALUES (12639, 3, 134);
INSERT INTO `sys_role_menu` VALUES (12583, 3, 135);
INSERT INTO `sys_role_menu` VALUES (12584, 3, 136);
INSERT INTO `sys_role_menu` VALUES (12585, 3, 137);
INSERT INTO `sys_role_menu` VALUES (12673, 3, 138);
INSERT INTO `sys_role_menu` VALUES (12674, 3, 139);
INSERT INTO `sys_role_menu` VALUES (12679, 3, 140);
INSERT INTO `sys_role_menu` VALUES (12680, 3, 141);
INSERT INTO `sys_role_menu` VALUES (12685, 3, 142);
INSERT INTO `sys_role_menu` VALUES (12675, 3, 143);
INSERT INTO `sys_role_menu` VALUES (12676, 3, 144);
INSERT INTO `sys_role_menu` VALUES (12677, 3, 145);
INSERT INTO `sys_role_menu` VALUES (12678, 3, 146);
INSERT INTO `sys_role_menu` VALUES (12681, 3, 147);
INSERT INTO `sys_role_menu` VALUES (12682, 3, 148);
INSERT INTO `sys_role_menu` VALUES (12683, 3, 149);
INSERT INTO `sys_role_menu` VALUES (12684, 3, 150);
INSERT INTO `sys_role_menu` VALUES (12686, 3, 152);
INSERT INTO `sys_role_menu` VALUES (12687, 3, 153);
INSERT INTO `sys_role_menu` VALUES (12688, 3, 154);
INSERT INTO `sys_role_menu` VALUES (12689, 3, 155);
INSERT INTO `sys_role_menu` VALUES (12669, 3, 161);
INSERT INTO `sys_role_menu` VALUES (12640, 3, 164);
INSERT INTO `sys_role_menu` VALUES (12641, 3, 165);
INSERT INTO `sys_role_menu` VALUES (12642, 3, 166);
INSERT INTO `sys_role_menu` VALUES (12643, 3, 167);
INSERT INTO `sys_role_menu` VALUES (12644, 3, 168);
INSERT INTO `sys_role_menu` VALUES (12657, 3, 170);
INSERT INTO `sys_role_menu` VALUES (12658, 3, 171);
INSERT INTO `sys_role_menu` VALUES (12664, 3, 172);
INSERT INTO `sys_role_menu` VALUES (12668, 3, 173);
INSERT INTO `sys_role_menu` VALUES (12659, 3, 174);
INSERT INTO `sys_role_menu` VALUES (12660, 3, 175);
INSERT INTO `sys_role_menu` VALUES (12661, 3, 176);
INSERT INTO `sys_role_menu` VALUES (12662, 3, 177);
INSERT INTO `sys_role_menu` VALUES (12663, 3, 178);
INSERT INTO `sys_role_menu` VALUES (12665, 3, 179);
INSERT INTO `sys_role_menu` VALUES (12666, 3, 180);
INSERT INTO `sys_role_menu` VALUES (12667, 3, 181);
INSERT INTO `sys_role_menu` VALUES (12645, 3, 182);
INSERT INTO `sys_role_menu` VALUES (12646, 3, 183);
INSERT INTO `sys_role_menu` VALUES (12647, 3, 184);
INSERT INTO `sys_role_menu` VALUES (12690, 3, 185);
INSERT INTO `sys_role_menu` VALUES (12695, 3, 186);
INSERT INTO `sys_role_menu` VALUES (12691, 3, 187);
INSERT INTO `sys_role_menu` VALUES (12692, 3, 188);
INSERT INTO `sys_role_menu` VALUES (12693, 3, 189);
INSERT INTO `sys_role_menu` VALUES (12694, 3, 190);
INSERT INTO `sys_role_menu` VALUES (12670, 3, 197);
INSERT INTO `sys_role_menu` VALUES (12648, 3, 198);
INSERT INTO `sys_role_menu` VALUES (12649, 3, 199);
INSERT INTO `sys_role_menu` VALUES (12650, 3, 200);
INSERT INTO `sys_role_menu` VALUES (12651, 3, 201);
INSERT INTO `sys_role_menu` VALUES (12671, 3, 202);
INSERT INTO `sys_role_menu` VALUES (12672, 3, 203);
INSERT INTO `sys_role_menu` VALUES (12586, 3, 204);
INSERT INTO `sys_role_menu` VALUES (12590, 3, 205);
INSERT INTO `sys_role_menu` VALUES (12591, 3, 206);
INSERT INTO `sys_role_menu` VALUES (12615, 3, 212);
INSERT INTO `sys_role_menu` VALUES (12616, 3, 213);
INSERT INTO `sys_role_menu` VALUES (12617, 3, 214);
INSERT INTO `sys_role_menu` VALUES (12618, 3, 215);
INSERT INTO `sys_role_menu` VALUES (12701, 3, 221);
INSERT INTO `sys_role_menu` VALUES (12702, 3, 222);
INSERT INTO `sys_role_menu` VALUES (12703, 3, 223);
INSERT INTO `sys_role_menu` VALUES (12704, 3, 224);
INSERT INTO `sys_role_menu` VALUES (12705, 3, 225);
INSERT INTO `sys_role_menu` VALUES (12707, 3, 226);
INSERT INTO `sys_role_menu` VALUES (12708, 3, 227);
INSERT INTO `sys_role_menu` VALUES (12709, 3, 228);
INSERT INTO `sys_role_menu` VALUES (12710, 3, 229);
INSERT INTO `sys_role_menu` VALUES (12711, 3, 230);
INSERT INTO `sys_role_menu` VALUES (12712, 3, 231);
INSERT INTO `sys_role_menu` VALUES (12713, 3, 232);
INSERT INTO `sys_role_menu` VALUES (12714, 3, 233);
INSERT INTO `sys_role_menu` VALUES (12715, 3, 234);
INSERT INTO `sys_role_menu` VALUES (12716, 3, 235);
INSERT INTO `sys_role_menu` VALUES (12706, 3, 236);
INSERT INTO `sys_role_menu` VALUES (12717, 3, 237);
INSERT INTO `sys_role_menu` VALUES (12718, 3, 238);
INSERT INTO `sys_role_menu` VALUES (12719, 3, 239);
INSERT INTO `sys_role_menu` VALUES (12720, 3, 240);
INSERT INTO `sys_role_menu` VALUES (12721, 3, 241);
INSERT INTO `sys_role_menu` VALUES (12722, 3, 242);
INSERT INTO `sys_role_menu` VALUES (12723, 3, 243);
INSERT INTO `sys_role_menu` VALUES (12724, 3, 244);
INSERT INTO `sys_role_menu` VALUES (12725, 3, 245);
INSERT INTO `sys_role_menu` VALUES (12726, 3, 246);
INSERT INTO `sys_role_menu` VALUES (12619, 3, 247);
INSERT INTO `sys_role_menu` VALUES (12620, 3, 248);
INSERT INTO `sys_role_menu` VALUES (12621, 3, 249);
INSERT INTO `sys_role_menu` VALUES (12622, 3, 250);
INSERT INTO `sys_role_menu` VALUES (12623, 3, 251);
INSERT INTO `sys_role_menu` VALUES (12592, 3, 252);
INSERT INTO `sys_role_menu` VALUES (12652, 3, 253);
INSERT INTO `sys_role_menu` VALUES (12655, 3, 254);
INSERT INTO `sys_role_menu` VALUES (12656, 3, 255);
INSERT INTO `sys_role_menu` VALUES (12653, 3, 256);
INSERT INTO `sys_role_menu` VALUES (12654, 3, 257);
INSERT INTO `sys_role_menu` VALUES (12696, 3, 258);
INSERT INTO `sys_role_menu` VALUES (12697, 3, 259);
INSERT INTO `sys_role_menu` VALUES (12698, 3, 260);
INSERT INTO `sys_role_menu` VALUES (12699, 3, 261);
INSERT INTO `sys_role_menu` VALUES (12700, 3, 262);
INSERT INTO `sys_role_menu` VALUES (10820, 4, -666666);
INSERT INTO `sys_role_menu` VALUES (10827, 4, 1);
INSERT INTO `sys_role_menu` VALUES (10828, 4, 2);
INSERT INTO `sys_role_menu` VALUES (10829, 4, 3);
INSERT INTO `sys_role_menu` VALUES (10830, 4, 4);
INSERT INTO `sys_role_menu` VALUES (10812, 4, 15);
INSERT INTO `sys_role_menu` VALUES (10813, 4, 19);
INSERT INTO `sys_role_menu` VALUES (10814, 4, 23);
INSERT INTO `sys_role_menu` VALUES (10821, 4, 32);
INSERT INTO `sys_role_menu` VALUES (10735, 4, 34);
INSERT INTO `sys_role_menu` VALUES (10822, 4, 35);
INSERT INTO `sys_role_menu` VALUES (10824, 4, 38);
INSERT INTO `sys_role_menu` VALUES (10826, 4, 39);
INSERT INTO `sys_role_menu` VALUES (10726, 4, 44);
INSERT INTO `sys_role_menu` VALUES (10725, 4, 45);
INSERT INTO `sys_role_menu` VALUES (10823, 4, 48);
INSERT INTO `sys_role_menu` VALUES (10744, 4, 59);
INSERT INTO `sys_role_menu` VALUES (10745, 4, 60);
INSERT INTO `sys_role_menu` VALUES (10750, 4, 61);
INSERT INTO `sys_role_menu` VALUES (10732, 4, 67);
INSERT INTO `sys_role_menu` VALUES (10736, 4, 69);
INSERT INTO `sys_role_menu` VALUES (10737, 4, 70);
INSERT INTO `sys_role_menu` VALUES (10738, 4, 71);
INSERT INTO `sys_role_menu` VALUES (10739, 4, 72);
INSERT INTO `sys_role_menu` VALUES (10740, 4, 73);
INSERT INTO `sys_role_menu` VALUES (10741, 4, 78);
INSERT INTO `sys_role_menu` VALUES (10742, 4, 82);
INSERT INTO `sys_role_menu` VALUES (10811, 4, 98);
INSERT INTO `sys_role_menu` VALUES (10746, 4, 100);
INSERT INTO `sys_role_menu` VALUES (10747, 4, 101);
INSERT INTO `sys_role_menu` VALUES (10748, 4, 102);
INSERT INTO `sys_role_menu` VALUES (10749, 4, 103);
INSERT INTO `sys_role_menu` VALUES (10751, 4, 104);
INSERT INTO `sys_role_menu` VALUES (10752, 4, 105);
INSERT INTO `sys_role_menu` VALUES (10753, 4, 106);
INSERT INTO `sys_role_menu` VALUES (10754, 4, 107);
INSERT INTO `sys_role_menu` VALUES (10727, 4, 113);
INSERT INTO `sys_role_menu` VALUES (10755, 4, 130);
INSERT INTO `sys_role_menu` VALUES (10756, 4, 131);
INSERT INTO `sys_role_menu` VALUES (10757, 4, 132);
INSERT INTO `sys_role_menu` VALUES (10758, 4, 133);
INSERT INTO `sys_role_menu` VALUES (10759, 4, 134);
INSERT INTO `sys_role_menu` VALUES (10728, 4, 135);
INSERT INTO `sys_role_menu` VALUES (10729, 4, 136);
INSERT INTO `sys_role_menu` VALUES (10730, 4, 137);
INSERT INTO `sys_role_menu` VALUES (10788, 4, 138);
INSERT INTO `sys_role_menu` VALUES (10789, 4, 139);
INSERT INTO `sys_role_menu` VALUES (10794, 4, 140);
INSERT INTO `sys_role_menu` VALUES (10795, 4, 141);
INSERT INTO `sys_role_menu` VALUES (10800, 4, 142);
INSERT INTO `sys_role_menu` VALUES (10790, 4, 143);
INSERT INTO `sys_role_menu` VALUES (10791, 4, 144);
INSERT INTO `sys_role_menu` VALUES (10792, 4, 145);
INSERT INTO `sys_role_menu` VALUES (10793, 4, 146);
INSERT INTO `sys_role_menu` VALUES (10796, 4, 147);
INSERT INTO `sys_role_menu` VALUES (10797, 4, 148);
INSERT INTO `sys_role_menu` VALUES (10798, 4, 149);
INSERT INTO `sys_role_menu` VALUES (10799, 4, 150);
INSERT INTO `sys_role_menu` VALUES (10801, 4, 152);
INSERT INTO `sys_role_menu` VALUES (10802, 4, 153);
INSERT INTO `sys_role_menu` VALUES (10803, 4, 154);
INSERT INTO `sys_role_menu` VALUES (10804, 4, 155);
INSERT INTO `sys_role_menu` VALUES (10784, 4, 161);
INSERT INTO `sys_role_menu` VALUES (10760, 4, 164);
INSERT INTO `sys_role_menu` VALUES (10761, 4, 165);
INSERT INTO `sys_role_menu` VALUES (10762, 4, 166);
INSERT INTO `sys_role_menu` VALUES (10763, 4, 167);
INSERT INTO `sys_role_menu` VALUES (10764, 4, 168);
INSERT INTO `sys_role_menu` VALUES (10772, 4, 170);
INSERT INTO `sys_role_menu` VALUES (10773, 4, 171);
INSERT INTO `sys_role_menu` VALUES (10779, 4, 172);
INSERT INTO `sys_role_menu` VALUES (10783, 4, 173);
INSERT INTO `sys_role_menu` VALUES (10774, 4, 174);
INSERT INTO `sys_role_menu` VALUES (10775, 4, 175);
INSERT INTO `sys_role_menu` VALUES (10776, 4, 176);
INSERT INTO `sys_role_menu` VALUES (10777, 4, 177);
INSERT INTO `sys_role_menu` VALUES (10778, 4, 178);
INSERT INTO `sys_role_menu` VALUES (10780, 4, 179);
INSERT INTO `sys_role_menu` VALUES (10781, 4, 180);
INSERT INTO `sys_role_menu` VALUES (10782, 4, 181);
INSERT INTO `sys_role_menu` VALUES (10765, 4, 182);
INSERT INTO `sys_role_menu` VALUES (10766, 4, 183);
INSERT INTO `sys_role_menu` VALUES (10767, 4, 184);
INSERT INTO `sys_role_menu` VALUES (10805, 4, 185);
INSERT INTO `sys_role_menu` VALUES (10810, 4, 186);
INSERT INTO `sys_role_menu` VALUES (10806, 4, 187);
INSERT INTO `sys_role_menu` VALUES (10807, 4, 188);
INSERT INTO `sys_role_menu` VALUES (10808, 4, 189);
INSERT INTO `sys_role_menu` VALUES (10809, 4, 190);
INSERT INTO `sys_role_menu` VALUES (10785, 4, 197);
INSERT INTO `sys_role_menu` VALUES (10768, 4, 198);
INSERT INTO `sys_role_menu` VALUES (10769, 4, 199);
INSERT INTO `sys_role_menu` VALUES (10770, 4, 200);
INSERT INTO `sys_role_menu` VALUES (10771, 4, 201);
INSERT INTO `sys_role_menu` VALUES (10786, 4, 202);
INSERT INTO `sys_role_menu` VALUES (10787, 4, 203);
INSERT INTO `sys_role_menu` VALUES (10731, 4, 204);
INSERT INTO `sys_role_menu` VALUES (10733, 4, 205);
INSERT INTO `sys_role_menu` VALUES (10734, 4, 206);
INSERT INTO `sys_role_menu` VALUES (10815, 4, 207);
INSERT INTO `sys_role_menu` VALUES (10816, 4, 208);
INSERT INTO `sys_role_menu` VALUES (10817, 4, 209);
INSERT INTO `sys_role_menu` VALUES (10818, 4, 210);
INSERT INTO `sys_role_menu` VALUES (10819, 4, 211);
INSERT INTO `sys_role_menu` VALUES (10825, 4, 212);
INSERT INTO `sys_role_menu` VALUES (10743, 4, 213);
INSERT INTO `sys_role_menu` VALUES (9689, 5, -666666);
INSERT INTO `sys_role_menu` VALUES (9713, 5, 1);
INSERT INTO `sys_role_menu` VALUES (9714, 5, 2);
INSERT INTO `sys_role_menu` VALUES (9715, 5, 3);
INSERT INTO `sys_role_menu` VALUES (9716, 5, 4);
INSERT INTO `sys_role_menu` VALUES (9686, 5, 15);
INSERT INTO `sys_role_menu` VALUES (9687, 5, 19);
INSERT INTO `sys_role_menu` VALUES (9688, 5, 23);
INSERT INTO `sys_role_menu` VALUES (9690, 5, 32);
INSERT INTO `sys_role_menu` VALUES (9691, 5, 34);
INSERT INTO `sys_role_menu` VALUES (9692, 5, 35);
INSERT INTO `sys_role_menu` VALUES (9694, 5, 38);
INSERT INTO `sys_role_menu` VALUES (9712, 5, 39);
INSERT INTO `sys_role_menu` VALUES (9658, 5, 44);
INSERT INTO `sys_role_menu` VALUES (9657, 5, 45);
INSERT INTO `sys_role_menu` VALUES (9693, 5, 48);
INSERT INTO `sys_role_menu` VALUES (9695, 5, 59);
INSERT INTO `sys_role_menu` VALUES (9696, 5, 60);
INSERT INTO `sys_role_menu` VALUES (9697, 5, 61);
INSERT INTO `sys_role_menu` VALUES (9664, 5, 67);
INSERT INTO `sys_role_menu` VALUES (9665, 5, 69);
INSERT INTO `sys_role_menu` VALUES (9666, 5, 73);
INSERT INTO `sys_role_menu` VALUES (9667, 5, 78);
INSERT INTO `sys_role_menu` VALUES (9668, 5, 82);
INSERT INTO `sys_role_menu` VALUES (9685, 5, 98);
INSERT INTO `sys_role_menu` VALUES (9669, 5, 100);
INSERT INTO `sys_role_menu` VALUES (9670, 5, 104);
INSERT INTO `sys_role_menu` VALUES (9659, 5, 113);
INSERT INTO `sys_role_menu` VALUES (9698, 5, 130);
INSERT INTO `sys_role_menu` VALUES (9671, 5, 131);
INSERT INTO `sys_role_menu` VALUES (9660, 5, 135);
INSERT INTO `sys_role_menu` VALUES (9661, 5, 136);
INSERT INTO `sys_role_menu` VALUES (9662, 5, 137);
INSERT INTO `sys_role_menu` VALUES (9707, 5, 138);
INSERT INTO `sys_role_menu` VALUES (9708, 5, 139);
INSERT INTO `sys_role_menu` VALUES (9680, 5, 140);
INSERT INTO `sys_role_menu` VALUES (9709, 5, 141);
INSERT INTO `sys_role_menu` VALUES (9710, 5, 142);
INSERT INTO `sys_role_menu` VALUES (9679, 5, 143);
INSERT INTO `sys_role_menu` VALUES (9681, 5, 147);
INSERT INTO `sys_role_menu` VALUES (9682, 5, 152);
INSERT INTO `sys_role_menu` VALUES (9705, 5, 161);
INSERT INTO `sys_role_menu` VALUES (9699, 5, 164);
INSERT INTO `sys_role_menu` VALUES (9672, 5, 165);
INSERT INTO `sys_role_menu` VALUES (9702, 5, 170);
INSERT INTO `sys_role_menu` VALUES (9703, 5, 171);
INSERT INTO `sys_role_menu` VALUES (9704, 5, 172);
INSERT INTO `sys_role_menu` VALUES (9677, 5, 173);
INSERT INTO `sys_role_menu` VALUES (9675, 5, 174);
INSERT INTO `sys_role_menu` VALUES (9676, 5, 179);
INSERT INTO `sys_role_menu` VALUES (9700, 5, 182);
INSERT INTO `sys_role_menu` VALUES (9673, 5, 183);
INSERT INTO `sys_role_menu` VALUES (9711, 5, 185);
INSERT INTO `sys_role_menu` VALUES (9684, 5, 186);
INSERT INTO `sys_role_menu` VALUES (9683, 5, 187);
INSERT INTO `sys_role_menu` VALUES (9706, 5, 197);
INSERT INTO `sys_role_menu` VALUES (9701, 5, 198);
INSERT INTO `sys_role_menu` VALUES (9674, 5, 199);
INSERT INTO `sys_role_menu` VALUES (9678, 5, 202);
INSERT INTO `sys_role_menu` VALUES (9663, 5, 204);
INSERT INTO `sys_role_menu` VALUES (5551, 6, -666666);
INSERT INTO `sys_role_menu` VALUES (5546, 6, 34);
INSERT INTO `sys_role_menu` VALUES (5552, 6, 44);
INSERT INTO `sys_role_menu` VALUES (5544, 6, 45);
INSERT INTO `sys_role_menu` VALUES (5547, 6, 69);
INSERT INTO `sys_role_menu` VALUES (5548, 6, 70);
INSERT INTO `sys_role_menu` VALUES (5549, 6, 71);
INSERT INTO `sys_role_menu` VALUES (5550, 6, 72);
INSERT INTO `sys_role_menu` VALUES (5545, 6, 113);
INSERT INTO `sys_role_menu` VALUES (5845, 15, -666666);
INSERT INTO `sys_role_menu` VALUES (5824, 15, 44);
INSERT INTO `sys_role_menu` VALUES (5829, 15, 59);
INSERT INTO `sys_role_menu` VALUES (5830, 15, 60);
INSERT INTO `sys_role_menu` VALUES (5835, 15, 61);
INSERT INTO `sys_role_menu` VALUES (5831, 15, 100);
INSERT INTO `sys_role_menu` VALUES (5832, 15, 101);
INSERT INTO `sys_role_menu` VALUES (5833, 15, 102);
INSERT INTO `sys_role_menu` VALUES (5834, 15, 103);
INSERT INTO `sys_role_menu` VALUES (5836, 15, 104);
INSERT INTO `sys_role_menu` VALUES (5837, 15, 105);
INSERT INTO `sys_role_menu` VALUES (5838, 15, 106);
INSERT INTO `sys_role_menu` VALUES (5839, 15, 107);
INSERT INTO `sys_role_menu` VALUES (5825, 15, 113);
INSERT INTO `sys_role_menu` VALUES (5840, 15, 130);
INSERT INTO `sys_role_menu` VALUES (5841, 15, 131);
INSERT INTO `sys_role_menu` VALUES (5842, 15, 132);
INSERT INTO `sys_role_menu` VALUES (5843, 15, 133);
INSERT INTO `sys_role_menu` VALUES (5844, 15, 134);
INSERT INTO `sys_role_menu` VALUES (5826, 15, 135);
INSERT INTO `sys_role_menu` VALUES (5827, 15, 136);
INSERT INTO `sys_role_menu` VALUES (5828, 15, 137);
INSERT INTO `sys_role_menu` VALUES (10698, 16, -666666);
INSERT INTO `sys_role_menu` VALUES (10720, 16, 1);
INSERT INTO `sys_role_menu` VALUES (10721, 16, 2);
INSERT INTO `sys_role_menu` VALUES (10722, 16, 3);
INSERT INTO `sys_role_menu` VALUES (10723, 16, 4);
INSERT INTO `sys_role_menu` VALUES (10692, 16, 15);
INSERT INTO `sys_role_menu` VALUES (10693, 16, 19);
INSERT INTO `sys_role_menu` VALUES (10694, 16, 23);
INSERT INTO `sys_role_menu` VALUES (10699, 16, 32);
INSERT INTO `sys_role_menu` VALUES (10637, 16, 34);
INSERT INTO `sys_role_menu` VALUES (10700, 16, 35);
INSERT INTO `sys_role_menu` VALUES (10702, 16, 38);
INSERT INTO `sys_role_menu` VALUES (10719, 16, 39);
INSERT INTO `sys_role_menu` VALUES (10628, 16, 44);
INSERT INTO `sys_role_menu` VALUES (10627, 16, 45);
INSERT INTO `sys_role_menu` VALUES (10701, 16, 48);
INSERT INTO `sys_role_menu` VALUES (10704, 16, 59);
INSERT INTO `sys_role_menu` VALUES (10705, 16, 60);
INSERT INTO `sys_role_menu` VALUES (10706, 16, 61);
INSERT INTO `sys_role_menu` VALUES (10634, 16, 67);
INSERT INTO `sys_role_menu` VALUES (10638, 16, 69);
INSERT INTO `sys_role_menu` VALUES (10639, 16, 70);
INSERT INTO `sys_role_menu` VALUES (10640, 16, 71);
INSERT INTO `sys_role_menu` VALUES (10641, 16, 72);
INSERT INTO `sys_role_menu` VALUES (10642, 16, 73);
INSERT INTO `sys_role_menu` VALUES (10643, 16, 74);
INSERT INTO `sys_role_menu` VALUES (10644, 16, 75);
INSERT INTO `sys_role_menu` VALUES (10645, 16, 77);
INSERT INTO `sys_role_menu` VALUES (10646, 16, 78);
INSERT INTO `sys_role_menu` VALUES (10647, 16, 82);
INSERT INTO `sys_role_menu` VALUES (10691, 16, 98);
INSERT INTO `sys_role_menu` VALUES (10650, 16, 100);
INSERT INTO `sys_role_menu` VALUES (10651, 16, 101);
INSERT INTO `sys_role_menu` VALUES (10652, 16, 102);
INSERT INTO `sys_role_menu` VALUES (10653, 16, 104);
INSERT INTO `sys_role_menu` VALUES (10654, 16, 105);
INSERT INTO `sys_role_menu` VALUES (10655, 16, 106);
INSERT INTO `sys_role_menu` VALUES (10629, 16, 113);
INSERT INTO `sys_role_menu` VALUES (10707, 16, 130);
INSERT INTO `sys_role_menu` VALUES (10656, 16, 131);
INSERT INTO `sys_role_menu` VALUES (10657, 16, 132);
INSERT INTO `sys_role_menu` VALUES (10658, 16, 133);
INSERT INTO `sys_role_menu` VALUES (10630, 16, 135);
INSERT INTO `sys_role_menu` VALUES (10631, 16, 136);
INSERT INTO `sys_role_menu` VALUES (10632, 16, 137);
INSERT INTO `sys_role_menu` VALUES (10714, 16, 138);
INSERT INTO `sys_role_menu` VALUES (10715, 16, 139);
INSERT INTO `sys_role_menu` VALUES (10680, 16, 140);
INSERT INTO `sys_role_menu` VALUES (10716, 16, 141);
INSERT INTO `sys_role_menu` VALUES (10717, 16, 142);
INSERT INTO `sys_role_menu` VALUES (10677, 16, 143);
INSERT INTO `sys_role_menu` VALUES (10678, 16, 144);
INSERT INTO `sys_role_menu` VALUES (10679, 16, 145);
INSERT INTO `sys_role_menu` VALUES (10681, 16, 147);
INSERT INTO `sys_role_menu` VALUES (10682, 16, 148);
INSERT INTO `sys_role_menu` VALUES (10683, 16, 149);
INSERT INTO `sys_role_menu` VALUES (10684, 16, 152);
INSERT INTO `sys_role_menu` VALUES (10685, 16, 153);
INSERT INTO `sys_role_menu` VALUES (10686, 16, 154);
INSERT INTO `sys_role_menu` VALUES (10712, 16, 161);
INSERT INTO `sys_role_menu` VALUES (10708, 16, 164);
INSERT INTO `sys_role_menu` VALUES (10659, 16, 165);
INSERT INTO `sys_role_menu` VALUES (10660, 16, 166);
INSERT INTO `sys_role_menu` VALUES (10661, 16, 167);
INSERT INTO `sys_role_menu` VALUES (10709, 16, 170);
INSERT INTO `sys_role_menu` VALUES (10710, 16, 171);
INSERT INTO `sys_role_menu` VALUES (10711, 16, 172);
INSERT INTO `sys_role_menu` VALUES (10675, 16, 173);
INSERT INTO `sys_role_menu` VALUES (10669, 16, 174);
INSERT INTO `sys_role_menu` VALUES (10670, 16, 175);
INSERT INTO `sys_role_menu` VALUES (10671, 16, 176);
INSERT INTO `sys_role_menu` VALUES (10672, 16, 178);
INSERT INTO `sys_role_menu` VALUES (10673, 16, 179);
INSERT INTO `sys_role_menu` VALUES (10674, 16, 180);
INSERT INTO `sys_role_menu` VALUES (10662, 16, 182);
INSERT INTO `sys_role_menu` VALUES (10663, 16, 183);
INSERT INTO `sys_role_menu` VALUES (10664, 16, 184);
INSERT INTO `sys_role_menu` VALUES (10718, 16, 185);
INSERT INTO `sys_role_menu` VALUES (10690, 16, 186);
INSERT INTO `sys_role_menu` VALUES (10687, 16, 187);
INSERT INTO `sys_role_menu` VALUES (10688, 16, 188);
INSERT INTO `sys_role_menu` VALUES (10689, 16, 189);
INSERT INTO `sys_role_menu` VALUES (10713, 16, 197);
INSERT INTO `sys_role_menu` VALUES (10665, 16, 198);
INSERT INTO `sys_role_menu` VALUES (10666, 16, 199);
INSERT INTO `sys_role_menu` VALUES (10667, 16, 200);
INSERT INTO `sys_role_menu` VALUES (10668, 16, 201);
INSERT INTO `sys_role_menu` VALUES (10676, 16, 202);
INSERT INTO `sys_role_menu` VALUES (10633, 16, 204);
INSERT INTO `sys_role_menu` VALUES (10635, 16, 205);
INSERT INTO `sys_role_menu` VALUES (10636, 16, 206);
INSERT INTO `sys_role_menu` VALUES (10724, 16, 207);
INSERT INTO `sys_role_menu` VALUES (10695, 16, 208);
INSERT INTO `sys_role_menu` VALUES (10696, 16, 209);
INSERT INTO `sys_role_menu` VALUES (10697, 16, 210);
INSERT INTO `sys_role_menu` VALUES (10703, 16, 212);
INSERT INTO `sys_role_menu` VALUES (10648, 16, 213);
INSERT INTO `sys_role_menu` VALUES (10649, 16, 214);
INSERT INTO `sys_role_menu` VALUES (10837, 17, -666666);
INSERT INTO `sys_role_menu` VALUES (10832, 17, 32);
INSERT INTO `sys_role_menu` VALUES (10831, 17, 45);
INSERT INTO `sys_role_menu` VALUES (10833, 17, 67);
INSERT INTO `sys_role_menu` VALUES (10834, 17, 68);
INSERT INTO `sys_role_menu` VALUES (10835, 17, 205);
INSERT INTO `sys_role_menu` VALUES (10836, 17, 206);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `inviter_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 175 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与角色对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (134, 1, 0);
INSERT INTO `sys_user_role` VALUES (61, 16, 3);
INSERT INTO `sys_user_role` VALUES (126, 17, 5);
INSERT INTO `sys_user_role` VALUES (19, 18, 6);
INSERT INTO `sys_user_role` VALUES (107, 19, 5);
INSERT INTO `sys_user_role` VALUES (123, 30, 3);
INSERT INTO `sys_user_role` VALUES (68, 31, 4);
INSERT INTO `sys_user_role` VALUES (62, 51, 3);
INSERT INTO `sys_user_role` VALUES (63, 52, 3);
INSERT INTO `sys_user_role` VALUES (64, 52, 4);
INSERT INTO `sys_user_role` VALUES (65, 52, 5);
INSERT INTO `sys_user_role` VALUES (66, 52, 6);
INSERT INTO `sys_user_role` VALUES (74, 53, 3);
INSERT INTO `sys_user_role` VALUES (129, 54, 16);
INSERT INTO `sys_user_role` VALUES (122, 55, 3);
INSERT INTO `sys_user_role` VALUES (108, 65, 3);
INSERT INTO `sys_user_role` VALUES (109, 66, 3);
INSERT INTO `sys_user_role` VALUES (114, 67, 3);
INSERT INTO `sys_user_role` VALUES (117, 67, 4);
INSERT INTO `sys_user_role` VALUES (116, 67, 5);
INSERT INTO `sys_user_role` VALUES (115, 67, 6);
INSERT INTO `sys_user_role` VALUES (130, 68, 3);
INSERT INTO `sys_user_role` VALUES (166, 69, 4);
INSERT INTO `sys_user_role` VALUES (132, 70, 0);
INSERT INTO `sys_user_role` VALUES (135, 71, 0);
INSERT INTO `sys_user_role` VALUES (137, 72, 0);
INSERT INTO `sys_user_role` VALUES (138, 73, 3);
INSERT INTO `sys_user_role` VALUES (139, 74, 3);
INSERT INTO `sys_user_role` VALUES (140, 75, 3);
INSERT INTO `sys_user_role` VALUES (141, 76, 3);
INSERT INTO `sys_user_role` VALUES (142, 77, 4);
INSERT INTO `sys_user_role` VALUES (144, 78, 3);
INSERT INTO `sys_user_role` VALUES (151, 80, 4);
INSERT INTO `sys_user_role` VALUES (157, 81, 3);
INSERT INTO `sys_user_role` VALUES (164, 82, 16);
INSERT INTO `sys_user_role` VALUES (172, 83, 3);
INSERT INTO `sys_user_role` VALUES (171, 83, 4);
INSERT INTO `sys_user_role` VALUES (170, 83, 5);
INSERT INTO `sys_user_role` VALUES (169, 83, 6);
INSERT INTO `sys_user_role` VALUES (168, 83, 16);
INSERT INTO `sys_user_role` VALUES (167, 83, 17);
INSERT INTO `sys_user_role` VALUES (173, 84, 3);
INSERT INTO `sys_user_role` VALUES (174, 85, 3);

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`  (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'token',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `token`(`token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户Token' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------

-- ----------------------------
-- Table structure for tb_coupon
-- ----------------------------
DROP TABLE IF EXISTS `tb_coupon`;
CREATE TABLE `tb_coupon`  (
  `coupon_id` int(11) NOT NULL AUTO_INCREMENT,
  `coupon_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `coupon_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券图片',
  `valid_days` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '有效期天数',
  `min_money` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券可使用订单最低金额',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券抵扣金额',
  `delete_flag` int(1) NULL DEFAULT 0 COMMENT '是否删除 0未删除 -1已删除',
  `is_enable` int(1) NULL DEFAULT NULL COMMENT '是否启用 0未启用 1已启用',
  `max_receive` int(11) NULL DEFAULT NULL COMMENT '最多领取或购买数量(0为不限制数量)',
  `coupon_num` int(11) NULL DEFAULT NULL COMMENT '优惠券数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`coupon_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for tb_coupon_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_coupon_user`;
CREATE TABLE `tb_coupon_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `coupon_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `coupon_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券图片',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '优惠券领取时间',
  `employ_time` datetime(0) NULL DEFAULT NULL COMMENT '优惠券使用时间',
  `expiration_time` datetime(0) NULL DEFAULT NULL COMMENT '优惠券过期时间',
  `min_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券可使用订单最低金额',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券金额',
  `status` int(11) NULL DEFAULT NULL COMMENT '优惠券状态 0正常  1已使用  2已失效',
  `valid_days` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '有效天数',
  `coupon_id` int(11) NULL DEFAULT NULL COMMENT '绑定的优惠券id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_coupon_user
-- ----------------------------

-- ----------------------------
-- Table structure for tb_daily_tasks
-- ----------------------------
DROP TABLE IF EXISTS `tb_daily_tasks`;
CREATE TABLE `tb_daily_tasks`  (
  `id` int(11) NOT NULL COMMENT '主键id',
  `tasks_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `task_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务类型',
  `species` int(11) NULL DEFAULT NULL COMMENT '任务奖励的金币数',
  `one_mission` int(1) NULL DEFAULT NULL COMMENT '是否一次性任务 0否 1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '每日任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_daily_tasks
-- ----------------------------

-- ----------------------------
-- Table structure for tb_footprint
-- ----------------------------
DROP TABLE IF EXISTS `tb_footprint`;
CREATE TABLE `tb_footprint`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '浏览id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `good_id` int(11) NULL DEFAULT NULL COMMENT '商品id',
  `visit_time` datetime(0) NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4498 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户浏览足迹表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_footprint
-- ----------------------------

-- ----------------------------
-- Table structure for tb_good_things
-- ----------------------------
DROP TABLE IF EXISTS `tb_good_things`;
CREATE TABLE `tb_good_things`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `things_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '好物名称',
  `img` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '好物图片',
  `is_shelf` int(1) NULL DEFAULT 0 COMMENT '0上架  1下架',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分享好物的地址',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '好物描述',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分享好物表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_good_things
-- ----------------------------

-- ----------------------------
-- Table structure for tb_orders
-- ----------------------------
DROP TABLE IF EXISTS `tb_orders`;
CREATE TABLE `tb_orders`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '下单人id',
  `consignee` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `user_coupons_id` int(11) NULL DEFAULT NULL COMMENT '用户优惠券id',
  `coupon_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `address_id` int(11) NULL DEFAULT NULL COMMENT '地址id',
  `address_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `goods_id` int(11) NULL DEFAULT NULL COMMENT '商品id',
  `express_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '快递名称',
  `express_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '快递单号',
  `day_id` int(11) NULL DEFAULT NULL COMMENT '租赁天数id',
  `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `finish_time` datetime(0) NULL DEFAULT NULL COMMENT '完成时间',
  `pay_way` int(11) NULL DEFAULT NULL COMMENT '支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）',
  `pay_status` int(1) NULL DEFAULT NULL COMMENT '订单状态：0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消',
  `one_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '一级佣金',
  `one_user_id` int(11) NULL DEFAULT NULL COMMENT '一级用户',
  `two_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '二级佣金',
  `two_user_id` int(11) NULL DEFAULT NULL COMMENT '二级用户',
  `spec_id` int(11) NULL DEFAULT NULL COMMENT '规格id',
  `selected_spec` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '已选规格的json数据',
  `pay_num` int(11) NULL DEFAULT NULL COMMENT '购买数量',
  `lease_term` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租赁天数',
  `freight` decimal(10, 2) NULL DEFAULT NULL COMMENT '运费',
  `deposit` decimal(10, 2) NULL DEFAULT NULL COMMENT '押金',
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `gross_rent` decimal(10, 2) NULL DEFAULT NULL COMMENT '总租金',
  `pay_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额（合计）',
  `lease_start_date` date NULL DEFAULT NULL COMMENT '预计起租日',
  `return_date` date NULL DEFAULT NULL COMMENT '预计归还日',
  `take_state` int(1) NULL DEFAULT NULL COMMENT '提货方式： 1商家邮寄 2到店自提',
  `estimate_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '预估价格',
  `buyout_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '买断价格（补差）',
  `cancellation_time` datetime(0) NULL DEFAULT NULL COMMENT '到店自提的核销时间',
  `daily_rent` decimal(10, 2) NULL DEFAULT NULL COMMENT '日租金',
  `pickup_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自提地址',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `platform_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '平台本单收入',
  `merchant_income` decimal(10, 2) NULL DEFAULT NULL COMMENT '商家本单收入',
  `business_id` int(11) NULL DEFAULT NULL COMMENT '商家id',
  `continue_date` date NULL DEFAULT NULL COMMENT '续租日期',
  `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT '取消时间',
  `supplement_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '补缴的租金',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_orders
-- ----------------------------

-- ----------------------------
-- Table structure for tb_release_unused
-- ----------------------------
DROP TABLE IF EXISTS `tb_release_unused`;
CREATE TABLE `tb_release_unused`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '闲置物品描述',
  `img` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物品图片',
  `is_ground` int(1) NULL DEFAULT 0 COMMENT '0上架  1下架',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布的位置',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '发布物品的价格',
  `delivery_id` int(11) NULL DEFAULT NULL COMMENT '发货方式id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发布闲置物品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_release_unused
-- ----------------------------

-- ----------------------------
-- Table structure for tb_signin
-- ----------------------------
DROP TABLE IF EXISTS `tb_signin`;
CREATE TABLE `tb_signin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务表id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `check_in_date` date NULL DEFAULT NULL COMMENT '签到日期',
  `consecutive_days` int(11) NULL DEFAULT NULL COMMENT '用户连续签到的天数',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `sign_status` tinyint(1) NULL DEFAULT 0 COMMENT '0未签  1已签  -1断签',
  `task_id` int(11) NULL DEFAULT NULL COMMENT '任务表id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户签到表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_signin
-- ----------------------------

-- ----------------------------
-- Table structure for tb_signin_reward
-- ----------------------------
DROP TABLE IF EXISTS `tb_signin_reward`;
CREATE TABLE `tb_signin_reward`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `consecutive_days` int(11) NULL DEFAULT NULL COMMENT '连续签到的天数',
  `gold_coins` int(11) NULL DEFAULT NULL COMMENT '对应奖励的金币数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '签到天数奖励表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_signin_reward
-- ----------------------------
INSERT INTO `tb_signin_reward` VALUES (1, 1, 1);
INSERT INTO `tb_signin_reward` VALUES (2, 2, 2);
INSERT INTO `tb_signin_reward` VALUES (3, 3, 3);
INSERT INTO `tb_signin_reward` VALUES (4, 4, 4);
INSERT INTO `tb_signin_reward` VALUES (5, 5, 5);
INSERT INTO `tb_signin_reward` VALUES (6, 6, 6);
INSERT INTO `tb_signin_reward` VALUES (7, 7, 7);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `sex` int(11) NULL DEFAULT NULL COMMENT '性别 1男 2女',
  `age` int(4) NULL DEFAULT NULL COMMENT '年龄',
  `open_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信小程序openId',
  `wx_open_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信公众号openId',
  `account_open_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信App  openId',
  `balance` decimal(10, 2) NULL DEFAULT NULL COMMENT '收益',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `profile` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个人介绍',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加密盐',
  `apple_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '苹果id',
  `sys_phone` int(11) NULL DEFAULT NULL COMMENT '手机类型 1安卓 2ios',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态 1正常 2禁用',
  `platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源 APP 小程序  公众号',
  `clientid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'app推送id',
  `zhi_fu_bao_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝名称',
  `zhi_fu_bao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝账号',
  `is_send_msg` int(11) NULL DEFAULT NULL COMMENT '是否推送消息',
  `wx_img` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信提现图片',
  `msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '最近一条信息内容',
  `is_auth` int(1) NULL DEFAULT 0 COMMENT '是否已认证 0否 1是',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `invitation_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请码',
  `inviter_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请人的邀请码',
  `inviter_inviter_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请人上级的邀请码',
  `divide_scale` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下级抽佣比例',
  `divide_two_scale` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二级抽佣比例',
  `is_admin` int(1) NULL DEFAULT NULL COMMENT '是否是管理员 0否 1是',
  `total_gold` int(11) NULL DEFAULT 0 COMMENT '总金币数',
  `user_type` int(1) UNSIGNED ZEROFILL NULL DEFAULT 1 COMMENT '1普通用户  2商家',
  `order_jifen` decimal(18, 2) NULL DEFAULT NULL COMMENT '积分',
  `is_invitation` int(11) NULL DEFAULT 0,
  `is_vip` int(1) NULL DEFAULT 2 COMMENT '1是会员2不是',
  `reputation_status` int(1) NULL DEFAULT 3 COMMENT '1信誉差 2信誉良好 3信誉非常好',
  `vip_end_time` datetime(0) NULL DEFAULT NULL COMMENT '会员结束时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------

-- ----------------------------
-- Table structure for tb_user_task
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_task`;
CREATE TABLE `tb_user_task`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `task_id` int(11) NULL DEFAULT NULL COMMENT '任务id',
  `reward_claimed` int(1) NULL DEFAULT NULL COMMENT '奖励是否已被领取: 0未领取  1已领取',
  `state` int(1) NULL DEFAULT NULL COMMENT '是否完成： 0未完成  1已完成',
  `create_time` date NULL DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户每日任务完成情况表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_task
-- ----------------------------

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'tokenid',
  `user_id` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '用户id',
  `uuid` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT 'token值',
  `admin_id` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '管理员id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_name`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19470 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of token
-- ----------------------------

-- ----------------------------
-- Table structure for trends
-- ----------------------------
DROP TABLE IF EXISTS `trends`;
CREATE TABLE `trends`  (
  `trends_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '动态id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '动态类型id',
  `trends_image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '动态图片',
  `trends_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '动态内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '发布的价格',
  `audit_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '审核内容',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 0待审核 1审核通过 2审核不通过',
  `video_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '视频地址',
  `browse_count` int(11) NULL DEFAULT NULL COMMENT '浏览量',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区',
  `lng` double(10, 6) NULL DEFAULT NULL COMMENT '经度',
  `lat` double(10, 6) NULL DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`trends_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户动态表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trends
-- ----------------------------

-- ----------------------------
-- Table structure for trends_comment
-- ----------------------------
DROP TABLE IF EXISTS `trends_comment`;
CREATE TABLE `trends_comment`  (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `trends_id` int(11) NULL DEFAULT NULL COMMENT '动态id',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '评论内容',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `parent_comment_id` int(11) NULL DEFAULT NULL COMMENT '回复的评论id',
  `is_look` int(1) NULL DEFAULT NULL COMMENT '被回复人是否已查看0否 1是',
  `by_user_id` int(11) NULL DEFAULT NULL COMMENT '被回复的用户id',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '动态评论' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trends_comment
-- ----------------------------

-- ----------------------------
-- Table structure for trends_like
-- ----------------------------
DROP TABLE IF EXISTS `trends_like`;
CREATE TABLE `trends_like`  (
  `like_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `trends_id` int(11) NULL DEFAULT NULL COMMENT '动态id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `comment_id` int(11) NULL DEFAULT NULL COMMENT '评论id',
  `is_look` int(1) NULL DEFAULT NULL COMMENT '被点赞是否已查看0否 1是',
  `by_user_id` int(11) NULL DEFAULT NULL COMMENT '被点赞的用户',
  PRIMARY KEY (`like_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '动态点赞' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trends_like
-- ----------------------------

-- ----------------------------
-- Table structure for trends_type
-- ----------------------------
DROP TABLE IF EXISTS `trends_type`;
CREATE TABLE `trends_type`  (
  `id` int(11) NOT NULL,
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '动态类型的名称',
  `sort` int(255) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '动态类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of trends_type
-- ----------------------------

-- ----------------------------
-- Table structure for user_certification
-- ----------------------------
DROP TABLE IF EXISTS `user_certification`;
CREATE TABLE `user_certification`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户实名认证id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `id_card_front` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '身份证正面照片',
  `id_card_verso` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '身份证反面照片',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `id_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证件号码',
  `status` int(1) NULL DEFAULT NULL COMMENT '0待审核 1已通过 2未通过',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核原因',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `audit_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
  `consignee` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `task_id` int(11) NULL DEFAULT NULL COMMENT '任务id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 156 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '实名认证' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_certification
-- ----------------------------

-- ----------------------------
-- Table structure for user_extension
-- ----------------------------
DROP TABLE IF EXISTS `user_extension`;
CREATE TABLE `user_extension`  (
  `extension_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '推广id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '申请用户id',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `status` int(1) NULL DEFAULT NULL COMMENT '1已提交 2审核通过 3审核失败',
  `remarks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',
  `address_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `real_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  PRIMARY KEY (`extension_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '推广员申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_extension
-- ----------------------------

-- ----------------------------
-- Table structure for user_money
-- ----------------------------
DROP TABLE IF EXISTS `user_money`;
CREATE TABLE `user_money`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '钱包id',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '钱包金额',
  `user_id` bigint(11) NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 377 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_money
-- ----------------------------

-- ----------------------------
-- Table structure for user_money_details
-- ----------------------------
DROP TABLE IF EXISTS `user_money_details`;
CREATE TABLE `user_money_details`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '钱包详情id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `by_user_id` int(11) NULL DEFAULT NULL COMMENT '邀请用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `classify` int(11) NULL DEFAULT NULL COMMENT '用途 0充值余额 1支付订单 3下级分成 4提现',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型 1增加 2减少',
  `state` int(11) NULL DEFAULT 1 COMMENT '状态 1待支付 2已到账 3取消',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '金额',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `user_level` int(1) NULL DEFAULT NULL COMMENT '1一级 2二级',
  `relation_id` bigint(20) NULL DEFAULT NULL COMMENT '关联id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 411 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_money_details
-- ----------------------------

-- ----------------------------
-- Table structure for user_vip
-- ----------------------------
DROP TABLE IF EXISTS `user_vip`;
CREATE TABLE `user_vip`  (
  `vip_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会员id',
  `vip_name_type` int(2) NULL DEFAULT NULL COMMENT '会员类型0月1季2年',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '购买时间',
  `end_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到期时间',
  `is_vip` int(255) NULL DEFAULT NULL COMMENT '1是会员2不是',
  PRIMARY KEY (`vip_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_vip
-- ----------------------------

-- ----------------------------
-- Table structure for user_visitor
-- ----------------------------
DROP TABLE IF EXISTS `user_visitor`;
CREATE TABLE `user_visitor`  (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '访客id',
  `user_id` bigint(32) NULL DEFAULT NULL COMMENT '用户id',
  `by_user_id` bigint(32) NULL DEFAULT NULL COMMENT '访问用户id',
  `update_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_visitor
-- ----------------------------

-- ----------------------------
-- Table structure for vip_details
-- ----------------------------
DROP TABLE IF EXISTS `vip_details`;
CREATE TABLE `vip_details`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `vip_name_type` int(2) NULL DEFAULT NULL COMMENT '会员类型0月1季2年',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '会员价格',
  `vip_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员名称',
  `rate` decimal(10, 2) NULL DEFAULT NULL,
  `award` decimal(10, 2) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `vip_month_count` int(10) NULL DEFAULT NULL COMMENT '月数',
  `is_enable` int(1) NULL DEFAULT NULL COMMENT '是否启用(1启用 0未启用)',
  `language_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '语种',
  `language_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '语言名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vip_details
-- ----------------------------
INSERT INTO `vip_details` VALUES (2, 1, 3.00, '季会员', NULL, 0.01, '2024-09-06 18:41:21', 3, 1, 'zh', '简体中文');
INSERT INTO `vip_details` VALUES (3, 2, 4.00, '年会员', NULL, 0.01, NULL, 12, 1, 'zh', '简体中文');
INSERT INTO `vip_details` VALUES (8, 0, 0.01, '月会员', NULL, 0.01, '2025-08-22 09:40:56', 1, 1, 'zh', '简体中文');

-- ----------------------------
-- Table structure for vip_discount
-- ----------------------------
DROP TABLE IF EXISTS `vip_discount`;
CREATE TABLE `vip_discount`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `discount` decimal(10, 0) NULL DEFAULT NULL COMMENT '优惠金币',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vip_discount
-- ----------------------------

-- ----------------------------
-- Table structure for word_info
-- ----------------------------
DROP TABLE IF EXISTS `word_info`;
CREATE TABLE `word_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_at` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `lable` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对应值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of word_info
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
