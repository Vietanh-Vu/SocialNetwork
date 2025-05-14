USE
socialnetwork;
CREATE TABLE global_configs
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255)                        DEFAULT ''                NOT NULL,
    code VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' NULL,
    `desc`  mediumtext NULL,
    created datetime                            DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_code ON global_configs(code);

-- Insert the three global configs
INSERT INTO global_configs (name, code, `desc`, created)
VALUES
    ('Hate Speech Threshold', 'hate_speech_threshold', '0.7', CURRENT_TIMESTAMP),
    ('Threshold to Import to Problematic Comment', 'threshold_to_import_to_problematic_comment', '0.5', CURRENT_TIMESTAMP),
    ('Start Detect Comment', 'start_detect_comment', '1', CURRENT_TIMESTAMP);