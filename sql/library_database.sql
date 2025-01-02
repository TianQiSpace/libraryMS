/*
 Navicat Premium Data Transfer

 Source Server         : MySQL_80_13306
 Source Server Type    : MySQL
 Source Server Version : 80035 (8.0.35)
 Source Host           : localhost:13306
 Source Schema         : library_database

 Target Server Type    : MySQL
 Target Server Version : 80035 (8.0.35)
 File Encoding         : 65001

 Date: 29/12/2024 19:07:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '管理员用户唯一标识',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '管理员用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '管理员密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '管理员电子邮件',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '性别',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `role` tinyint NOT NULL DEFAULT 2 COMMENT '用户角色，1: 普通用户, 2: 管理员, 3: 超级管理员',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户信息最后更新时间',
  `status` int NOT NULL DEFAULT 0 COMMENT '账户是否能够正常使用，针对管理员用户，0标识可用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户表，存储系统中的用户信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin_user
-- ----------------------------
INSERT INTO `admin_user` VALUES (1, 'TianQi', 'b0a690d8bdbe8118a6dd0c7ca619a0da', '1135733390@qq.com', '男', 19, 2, '2024-10-20 13:31:47', '2024-12-10 08:48:42', 0);

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `book_id` int NOT NULL AUTO_INCREMENT COMMENT '图书ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '书名',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '作者',
  `isbn` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ISBN号',
  `publish_date` date NULL DEFAULT NULL COMMENT '出版日期',
  `publisher_id` int NULL DEFAULT NULL COMMENT '出版社ID',
  `category_id` int NOT NULL COMMENT '分类ID',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '简介',
  `cover_image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `status` int NOT NULL DEFAULT 0 COMMENT '书籍是否被禁用',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `quantity` int NOT NULL COMMENT '书籍的库存量',
  `male_point` int NULL DEFAULT 0 COMMENT '男频排行指数',
  `female_point` int NULL DEFAULT 0 COMMENT '女频排行指数',
  PRIMARY KEY (`book_id`) USING BTREE,
  UNIQUE INDEX `uk_isbn`(`isbn` ASC) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_title`(`title` ASC) USING BTREE,
  INDEX `idx_author`(`author` ASC) USING BTREE,
  INDEX `fk_book_publisher`(`publisher_id` ASC) USING BTREE,
  CONSTRAINT `book_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `book_category` (`category_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_book_publisher` FOREIGN KEY (`publisher_id`) REFERENCES `publisher` (`publisher_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图书表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (1, 'Java程序设计', '韩建平', '1', '2024-10-22', 2, 1, 'Java 是由 Sun Microsystems 公司于 1995 年 5 月推出的高级程序设计语言。\r\n\r\nJava 可运行于多个平台，如 Windows, Mac OS 及其他多种 UNIX 版本的系统。\r\n\r\n本教程通过简单的实例将让大家更好的了解 Java 编程语言。\r\n\r\n移动操作系统 Android 大部分的代码采用 Java 编程语言编程。', 'https://bookcover.yuewen.com/qdbimg/349573/1016211814/600.webp', 1, '2024-10-22 22:10:33', '2024-11-05 01:54:09', 14, 45, 1);
INSERT INTO `book` VALUES (2, 'C/C++代码调试的艺术', '张海洋', '2', '2024-10-24', 3, 1, 'C++ 是一种高级语言，它是由 Bjarne Stroustrup 于 1979 年在贝尔实验室开始设计开发的。C++ 进一步扩充和完善了 C 语言，是一种面向对象的程序设计语言。C++ 可运行于多种平台上，如 Windows、MAC 操作系统以及 UNIX 的各种版本。', 'https://bookcover.yuewen.com/qdbimg/349573/1026239287/600.webp', 0, '2024-10-24 10:19:27', '2024-11-28 03:29:12', 3, 105, 21);
INSERT INTO `book` VALUES (3, '史记', '司马迁', '3', '2024-10-29', 1, 3, '《史记》全书包括十二本纪（记历代帝王政绩）、三十世家（记诸侯国和汉代诸侯、勋贵兴亡）、七十列传（记重要人物的言行事迹，主要叙人臣，其中最后一篇为自序）、十表（大事年表）、八书（记各种典章制度记礼、乐、音律、历法、天文、封禅、水利、财用）。《史记》共一百三十篇，五十二万六千五百余字，比《淮南子》多三十九万五千余字，比《吕氏春秋》多二十八万八千余字。《史记》规模巨大，体系完备，而且对此后的纪传体史书影响很深，历朝正史皆采用这种体裁撰写', 'https://bookcover.yuewen.com/qdbimg/349573/1019490727/600.webp', 0, '2024-10-29 15:58:55', '2024-11-04 17:14:24', 12, 51, 0);
INSERT INTO `book` VALUES (4, '《当代》杂志（2021年6期）', '《当代》杂志社', '4', '2024-10-09', 6, 7, '人民文学出版社《当代》杂志2021年第六期，刊载冯骥才新作：《多瑙河峡谷》、《枯井》；刊载长篇小说：《每天挖地不止》（林那北）；刊载中篇小说：《狼踪（外一篇）》（韩东）、《她的云》（丁东亚）、《好归》（好归）、《遗产》（蒋在）；讲谈：《颠张狂草——历代书法人物之张旭》（张国擎）；纪事：《长勿相忘》（刘东黎）；文学拉力赛传真——2021年第五站冠军揭晓，2021年第五站读者来信选登；其他：《当代》2021年总目录，及《当代》《当代长篇小说选刊》读者调查启事。', 'https://bookcover.yuewen.com/qdbimg/349573/1031847053/600.webp', 0, '2024-10-30 08:40:08', '2024-11-04 17:14:28', 11, 40, 0);
INSERT INTO `book` VALUES (5, '红星照耀中国', '(美)埃德加·斯诺', '5', '1991-04-26', 7, 9, '《红星照耀中国》是西方记者对中国共产党和红军的第一部采访记录，也是新闻史和报告文学史上里程碑式的作品。书中不仅记载了大量有关中国红军和苏区，以及毛泽东等革命领导人的第一手资料，而且深入分析和探究了“红色中国”产生、发展的原因，对中国共产党和中国革命做出了客观的评价。由于其对历史的权威记录，以及对历史趋势的准确预见，本书成为风靡全球的经典名著，被译成近二十种文字。', 'https://bookcover.yuewen.com/qdbimg/349573/1015409387/600.webp', 0, '2024-11-02 22:06:16', '2024-11-04 17:14:32', 13, 11, 1);
INSERT INTO `book` VALUES (6, '钢铁是怎样炼成的', '(苏)奥斯特洛夫斯基', '6', '2006-07-28', 8, 9, '《钢铁是怎样炼成的》是一部闪烁着崇高理想主义光芒的半自传体长篇小说，小说成功地塑造了保尔·柯察金这一无产阶级英雄形象，以生动而又富于生活气息的语言、震撼人心的精神力量，展现了保尔·柯察金的生活和情感世界，成为了激励中国几代青年人的文学经典。', 'https://bookcover.yuewen.com/qdbimg/349573/1015409503/600.webp', 0, '2024-11-02 22:09:20', '2024-11-04 17:14:34', 9, 1, 11);
INSERT INTO `book` VALUES (7, '诡秘之主', '爱潜水的乌贼', '7', '2024-01-02', 9, 8, '蒸汽与机械的浪潮中，谁能触及非凡？历史和黑暗的迷雾里，又是谁在耳语？我从诡秘中醒来，睁眼看见这个世界：\r\n枪械，大炮，巨舰，飞空艇，差分机；魔药，占卜，诅咒，倒吊人，封印物……光明依旧照耀，神秘从未远离，这是一段“愚者”的传说。111111', 'https://bookcover.yuewen.com/qdbimg/349573/1010868264/600.webp', 0, '2024-11-02 22:15:21', '2024-11-20 10:46:24', 18, 104, 11);
INSERT INTO `book` VALUES (8, '中国通史', '吕思勉', '8', '2018-03-01', 8, 5, '“读史使人明智”，这是17世纪英国哲学家培根的一句名言。读史就要读出历史的真实，就要读懂历史发展的规律，进而理性地参与到历史的发展之中。史学大师吕思勉（1884—1957）所著的《中国通史》（原名《白话本国史》）堪称与钱穆《国史大纲》双峰对峙史学巨著，迄今为止，仍旧是一部最权威和最完整的中国通史。', 'https://bookcover.yuewen.com/qdbimg/349573/1001606642/600.webp', 0, '2024-11-03 07:43:25', '2024-11-04 17:14:38', 16, 20, 10);
INSERT INTO `book` VALUES (9, '机械设计基础', '范元勋', '9', '2021-02-18', 5, 6, '本书根据编者多年来从事工程制图和计算机绘图的教学经验，在内容的处理上，做到系统性和实用性相结合。内容包括CAXA用户界面、命令执行的操作方法、对象的拾取和绘图的基本操作过程等；绘图辅助功能以及图形显示控制；基本绘图功能；高级绘图功能；各种编辑功能；标注功能；图幅功能；图块、图库操作和拼画装配图；文件操作、创建视口、OLE、外部引用及图片管理等；打印及系统工具等。', 'https://bookcover.yuewen.com/qdbimg/349573/1023281222/600.webp', 1, '2024-11-03 07:45:46', '2024-11-28 03:29:33', 10, 1, 2);
INSERT INTO `book` VALUES (10, '测试', '张三', '10', '2024-11-04', 1, 1, '测试书籍', 'https://bookcover.yuewen.com/qdbimg/349573/1034509849/150.webp', 0, '2024-11-04 15:54:48', '2024-11-04 17:14:45', 1, 0, 0);
INSERT INTO `book` VALUES (12, '光阴之外', '耳根', '173071820', '2023-07-21', 9, 8, '天地是万物众生的客舍，光阴是古往今来的过客。\r\n死生的差异，就好像梦与醒的不同，纷纭变换，不可究诘。\r\n那么超越了生死，超脱了天地，在光阴之外，等待我们的是什么？\r\n这是耳根继《仙逆》《求魔》《我欲封天》《一念永恒》《三寸人间》后，创作的第六部长篇小说《光阴之外》', 'https://bookcover.yuewen.com/qdbimg/349573/1031777108/600.webp', 0, '2024-11-04 19:03:23', '2024-11-04 19:03:41', 100, 42, 0);
INSERT INTO `book` VALUES (13, '逼我重生是吧', '幼儿园一把手', '173073938', '2023-05-12', 9, 8, '年少有为的程逐在网上看到了一个问题：“如果给你一个回到过去重生的机会，你想要吗？”\r\n作为圈子里公认的逼气最重的人，程逐看着评论区里几千条的想要重生，直接装起来了，回了句“不想”，并配上了银行卡里那亮眼的余额截图。\r\n然而下一刻......\r\n他重生了！\r\n……\r\n本书又名《好女孩别辜负，坏女孩别浪费》、《谁说我只想搞钱，成年人不做选择》、《浪子把头都浪掉了，怎么个回法》、《叫你心中无女人，没叫你身边没女人》等。', 'https://bookcover.yuewen.com/qdbimg/349573/1037551048/600.webp', 1, '2024-11-05 00:56:27', '2024-11-05 00:56:40', 10, 0, 0);
INSERT INTO `book` VALUES (14, '高武纪元', '烽仙', '173107359', '2024-11-08', 9, 8, '从南洋深海中飞起的黑龙，掀起灭世海啸……火焰魔灵毁灭一座座钢筋水泥城市，于核爆中心安然离去……域外神明试图统治整片星海……\r\n这是人类科技高度发达的未来世界。\r\n也是掀起生命进化狂潮的高武纪元。\r\n即将高考的武道学生李源，心怀能观想星海的奇异神宫，在这个世界艰难前行。\r\n多年以后。\r\n“我现在的飞行速度是122682米/每秒，力量爆发是……”李源在距蓝星表层约180公里的大气层中极速飞行，冰冷眸子盯着昏暗虚空尽头那条形似神话传说中神龙的庞然大物：“你，应该是所有入侵半神生命体中最强的一个了。”\r\n“只可惜，现在的我，可以称之为……武神！”\r\n——\r\n——\r\n已完成高订11万作品《渊天尊》，高订3万作品《洪主》，精品《寒天帝》，人品保证，放心入坑。', 'https://bookcover.yuewen.com/qdbimg/349573/1039141715/600.webp', 0, '2024-11-08 21:46:16', '2024-11-08 21:46:16', 19, 10, 0);
INSERT INTO `book` VALUES (15, '活着', '余华', '173141108', '2012-01-12', 1, 9, '活着(新版)》讲述了农村人福贵悲惨的人生遭遇。福贵本是个阔少爷，可他嗜赌如命，终于赌光了家业，一贫如洗。他的父亲被他活活气死，母亲则在穷困中患了重病，福贵前去求药，却在途中被国民党抓去当壮丁。经过几番波折回到家里，才知道母亲早已去世，妻子家珍含辛茹苦地养大两个儿女。此后更加悲惨的命运一次又一次降临到福贵身上，他的妻子、儿女和孙子相继死去，最后只剩福贵和一头老牛相依为命，但老人依旧活着，仿佛比往日更加洒脱与坚强。\r\n\r\n《活着(新版)》荣获意大利格林扎纳•卡佛文学奖最高奖项（1998年）、台湾《中国时报》10本好书奖（1994年）、香港“博益”15本好书奖（1994年）、第三届世界华文“冰心文学奖”（2002年），入选香港《亚洲周刊》评选的“20世纪中文小说百年百强”、中国百位批评家和文学编辑评选的“20世纪90年代最有影响的10部作品”。', 'https://img9.doubanio.com/view/subject/l/public/s29869926.jpg', 1, '2024-11-12 19:31:27', '2024-11-12 19:33:07', 10, 0, 0);
INSERT INTO `book` VALUES (16, '宿命之环', '爱潜水的乌贼', '173207096', '2024-11-01', 9, 8, '诡秘世界第二部。\r\n1368之年，七月之末，深红将从天而降。11111', 'https://bookcover.yuewen.com/qdbimg/349573/1036370336/600.webp', 0, '2024-11-20 10:48:48', '2024-11-20 10:48:59', 19, 0, 0);
INSERT INTO `book` VALUES (17, '测试2', '测试', '173273586', '2024-11-23', 1, 12, '暂无', 'https://bookcover.yuewen.com/qdbimg/349573/1035420986/150.webp', 0, '2024-11-28 03:30:46', '2024-11-28 03:30:54', 21, 0, 0);

-- ----------------------------
-- Table structure for book_category
-- ----------------------------
DROP TABLE IF EXISTS `book_category`;
CREATE TABLE `book_category`  (
  `category_id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '分类描述',
  PRIMARY KEY (`category_id`) USING BTREE,
  UNIQUE INDEX `uk_category_name`(`category_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图书分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of book_category
-- ----------------------------
INSERT INTO `book_category` VALUES (1, '计算机', '计算机行业有关书籍等，包括教程、经验等。');
INSERT INTO `book_category` VALUES (3, '史学', '历史系列图书2024-10-28');
INSERT INTO `book_category` VALUES (5, '汉语言文学', '汉语言文学相关的书籍分类');
INSERT INTO `book_category` VALUES (6, '机械', '机械专业类目图书');
INSERT INTO `book_category` VALUES (7, '杂志', '休闲娱乐');
INSERT INTO `book_category` VALUES (8, '网络文学', '热火网文');
INSERT INTO `book_category` VALUES (9, '纪实文学', '一种以真实事件为基础，采用文学手法进行创作的文体。它结合了新闻报道的客观性和文学作品的艺术性，旨在通过详尽的调查研究和生动的文字表达，向读者呈现历史事件、社会现象或个人经历的真实面貌。');
INSERT INTO `book_category` VALUES (12, '默认', '默认书籍类别222');

-- ----------------------------
-- Table structure for borrow_record
-- ----------------------------
DROP TABLE IF EXISTS `borrow_record`;
CREATE TABLE `borrow_record`  (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '借阅记录ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `book_id` int NOT NULL COMMENT '图书ID',
  `book_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '借阅书籍的名称',
  `category_id` int NOT NULL COMMENT '图书类别ID',
  `borrow_date` date NOT NULL COMMENT '借阅日期',
  `return_date` date NULL DEFAULT NULL COMMENT '归还日期',
  `due_date` date NOT NULL COMMENT '应还日期',
  `borrow_status` int NOT NULL DEFAULT 0 COMMENT '借阅归还状态0在读1归还',
  `borrow_status_text` enum('已归还','未归还','待处理') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '未归还',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_book_id`(`book_id` ASC) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  CONSTRAINT `borrow_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `normal_user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `borrow_record_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `borrow_record_ibfk_3` FOREIGN KEY (`category_id`) REFERENCES `book_category` (`category_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '借阅记录表 - 增加了借阅状态文本描述' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of borrow_record
-- ----------------------------
INSERT INTO `borrow_record` VALUES (67, 1, 2, 'C/C++代码调试的艺术', 1, '2024-12-10', NULL, '2025-01-09', 0, '未归还', '2024-12-10 08:57:35', '2024-12-10 08:57:35');
INSERT INTO `borrow_record` VALUES (68, 1, 12, '光阴之外', 8, '2024-12-25', NULL, '2025-01-24', 0, '未归还', '2024-12-25 12:08:39', '2024-12-25 12:08:39');

-- ----------------------------
-- Table structure for borrowinghandling
-- ----------------------------
DROP TABLE IF EXISTS `borrowinghandling`;
CREATE TABLE `borrowinghandling`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` int NOT NULL,
  `book_id` int NOT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of borrowinghandling
-- ----------------------------

-- ----------------------------
-- Table structure for normal_user
-- ----------------------------
DROP TABLE IF EXISTS `normal_user`;
CREATE TABLE `normal_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '普通用户唯一标识',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '普通用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '普通密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '普通电子邮件',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '普通性别',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `role` tinyint NOT NULL DEFAULT 1 COMMENT '用户角色，1: 普通用户, 2: 管理员, 3: 超级管理员',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '普通用户创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '普通用户信息最后更新时间',
  `borrow_quota` int NULL DEFAULT 10 COMMENT '普通用户可用的借阅额度，初始为10本',
  `credit_points` int NULL DEFAULT 100 COMMENT '普通用户的信誉积分，初始为100',
  `borrow_number` int NULL DEFAULT 0 COMMENT '普通用户已经使用的借阅额度0-10',
  `status` int NOT NULL DEFAULT 0 COMMENT '标识账户是否可用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户表，存储系统中的用户信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of normal_user
-- ----------------------------
INSERT INTO `normal_user` VALUES (1, 'TianQi', 'b0a690d8bdbe8118a6dd0c7ca619a0da', '1135733390@qq.com', '男', 19, 1, '2024-12-10 08:54:08', '2024-12-10 08:55:29', 10, 100, 2, 0);
INSERT INTO `normal_user` VALUES (26, '汲川大王', 'b0a690d8bdbe8118a6dd0c7ca619a0da', '1135733390@qq.com', '男', 18, 1, '2024-12-23 18:01:23', '2024-12-23 18:01:23', 10, 100, 0, 0);

-- ----------------------------
-- Table structure for notifications
-- ----------------------------
DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，唯一标识每条通知',
  `user_id` int NOT NULL COMMENT '消息要发送到的用户标识',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息标题',
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息内容',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息创建的时间',
  `message_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息状态（未读/已读）',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息类型（系统通知/个人消息）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `normal_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 167 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储系统中的所有普通用户的通知记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notifications
-- ----------------------------
INSERT INTO `notifications` VALUES (165, 1, '借阅通知', '借阅通知！书籍：【C/C++代码调试的艺术】 借阅成功！请于30天内归还！', '2024-12-10 08:57:35', '未读', '系统通知');
INSERT INTO `notifications` VALUES (166, 1, '借阅通知', '借阅通知！书籍：【光阴之外】 借阅成功！请于30天内归还！', '2024-12-25 12:08:38', '未读', '系统通知');

-- ----------------------------
-- Table structure for publisher
-- ----------------------------
DROP TABLE IF EXISTS `publisher`;
CREATE TABLE `publisher`  (
  `publisher_id` int NOT NULL AUTO_INCREMENT COMMENT '出版社ID',
  `publisher_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出版社名称',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '官方网站',
  PRIMARY KEY (`publisher_id`) USING BTREE,
  UNIQUE INDEX `uk_publisher_name`(`publisher_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '出版社表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of publisher
-- ----------------------------
INSERT INTO `publisher` VALUES (1, '清华同方', '北京老胡同口', '19561861111', '1234567890@qq.com', 'www.qinghua.com');
INSERT INTO `publisher` VALUES (2, '黑马程序员', '武汉', '15772736317', '123456798@qq.com', 'www.heima.com');
INSERT INTO `publisher` VALUES (3, '尚硅谷', '北京', '19672716218', '123456789@qq.com', 'www.atguigu.com');
INSERT INTO `publisher` VALUES (5, '机械出版社', '哈尔滨', '15772736218', '1234567890@qq.com', 'www.jixie.com');
INSERT INTO `publisher` VALUES (6, '《当代》杂志社', '燕京', '19086172345', '123654789@qq.com', 'www.yilin.com');
INSERT INTO `publisher` VALUES (7, '人民出版社', '北京', '17216452345', '1234567890@qq.com', 'www.renminchuban.com');
INSERT INTO `publisher` VALUES (8, '中国出版集团', '上海', '13264541234', '1234567890@qq.com', 'www.zhongguochuban.com');
INSERT INTO `publisher` VALUES (9, '起点中文网', '上海', '19876512433', '1234567890@qq.com', 'www.qidian.com');

-- ----------------------------
-- Table structure for super_admin_user
-- ----------------------------
DROP TABLE IF EXISTS `super_admin_user`;
CREATE TABLE `super_admin_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '超管用户唯一标识',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '超管用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '超管密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '超管电子邮件',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '超管性别',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `role` tinyint NOT NULL DEFAULT 3 COMMENT '用户角色，1: 普通用户, 2: 管理员, 3: 超级管理员',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '超管用户创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '超管用户信息最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户表，存储系统中的用户信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of super_admin_user
-- ----------------------------
INSERT INTO `super_admin_user` VALUES (1, 'TianQi', 'b0a690d8bdbe8118a6dd0c7ca619a0da', '1135733390@qq.com', '男', 20, 3, '2024-10-20 11:05:10', '2024-12-10 08:48:46');

-- ----------------------------
-- Table structure for verification_code
-- ----------------------------
DROP TABLE IF EXISTS `verification_code`;
CREATE TABLE `verification_code`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户邮箱地址',
  `code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '验证码',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '验证码创建时间',
  `expires_time` timestamp NOT NULL COMMENT '验证码过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of verification_code
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
