/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : lab_reservation

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 30/05/2026 17:22:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for laboratories
-- ----------------------------
DROP TABLE IF EXISTS `laboratories`;
CREATE TABLE `laboratories`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `capacity` int(0) NOT NULL,
  `equipment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `rules` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'AVAILABLE',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of laboratories
-- ----------------------------
INSERT INTO `laboratories` VALUES (1, '计算机实验室1', '实验楼1-101', '计算机', 40, '电脑40台，投影仪1台', '1. 保持安静 2. 禁止饮食 3. 爱护设备', '开放时间：8:00-22:00', 'UNAVAILABLE', '2026-04-22 15:03:43', '2026-05-28 16:21:35');
INSERT INTO `laboratories` VALUES (2, '化学实验室1', '实验楼2-201', '化学', 30, '实验台30个，通风橱10个', '1. 穿实验服 2. 遵守实验规程 3. 熟悉操作流程', '开放时间：9:00-21:00', 'AVAILABLE', '2026-04-22 15:03:43', '2026-05-28 16:21:35');
INSERT INTO `laboratories` VALUES (3, '物理实验室1', '实验楼3-301', '物理', 25, '示波器25台，天平10台', '1. 轻拿轻放 2. 记录数据 3. 整理实验台', '开放时间：8:30-21:30', 'AVAILABLE', '2026-04-22 15:03:43', '2026-05-28 16:21:35');
INSERT INTO `laboratories` VALUES (4, '计算机实验室2', '实验楼1-102', '计算机', 45, '电脑45台，投影仪2台，白板2个', '1. 保持安静 2. 禁止饮食 3. 爱护设备 4. 使用前请预约', '开放时间：8:00-22:00', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (5, '计算机实验室3', '实验楼1-103', '计算机', 50, '电脑50台，投影仪2台，电子讲台1套', '1. 保持安静 2. 禁止饮食 3. 爱护设备', '开放时间：8:00-22:00', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (6, '化学实验室2', '实验楼2-202', '化学', 35, '实验台35个，通风橱12个，紧急淋浴1套', '1. 穿实验服 2. 戴护目镜 3. 遵守实验规程', '开放时间：9:00-21:00', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (7, '化学实验室3', '实验楼2-203', '化学', 40, '实验台40个，通风橱15个，废液处理设备1套', '1. 穿实验服 2. 戴护目镜 3. 严禁明火', '开放时间：9:00-21:00', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (8, '物理实验室2', '实验楼3-302', '物理', 30, '示波器30台，天平12台，光学仪器若干', '1. 轻拿轻放 2. 记录数据 3. 整理实验台', '开放时间：8:30-21:30', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (9, '生物实验室1', '实验楼4-401', '生物', 28, '显微镜28台，培养箱5个，离心机2台', '1. 无菌操作 2. 禁止饮食 3. 实验后消毒', '开放时间：9:00-20:00', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (10, '电子实验室1', '实验楼5-501', '电子', 32, '示波器32台，信号发生器16台，万用表32个', '1. 注意防静电 2. 按规程操作 3. 实验后断电', '开放时间：8:00-21:00', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (11, '电子实验室2', '实验楼5-502', '电子', 36, '示波器36台，焊接设备10套，电路板制作工具', '1. 注意防静电 2. 焊接需授权 3. 安全第一', '开放时间：8:00-21:00', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (12, '机械工程实验室1', '实验楼6-601', '机械', 25, '车床5台，铣床3台，3D打印机2台', '1. 穿戴安全装备 2. 禁止独自操作大型设备 3. 遵守操作规程', '开放时间：9:00-20:00', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (13, '人工智能实验室1', '实验楼7-701', '人工智能', 40, '高性能GPU服务器4台，深度学习工作站40台', '1. 爱护设备 2. 遵守实验室规定 3. 使用前培训', '开放时间：全天24小时', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (14, '网络工程实验室1', '实验楼7-702', '网络', 35, '路由器20台，交换机30台，服务器5台', '1. 禁止修改网络配置 2. 遵守网络安全规定 3. 保持整洁', '开放时间：8:00-22:00', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (15, '材料科学实验室1', '实验楼8-801', '材料', 22, '金相显微镜10台，热处理炉3台，硬度计5台', '1. 高温设备注意安全 2. 佩戴防护装备 3. 实验后清理', '开放时间：9:00-18:00', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (16, '机器人实验室1', '实验楼9-901', '机器人', 20, '工业机器人3台，服务机器人5台，编程电脑20台', '1. 机器人运行时保持安全距离 2. 编程需授权 3. 使用后归位', '开放时间：9:00-21:00', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (17, '微电子实验室1', '实验楼9-902', '微电子', 18, '光刻机1台，镀膜设备2套，显微镜15台', '1. 净化室标准 2. 禁止携带食品饮料 3. 按规程操作', '开放时间：8:00-18:00', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');
INSERT INTO `laboratories` VALUES (18, '新能源实验室1', '实验楼10-1001', '新能源', 24, '太阳能测试设备3套，锂电池测试设备5套，电化学工作站10台', '1. 高压设备注意安全 2. 遵守实验室规程 3. 废弃物分类处理', '开放时间：9:00-20:00', 'AVAILABLE', '2026-06-01 00:00:00', '2026-06-01 00:00:00');

-- ----------------------------
-- Table structure for notifications
-- ----------------------------
DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NOT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `is_read` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notifications
-- ----------------------------
INSERT INTO `notifications` VALUES (12, 6, 'RESERVATION_SUCCESS', '预约成功！\n实验室：化学实验室1\n时段：2026-06-15 10:00-12:00', 1, '2026-05-27 17:47:15');
INSERT INTO `notifications` VALUES (13, 6, 'RESERVATION_REJECTED', '您的预约申请已被驳回！', 1, '2026-05-27 18:21:01');
INSERT INTO `notifications` VALUES (14, 6, 'RESERVATION_CANCELLED_BY_ADMIN', '您的预约因违规已被管理员取消！', 1, '2026-05-28 23:32:58');
INSERT INTO `notifications` VALUES (15, 6, 'RESERVATION_CANCEL', '预约已取消！', 1, '2026-05-28 23:40:05');
INSERT INTO `notifications` VALUES (16, 6, 'RESERVATION_CANCEL', '预约已取消！', 1, '2026-05-29 00:11:26');
INSERT INTO `notifications` VALUES (17, 5, 'RESERVATION_SUCCESS', '预约成功！\n实验室：化学实验室1\n时段：2026-05-29 09:00-11:00', 1, '2026-05-29 00:23:09');
INSERT INTO `notifications` VALUES (18, 5, 'RESERVATION_SUCCESS', '预约成功！\n实验室：化学实验室1\n时段：2026-05-29 11:00-13:00', 0, '2026-05-29 00:23:28');
INSERT INTO `notifications` VALUES (19, 6, 'RESERVATION_SUCCESS', '预约成功！\n实验室：化学实验室1\n时段：2026-05-29 14:00-16:00', 1, '2026-05-29 00:26:34');
INSERT INTO `notifications` VALUES (20, 6, 'RESERVATION_REJECTED', '您的预约申请已被驳回！\n驳回理由：实验室临时关闭', 1, '2026-05-29 00:47:43');

-- ----------------------------
-- Table structure for reservation_times
-- ----------------------------
DROP TABLE IF EXISTS `reservation_times`;
CREATE TABLE `reservation_times`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `laboratory_id` bigint(0) NOT NULL,
  `start_time` time(0) NOT NULL,
  `end_time` time(0) NOT NULL,
  `day_of_week` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `max_bookings` int(0) NOT NULL DEFAULT 1,
  `is_available` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `laboratory_id`(`laboratory_id`) USING BTREE,
  CONSTRAINT `reservation_times_ibfk_1` FOREIGN KEY (`laboratory_id`) REFERENCES `laboratories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reservation_times
-- ----------------------------
INSERT INTO `reservation_times` VALUES (1, 1, '08:00:00', '10:00:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (2, 1, '10:00:00', '12:00:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (3, 1, '14:00:00', '16:00:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (4, 1, '16:00:00', '18:00:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (5, 1, '18:00:00', '22:00:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (6, 2, '09:00:00', '11:00:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (7, 2, '11:00:00', '13:00:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (8, 2, '14:00:00', '16:00:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (9, 2, '16:00:00', '18:00:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (10, 2, '18:00:00', '21:00:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (11, 3, '08:30:00', '10:30:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (12, 3, '10:30:00', '12:30:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (13, 3, '14:30:00', '16:30:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (14, 3, '16:30:00', '18:30:00', 'MONDAY', 1, 1);
INSERT INTO `reservation_times` VALUES (15, 3, '18:30:00', '21:30:00', 'MONDAY', 1, 1);

-- ----------------------------
-- Table structure for reservations
-- ----------------------------
DROP TABLE IF EXISTS `reservations`;
CREATE TABLE `reservations`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NOT NULL,
  `laboratory_id` bigint(0) NOT NULL,
  `reservation_time_id` bigint(0) NOT NULL,
  `reservation_date` date NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PENDING',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `reject_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '驳回理由',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `laboratory_id`(`laboratory_id`) USING BTREE,
  INDEX `reservation_time_id`(`reservation_time_id`) USING BTREE,
  CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`laboratory_id`) REFERENCES `laboratories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `reservations_ibfk_3` FOREIGN KEY (`reservation_time_id`) REFERENCES `reservation_times` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reservations
-- ----------------------------
INSERT INTO `reservations` VALUES (1, 6, 1, 1, '2026-05-30', 'PENDING', '2026-05-27 17:03:56', '2026-05-27 17:03:56', NULL);
INSERT INTO `reservations` VALUES (2, 6, 1, 1, '2026-05-27', 'COMPLETED', '2026-05-27 17:04:26', '2026-05-27 17:04:26', NULL);
INSERT INTO `reservations` VALUES (3, 6, 1, 2, '2026-06-01', 'PENDING', '2026-05-27 17:10:49', '2026-05-27 17:10:49', NULL);
INSERT INTO `reservations` VALUES (4, 6, 2, 3, '2026-06-02', 'PENDING', '2026-05-27 17:12:19', '2026-05-27 17:12:19', NULL);
INSERT INTO `reservations` VALUES (5, 6, 2, 4, '2026-06-03', 'REJECTED', '2026-05-27 17:14:36', '2026-05-27 17:14:36', '实验室临时关闭');
INSERT INTO `reservations` VALUES (6, 6, 1, 5, '2026-06-04', 'PENDING', '2026-05-27 17:19:15', '2026-05-27 17:19:15', NULL);
INSERT INTO `reservations` VALUES (7, 6, 1, 1, '2026-06-05', 'PENDING', '2026-05-27 17:22:19', '2026-05-27 17:22:19', NULL);
INSERT INTO `reservations` VALUES (8, 6, 1, 2, '2026-06-06', 'CANCELLED', '2026-05-27 17:26:56', '2026-05-27 17:26:56', NULL);
INSERT INTO `reservations` VALUES (9, 6, 1, 3, '2026-06-07', 'PENDING', '2026-05-27 17:30:26', '2026-05-27 17:30:26', NULL);
INSERT INTO `reservations` VALUES (10, 6, 2, 5, '2026-06-08', 'CANCELLED', '2026-05-27 17:40:37', '2026-05-27 17:40:37', NULL);
INSERT INTO `reservations` VALUES (11, 6, 1, 1, '2026-06-10', 'CANCELLED', '2026-05-27 17:45:53', '2026-05-27 17:45:53', NULL);
INSERT INTO `reservations` VALUES (12, 6, 2, 2, '2026-06-15', 'REJECTED', '2026-05-27 17:47:15', '2026-05-27 17:47:15', NULL);
INSERT INTO `reservations` VALUES (13, 5, 2, 6, '2026-05-29', 'COMPLETED', '2026-05-29 00:23:09', '2026-05-29 00:23:09', NULL);
INSERT INTO `reservations` VALUES (14, 5, 2, 7, '2026-05-29', 'COMPLETED', '2026-05-29 00:23:28', '2026-05-29 00:23:28', NULL);
INSERT INTO `reservations` VALUES (15, 6, 2, 8, '2026-05-29', 'COMPLETED', '2026-05-29 00:26:34', '2026-05-29 00:26:34', NULL);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'STUDENT',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `student_id`(`student_id`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin00', '111', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'admin@example.com', NULL, 'ADMIN', 'ACTIVE', '2026-04-22 15:03:43', '2026-05-27 18:20:10');
INSERT INTO `users` VALUES (2, '2021001', '张三', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'zhangsan@example.com', NULL, 'STUDENT', 'ACTIVE', '2026-04-22 15:03:43', '2026-04-22 15:03:43');
INSERT INTO `users` VALUES (3, '2021002', '李四', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'lisi@example.com', NULL, 'STUDENT', 'ACTIVE', '2026-04-22 15:03:43', '2026-04-22 15:03:43');
INSERT INTO `users` VALUES (4, 'test001', 'test', '$2a$10$jugco0Gfg2GZGpxsftMTZuZt2o64.c94RN.qmGs7yf/GvMfBCSO7S', 'test@example.com', NULL, 'STUDENT', 'ACTIVE', '2026-05-27 16:35:22', '2026-05-27 16:35:22');
INSERT INTO `users` VALUES (5, 'test002', 'test2', '$2a$10$8Rrr.ZdHuCDlz/QP9rLpeui6uFfzs6zcduB5hjbzTGm0e9csZ1FRi', 'test2@example.com', NULL, 'STUDENT', 'ACTIVE', '2026-05-27 16:39:20', '2026-05-27 16:39:20');
INSERT INTO `users` VALUES (6, 'test003', 'test3', '$2a$10$zDMDrtbeD8qWVGchRYx32uzRAfwxAwUWNPUIIpf8V1nkswMUuakqW', 'test3@example.com', NULL, 'STUDENT', 'ACTIVE', '2026-05-27 16:41:25', '2026-05-27 16:41:25');
INSERT INTO `users` VALUES (8, 'admin', '管理员', '$2a$10$LLwqoF3rMGDIme.oyy4ZfO/u3Gfd6WpjcZlkDKtFJQLbbT5T77NV2', 'admin001@example.com', NULL, 'ADMIN', 'ACTIVE', '2026-05-27 17:52:23', '2026-05-27 18:20:19');

SET FOREIGN_KEY_CHECKS = 1;
