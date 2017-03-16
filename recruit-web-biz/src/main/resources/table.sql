CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户Id',
  `userAccount` varchar(50) NOT NULL COMMENT '用户账号',
  `password` varchar(100) NOT NULL COMMENT '用户密码',
  `userName` varchar(50) DEFAULT NULL COMMENT '用户名称',
  `salt` varchar(20) NOT NULL COMMENT '盐',
  `roleId` int(11) DEFAULT NULL COMMENT '角色Id',
  `createdTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatedTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_IDX` (`userAccount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';