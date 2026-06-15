-- 无人机信息表 - SQLite 版本
-- 表名: t_uav
-- 说明: 存储无人机的基本信息和规格参数

CREATE TABLE IF NOT EXISTS t_uav (
    id INTEGER PRIMARY KEY AUTOINCREMENT,           -- 主键ID，自增
    uav_code VARCHAR(64) NOT NULL UNIQUE,          -- 注册编号，唯一标识
    model VARCHAR(100) NOT NULL,                   -- 无人机型号
    manufacturer VARCHAR(100),                     -- 制造商
    max_payload DOUBLE,                            -- 最大载重（kg）
    max_altitude INTEGER,                          -- 最大飞行高度（m）
    max_flight_time INTEGER,                       -- 最大续航时间（min）
    max_speed DOUBLE,                              -- 最大速度（m/s）
    wingspan DOUBLE,                               -- 翼展（cm）
    weight DOUBLE,                                 -- 自重（kg）
    status TINYINT NOT NULL DEFAULT 1,             -- 状态：1-正常，0-停用
    remark VARCHAR(500),                           -- 备注信息
    ai_generated TINYINT NOT NULL DEFAULT 0,       -- AI生成标记：1-AI生成，0-手动录入
    deleted TINYINT NOT NULL DEFAULT 0,            -- 删除标记：0-未删除，1-已删除
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

-- 初始化示例数据
-- 示例1：DJI Mini 3 Pro 消费级无人机
INSERT OR IGNORE INTO t_uav (uav_code, model, manufacturer, max_payload, max_altitude, max_flight_time, max_speed, wingspan, weight, status, remark, ai_generated) VALUES
('UAV-2026-001', 'DJI Mini 3 Pro', 'DJI', 0.5, 4000, 34, 16.0, 25.0, 0.249, 1, '消费级迷你无人机', 0);

-- 示例2：Matrice 300 RTK 工业级无人机
INSERT OR IGNORE INTO t_uav (uav_code, model, manufacturer, max_payload, max_altitude, max_flight_time, max_speed, wingspan, weight, status, remark, ai_generated) VALUES
('UAV-2026-002', 'Matrice 300 RTK', 'DJI', 2.7, 5000, 55, 23.0, 895.0, 6.3, 1, '工业级巡检无人机', 0);

-- 示例3：AI生成的无人机数据
INSERT OR IGNORE INTO t_uav (uav_code, model, manufacturer, max_payload, max_altitude, max_flight_time, max_speed, wingspan, weight, status, remark, ai_generated) VALUES
('UAV-2026-003', '消费级四旋翼-AI', 'AI生成', 1.2, 2000, 28, 14.5, 35.0, 0.8, 1, '由AI规则引擎自动生成', 1);