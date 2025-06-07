USE
socialnetwork;

-- Tạo bảng comment_ban_appeals để lưu trữ khiếu nại về việc bị chặn comment
CREATE TABLE comment_ban_appeals
(
    appeal_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id        BIGINT NOT NULL,
    reason         TEXT   NOT NULL, -- Lý do khiếu nại của user
    status         ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    admin_response TEXT,            -- Phản hồi của admin
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    resolved_at    DATETIME,        -- Thời gian xử lý xong

    CONSTRAINT fk_appeal_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

-- Tạo indexes để tối ưu performance
CREATE INDEX idx_comment_ban_appeals_user_id ON comment_ban_appeals (user_id);
CREATE INDEX idx_comment_ban_appeals_status ON comment_ban_appeals (status);
CREATE INDEX idx_comment_ban_appeals_created_at ON comment_ban_appeals (created_at);