-- 新建数据库
CREATE
DATABASE IF NOT EXISTS `aichat`;
USE `aichat`;

-- 新建 对话记录 表
CREATE TABLE IF NOT EXISTS `dialogue` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`question` text COMMENT '用户提问',
`answer` text COMMENT '模型回答',
`last_id` bigint DEFAULT NULL COMMENT '上一句对话的主键id',
`deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志',
PRIMARY KEY (`id`) USING BTREE,
KEY `last_id` (`last_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb3 COMMENT='用于存储对话记录';