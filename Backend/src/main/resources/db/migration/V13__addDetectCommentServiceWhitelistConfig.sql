USE
socialnetwork;

-- Thêm cấu hình IP whitelist cho detect-comment-service
INSERT INTO global_configs (name, code, `desc`, created)
VALUES ('Detect Comment Service IP Whitelist', 'detect_comment_ip_whitelist',
        '127.0.0.1,172.17.0.1,172.20.0.1,172.20.0.9,172.20.0.10,172.20.0.6,api-service', CURRENT_TIMESTAMP);