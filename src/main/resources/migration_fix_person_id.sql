-- ============================================================
-- 修复 user 表 person_id 字段的约束问题
-- 日期: 2026-06-03
-- ============================================================

-- 1. 检查并修改 user 表结构
-- 移除 person_id 的不合适默认值，改为允许 NULL
ALTER TABLE user MODIFY COLUMN person_id INT NULL;

-- 2. 更新现有的 person_id = -1 的记录，改为 NULL
UPDATE user SET person_id = NULL WHERE person_id = -1;

-- 3. 确保新创建的 User 能正确关联到 Person
-- 可选：如果需要强制外键约束，取消下面的注释
-- ALTER TABLE user ADD CONSTRAINT fk_user_person FOREIGN KEY (person_id) REFERENCES person(id);
