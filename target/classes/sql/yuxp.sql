-- 2019-12-31 by 吁
ALTER TABLE `sys_file`
MODIFY COLUMN `type`  varchar(64) NULL DEFAULT '' COMMENT '文件类型' AFTER `id`,
MODIFY COLUMN `url`  varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'URL地址' AFTER `type`;

