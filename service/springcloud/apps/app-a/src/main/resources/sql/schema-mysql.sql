CREATE DATABASE IF NOT EXISTS springboot DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

SHOW CREATE DATABASE springboot;

USE springboot;

# DROP TABLE IF EXISTS springboot.user;

CREATE TABLE IF NOT EXISTS springboot.user
(
    userId                BIGINT(50) AUTO_INCREMENT PRIMARY KEY,
    userName              VARCHAR(20) DEFAULT ''                                             NOT NULL
    COMMENT '用户名，默认为手机号',
    roleId                BIGINT(50)                                                         NOT NULL
    COMMENT '属于的角色',
    password              VARCHAR(50)                                                        NULL
    COMMENT '用户密码，需要加密',
    email                 VARCHAR(100)                                                       NULL
    COMMENT '邮件',
    birthday              DATETIME                                                           NULL
    COMMENT '生日',
    phone                 VARCHAR(11) DEFAULT ''                                             NULL
    COMMENT '手机号',
    identityNo            VARCHAR(18) DEFAULT ''                                             NULL
    COMMENT '身份证号码',
    sex                   TINYINT(1) DEFAULT '0'                                             NOT NULL
    COMMENT '性别',
    image                 VARCHAR(100) DEFAULT ''                                            NULL
    COMMENT '用户头像',
    joinTime              DATETIME DEFAULT CURRENT_TIMESTAMP                                 NOT NULL
    COMMENT '加入时间即注册时间',
    loginTime             DATETIME DEFAULT CURRENT_TIMESTAMP                                 NOT NULL
    COMMENT '登陆时间',
    lastPasswordResetTime DATETIME DEFAULT CURRENT_TIMESTAMP                                 NOT NULL
    COMMENT '上一次密码修改时间',
    status                INT DEFAULT '0'                                                    NULL
    COMMENT '当前状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SHOW TABLES;
# ================================================================================
# ================================================================================
