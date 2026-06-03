-- ============================================================
-- 学生管理系统 - 课外活动六大模块建表脚本
-- 数据库: MySQL 8.x
-- JPA ddl-auto=update 会自动建表，此脚本供手动部署/参考使用
-- ============================================================

-- 🏆 学科竞赛
CREATE TABLE IF NOT EXISTS competition (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    student_id       INT            NOT NULL COMMENT '学生ID (FK→person)',
    competition_name VARCHAR(255)   COMMENT '竞赛名称',
    level            VARCHAR(50)    COMMENT '级别（国家级/省级/校级）',
    award            VARCHAR(100)   COMMENT '获奖情况（一等奖/二等奖等）',
    award_date       VARCHAR(50)    COMMENT '获奖时间',
    organizer        VARCHAR(255)   COMMENT '主办单位',
    description      TEXT           COMMENT '描述',
    status           VARCHAR(20)    DEFAULT 'PENDING' COMMENT '审批状态: PENDING/APPROVED/REJECTED',
    teacher_comment  TEXT           COMMENT '教师审批意见',
    INDEX idx_competition_student (student_id),
    INDEX idx_competition_status  (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学科竞赛';

-- 🔬 科技成果
CREATE TABLE IF NOT EXISTS science_achievement (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    student_id       INT            NOT NULL COMMENT '学生ID (FK→person)',
    achievement_name VARCHAR(255)   COMMENT '成果名称',
    type             VARCHAR(50)    COMMENT '类型（论文/专利/软件著作权）',
    publish_date     VARCHAR(50)    COMMENT '发表/获批时间',
    publisher        VARCHAR(255)   COMMENT '发表机构/期刊',
    description      TEXT           COMMENT '描述',
    status           VARCHAR(20)    DEFAULT 'PENDING' COMMENT '审批状态',
    teacher_comment  TEXT           COMMENT '教师审批意见',
    INDEX idx_achievement_student (student_id),
    INDEX idx_achievement_status  (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科技成果';

-- 🏢 校外实习
CREATE TABLE IF NOT EXISTS internship (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    student_id       INT            NOT NULL COMMENT '学生ID (FK→person)',
    company_name     VARCHAR(255)   COMMENT '实习单位',
    position         VARCHAR(100)   COMMENT '实习岗位',
    start_date       VARCHAR(50)    COMMENT '开始时间',
    end_date         VARCHAR(50)    COMMENT '结束时间',
    location         VARCHAR(255)   COMMENT '实习地点',
    description      TEXT           COMMENT '实习描述',
    status           VARCHAR(20)    DEFAULT 'PENDING' COMMENT '审批状态',
    teacher_comment  TEXT           COMMENT '教师审批意见',
    INDEX idx_internship_student (student_id),
    INDEX idx_internship_status  (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='校外实习';

-- 💡 创新项目
CREATE TABLE IF NOT EXISTS innovation_project (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    student_id       INT            NOT NULL COMMENT '学生ID (FK→person)',
    project_name     VARCHAR(255)   COMMENT '项目名称',
    level            VARCHAR(50)    COMMENT '项目级别（国家级/省级/校级）',
    role             VARCHAR(50)    COMMENT '参与角色（负责人/成员）',
    start_date       VARCHAR(50)    COMMENT '开始时间',
    end_date         VARCHAR(50)    COMMENT '结束时间',
    project_status   VARCHAR(50)    COMMENT '项目状态（进行中/已结题）',
    description      TEXT           COMMENT '项目描述',
    status           VARCHAR(20)    DEFAULT 'PENDING' COMMENT '审批状态',
    teacher_comment  TEXT           COMMENT '教师审批意见',
    INDEX idx_project_student (student_id),
    INDEX idx_project_status  (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='创新项目';

-- 📚 培训讲座
CREATE TABLE IF NOT EXISTS training_lecture (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    student_id       INT            NOT NULL COMMENT '学生ID (FK→person)',
    training_name    VARCHAR(255)   COMMENT '培训/讲座名称',
    organizer        VARCHAR(255)   COMMENT '主办方',
    lecture_date     VARCHAR(50)    COMMENT '时间',
    location         VARCHAR(255)   COMMENT '地点',
    description      TEXT           COMMENT '描述',
    status           VARCHAR(20)    DEFAULT 'PENDING' COMMENT '审批状态',
    teacher_comment  TEXT           COMMENT '教师审批意见',
    INDEX idx_lecture_student (student_id),
    INDEX idx_lecture_status  (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='培训讲座';

-- 🌍 社会实践
CREATE TABLE IF NOT EXISTS social_practice (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    student_id       INT            NOT NULL COMMENT '学生ID (FK→person)',
    practice_name    VARCHAR(255)   COMMENT '实践名称',
    location         VARCHAR(255)   COMMENT '地点',
    start_date       VARCHAR(50)    COMMENT '开始时间',
    end_date         VARCHAR(50)    COMMENT '结束时间',
    role             VARCHAR(50)    COMMENT '担任角色',
    description      TEXT           COMMENT '描述',
    status           VARCHAR(20)    DEFAULT 'PENDING' COMMENT '审批状态',
    teacher_comment  TEXT           COMMENT '教师审批意见',
    INDEX idx_practice_student (student_id),
    INDEX idx_practice_status  (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社会实践';

-- 🏃 日常活动
CREATE TABLE IF NOT EXISTS daily_activity (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    student_id       INT            NOT NULL COMMENT '学生ID (FK→person)',
    activity_name    VARCHAR(255)   COMMENT '活动名称',
    type             VARCHAR(50)    COMMENT '类型（体育/文艺/聚会/其他）',
    activity_date    VARCHAR(50)    COMMENT '活动时间',
    location         VARCHAR(255)   COMMENT '地点',
    role             VARCHAR(50)    COMMENT '角色（参与者/组织者）',
    duration         VARCHAR(50)    COMMENT '时长（小时）',
    description      TEXT           COMMENT '活动描述',
    INDEX idx_activity_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日常活动';

-- 📝 日志信息
CREATE TABLE IF NOT EXISTS daily_log (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    student_id       INT            NOT NULL COMMENT '学生ID (FK→person)',
    title            VARCHAR(255)   COMMENT '日志标题',
    type             VARCHAR(50)    COMMENT '类型（消费/学习/生活/其他）',
    log_date         VARCHAR(50)    COMMENT '日期',
    content          TEXT           COMMENT '日志内容',
    mood             VARCHAR(50)    COMMENT '心情',
    tags             VARCHAR(255)   COMMENT '标签（逗号分隔）',
    INDEX idx_log_student (student_id),
    INDEX idx_log_type     (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志信息';
