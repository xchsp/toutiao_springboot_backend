/*
Navicat MySQL Data Transfer
Source Server         : mysql
Source Server Version : 50547
Source Host           : localhost:3306
Source Database       : flybbs
Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001
Date: 2018-12-06 11:47:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tab_comment
-- ----------------------------
DROP TABLE IF EXISTS `tab_comment`;
CREATE TABLE `tab_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `topic_id` int(11) NOT NULL DEFAULT '0',
  `comment_content` varchar(1000) NOT NULL DEFAULT '',
  `like_num` int(11) NOT NULL DEFAULT '0',
  `is_choose` int(11) NOT NULL DEFAULT '0',
  `comment_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tab_comment
-- ----------------------------

-- ----------------------------
-- Table structure for tab_topic
-- ----------------------------
DROP TABLE IF EXISTS `tab_topic`;
CREATE TABLE `tab_topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL DEFAULT '',
  `content` longtext NOT NULL,
  `is_delete` int(11) NOT NULL DEFAULT '0',
  `view_times` int(11) NOT NULL DEFAULT '0',
  `kiss_num` int(11) NOT NULL DEFAULT '0',
  `is_top` int(11) NOT NULL DEFAULT '0',
  `is_good` int(11) NOT NULL DEFAULT '0',
  `is_end` int(11) NOT NULL DEFAULT '0',
  `comment_num` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00',
  `userid` int(11) NOT NULL DEFAULT '0',
  `topic_category_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tab_topic
-- ----------------------------

-- ----------------------------
-- Table structure for tab_topic_category
-- ----------------------------
DROP TABLE IF EXISTS `tab_topic_category`;
CREATE TABLE `tab_topic_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tab_topic_category
-- ----------------------------

-- ----------------------------
-- Table structure for tab_user
-- ----------------------------
DROP TABLE IF EXISTS `tab_user`;
CREATE TABLE `tab_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL DEFAULT '',
  `nickname` varchar(100) NOT NULL DEFAULT '',
  `passwd` varchar(100) NOT NULL DEFAULT '',
  `city` varchar(100) NOT NULL DEFAULT '',
  `sign` varchar(500) NOT NULL DEFAULT '',
  `sex` int(11) NOT NULL DEFAULT '0',
  `pic_path` varchar(500) NOT NULL DEFAULT '',
  `qq` varchar(50) NOT NULL DEFAULT '',
  `weibo` varchar(100) NOT NULL DEFAULT '',
  `vip_grade` int(11) NOT NULL DEFAULT '0',
  `kiss_num` int(11) NOT NULL DEFAULT '0',
  `join_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tab_user
-- ----------------------------

-- ----------------------------
-- Table structure for tab_user_collect_topic
-- ----------------------------
DROP TABLE IF EXISTS `tab_user_collect_topic`;
CREATE TABLE `tab_user_collect_topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `topic_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tab_user_collect_topic
-- ----------------------------

-- ----------------------------
-- Table structure for tab_user_message
-- ----------------------------
DROP TABLE IF EXISTS `tab_user_message`;
CREATE TABLE `tab_user_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trigger_msg_user_id` int(11) NOT NULL DEFAULT '0',
  `recv_msg_user_id` int(11) NOT NULL DEFAULT '0',
  `topic_id` int(11) NOT NULL DEFAULT '0',
  `msg_type` int(11) NOT NULL DEFAULT '0',
  `is_read` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tab_user_message
-- ----------------------------

-- ----------------------------
-- Table structure for tab_user_qiandao
-- ----------------------------
DROP TABLE IF EXISTS `tab_user_qiandao`;
CREATE TABLE `tab_user_qiandao` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `total` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tab_user_qiandao
-- ----------------------------

INSERT INTO `tab_topic_category` VALUES ('1', '提问');
INSERT INTO `tab_topic_category` VALUES ('2', '分享');
INSERT INTO `tab_topic_category` VALUES ('3', '讨论');
INSERT INTO `tab_topic_category` VALUES ('4', '建议');
INSERT INTO `tab_topic_category` VALUES ('5', '公告');
INSERT INTO `tab_topic_category` VALUES ('6', '动态');


alter table tab_user add COLUMN isadmin int(1) NOT NULL DEFAULT 0;

create table tab_user_comment_agree(
	id int PRIMARY key not null AUTO_INCREMENT,
	userid int not null DEFAULT 0,
	commentid int not null DEFAULT 0
);

alter table tab_user add COLUMN active_code varchar(100) NOT NULL DEFAULT '';
alter table tab_user add COLUMN active_state int(10) NOT NULL DEFAULT 0;


alter table tab_topic add COLUMN topic_type int(1) NOT NULL DEFAULT 0;
alter table tab_topic add COLUMN video_url varchar(200) NOT NULL DEFAULT '';
alter table tab_topic add COLUMN cover_url1 varchar(200) NOT NULL DEFAULT '';
alter table tab_topic add COLUMN cover_url2 varchar(200) NOT NULL DEFAULT '';
alter table tab_topic add COLUMN cover_url3 varchar(200) NOT NULL DEFAULT '';

DROP TABLE IF EXISTS `tab_topic_category_relation`;
CREATE TABLE `tab_topic_category_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `topic_id` int(11) NOT NULL DEFAULT '0',
  `category_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;