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